package ksbysample.webapp.bootnpmgeb.helper.mail

import com.google.common.io.Files
import ksbysample.webapp.bootnpmgeb.entity.SurveyOptions
import ksbysample.webapp.bootnpmgeb.helper.db.SurveyOptionsHelper
import ksbysample.webapp.bootnpmgeb.values.JobValues
import ksbysample.webapp.bootnpmgeb.values.SexValues
import ksbysample.webapp.bootnpmgeb.values.Type1Values
import ksbysample.webapp.bootnpmgeb.values.Type2Values
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.ConfirmForm
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils
import spock.lang.Specification

import javax.mail.Message
import javax.mail.internet.MimeMessage
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

@SpringBootTest
class InquiryMailHelperTest extends Specification {

    @Autowired
    private InquiryMailHelper inquiryMailHelper

    @Autowired
    private SurveyOptionsHelper soh

    ConfirmForm confirmForm

    def setup() {
        confirmForm = new ConfirmForm(
                name: "田中　太郎"
                , kana: "たなか　たろう"
                , sex: SexValues.MALE.text
                , age: "30"
                , job: JobValues.EMPLOYEE.text
                , zipcode: "100-0005"
                , address: "東京都千代田区丸の内"
                , tel: "03-1234-5678"
                , email: "test@sample.co.jp"
                , type1: Type1Values.PRODUCT.text
                , type2: [Type2Values.CATALOGUE.text
                          , Type2Values.ESTIMATE.text
                          , Type2Values.OTHER.text].stream()
                .collect(Collectors.joining("、"))
                , inquiry: "これは\r\nテスト\r\nです。"
                , survey: soh.selectItemList("survey").stream()
                .map { SurveyOptions surveyOptions -> surveyOptions.itemName }
                .collect()
        )
    }

    def "全ての項目に値がセットされている場合のテスト"() {
        setup:
        MimeMessage message = inquiryMailHelper.createMessage(confirmForm)

        expect:
        message.getFrom()*.toString() == ["inquiry-form@sample.co.jp"]
        message.getRecipients(Message.RecipientType.TO)*.toString() == ["inquiry@sample.co.jp"]
        message.getSubject() == "問い合わせフォームからお問い合わせがありました"
        message.getContent() == Files.asCharSource(
                new File("src/test/resources/ksbysample/webapp/bootnpmgeb/helper/mail/InquiryMailHelperTest-001.txt")
                , StandardCharsets.UTF_8).read()
    }

    def "任意項目には値がセットされていない場合のテスト"() {
        setup:
        confirmForm.job = StringUtils.EMPTY
        confirmForm.tel = StringUtils.EMPTY
        confirmForm.survey = []
        MimeMessage message = inquiryMailHelper.createMessage(confirmForm)

        expect:
        message.getFrom()*.toString() == ["inquiry-form@sample.co.jp"]
        message.getRecipients(Message.RecipientType.TO)*.toString() == ["inquiry@sample.co.jp"]
        message.getSubject() == "問い合わせフォームからお問い合わせがありました"
        message.getContent() == StreamUtils.copyToString(
                new ClassPathResource("ksbysample/webapp/bootnpmgeb/helper/mail/InquiryMailHelperTest-002.txt")
                        .getInputStream(), StandardCharsets.UTF_8)
    }

}
