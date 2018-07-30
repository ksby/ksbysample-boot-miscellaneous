package ksbysample.webapp.bootnpmgeb.web.inquiry.form

import ksbysample.webapp.bootnpmgeb.values.Type1Values
import ksbysample.webapp.bootnpmgeb.values.Type2Values
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintViolation
import javax.validation.Validation

class InquiryInput03FormNotEmptyRuleTest extends Specification {

    def validator
    def inquiryInput03FormNotEmptyRule

    def setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator()
        inquiryInput03FormNotEmptyRule = new InquiryInput03FormNotEmptyRule(
                type1: Type1Values.PRODUCT.value
                , type2: [Type2Values.ESTIMATE.value, Type2Values.OTHER.value]
                , inquiry: "これはテストです"
                , survey: ["1", "2"]
        )
    }

    @Unroll
    def "type1 の NotEmpty のテスト(#type1 --> #size)"() {
        setup:
        inquiryInput03FormNotEmptyRule.type1 = type1
        Set<ConstraintViolation<InquiryInput03FormNotEmptyRule>> constraintViolations =
                validator.validate(inquiryInput03FormNotEmptyRule)

        expect:
        constraintViolations.size() == size

        where:
        type1                     || size
        ""                        || 1
        Type1Values.PRODUCT.value || 0
    }

    @Unroll
    def "type2 の NotEmpty のテスト(#type2 --> #size)"() {
        setup:
        inquiryInput03FormNotEmptyRule.type2 = type2
        Set<ConstraintViolation<InquiryInput03FormNotEmptyRule>> constraintViolations =
                validator.validate(inquiryInput03FormNotEmptyRule)

        expect:
        constraintViolations.size() == size

        where:
        type2                                                                              || size
        []                                                                                 || 1
        [Type2Values.ESTIMATE.value]                                                       || 0
        [Type2Values.ESTIMATE.value, Type2Values.CATALOGUE.value, Type2Values.OTHER.value] || 0
    }

    @Unroll
    def "inquiry の NotEmpty のテスト(#inquiry --> #size)"() {
        setup:
        inquiryInput03FormNotEmptyRule.inquiry = inquiry
        Set<ConstraintViolation<InquiryInput03FormNotEmptyRule>> constraintViolations =
                validator.validate(inquiryInput03FormNotEmptyRule)

        expect:
        constraintViolations.size() == size

        where:
        inquiry || size
        ""      || 1
        "テスト"   || 0
    }

}
