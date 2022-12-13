package com.example.PersonalSpringStudy.account.dto;

import com.example.PersonalSpringStudy.account.entity.Account;
import com.example.PersonalSpringStudy.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;


@NoArgsConstructor
@Getter
public class UserInfoDto {

    private String email;
    private String nickname;

    private List<Post> post;

    public UserInfoDto(Account account) {
        this.email = account.getEmail();
        this.nickname = account.getNickname();
      //  this.post = account.
    }

}
