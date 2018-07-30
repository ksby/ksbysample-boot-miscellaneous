package ksbysample.webapp.bootnpmgeb.web.inquiry

import groovy.sql.Sql
import ksbysample.common.test.helper.TestHelper
import ksbysample.common.test.rule.mail.MailServerResource
import ksbysample.webapp.bootnpmgeb.dao.InquiryDataDao
import ksbysample.webapp.bootnpmgeb.entity.SurveyOptions
import ksbysample.webapp.bootnpmgeb.helper.db.SurveyOptionsHelper
import ksbysample.webapp.bootnpmgeb.values.*
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput01Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput02Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput03Form
import org.apache.commons.lang3.StringUtils
import org.junit.Rule
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.yaml.snakeyaml.Yaml
import spock.lang.Specification

import javax.mail.internet.MimeMessage
import javax.sql.DataSource
import java.util.stream.Collectors

import static ksbysample.common.test.matcher.HtmlResultMatchers.html
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(Enclosed)
class InquiryConfirmControllerTest {

    @SpringBootTest
    static class 確認画面のテスト extends Specification {

        InquiryInput01Form inquiryInput01Form_001 =
                (InquiryInput01Form) new Yaml().load(getClass().getResourceAsStream("InquiryInput01Form_001.yaml"))
        InquiryInput02Form inquiryInput02Form_001 = new InquiryInput02Form(
                zipcode1: "102"
                , zipcode2: "0072"
                , address: "東京都千代田区飯田橋１－１"
                , tel1: "03"
                , tel2: "1234"
                , tel3: "5678"
                , email: "taro.tanaka@sample.co.jp")
        InquiryInput03Form inquiryInput03Form_001 = new InquiryInput03Form(
                type1: Type1Values.PRODUCT.value
                , type2: [Type2Values.ESTIMATE.value, Type2Values.CATALOGUE.value, Type2Values.OTHER.value]
                , inquiry: "これはテストです"
                , survey: ["1", "2", "3", "4", "5", "6", "7", "8"])

        @Rule
        MailServerResource mailServerResource = new MailServerResource()

        @Autowired
        WebApplicationContext context

        @Autowired
        DataSource dataSource

        @Autowired
        ValuesHelper vh

        @Autowired
        SurveyOptionsHelper soh

        @Autowired
        InquiryDataDao inquiryDataDao

        MockMvc mockMvc

        Sql sql

        def setup() {
            mockMvc = MockMvcBuilders.webAppContextSetup(context)
                    .apply(springSecurity())
                    .build()
            sql = new Sql(dataSource)
            sql.execute("truncate table INQUIRY_DATA")
        }

        def teardown() {
            sql.close()
        }

