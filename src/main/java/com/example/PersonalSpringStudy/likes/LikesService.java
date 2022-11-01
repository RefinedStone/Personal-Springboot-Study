package com.example.PersonalSpringStudy.likes;

import com.example.PersonalSpringStudy.account.service.entity.Account;
import com.example.PersonalSpringStudy.post.Post;
import com.example.PersonalSpringStudy.post.PostRepository;
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

        Post post = postRepository.findById(postId).orElseThrow(RuntimeException::new);

        var r = likesRepository.findByAccountAndPost(account, post);
        if (r.isPresent()) {
            Likes likes = r.get();
            post.setLikesLength(likesRequestDto.getLikeCheck());
            return (boolean) likes.setLikeCheck(!(likesRequestDto.getLikeCheck()));
        } else {
            Likes likes = new Likes(account, post, likesRequestDto);
            likesRepository.save(likes);
            post.setLikesLength(likesRequestDto.getLikeCheck());
            return (boolean) likes.getLikeCheck();
        }

    }

}
