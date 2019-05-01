package ksbysample.lib.doma2lib.dao

import groovy.sql.Sql
import ksbysample.lib.doma2lib.entity.Employee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource

// 通常は spring.profiles.active は IntelliJ IDEA の JUnit の Run/Debug Configuration と build.gradle に定義するが、
// 今回はテストが１つしかないので @SpringBootTest の properties 属性で指定する
@SpringBootTest(properties = ["spring.profiles.active=develop"])
class EmployeeDaoTest extends Specification {

    static final def TESTDATA = [
            [id: 1, name: "鈴木　太郎", age: 42, sex: "男", update_time: "2019/05/01 00:00:00"],
            [id: 2, name: "渡辺　香", age: 36, sex: "女", update_time: "2019/04/30 16:51:47"],
            [id: 3, name: "木村　結衣", age: 23, sex: "女", update_time: "2019/04/01 09:15:02"]
    ]

    static final def TESTDATA2 =
            new Employee(id: null, name: "木村　太郎", age: 35, sex: "男", updateTime: null)

    @Autowired
    EmployeeDao employeeDao

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

    @Unroll
    def "selectById メソッドのテスト(#id --> #name, #age, #sex)"() {
        setup:
        TESTDATA.each {
            sql.execute("insert into employee values (:id, :name, :age, :sex, :update_time)", it)
        }
        def row = employeeDao.selectById(id)

        expect:
        row.name == name
        row.age == age
        row.sex == sex

        where:
        id || name    | age | sex
        1  || "鈴木　太郎" | 42  | "男"
        2  || "渡辺　香"  | 36  | "女"
    }

    def "insert メソッドのテスト"() {
        setup:
        employeeDao.insert(TESTDATA2)

        expect:
        def row = sql.firstRow("select * from employee where name = ${TESTDATA2.name}")
        row.name == TESTDATA2.name
        row.age == TESTDATA2.age
        row.sex == TESTDATA2.sex
    }

}
