package geb.gebspec

import geb.page.inquiry.InquiryInput01Page
import geb.spock.GebSpec

class SimpleTestSpec extends GebSpec {

    def "動作確認用"() {
        given: "入力画面１へアクセスする"
        to InquiryInput01Page
        waitFor { at InquiryInput01Page }
        $("#form-group-name .js-errmsg").displayed == false

        when: "何も入力せずに「次へ」ボタンをクリックする"
        btnNext.click(InquiryInput01Page)

        then: "「お名前（漢字）」の必須チェックエラーのメッセージが表示される"
        $("#form-group-name .js-errmsg").displayed == true
        $("#form-group-name .js-errmsg").text() == "お名前（漢字）を入力してください"
//        $("#form-group-name .js-errmsg").text() == "エラーです"
    }

}
