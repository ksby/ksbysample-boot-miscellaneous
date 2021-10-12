package ksbysample.webapp.bootnpmgeb.util.validator

import spock.lang.Specification

class EmailValidatorTest extends Specification {

    def "メールアドレスの Validation のテスト(#email --> #result)"() {
        expect:
        EmailValidator.validate(email) == result

        where:
        email                                                                   || result
        ""                                                                      || true
        "@"                                                                     || false
        "a@"                                                                    || false
        "@b"                                                                    || false
        "a@b"                                                                   || true
        "@@"                                                                    || false
        "a@@b"                                                                  || false
        "a@b@c"                                                                 || false
        // ASCII文字だけなので OK
        "taro.tanaka@sample.co.jp"                                              || true
        "1234567890@1234567890"                                                 || true
        "ABCDEFGHOJKLMNOPQRSTUVWXYZ@ABCDEFGHOJKLMNOPQRSTUVWXYZ"                 || true
        "abcdefghojklmnopqrstuvwxyz@abcdefghojklmnopqrstuvwxyz"                 || true
        "!\"#\$%&'()*+,-./:;<=>?[\\]^_`{|}~@!\"#\$%&'()*+,-./:;<=>?[\\]^_`{|}~" || true
        // スペースがあるので NG
        "taro tanaka@sample.co.jp"                                              || false
        "taro.tanaka@sample co.jp"                                              || false
        // 非ASCII文字があるので NG
        "田中太郎@sample.co.jp"                                                     || false
        "taro.tanaka@サンプル co.jp"                                                || false
    }

}
