package com.example.PersonalSpringStudy.account.controller;


import com.example.PersonalSpringStudy.account.dto.AccountReqDto;
import com.example.PersonalSpringStudy.account.dto.LoginReqDto;
import com.example.PersonalSpringStudy.account.service.AccountService;
import com.example.PersonalSpringStudy.global.dto.GlobalResDto;
import com.example.PersonalSpringStudy.global.dto.ResponseDto;
import com.example.PersonalSpringStudy.setting.jwt.util.JwtUtil;
import com.example.PersonalSpringStudy.setting.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    //회원가입
    @PostMapping("/signup")
    public GlobalResDto signup(@RequestBody @Valid AccountReqDto accountReqDto) {
        return accountService.signup(accountReqDto);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {

        return accountService.login(loginReqDto, response);
    }


    @GetMapping("/issue/token")
    public GlobalResDto issuedToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getAccount().getEmail(), "Access"));
        return new GlobalResDto("Success IssuedToken", HttpStatus.OK.value());
    }

    //내 포스트 불러오기
    @GetMapping("/mypost")
    public ResponseDto<?> getMyPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.getMyPost(userDetails.getAccount()));
    }

    //내 커멘트 불러오기
    @GetMapping("/mycomment")
    public ResponseDto<?> getMyComment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.getMyComment(userDetails.getAccount()));
    }

    //로그 아웃
    @PostMapping(value = "/logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return accountService.logout(userDetails.getAccount().getEmail());

    }

    //내 프로필 편집하기
    @PatchMapping
    public ResponseDto<?> editMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart AccountReqDto accountReqDto, MultipartHttpServletRequest request) throws IOException {
        MultipartFile file = request.getFile("profileImg");
        return accountService.editMyInfo(userDetails.getAccount(),accountReqDto, file);
    }
}
