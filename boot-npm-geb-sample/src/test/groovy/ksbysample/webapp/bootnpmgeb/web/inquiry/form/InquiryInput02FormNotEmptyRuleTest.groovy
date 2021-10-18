package ksbysample.webapp.bootnpmgeb.web.inquiry.form

import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validation

class InquiryInput02FormNotEmptyRuleTest extends Specification {

    def validator
    def inquiryInput02FormNotEmptyRule

    def setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator()
        inquiryInput02FormNotEmptyRule = new InquiryInput02FormNotEmptyRule(
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
        Set<ConstraintViolation<InquiryInput02FormNotEmptyRule>> constraintViolations =
                validator.validate(inquiryInput02FormNotEmptyRule)

        then:
        constraintViolations.size() == 0
    }

    def "zipcode1 の NotEmpty のテスト(#zipcode1 --> #size)"() {
        setup:
        inquiryInput02FormNotEmptyRule.zipcode1 = zipcode1
        Set<ConstraintViolation<InquiryInput02FormNotEmptyRule>> constraintViolations =
                validator.validate(inquiryInput02FormNotEmptyRule)

        expect:
        constraintViolations.size() == size

        where:
        zipcode1 || size
        ""       || 1
        "1"      || 0
    }

    def "zipcode2 の NotEmpty のテスト(#zipcode2 --> #size)"() {
        setup:
        inquiryInput02FormNotEmptyRule.zipcode2 = zipcode2
        Set<ConstraintViolation<InquiryInput02FormNotEmptyRule>> constraintViolations =
                validator.validate(inquiryInput02FormNotEmptyRule)

        expect:
        constraintViolations.size() == size

        where:
        zipcode2 || size
        ""       || 1
        "1"      || 0
    }

    def "address の NotEmpty のテスト(#address --> #size)"() {
        setup:
        inquiryInput02FormNotEmptyRule.address = address
        Set<ConstraintViolation<InquiryInput02FormNotEmptyRule>> constraintViolations =
                validator.validate(inquiryInput02FormNotEmptyRule)

        expect:
        constraintViolations.size() == size

        where:
        address || size
        ""      || 1
        "a"     || 0
    }

}
