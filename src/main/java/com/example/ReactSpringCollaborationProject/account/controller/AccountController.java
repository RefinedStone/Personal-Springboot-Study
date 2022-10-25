package com.example.ReactSpringCollaborationProject.account.controller;

import com.example.ReactSpringCollaborationProject.account.service.entity.dto.AccountReqDto;
import com.example.ReactSpringCollaborationProject.account.service.entity.dto.LoginReqDto;
import com.example.ReactSpringCollaborationProject.account.service.AccountService;
import com.example.ReactSpringCollaborationProject.account.service.jwt.util.JwtUtil;
import com.example.ReactSpringCollaborationProject.global.dto.GlobalResDto;
import com.example.ReactSpringCollaborationProject.global.dto.ResponseDto;
import com.example.ReactSpringCollaborationProject.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    @PostMapping("/signup")
    public GlobalResDto signup(@RequestBody @Valid AccountReqDto accountReqDto) {
        return accountService.signup(accountReqDto);
    }

    @PostMapping("/login")
    public GlobalResDto login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
        return accountService.login(loginReqDto, response);
    }

    @GetMapping("/issue/token")
    public GlobalResDto issuedToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getAccount().getEmail(), "Access"));
        return new GlobalResDto("Success IssuedToken", HttpStatus.OK.value());
    }


    @GetMapping("/mypost")
    public ResponseDto<?> getMyPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.getMyPost(userDetails.getAccount()));
    }
    @GetMapping("/mycomment")
    public ResponseDto<?> getMyComment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.getMyComment(userDetails.getAccount()));
    }

}
