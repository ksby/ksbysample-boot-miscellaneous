package ksbysample.webapp.bootnpmgeb.web.inquiry.form

import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintViolation
import javax.validation.Validation

class InquiryInput01FormTest extends Specification {

    def validator
    def inquiryInput01Form

    def setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator()
        inquiryInput01Form = new InquiryInput01Form(
                lastname: "田中"
                , firstname: "太郎"
                , lastkana: "たなか"
                , firstkana: "たろう"
                , sex: "1"
                , age: "30"
                , job: "1")
    }

    def "placeholder で表示している例の Bean Validation のテスト"() {
        when:
        Set<ConstraintViolation<InquiryInput01Form>> constraintViolations =
                validator.validate(inquiryInput01Form)

        then:
        constraintViolations.size() == 0
    }

    @Unroll
    def "lastname の Bean Validation の Bean Validation のテスト(#lastname --> #size)"() {
        setup:
        inquiryInput01Form.lastname = lastname
        Set<ConstraintViolation<InquiryInput01Form>> constraintViolations =
                validator.validate(inquiryInput01Form)

        expect:
        constraintViolations.size() == size

        where:
        lastname || size
        ""       || 2
        "a"      || 0
        "a" * 20 || 0
        "a" * 21 || 1
    }

    @Unroll
    def "firstname の Bean Validation のテスト(#firstname --> #size)"() {
        setup:
        inquiryInput01Form.firstname = firstname
        Set<ConstraintViolation<InquiryInput01Form>> constraintViolations =
                validator.validate(inquiryInput01Form)

        expect:
        constraintViolations.size() == size

        where:
        firstname || size
        ""        || 2
        "a"       || 0
        "a" * 20  || 0
        "a" * 21  || 1
    }

    @Unroll
    def "lastkana の Bean Validation のテスト(#lastkana --> #size)"() {
        setup:
        inquiryInput01Form.lastkana = lastkana
        Set<ConstraintViolation<InquiryInput01Form>> constraintViolations =
                validator.validate(inquiryInput01Form)

        expect:
        constraintViolations.size() == size

        where:
        lastkana || size
        ""       || 3
        "a"      || 1
        "a" * 20 || 1
        "a" * 21 || 2
        "あ"      || 0
        "あ" * 20 || 0
        "あ" * 21 || 1
        "ア"      || 1
        "A"      || 1
        "1"      || 1
        "あa"     || 1
    }

    @Unroll
    def "firstkana の Bean Validation のテスト(#firstkana --> #size)"() {
        setup:
        inquiryInput01Form.firstkana = firstkana
        Set<ConstraintViolation<InquiryInput01Form>> constraintViolations =
                validator.validate(inquiryInput01Form)

        expect:
        constraintViolations.size() == size

        where:
        firstkana || size
        ""        || 3
        "a"       || 1
        "a" * 20  || 1
        "a" * 21  || 2
        "あ"       || 0
        "あ" * 20  || 0
        "あ" * 21  || 1
        "ア"       || 1
        "A"       || 1
        "1"       || 1
        "あa"      || 1
    }

    @Unroll
    def "sex の Bean Validation のテスト(#sex --> #size)"() {
        setup:
        inquiryInput01Form.sex = sex
        Set<ConstraintViolation<InquiryInput01Form>> constraintViolations =
                validator.validate(inquiryInput01Form)

        expect:
        constraintViolations.size() == size

        where:
        sex || size
        ""  || 1
        "0" || 1
        "1" || 0
        "2" || 0
        "3" || 1
    }

    @Unroll
    def "age の Bean Validation のテスト(#age --> #size)"() {
        setup:
        inquiryInput01Form.age = age
        Set<ConstraintViolation<InquiryInput01Form>> constraintViolations =
                validator.validate(inquiryInput01Form)

        expect:
        constraintViolations.size() == size

        where:
        age    || size
        ""     || 2
        "0"    || 0
        "1"    || 0
        "999"  || 0
        "1000" || 1
        "0.1"  || 1
    }

    @Unroll
    def "job の Bean Validation のテスト(#job --> #size)"() {
        setup:
        inquiryInput01Form.job = job
        Set<ConstraintViolation<InquiryInput01Form>> constraintViolations =
                validator.validate(inquiryInput01Form)

        expect:
        constraintViolations.size() == size

        where:
        job || size
        ""  || 0
        "0" || 1
        "1" || 0
        "2" || 0
        "3" || 0
        "4" || 1
    }

}
