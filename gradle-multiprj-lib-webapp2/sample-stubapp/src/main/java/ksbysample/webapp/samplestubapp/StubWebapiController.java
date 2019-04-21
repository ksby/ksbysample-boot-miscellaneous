package ksbysample.webapp.samplestubapp;

import ksbysample.lib.samplelib.StrNumUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StubWebapiController {

    @PostMapping("/plus")
    public PlusResponse plus(@RequestBody PlusForm plusForm) {
        String result = StrNumUtils.plus(plusForm.getV1(), plusForm.getV2());
        PlusResponse plusResponse = new PlusResponse(result);
        return plusResponse;
    }

}
