package ksbysample.webapp.samplewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SampleWebappApplication {

    private final RestTemplateBuilder restTemplateBuilder;

    public SampleWebappApplication(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleWebappApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return this.restTemplateBuilder
                .rootUri("http://localhost:9080")
                .build();
    }

}
