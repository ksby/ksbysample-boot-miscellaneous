package ksbysample.app.backendapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webapi/sample")
public class SampleController {

    @PostMapping
    public ResponseEntity<SampleResponse> index() {
        SampleResponse sampleResponse =
                SampleResponse.builder()
                        .code("123")
                        .value("sample data")
                        .build();
        return new ResponseEntity<>(sampleResponse, HttpStatus.OK);
    }

}
