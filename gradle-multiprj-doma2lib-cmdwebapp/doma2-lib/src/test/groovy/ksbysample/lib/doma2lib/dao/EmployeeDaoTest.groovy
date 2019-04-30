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

    static final String TESTDATA_NAME = "木村　太郎"

    @Autowired
    EmployeeDao employeeDao

    @Autowired
    private DataSource dataSource

    def sql

    void setup() {
        sql = new Sql(dataSource)
    }

    void tearDown() {
        sql.close()
    }

    @Unroll
    def "selectById メソッドのテスト(#id --> #name, #age, #sex)"() {
        setup:
        def result = employeeDao.selectById(id)

        expect:
        result.name == name
        result.age == age
        result.sex == sex

        where:
        id || name    | age | sex
        1  || "田中　太郎" | 20  | "男"
        2  || "鈴木　花子" | 18  | "女"
    }

    def "insert メソッドのテスト"() {
        setup:
        sql.execute("delete from employee where name = ${TESTDATA_NAME}")
        Employee employee = new Employee(id: null, name: "${TESTDATA_NAME}", age: 35, sex: "男", updateTime: null)
        employeeDao.insert(employee)

        expect:
        def result = sql.firstRow("select * from employee where name = ${TESTDATA_NAME}")
        result.name == TESTDATA_NAME
        result.age == 35
        result.sex == "男"

        cleanup:
        sql.execute("delete from employee where name = ${TESTDATA_NAME}")
    }

}
