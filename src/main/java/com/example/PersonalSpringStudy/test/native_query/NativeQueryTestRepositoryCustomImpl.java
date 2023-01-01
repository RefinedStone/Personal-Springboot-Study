package com.example.PersonalSpringStudy.test.native_query;

import com.example.PersonalSpringStudy.comment.entity.QComment;
import com.example.PersonalSpringStudy.post.Post;
import com.example.PersonalSpringStudy.post.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static com.example.PersonalSpringStudy.post.QPost.post;


@Component
//@Primary
public class NativeQueryTestRepositoryCustomImpl implements NativeQueryTestRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    static StringBuilder sb = new StringBuilder();


    public NativeQueryTestRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //    @PersistenceContext
    @PersistenceContext
    EntityManager em;


    /*QueryDsl*/
    @Override
    public Post testQuery() {
        QPost qPost = QPost.post;
        QComment qComment = QComment.comment;

        return queryFactory
                .select(post)
                .from(post)
                .fetchOne();
    }

    /*Native SQL*/
    @Override
    public List<?> nativeQuery() {
        //초기화 hint 메모리와 연산을 가장 아끼는 방법이다.
        sb.setLength(0);
        //"SELECT P.POST_ID FROM POST P";
        sb
                .append("SELECT").append(" ")
                .append("P.POST_ID").append(" ")
                .append("FROM").append(" ")
                .append("POST P");
        //인라인 금지
        String sql = sb.toString();
        Query nativeQuery = em.createNativeQuery(sql);
        //인라인 금지
        List<?> resultList = nativeQuery.getResultList();
        return resultList;
    }


    /*catch err*/
    @Override
    public Long cathErr() {
        return null;
    }
}
