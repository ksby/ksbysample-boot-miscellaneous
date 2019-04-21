package ksbysample.webapp.samplewebapp;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/sample")
public class SampleController {

    private final RestTemplate restTemplate;

    public SampleController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping
    @ResponseBody
    public String index() {
        PlusForm plusForm = new PlusForm();
        plusForm.setV1("1");
        plusForm.setV2("2");

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<PlusForm> request = new HttpEntity<>(plusForm, httpHeaders);

        ResponseEntity<PlusResponse> response =
                restTemplate.postForEntity("/plus", request, PlusResponse.class);

        return response.getBody().getResult();
    }

}
