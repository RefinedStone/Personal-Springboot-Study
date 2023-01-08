package com.example.PersonalSpringStudy.test.list_param;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class ParamTestController {

    private final ParamTestService paramTestService;

    @GetMapping("/v1")
    public void v1(@RequestParam(name = "list", required = false) List<String> list) {
        System.out.println(list);
    }
    @GetMapping("/v2")
    public void v2(@RequestParam(name = "list", required = false) List<ParamEnum> list) {
        System.out.println(list);
    }

}
