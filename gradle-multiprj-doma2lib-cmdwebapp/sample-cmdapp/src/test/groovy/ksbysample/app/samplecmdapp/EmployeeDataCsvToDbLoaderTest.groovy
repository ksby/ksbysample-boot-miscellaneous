package ksbysample.app.samplecmdapp

import groovy.sql.Sql
import ksbysample.lib.doma2lib.dao.EmployeeDao
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

import javax.sql.DataSource

// 通常は spring.profiles.active は IntelliJ IDEA の JUnit の Run/Debug Configuration と build.gradle に定義するが、
// 今回はテストが１つしかないので @SpringBootTest の properties 属性で指定する
@SpringBootTest(properties = ["spring.profiles.active=develop"])
class EmployeeDataCsvToDbLoaderTest extends Specification {

    @Autowired
    private ApplicationContext context

    @Autowired
    private DataSource dataSource

    @Autowired
    private final EmployeeDao employeeDao

    @Autowired
    private final ModelMapper modelMapper

    def sql

    void setup() {
        sql = new Sql(dataSource)
    }

    void cleanup() {
        sql.close()
    }

    def "EmployeeDataCsvToDbLoader.run メソッドを実行すると employee.csv のデータが employee テーブルに登録される"() {
        setup:
        // EmployeeDataCsvToDbLoader クラスは batch.execute=EmployeeDataCsvToDbLoader が指定されていないと Bean として
        // 登録されず run メソッドが自動で実行されない。テストでは手動で Bean に登録することで run メソッドが自動実行されることを
        // 回避する。
        EmployeeDataCsvToDbLoader employeeDataCsvToDbLoader =
                new EmployeeDataCsvToDbLoader(employeeDao, modelMapper)
        employeeDataCsvToDbLoader = (EmployeeDataCsvToDbLoader) context
                .getAutowireCapableBeanFactory()
                .applyBeanPostProcessorsAfterInitialization(employeeDataCsvToDbLoader, "employeeDataCsvToDbLoader")

        // src/test/resources の下の employee.csv の File オブジェクトを取得する
        def url = getClass().getResource("/employee.csv")
        def employeeCsvFile = new File(url.toURI())

        // employee テーブルからテストデータを削除する
        sql.execute("delete from employee where name in ('高橋　蓮', '渡辺　結月')")

        expect:
        employeeDataCsvToDbLoader.run("-csvfile=${employeeCsvFile.absolutePath}")
        def results = sql.rows("select name, age, sex from employee where name in ('高橋　蓮', '渡辺　結月')")
        results == [
                [name: "高橋　蓮", age: 15, sex: "男"],
                [name: "渡辺　結月", age: 24, sex: "女"]
        ]

        cleanup:
        sql.execute("delete from employee where name in ('高橋　蓮', '渡辺　結月')")
    }

}
