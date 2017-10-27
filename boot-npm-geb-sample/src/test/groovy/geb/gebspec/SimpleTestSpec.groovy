package geb.gebspec

import geb.page.inquiry.InquiryInput01Page
import geb.spock.GebSpec

class SimpleTestSpec extends GebSpec {

    def "動作確認用"() {
        expect: "入力画面１へアクセスする"
        to InquiryInput01Page
        waitFor { at InquiryInput01Page }
    }

}
