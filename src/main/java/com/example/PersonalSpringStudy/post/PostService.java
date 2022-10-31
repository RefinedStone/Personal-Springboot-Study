package com.example.ReactSpringCollaborationProject.post;

import com.example.ReactSpringCollaborationProject.account.service.entity.Account;
import com.example.ReactSpringCollaborationProject.aws_s3.S3UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final S3UploadUtil s3UploadUtil;

    @Autowired
    public PostService(PostRepository postRepository, S3UploadUtil s3UploadUtil) {
        this.postRepository = postRepository;
        this.s3UploadUtil = s3UploadUtil;
    }


    // 모든 글 읽어오기
    public List<PostResponseDto> getAllpost() {
       // var postLists = postRepository.findAllByOrderByCreatedAtDesc();
        var postDtoLists = postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).collect(Collectors.toList());
        return postDtoLists;
    }

    //글 쓰기
//    @Transactional
//    public PostResponseDto createPost(PostRequestDto requestDto, Account account) {
//        Post post = new Post(requestDto, account);
//        postRepository.save(post);
//        return new PostResponseDto(post);
//    }

    @Transactional
    public PostResponseDto createPost(String contents, MultipartFile imgFile, Account account) throws IOException {
        if (!(imgFile == null)) {
            var r = s3UploadUtil.upload(imgFile, "test");
            Post post = new Post(contents, account, r);
            postRepository.save(post);
            return new PostResponseDto(post);
        } else {
            Post post = new Post(contents, account);
            postRepository.save(post);
            return new PostResponseDto(post);
        }
    }


    //글 수정
    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto, Long id, Account account) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("id 없습니다.")
        );

        if (!account.getEmail().equals(post.getEmail())) {
            throw new IllegalArgumentException("Email 불일치");
        }
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    //글 삭제
    @Transactional
    public String deletePost(Long id, Account account) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("id 없습니다.")
        );
        if (!account.getEmail().equals(post.getEmail())) {
            throw new IllegalArgumentException("email 불일치");
        }
        postRepository.deleteById(id);
        if (!(post.getUrlKey() == null)) {
            s3UploadUtil.delete(post.getUrlKey());
        }
        return "delete success";
    }

    //테스트
    public Optional<Post> getOnePost(Account account) {
        Optional<Post> post = postRepository.findById(15L);
        return post;
    }
}