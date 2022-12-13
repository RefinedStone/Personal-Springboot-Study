package com.example.PersonalSpringStudy.setting.emailing;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class EmailRequestDto {

    @Email
    private String email;

}
