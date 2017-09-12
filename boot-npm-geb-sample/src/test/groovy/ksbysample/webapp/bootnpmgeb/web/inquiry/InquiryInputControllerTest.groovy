package ksbysample.webapp.bootnpmgeb.web.inquiry

import ksbysample.common.test.helper.TestHelper
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput01Form
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.yaml.snakeyaml.Yaml

import static ksbysample.common.test.matcher.HtmlResultMatchers.html
import static org.assertj.core.api.Assertions.catchThrowable
import static org.assertj.core.api.AssertionsForClassTypes.assertThat
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(Enclosed)
class InquiryInputControllerTest {

    @RunWith(SpringRunner)
    @SpringBootTest
    static class 入力画面1のテスト {
        private InquiryInput01Form inquiryInput01Form_001 =
                (InquiryInput01Form) new Yaml().load(getClass().getResourceAsStream("InquiryInput01Form_001.yaml"))

        @Autowired
        private WebApplicationContext context

        MockMvc mockMvc

        @Before
        void setup() {
            mockMvc = MockMvcBuilders.webAppContextSetup(context)
                    .build()
        }

        @Test
        void "初期表示時は画面の項目には何もセットされない"() {
            expect:
            mockMvc.perform(get("/inquiry/input/01"))
                    .andExpect(status().isOk())
                    .andExpect(html("#lastname").val(""))
                    .andExpect(html("#firstname").val(""))
                    .andExpect(html("#lastkana").val(""))
                    .andExpect(html("#firstkana").val(""))
                    .andExpect(html("input[name='sex'][checked='checked']").notExists())
                    .andExpect(html("#age").val(""))
                    .andExpect(html("select[name=job] option[selected]").notExists())
        }

        @Test
        void "項目全てに入力して入力画面２へ遷移してから戻ると以前入力したデータがセットされて表示される"() {
            expect: "項目全てに入力して「次へ」ボタンをクリックする"
            MvcResult result = mockMvc.perform(
                    TestHelper.postForm("/inquiry/input/01?move=next", inquiryInput01Form_001))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/02"))
                    .andReturn()
            MockHttpSession session = result.getRequest().getSession()

            and: "再び入力画面１を表示する"
            mockMvc.perform(get("/inquiry/input/01").session(session))
                    .andExpect(status().isOk())
                    .andExpect(html("#lastname").val(inquiryInput01Form_001.lastname))
                    .andExpect(html("#firstname").val(inquiryInput01Form_001.firstname))
                    .andExpect(html("#lastkana").val(inquiryInput01Form_001.lastkana))
                    .andExpect(html("#firstkana").val(inquiryInput01Form_001.firstkana))
                    .andExpect(html("input[name='sex'][checked='checked']").val(inquiryInput01Form_001.sex))
                    .andExpect(html("#age").val(inquiryInput01Form_001.age))
                    .andExpect(html("select[name=job] option[selected]").val(inquiryInput01Form_001.job))
        }

        @Test
        void "入力チェックエラーのあるデータで入力画面２へ遷移しようとするとIllegalArgumentExceptionが発生する"() {
            setup: "入力チェックエラーになるデータを用意する"
            inquiryInput01Form_001.lastname = "x" * 21

            expect: "入力画面１の「次へ」ボタンをクリックする"
            Throwable thrown = catchThrowable({
                mockMvc.perform(
                        TestHelper.postForm("/inquiry/input/01?move=next", inquiryInput01Form_001))
                        .andExpect(status().isOk())
            })
            assertThat(thrown.cause).isInstanceOf(IllegalArgumentException)
        }

    }

}
