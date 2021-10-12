package ksbysample.webapp.bootnpmgeb.web.inquiry.form

import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validation

class InquiryInput02FormTest extends Specification {

    def validator
    def inquiryInput02Form

    def setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator()
        inquiryInput02Form = new InquiryInput02Form(
                zipcode1: "102"
                , zipcode2: "0072"
                , address: "東京都千代田区飯田橋１－１"
                , tel1: "03"
                , tel2: "1234"
                , tel3: "5678"
                , email: "taro.tanaka@sample.co.jp")
    }

    def "placeholder で表示している例の Bean Validation のテスト"() {
        when:
        Set<ConstraintViolation<InquiryInput01Form>> constraintViolations =
                validator.validate(inquiryInput02Form)

        then:
        constraintViolations.size() == 0
    }

    def "zipcode1 の Bean Validation のテスト(#zipcode1 --> #size)"() {
        setup:
        inquiryInput02Form.zipcode1 = zipcode1
        Set<ConstraintViolation<InquiryInput02Form>> constraintViolations =
                validator.validate(inquiryInput02Form)

        expect:
        constraintViolations.size() == size

        where:
        zipcode1 || size
        ""       || 0
        "1"      || 0
        "1" * 3  || 0
        "1" * 4  || 1
    }

    def "zipcode2 の Bean Validation のテスト(#zipcode2 --> #size)"() {
        setup:
        inquiryInput02Form.zipcode2 = zipcode2
        Set<ConstraintViolation<InquiryInput02Form>> constraintViolations =
                validator.validate(inquiryInput02Form)

        expect:
        constraintViolations.size() == size

        where:
        zipcode2 || size
        ""       || 0
        "1"      || 0
        "1" * 4  || 0
        "1" * 5  || 1
    }

    def "address の Bean Validation のテスト(#address --> #size)"() {
        setup:
        inquiryInput02Form.address = address
        Set<ConstraintViolation<InquiryInput02Form>> constraintViolations =
                validator.validate(inquiryInput02Form)

        expect:
        constraintViolations.size() == size

        where:
        address   || size
        ""        || 0
        "a"       || 0
        "a" * 256 || 0
        "a" * 257 || 1
    }

    def "tel1 の Bean Validation のテスト(#tel1 --> #size)"() {
        setup:
        inquiryInput02Form.tel1 = tel1
        Set<ConstraintViolation<InquiryInput02Form>> constraintViolations =
                validator.validate(inquiryInput02Form)

        expect:
        constraintViolations.size() == size

        where:
        tel1    || size
        ""      || 0
        "1"     || 0
        "1" * 5 || 0
        "1" * 6 || 1
    }

    def "tel2 の Bean Validation のテスト(#tel2 --> #size)"() {
        setup:
        inquiryInput02Form.tel2 = tel2
        Set<ConstraintViolation<InquiryInput02Form>> constraintViolations =
                validator.validate(inquiryInput02Form)

        expect:
        constraintViolations.size() == size

        where:
        tel2    || size
        ""      || 0
        "1"     || 0
        "1" * 4 || 0
        "1" * 5 || 1
    }

    def "tel3 の Bean Validation のテスト(#tel3 --> #size)"() {
        setup:
        inquiryInput02Form.tel3 = tel3
        Set<ConstraintViolation<InquiryInput02Form>> constraintViolations =
                validator.validate(inquiryInput02Form)

        expect:
        constraintViolations.size() == size

        where:
        tel3    || size
        ""      || 0
        "1"     || 0
        "1" * 4 || 0
        "1" * 5 || 1
    }

    def "email の Bean Validation のテスト(#email --> #size)"() {
        setup:
        inquiryInput02Form.email = email
        Set<ConstraintViolation<InquiryInput02Form>> constraintViolations =
                validator.validate(inquiryInput02Form)

        expect:
        constraintViolations.size() == size

        where:
        email     || size
        ""        || 0
        "a"       || 0
        "a" * 256 || 0
        "a" * 257 || 1
    }

}
