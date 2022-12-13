package com.example.PersonalSpringStudy.setting.emailing;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailingController {

    private final EmailingService emailingService;

    //이메일 인증
    @PostMapping("/account/signup/email-check")
    public String mailConfirm(@RequestBody @Valid EmailRequestDto email) throws MessagingException, UnsupportedEncodingException {
        System.out.println("");
        return emailingService.sendEmail(email.getEmail());
    }


}

