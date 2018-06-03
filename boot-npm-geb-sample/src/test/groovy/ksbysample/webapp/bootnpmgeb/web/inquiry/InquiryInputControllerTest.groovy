package ksbysample.webapp.bootnpmgeb.web.inquiry

import ksbysample.common.test.helper.TestHelper
import ksbysample.webapp.bootnpmgeb.values.Type1Values
import ksbysample.webapp.bootnpmgeb.values.Type2Values
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput01Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput02Form
import ksbysample.webapp.bootnpmgeb.web.inquiry.form.InquiryInput03Form
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
                    .andExpect(html("select[name='job'] option[selected]").notExists())
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
                    .andExpect(html("select[name='job'] option[selected]").val(inquiryInput01Form_001.job))
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

    @RunWith(SpringRunner)
    @SpringBootTest
    static class 入力画面2のテスト {
        private InquiryInput01Form inquiryInput01Form_001 =
                (InquiryInput01Form) new Yaml().load(getClass().getResourceAsStream("InquiryInput01Form_001.yaml"))
        private InquiryInput02Form inquiryInput02Form_001 = new InquiryInput02Form(
                zipcode1: "102"
                , zipcode2: "0072"
                , address: "東京都千代田区飯田橋１－１"
                , tel1: "03"
                , tel2: "1234"
                , tel3: "5678"
                , email: "taro.tanaka@sample.co.jp")

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
            mockMvc.perform(get("/inquiry/input/02"))
                    .andExpect(status().isOk())
                    .andExpect(html("#zipcode1").val(""))
                    .andExpect(html("#zipcode2").val(""))
                    .andExpect(html("#address").val(""))
                    .andExpect(html("#tel1").val(""))
                    .andExpect(html("#tel2").val(""))
                    .andExpect(html("#tel3").val(""))
                    .andExpect(html("#email").val(""))
        }

        @Test
        void "項目全てに入力して前の画面へ戻るボタンをクリックすると入力画面１へ戻り、次へ戻るボタンを押して入力画面２へ戻ると以前入力したデータがセットされて表示される"() {
            when: "入力画面１で項目全てに入力して「次へ」ボタンをクリックする"
            MvcResult result = mockMvc.perform(
                    TestHelper.postForm("/inquiry/input/01?move=next", inquiryInput01Form_001))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/02"))
                    .andReturn()
            MockHttpSession session = result.getRequest().getSession()

            and: "入力画面２で項目全てに入力して「前の画面へ戻る」ボタンをクリックする"
            mockMvc.perform(TestHelper.postForm("/inquiry/input/02?move=back", inquiryInput02Form_001)
                    .session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/01"))

            and: "入力画面１で「次へ」ボタンをクリックする"
            mockMvc.perform(TestHelper.postForm("/inquiry/input/01?move=next", inquiryInput01Form_001)
                    .session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/02"))

            then: "入力画面２が以前入力したデータがセットされて表示される"
            mockMvc.perform(get("/inquiry/input/02").session(session))
                    .andExpect(status().isOk())
                    .andExpect(html("#zipcode1").val(inquiryInput02Form_001.zipcode1))
                    .andExpect(html("#zipcode2").val(inquiryInput02Form_001.zipcode2))
                    .andExpect(html("#address").val(inquiryInput02Form_001.address))
                    .andExpect(html("#tel1").val(inquiryInput02Form_001.tel1))
                    .andExpect(html("#tel2").val(inquiryInput02Form_001.tel2))
                    .andExpect(html("#tel3").val(inquiryInput02Form_001.tel3))
                    .andExpect(html("#email").val(inquiryInput02Form_001.email))
        }

        @Test
        void "項目全てに入力して次へボタンをクリックすると入力画面３へ遷移し、前の画面へ戻るボタンを押して入力画面２へ戻ると以前入力したデータがセットされて表示される"() {
            when: "入力画面２で項目全てに入力して「次へ」ボタンをクリックする"
            MvcResult result = mockMvc.perform(TestHelper.postForm("/inquiry/input/02?move=next", inquiryInput02Form_001))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/03"))
                    .andReturn()
            MockHttpSession session = result.getRequest().getSession()

            and: "入力画面３で「前の画面へ戻る」ボタンをクリックする"
            mockMvc.perform(TestHelper.postForm("/inquiry/input/03?move=back", inquiryInput01Form_001)
                    .session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/02"))

            then: "入力画面２が以前入力したデータがセットされて表示される"
            mockMvc.perform(get("/inquiry/input/02").session(session))
                    .andExpect(status().isOk())
                    .andExpect(html("#zipcode1").val(inquiryInput02Form_001.zipcode1))
                    .andExpect(html("#zipcode2").val(inquiryInput02Form_001.zipcode2))
                    .andExpect(html("#address").val(inquiryInput02Form_001.address))
                    .andExpect(html("#tel1").val(inquiryInput02Form_001.tel1))
                    .andExpect(html("#tel2").val(inquiryInput02Form_001.tel2))
                    .andExpect(html("#tel3").val(inquiryInput02Form_001.tel3))
                    .andExpect(html("#email").val(inquiryInput02Form_001.email))
        }

        @Test
        void "入力チェックエラーのあるデータで「前の画面へ戻る」ボタンをクリックするとIllegalArgumentExceptionが発生する"() {
            setup: "入力チェックエラーになるデータを用意する"
            inquiryInput02Form_001.zipcode1 = "1"

            expect: "入力画面２の「前の画面へ戻る」ボタンをクリックする"
            Throwable thrown = catchThrowable({
                mockMvc.perform(
                        TestHelper.postForm("/inquiry/input/02?move=back", inquiryInput02Form_001))
                        .andExpect(status().isOk())
            })
            assertThat(thrown.cause).isInstanceOf(IllegalArgumentException)
        }

        @Test
        void "入力チェックエラーのあるデータで「次へ」ボタンをクリックするとIllegalArgumentExceptionが発生する"() {
            setup: "入力チェックエラーになるデータを用意する"
            inquiryInput02Form_001.zipcode1 = "1"

            expect: "入力画面２の「次へ」ボタンをクリックする"
            Throwable thrown = catchThrowable({
                mockMvc.perform(
                        TestHelper.postForm("/inquiry/input/02?move=next", inquiryInput02Form_001))
                        .andExpect(status().isOk())
            })
            assertThat(thrown.cause).isInstanceOf(IllegalArgumentException)
        }

    }

    @RunWith(SpringRunner)
    @SpringBootTest
    static class 入力画面3のテスト {
        private InquiryInput01Form inquiryInput01Form_001 =
                (InquiryInput01Form) new Yaml().load(getClass().getResourceAsStream("InquiryInput01Form_001.yaml"))
        private InquiryInput02Form inquiryInput02Form_001 = new InquiryInput02Form(
                zipcode1: "102"
                , zipcode2: "0072"
                , address: "東京都千代田区飯田橋１－１"
                , tel1: "03"
                , tel2: "1234"
                , tel3: "5678"
                , email: "taro.tanaka@sample.co.jp")
        private InquiryInput03Form inquiryInput03Form_001 = new InquiryInput03Form(
                type1: Type1Values.PRODUCT.value
                , type2: [Type2Values.ESTIMATE.value, Type2Values.CATALOGUE.value, Type2Values.OTHER.value]
                , inquiry: "これはテストです"
                , survey: ["1", "2", "3", "4", "5", "6", "7", "8"])

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
            mockMvc.perform(get("/inquiry/input/03"))
                    .andExpect(status().isOk())
                    .andExpect(html("select[name='type1'] option[selected]").notExists())
                    .andExpect(html("input[name='type2'][checked='checked']").notExists())
                    .andExpect(html("#inquiry").val(""))
                    .andExpect(html("input[name='survey'][checked='checked']").notExists())
        }

        @Test
        void "項目全てに入力して前の画面へ戻るボタンをクリックすると入力画面２へ戻り、次へ戻るボタンを押して入力画面３へ戻ると以前入力したデータがセットされて表示される"() {
            when: "入力画面１で項目全てに入力して「次へ」ボタンをクリックする"
            MvcResult result = mockMvc.perform(
                    TestHelper.postForm("/inquiry/input/01?move=next", inquiryInput01Form_001))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/02"))
                    .andReturn()
            MockHttpSession session = result.getRequest().getSession()

            and: "入力画面２で項目全てに入力して「次へ」ボタンをクリックする"
            mockMvc.perform(TestHelper.postForm("/inquiry/input/02?move=next", inquiryInput02Form_001)
                    .session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/03"))

            and: "入力画面３で項目全てに入力して「前の画面へ戻る」ボタンをクリックする"
            mockMvc.perform(TestHelper.postForm("/inquiry/input/03?move=back", inquiryInput03Form_001)
                    .session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/02"))

            and: "入力画面２で「次へ」ボタンをクリックする"
            mockMvc.perform(TestHelper.postForm("/inquiry/input/02?move=next", inquiryInput02Form_001)
                    .session(session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("**/inquiry/input/03"))

            then: "入力画面３が以前入力したデータがセットされて表示される"
            mockMvc.perform(get("/inquiry/input/03").session(session))
                    .andExpect(status().isOk())
                    .andExpect(html("select[name='type1'] option[selected]").val(inquiryInput03Form_001.type1))
                    .andExpect(html("input[name='type2'][checked='checked']").count(3))
                    .andExpect(html("#inquiry").val(inquiryInput03Form_001.inquiry))
                    .andExpect(html("input[name='survey'][checked='checked']").count(8))
        }

        @Test
        void "項目全てに入力して次へボタンをクリックすると確認画面へ遷移し、前の画面へ戻るボタンを押して入力画面３へ戻ると以前入力したデータがセットされて表示される"() {
            expect:
            assert false, "確認画面を実装してからテストを作成する"
        }

        @Test
        void "入力チェックエラーのあるデータで「次へ」ボタンをクリックするとIllegalArgumentExceptionが発生する"() {
            setup: "入力チェックエラーになるデータを用意する"
            inquiryInput03Form_001.type1 = ""

            expect: "入力画面３の「次へ」ボタンをクリックする"
            Throwable thrown = catchThrowable({
                mockMvc.perform(
                        TestHelper.postForm("/inquiry/input/03?move=next", inquiryInput03Form_001))
                        .andExpect(status().isOk())
            })
            assertThat(thrown.cause).isInstanceOf(IllegalArgumentException)
        }

    }

}
