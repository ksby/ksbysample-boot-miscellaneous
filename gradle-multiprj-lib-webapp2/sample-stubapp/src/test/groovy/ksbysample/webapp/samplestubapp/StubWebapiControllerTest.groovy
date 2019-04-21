package ksbysample.webapp.samplestubapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class StubWebapiControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    @Unroll
    def "plus WebAPI のテスト(#v1, #v2 --> #result)"() {
        expect:
        mvc.perform(post("/plus")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("""
                                    {
                                      "v1": ${v1},
                                      "v2": ${v2}
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.result').value(result))

        where:
        v1    | v2  || result
        "1"   | "2" || "3"
        "999" | "1" || "1000"
    }

}
