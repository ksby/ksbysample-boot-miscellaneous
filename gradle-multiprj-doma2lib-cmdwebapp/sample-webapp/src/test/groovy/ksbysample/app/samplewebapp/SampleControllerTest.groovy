package ksbysample.app.samplewebapp

import groovy.sql.Sql
import ksbysample.lib.doma2lib.entity.Employee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import javax.sql.DataSource

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

// 通常は spring.profiles.active は IntelliJ IDEA の JUnit の Run/Debug Configuration と build.gradle に定義するが、
// 今回はテストが１つしかないので @SpringBootTest の properties 属性で指定する
@SpringBootTest(properties = ["spring.profiles.active=develop"])
@AutoConfigureMockMvc
class SampleControllerTest extends Specification {

    static final def TESTDATA = [
            [id: 1, name: "鈴木　太郎", age: 42, sex: "男", update_time: "2019/05/01 00:00:00"],
            [id: 2, name: "渡辺　香", age: 36, sex: "女", update_time: "2019/04/30 16:51:47"],
            [id: 3, name: "木村　結衣", age: 23, sex: "女", update_time: "2019/04/01 09:15:02"]
    ]

    @Autowired
    private MockMvc mvc

    @Autowired
    private DataSource dataSource

    def sql

    List<Employee> backupData

    void setup() {
        sql = new Sql(dataSource)

        // employee テーブルのバックアップを取得後クリアする
        backupData = sql.rows("select * from employee")
        sql.execute("truncate table employee")
    }

    void tearDown() {
        // バックアップからemployee テーブルのデータをリカバリする
        sql.execute("truncate table employee")
        backupData.each {
            sql.execute("insert into employee values (:id, :name, :age, :sex, :update_time)", it)
        }

        sql.close()
    }

    def "employeeテーブルにデータがない場合にはcontentも空になる"() {
        expect:
        mvc.perform(get("/sample"))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
    }

    def "employeeテーブルにデータがある場合には全てのデータが出力される"() {
        setup:
        TESTDATA.each {
            sql.execute("insert into employee values (:id, :name, :age, :sex, :update_time)", it)
        }
        def expectedContent =
                TESTDATA.collect { String.format("name = ${it.name}, age = ${it.age}, sex = ${it.sex}") }
                        .join("\n")

        expect:
        mvc.perform(get("/sample"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent))
    }

}
