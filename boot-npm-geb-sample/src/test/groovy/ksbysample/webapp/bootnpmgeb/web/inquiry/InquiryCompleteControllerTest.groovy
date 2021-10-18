package ksbysample.webapp.bootnpmgeb.web.inquiry

import ksbysample.common.test.extension.mail.MailServerExtension
import ksbysample.common.test.helper.TestHelper
import ksbysample.webapp.bootnpmgeb.values.Type1Values
import ksbysample.webapp.bootnpmgeb.values.Type2Values
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput01Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput02Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput03Form
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.yaml.snakeyaml.Yaml

import static ksbysample.common.test.matcher.HtmlResultMatchers.html
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession

@SpringBootTest
class InquiryCompleteControllerTest {

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

    @Autowired
    private MailServerExtension mailServerExtension

    @Autowired
    WebApplicationContext context

    MockMvc mockMvc

    @BeforeEach
    void setup() {
        mailServerExtension.start()
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .apply(sharedHttpSession())
                .build()
    }

    @AfterEach
    void cleanup() {
        mailServerExtension.stop()
    }

    @Test
    void "完了画面で「入力画面へ」ボタンをクリックして入力画面１へ戻ると入力していたデータがクリアされる"() {
        when: "入力画面１で項目全てに入力して「次へ」ボタンをクリックする"
        mockMvc.perform(TestHelper.postForm("/inquiry/input/01?move=next", inquiryInput01Form_001).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/inquiry/input/02"))

        and: "入力画面２で項目全てに入力して「次へ」ボタンをクリックする"
        mockMvc.perform(TestHelper.postForm("/inquiry/input/02?move=next", inquiryInput02Form_001).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/inquiry/input/03"))

        and: "入力画面３で項目全てに入力して「次へ」ボタンをクリックする"
        mockMvc.perform(TestHelper.postForm("/inquiry/input/03?move=next", inquiryInput03Form_001).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/inquiry/confirm"))

        and: "確認画面で「送信」ボタンをクリックする"
        mockMvc.perform(post("/inquiry/confirm/send").contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/inquiry/complete/"))

        and: "完了画面を表示する"
        mockMvc.perform(get("/inquiry/complete/"))
                .andExpect(status().isOk())

        expect: "完了画面で「入力画面へ」ボタンをクリックする（入力画面１に単にアクセスする）と入力したデータはクリアされている"
        mockMvc.perform(get("/inquiry/input/01/"))
                .andExpect(status().isOk())
                .andExpect(html("#lastname").val(""))
                .andExpect(html("#firstname").val(""))
                .andExpect(html("#lastkana").val(""))
                .andExpect(html("#firstkana").val(""))
                .andExpect(html("input[name='sex'][checked='checked']").notExists())
                .andExpect(html("#age").val(""))
                .andExpect(html("select[name='job'] option[selected]").notExists())
    }

}