        def "入力画面１～３で全ての項目に入力した場合のテスト"() {
            when: "入力画面１で項目全てに入力して「次へ」ボタンをクリックする"
            MvcResult result = mockMvc.perform(TestHelper.postForm("/inquiry/input/01?move=next", inquiryInput01Form_001).with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/02"))
                    .andReturn()
            MockHttpSession session = result.getRequest().getSession()

            and: "入力画面２で項目全てに入力して「次へ」ボタンをクリックする"
            mockMvc.perform(TestHelper.postForm("/inquiry/input/02?move=next", inquiryInput02Form_001).with(csrf()).session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/03"))

            and: "入力画面３で項目全てに入力して「次へ」ボタンをクリックする"
            mockMvc.perform(TestHelper.postForm("/inquiry/input/03?move=next", inquiryInput03Form_001).with(csrf()).session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/confirm"))

            then: "確認画面に入力画面１～３で入力したデータが表示される"
            mockMvc.perform(get("/inquiry/confirm").session(session))
                    .andExpect(status().isOk())
                    .andExpect(html("#name").text(inquiryInput01Form_001.lastname + "　" + inquiryInput01Form_001.firstname))
                    .andExpect(html("#kana").text(inquiryInput01Form_001.lastkana + "　" + inquiryInput01Form_001.firstkana))
                    .andExpect(html("#sex").text(vh.getText(SexValues, inquiryInput01Form_001.sex)))
                    .andExpect(html("#age").text(inquiryInput01Form_001.age + " 歳"))
                    .andExpect(html("#job").text(vh.getText(JobValues, inquiryInput01Form_001.job)))
                    .andExpect(html("#zipcode").text("〒 " + inquiryInput02Form_001.zipcode1 + "-" + inquiryInput02Form_001.zipcode2))
                    .andExpect(html("#address").text(inquiryInput02Form_001.address))
                    .andExpect(html("#tel").text(inquiryInput02Form_001.tel1 + "-" + inquiryInput02Form_001.tel2 + "-" + inquiryInput02Form_001.tel3))
                    .andExpect(html("#email").text(inquiryInput02Form_001.email))
                    .andExpect(html("#type1").text(vh.getText(Type1Values, inquiryInput03Form_001.type1)))
                    .andExpect(html("#type2").text([Type2Values.ESTIMATE.text
                                                    , Type2Values.CATALOGUE.text
                                                    , Type2Values.OTHER.text].stream().collect(Collectors.joining("、"))))
                    .andExpect(html("#inquiry").text(inquiryInput03Form_001.inquiry))
                    .andExpect(html("#survey > ul > li").count(8))
                    .andExpect(html("#survey > ul > li:nth-of-type(1)").text(
                    soh.selectItemList("survey").stream()
                            .filter({ SurveyOptions surveyOptions -> StringUtils.equals(surveyOptions.itemValue, "1") })
                            .map { SurveyOptions surveyOptions -> surveyOptions.itemName }
                            .findFirst().get()))

            and: "確認画面で「送信」ボタンをクリックする"
            mockMvc.perform(post("/inquiry/confirm/send").contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf()).session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/complete/"))

            then: "DBとメールを確認する"
            def rows = sql.rows("SELECT * FROM INQUIRY_DATA")
            rows.size() == 1
            rows[0]["lastname"] == "１２３４５６７８９０１２３４５６７８９０"
            rows[0]["firstname"] == "１２３４５６７８９０１２３４５６７８９０"
            rows[0]["lastkana"] == "あいうえおかきくけこさしすせそたちつてと"
            rows[0]["firstkana"] == "なにぬねのはひふへほまみむめもあいうえお"
            rows[0]["sex"] == "2"
            rows[0]["age"] == 999
            rows[0]["job"] == "3"
            rows[0]["zipcode1"] == "102"
            rows[0]["zipcode2"] == "0072"
            rows[0]["address"] == "東京都千代田区飯田橋１－１"
            rows[0]["tel1"] == "03"
            rows[0]["tel2"] == "1234"
            rows[0]["tel3"] == "5678"
            rows[0]["email"] == "taro.tanaka@sample.co.jp"
            rows[0]["type1"] == "1"
            rows[0]["type2"] == "1,2,3"
            rows[0]["inquiry"].asciiStream.text == inquiryInput03Form_001.inquiry
            rows[0]["survey"] == "1,2,3,4,5,6,7,8"

            mailServerResource.messagesCount == 1
            MimeMessage message = mailServerResource.firstMessage
            message.subject == "問い合わせフォームからお問い合わせがありました"
            message.content == new ClassPathResource("ksbysample/webapp/bootnpmgeb/web/inquiry/inquirymail-body_001.txt").inputStream.text
        }

        def "入力画面１～３で必須項目だけに入力した場合のテスト"() {
            when: "入力画面１で必須項目だけに入力して「次へ」ボタンをクリックする"
            inquiryInput01Form_001.job = StringUtils.EMPTY
            MvcResult result = mockMvc.perform(TestHelper.postForm("/inquiry/input/01?move=next", inquiryInput01Form_001).with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/02"))
                    .andReturn()
            MockHttpSession session = result.getRequest().getSession()

            and: "入力画面２で必須項目だけに入力して「次へ」ボタンをクリックする"
            inquiryInput02Form_001.tel1 = StringUtils.EMPTY
            inquiryInput02Form_001.tel2 = StringUtils.EMPTY
            inquiryInput02Form_001.tel3 = StringUtils.EMPTY
            mockMvc.perform(TestHelper.postForm("/inquiry/input/02?move=next", inquiryInput02Form_001).with(csrf()).session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/03"))

            and: "入力画面３で必須項目だけに入力して「次へ」ボタンをクリックする"
            inquiryInput03Form_001.survey = []
            mockMvc.perform(TestHelper.postForm("/inquiry/input/03?move=next", inquiryInput03Form_001).with(csrf()).session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/confirm"))

            then: "確認画面に入力画面１～３で入力したデータが表示される"
            mockMvc.perform(get("/inquiry/confirm").session(session))
                    .andExpect(status().isOk())
                    .andExpect(html("#name").text(inquiryInput01Form_001.lastname + "　" + inquiryInput01Form_001.firstname))
                    .andExpect(html("#kana").text(inquiryInput01Form_001.lastkana + "　" + inquiryInput01Form_001.firstkana))
                    .andExpect(html("#sex").text(vh.getText(SexValues, inquiryInput01Form_001.sex)))
                    .andExpect(html("#age").text(inquiryInput01Form_001.age + " 歳"))
                    .andExpect(html("#job").text(StringUtils.EMPTY))
                    .andExpect(html("#zipcode").text("〒 " + inquiryInput02Form_001.zipcode1 + "-" + inquiryInput02Form_001.zipcode2))
                    .andExpect(html("#address").text(inquiryInput02Form_001.address))
                    .andExpect(html("#tel").text(StringUtils.EMPTY))
                    .andExpect(html("#email").text(inquiryInput02Form_001.email))
                    .andExpect(html("#type1").text(vh.getText(Type1Values, inquiryInput03Form_001.type1)))
                    .andExpect(html("#type2").text([Type2Values.ESTIMATE.text
                                                    , Type2Values.CATALOGUE.text
                                                    , Type2Values.OTHER.text].stream().collect(Collectors.joining("、"))))
                    .andExpect(html("#inquiry").text(inquiryInput03Form_001.inquiry))
                    .andExpect(html("#survey > ul > li").count(0))

            and: "確認画面で「送信」ボタンをクリックする"
            mockMvc.perform(post("/inquiry/confirm/send").contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf()).session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/complete/"))

            then: "DBとメールを確認する"
            def rows = sql.rows("SELECT * FROM INQUIRY_DATA")
            rows.size() == 1
            rows[0]["lastname"] == "１２３４５６７８９０１２３４５６７８９０"
            rows[0]["firstname"] == "１２３４５６７８９０１２３４５６７８９０"
            rows[0]["lastkana"] == "あいうえおかきくけこさしすせそたちつてと"
            rows[0]["firstkana"] == "なにぬねのはひふへほまみむめもあいうえお"
            rows[0]["sex"] == "2"
            rows[0]["age"] == 999
            rows[0]["job"] == StringUtils.EMPTY
            rows[0]["zipcode1"] == "102"
            rows[0]["zipcode2"] == "0072"
            rows[0]["address"] == "東京都千代田区飯田橋１－１"
            rows[0]["tel1"] == StringUtils.EMPTY
            rows[0]["tel2"] == StringUtils.EMPTY
            rows[0]["tel3"] == StringUtils.EMPTY
            rows[0]["email"] == "taro.tanaka@sample.co.jp"
            rows[0]["type1"] == "1"
            rows[0]["type2"] == "1,2,3"
            rows[0]["inquiry"].asciiStream.text == inquiryInput03Form_001.inquiry
            rows[0]["survey"] == StringUtils.EMPTY

            mailServerResource.messagesCount == 1
            MimeMessage message = mailServerResource.firstMessage
            message.subject == "問い合わせフォームからお問い合わせがありました"
            message.content == new ClassPathResource("ksbysample/webapp/bootnpmgeb/web/inquiry/inquirymail-body_002.txt").inputStream.text
        }

    }

}
