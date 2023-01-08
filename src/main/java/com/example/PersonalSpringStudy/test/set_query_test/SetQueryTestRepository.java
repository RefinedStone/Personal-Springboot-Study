package com.example.PersonalSpringStudy.test.set_query_test;

import com.example.PersonalSpringStudy.comment.entity.QComment;
import com.example.PersonalSpringStudy.post.Post;
import com.example.PersonalSpringStudy.post.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.example.PersonalSpringStudy.post.QPost.post;

@Component
@Repository
public class SetQueryTestRepository {
    private final JPAQueryFactory queryFactory;

    public SetQueryTestRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /*Set으로 테스트??*/
    public Post QueryTest() {
        QPost qPost = post;
        QComment qComment = QComment.comment;

        return queryFactory
                .select(post)
                .from(post)
                .fetchOne();

    }
}
