package com.example.ReactSpringCollaborationProject.likes;

import com.example.ReactSpringCollaborationProject.account.service.entity.Account;
import com.example.ReactSpringCollaborationProject.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    //좋아요 등록
    @Transactional
    public boolean createLikes(Account account, Long postId, LikesRequestDto likesRequestDto) {

        postRepository.findById(postId).orElseThrow(RuntimeException::new);

        var r = likesRepository.findByAccountAndPostId(account, postId);
        if (r.isPresent()) {
            Likes likes = r.get();
            return (boolean) likes.setLikeCheck(!(likesRequestDto.getLikeCheck()));
        } else {
            Likes likes = new Likes(account, postId, likesRequestDto);
            likesRepository.save(likes);
            return (boolean) likes.getLikeCheck();
        }
    }

}
