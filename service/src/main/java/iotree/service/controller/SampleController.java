package iotree.service.controller;

import iotree.service.dto.SampleReqDto;
import iotree.service.dto.SampleRespDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Value("${spring.profiles}")
    private String profiles;

    @GetMapping("/rest/hello")
    public String sampleGetRequest() {
        return "Hello " + profiles + " successfully.";
    }

    @PostMapping("/rest/sample")
    public SampleRespDto sampleRequest(@RequestBody SampleReqDto sampleReqDto) {
        SampleRespDto sampleRespDto = new SampleRespDto();
        try {
            sampleRespDto.setMessage("Hello world successfully.");
        } catch (Exception e) {
            sampleRespDto.setMessage("Some error occurred.");
        }
        return sampleRespDto;
    }
}
