package geb.gebspec.inquiry

import geb.page.inquiry.InquiryInput01Page
import geb.page.inquiry.InquiryInput02Page
import geb.spock.GebSpec
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import spock.lang.Unroll

class InquiryTestSpec extends GebSpec {

    def "入力画面１の画面初期表示時に想定している値がセットされている"() {
        setup: "入力画面１を表示する"
        to InquiryInput01Page

        expect: "初期値が表示されている"
        form.assertValueList(initialValueList)
    }

    def "入力画面１の各入力項目に最大文字数を入力できる"() {
        setup: "入力画面１を表示し入力項目に最大文字数の文字を入力する"
        to InquiryInput01Page
        form.setValueList(maxLengthValueList)

        expect: "入力した最大文字数の文字が入力されている"
        form.assertValueList(maxLengthValueList)
    }

    def "入力画面１に入力後、入力画面２へ遷移→入力画面１へ戻ると入力した値が表示される"() {
        given: "入力画面１を表示する"
        to InquiryInput01Page

        when: "最大文字数の文字を入力して次へボタンをクリックする"
        form.setValueList(maxLengthValueList)
        $("#inquiryInput01Form").sex = "1"
        form.btnNext.click(InquiryInput02Page)

        then: "入力画面２へ遷移し初期値が表示されている"
        form.assertValueList(initialValueList)

        and: "戻るボタンをクリックする"
        form.btnBack.click(InquiryInput01Page)

        then: "入力した最大文字数の文字が入力されている"
        form.assertValueList(maxLengthValueList)
        $("#inquiryInput01Form").sex == "1"
    }

    def "入力画面２の各入力項目に最大文字数を入力できる"() {
        setup: "入力画面１を表示して最大文字数の文字を入力してから次へボタンをクリックする"
        to InquiryInput01Page
        form.setValueList(maxLengthValueList)
        $("#inquiryInput01Form").sex = "1"
        form.btnNext.click(InquiryInput02Page)

        and: "入力画面２で最大文字数の文字を入力する"
        form.setValueList(maxLengthValueList)

        expect: "入力した最大文字数の文字が入力されている"
        form.assertValueList(maxLengthValueList)
    }

    def "入力画面２で郵便番号を入力してautocompleteで表示された住所を選択する"() {
        given: "入力画面１から入力画面２へ遷移する"
        to InquiryInput01Page
        form.setValueList(maxLengthValueList)
        $("#inquiryInput01Form").sex = "1"
        form.btnNext.click(InquiryInput02Page)

        when: "郵便番号を入力する"
        $("#inquiryInput02Form").zipcode1 = "100"
        $("#inquiryInput02Form").zipcode2 = "0005" << Keys.TAB

        and: "autocomplete のドロップダウンメニューが表示されたら最初の選択肢を選択する"
        waitFor(5) { $(".ui-autocomplete .ui-menu-item") }
        List<WebElement> elementList = $(".ui-autocomplete .ui-menu-item > div").allElements()
        elementList.first().click()

        then: "住所にクリックした選択肢がセットされている"
        $("#inquiryInput02Form").address == "東京都千代田区丸の内"
    }

    @Unroll
    def "入力画面２の電話番号とメールアドレスの組み合わせテスト(#tel1,#tel2,#tel3,#email)"() {
        given: "入力画面１から入力画面２へ遷移する"
        to InquiryInput01Page
        form.setValueList(maxLengthValueList)
        $("#inquiryInput01Form").sex = "1"
        form.btnNext.click(InquiryInput02Page)

        when: "電話番号と郵便番号を入力する"
        $("#inquiryInput02Form").tel1 = tel1 << Keys.TAB
        $("#inquiryInput02Form").tel2 = tel2 << Keys.TAB
        $("#inquiryInput02Form").tel3 = tel3 << Keys.TAB
        $("#inquiryInput02Form").email = email << Keys.TAB

        then: "エラーメッセージの表示状況をチェックする"
        $("#form-group-tel .js-errmsg").text() == telErrMsg
        $("#form-group-email .js-errmsg").text() == emailErrMsg

        where:
        tel1 | tel2   | tel3   | email                 || telErrMsg                            | emailErrMsg
        "03" | "1234" | "5678" | "tanaka@sample.co.jp" || ""                                   | ""
        "03" | "1234" | "5678" | ""                    || ""                                   | ""
        ""   | ""     | ""     | "tanaka@sample.co.jp" || ""                                   | ""
        ""   | ""     | ""     | ""                    || "電話番号とメールアドレスのいずれか一方を入力してください"       | "電話番号とメールアドレスのいずれか一方を入力してください"
        "3"  | "1234" | "5678" | ""                    || "市外局番の先頭には 0 の数字を入力してください"           | ""
        "03" | "123"  | "5678" | ""                    || "市外局番＋市内局番の組み合わせが数字６桁になるように入力してください" | ""
        "03" | "1234" | "567"  | ""                    || "加入者番号には４桁の数字を入力してください"              | ""
    }

}
