package geb.gebspec.inquiry

import geb.page.inquiry.*
import geb.spock.GebSpec
import groovy.sql.Sql
import ksbysample.common.test.rule.mail.MailServerResource
import ksbysample.webapp.bootnpmgeb.values.JobValues
import ksbysample.webapp.bootnpmgeb.values.SexValues
import ksbysample.webapp.bootnpmgeb.values.Type1Values
import ksbysample.webapp.bootnpmgeb.values.Type2Values
import org.junit.Rule
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import org.springframework.core.io.ClassPathResource
import spock.lang.Unroll

import javax.mail.internet.MimeMessage
import java.util.stream.Collectors

class InquiryTestSpec extends GebSpec {

    @Rule
    MailServerResource mailServerResource = new MailServerResource()

    Sql sql

    def setup() {
        // 外部プロセスから接続するので H2 TCP サーバへ接続する
        sql = Sql.newInstance("jdbc:h2:tcp://localhost:9092/mem:bootnpmgebdb", "sa", "")
        sql.execute("truncate table INQUIRY_DATA")
    }

    def teardown() {
        sql.close()
    }

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
        form.setValue("input[name='sex']", "1")
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
        form.setValue("input[name='sex']", "1")
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
        form.setValue("input[name='sex']", "1")
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
        form.setValue("input[name='sex']", "1")
        form.btnNext.click(InquiryInput02Page)

        when: "電話番号と郵便番号を入力する"
        form.setValue("#tel1", tel1)
        form.setValue("#tel2", tel2)
        form.setValue("#tel3", tel3)
        form.setValue("#email", email)

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

    def "入力画面１～３→確認画面→完了画面の全ての画面を通す"() {
        setup: "入力画面１を表示する"
        to InquiryInput01Page

        and: "データを入力して次へボタンをクリックし、入力画面２へ遷移する"
        form.setValueList(valueList01)
        form.btnNext.click(InquiryInput02Page)

        and: "データを入力して次へボタンをクリックし、入力画面３へ遷移する"
        form.setValueList(valueList01)
        form.btnNext.click(InquiryInput03Page)

        and: "データを入力して次へボタンをクリックし、確認画面へ遷移する"
        form.setValueList(valueList01)
        form.btnConfirm.click(InquiryConfirmPage)

        and: "確認画面にデータが表示されていることを確認する"
        form.assertTextList(textList01)
        $("#survey > ul > li").count(8)
        $("#survey > ul > li", 0).text() == "選択肢１だけ長くしてみる"
        $("#survey > ul > li", 7).text() == "8"

        and: "修正するボタンをクリックし、入力画面１へ戻る"
        form.btnInput01.click(InquiryInput01Page)
        form.assertValueList(valueList01)

        and: "次へボタンをクリックし、入力画面２へ遷移する"
        form.btnNext.click(InquiryInput02Page)
        form.assertValueList(valueList01)

        and: "次へボタンをクリックし、入力画面３へ遷移する"
        form.btnNext.click(InquiryInput03Page)
        form.assertValueList(valueList01)

        and: "次へボタンをクリックし、確認画面へ遷移する"
        form.btnConfirm.click(InquiryConfirmPage)
        form.assertTextList(textList01)
        $("#survey > ul > li").count(8)
        $("#survey > ul > li", 0).text() == "選択肢１だけ長くしてみる"
        $("#survey > ul > li", 7).text() == "8"

        expect: "送信するボタンをクリックし、完了画面へ遷移する"
        form.btnSend.click(InquiryCompletePage)

        // INQUIRY_DATA テーブルに１件データが登録されていることを確認する
        def rows = sql.rows("SELECT * FROM INQUIRY_DATA")
        rows.size() == 1
        rows[0]["lastname"] == "田中"
        rows[0]["firstname"] == "太郎"
        rows[0]["lastkana"] == "たなか"
        rows[0]["firstkana"] == "たろう"
        rows[0]["sex"] == SexValues.MALE.value
        rows[0]["age"] == 30
        rows[0]["job"] == JobValues.EMPLOYEE.value
        rows[0]["zipcode1"] == "100"
        rows[0]["zipcode2"] == "0005"
        rows[0]["address"] == "東京都千代田区飯田橋１－１"
        rows[0]["tel1"] == "03"
        rows[0]["tel2"] == "1234"
        rows[0]["tel3"] == "5678"
        rows[0]["email"] == "taro.tanaka@sample.co.jp"
        rows[0]["type1"] == Type1Values.PRODUCT.value
        rows[0]["type2"] == [Type2Values.ESTIMATE.value
                             , Type2Values.CATALOGUE.value
                             , Type2Values.OTHER.value].stream()
                .collect(Collectors.joining(","))
        rows[0]["inquiry"].asciiStream.text == "これは\r\nテスト\r\nです"
        rows[0]["survey"] == "1,2,3,4,5,6,7,8"

        // メールが１件送信されていることを確認する
        mailServerResource.messagesCount == 1
        MimeMessage message = mailServerResource.firstMessage
        message.subject == "問い合わせフォームからお問い合わせがありました"
        message.content == new ClassPathResource("ksbysample/webapp/bootnpmgeb/web/inquiry/inquirymail-body_003.txt").inputStream.text

        and: "再び入力画面１を表示する"
        btnToInput01.click(InquiryInput01Page)
        // 入力したデータは表示されていない
        form.assertValueList(initialValueList)
    }

}
