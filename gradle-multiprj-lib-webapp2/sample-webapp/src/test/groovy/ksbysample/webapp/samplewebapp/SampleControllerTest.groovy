package ksbysample.webapp.samplewebapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class SampleControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    @Autowired
    RestTemplate restTemplate

    def "/sample にアクセスすると 3 と表示される"() {
        setup:
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build()
        mockServer.expect(requestTo("http://localhost:9080/plus"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess('{"result": "3"}', MediaType.APPLICATION_JSON_UTF8))

        expect:
        mvc.perform(get("/sample"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"))
    }

}
