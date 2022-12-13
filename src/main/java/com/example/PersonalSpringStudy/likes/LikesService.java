package com.example.PersonalSpringStudy.likes;

import com.example.PersonalSpringStudy.account.entity.Account;
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
    public boolean createLikes(Account account, Long postId) {
        //게시글이 있는지 부터 체크합니다.
        Post post = postRepository.findById(postId).orElseThrow(RuntimeException::new);
        //Likes db에 해당 아이디와 포스트값으로 저장된 데이터가 있는지 판단
        var r = likesRepository.findByAccountAndPost(account, post);
        //db 데이터가 있으면
        if (r.isPresent()) {
            Likes likes = r.get();
            likes.setLikeCheck(!(likes.getLikeCheck()));
            post.setLikesLength(likes.getLikeCheck());
            return likes.getLikeCheck();
        }
        //db 데이터가 없으면
        else {
            Likes likes = new Likes(account, post);
            likesRepository.save(likes);
            post.setLikesLength(likes.getLikeCheck());
            return likes.getLikeCheck();
        }
    }

}
