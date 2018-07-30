package ksbysample.webapp.bootnpmgeb.web.inquiry.form

import ksbysample.common.test.helper.TestHelper
import ksbysample.webapp.bootnpmgeb.util.validator.EmailValidator
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.validation.Errors
import spock.lang.Specification
import spock.lang.Unroll

@RunWith(Enclosed)
class InquiryInput02FormValidatorTest {

    @SpringBootTest
    static class InquiryInput02FormValidator_メールアドレス以外 extends Specification {

        @Autowired
        private InquiryInput02FormValidator input02FormValidator

        Errors errors
        InquiryInput02Form inquiryInput02Form

        def setup() {
            errors = TestHelper.createErrors()
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
            setup:
            input02FormValidator.validate(inquiryInput02Form, errors)

            expect:
            errors.hasErrors() == false
        }

        @Unroll
        def "郵便番号の Validation のテスト(#zipcode1,#zipcode2 --> #hasErrors,#size)"() {
            setup:
            inquiryInput02Form.zipcode1 = zipcode1
            inquiryInput02Form.zipcode2 = zipcode2

            expect:
            input02FormValidator.validate(inquiryInput02Form, errors)
            errors.hasErrors() == hasErrors
            errors.getAllErrors().size() == size

            where:
            zipcode1 | zipcode2 || hasErrors | size
            ""       | ""       || false     | 0
            "999"    | ""       || true      | 1
            ""       | "9999"   || true      | 1
            "999"    | "9999"   || false     | 0
        }

        @Unroll
        def "電話番号の Validation のテスト(#tel1,#tel2,#tel3,#ignoreCheckRequired --> #hasErrors,#size)"() {
            given:
            inquiryInput02Form.tel1 = tel1
            inquiryInput02Form.tel2 = tel2
            inquiryInput02Form.tel3 = tel3
            inquiryInput02Form.email = email
            inquiryInput02Form.ignoreCheckRequired = ignoreCheckRequired

            when:
            input02FormValidator.validate(inquiryInput02Form, errors)

            then:
            errors.hasErrors() == hasErrors
            errors.getAllErrors().size() == size
            // メールアドレスのチェックは行われていないことをチェックする
            0 * EmailValidator.validate(email)

            where:
            tel1    | tel2   | tel3   | email | ignoreCheckRequired || hasErrors | size
            ""      | ""     | ""     | ""    | false               || true      | 1
            ""      | ""     | ""     | ""    | true                || false     | 0
            "03"    | ""     | ""     | ""    | false               || true      | 1
            ""      | "1234" | ""     | ""    | false               || true      | 1
            ""      | ""     | "5678" | ""    | false               || true      | 1
            "03"    | "1234" | "5678" | ""    | false               || false     | 0
            "3"     | "1234" | "5678" | ""    | false               || true      | 1
            "03"    | "123"  | "5678" | ""    | false               || true      | 1
            "03123" | "4"    | "5678" | ""    | false               || false     | 0
            "03"    | "1234" | "567"  | ""    | false               || true      | 1
        }

    }

    @RunWith(PowerMockRunner)
    @PowerMockRunnerDelegate(SpringRunner)
    @SpringBootTest
    @PrepareForTest(EmailValidator)
    @PowerMockIgnore("javax.management.*")
    static class InquiryInput02FormValidator_メールアドレス {

        @Autowired
        private InquiryInput02FormValidator input02FormValidator

        Errors errors
        InquiryInput02Form inquiryInput02Form

        @Before
        void setup() {
            errors = TestHelper.createErrors()
            inquiryInput02Form = new InquiryInput02Form(
                    zipcode1: "102"
                    , zipcode2: "0072"
                    , address: "東京都千代田区飯田橋１－１"
                    , tel1: ""
                    , tel2: ""
                    , tel3: ""
                    , email: "taro.tanaka@sample.co.jp")
        }

        @Test
        void "メールアドレスの Validation のテスト"() {
            when: "EmailValidator.validate が true を返すように設定してテストする"
            PowerMockito.mockStatic(EmailValidator)
            PowerMockito.when(EmailValidator.validate(Mockito.any())) thenReturn(true)
            input02FormValidator.validate(inquiryInput02Form, errors)

            then: "入力チェックエラーは発生しない"
            assert errors.hasErrors() == false
            assert errors.getAllErrors().size() == 0
            // EmailValidator.validate が呼び出されていることをチェックする
            PowerMockito.verifyStatic(EmailValidator, Mockito.times(1))
            EmailValidator.validate("taro.tanaka@sample.co.jp")

            and: "EmailValidator.validate が false を返すように設定してテストする"
            PowerMockito.when(EmailValidator.validate(Mockito.any())) thenReturn(false)
            input02FormValidator.validate(inquiryInput02Form, errors)

            then: "入力チェックエラーが発生する"
            assert errors.hasErrors() == true
            assert errors.getAllErrors().size() == 1
        }

    }

}
