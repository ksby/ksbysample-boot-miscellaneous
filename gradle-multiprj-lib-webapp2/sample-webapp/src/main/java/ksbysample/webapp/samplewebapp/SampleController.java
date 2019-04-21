package ksbysample.webapp.samplewebapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sample")
public class SampleController {

    @RequestMapping
    @ResponseBody
    public String index() {
        return "sample-webapp";
    }

}
