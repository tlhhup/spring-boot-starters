package ort.tlh.spring.test.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SwaggerController")
public class SwaggerController {

    @ApiOperation(value = "测试")
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }

}
