package com.example.ReactSpringCollaborationProject.account.service;

import com.example.ReactSpringCollaborationProject.account.repository.AccountRepository;
import com.example.ReactSpringCollaborationProject.account.repository.RefreshTokenRepository;
import com.example.ReactSpringCollaborationProject.account.service.entity.Account;
import com.example.ReactSpringCollaborationProject.account.service.entity.RefreshToken;
import com.example.ReactSpringCollaborationProject.account.service.entity.dto.AccountReqDto;
import com.example.ReactSpringCollaborationProject.account.service.entity.dto.LoginReqDto;
import com.example.ReactSpringCollaborationProject.account.service.jwt.dto.TokenDto;
import com.example.ReactSpringCollaborationProject.account.service.jwt.util.JwtUtil;
import com.example.ReactSpringCollaborationProject.comment.dto.CommentResponseDto;
import com.example.ReactSpringCollaborationProject.comment.entity.Comment;
import com.example.ReactSpringCollaborationProject.comment.repository.CommentRepository;
import com.example.ReactSpringCollaborationProject.global.dto.GlobalResDto;
import com.example.ReactSpringCollaborationProject.global.dto.ResponseDto;
import com.example.ReactSpringCollaborationProject.post.Post;
import com.example.ReactSpringCollaborationProject.post.PostRepository;
import com.example.ReactSpringCollaborationProject.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public GlobalResDto signup(AccountReqDto accountReqDto) {
        // email 중복 검사
        if (accountRepository.findByEmail(accountReqDto.getEmail()).isPresent()) {
            throw new RuntimeException("Overlap Check");
        }

        accountReqDto.setEncodePwd(passwordEncoder.encode(accountReqDto.getPassword()));
        Account account = new Account(accountReqDto);

        accountRepository.save(account);
        return new GlobalResDto("Success signup", HttpStatus.OK.value());
    }

    @Transactional
    public ResponseEntity<?> login(LoginReqDto loginReqDto, HttpServletResponse response) {

        Account account = accountRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(() -> new RuntimeException("Not found Account"));

        if (!passwordEncoder.matches(loginReqDto.getPassword(), account.getPassword())) {
            throw new RuntimeException("Not matches Password");
        }

        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getEmail());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(loginReqDto.getEmail());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginReqDto.getEmail());
            refreshTokenRepository.save(newToken);
        }
        //억지로 헤드 추가
        setHeader(response, tokenDto);
//        HttpHeaders head = new HttpHeaders();
//        head.add("Access_Token", tokenDto.getAccessToken());
//        return ResponseEntity.ok(response);
        return ResponseEntity.ok().body(response.getHeader("Access_Token"));

    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    public List<PostResponseDto> getMyPost(Account account) {

        var posts = postRepository.findAllByEmail(account.getEmail());
        var postResponseDtos = new ArrayList<PostResponseDto>();
        for (Post post : posts) {
            postResponseDtos.add(new PostResponseDto(post));
        }
        return postResponseDtos;
    }

    public List<CommentResponseDto> getMyComment(Account account) {
        var comments = commentRepository.findAllByEmail(account.getEmail());
        var commentResponseDtos = new ArrayList<CommentResponseDto>();
        for (Comment comment : comments) {
            commentResponseDtos.add(new CommentResponseDto(comment));
        }
        return commentResponseDtos;
    }

    //logout 기능
    @Transactional
    public ResponseDto<?> logout(String email) throws Exception {
        var refreshToken = refreshTokenRepository.findByAccountEmail(email).orElseThrow(RuntimeException::new);
        refreshTokenRepository.delete(refreshToken);
        return ResponseDto.success("Delete Success");
    }
}
