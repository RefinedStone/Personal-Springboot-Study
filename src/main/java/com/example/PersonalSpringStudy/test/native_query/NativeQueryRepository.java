package com.example.PersonalSpringStudy.test.native_query;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class NativeQueryRepository {
    StringBuilder sb = new StringBuilder();
    @PersistenceContext
    EntityManager em;

    //method
    public List<?> nativeQuery() {
        //초기화
        sb.setLength(0);
        //"SELECT P.POST_ID FROM POST P";
        sb
                .append("SELECT").append(" ")
                .append("P.POST_ID").append(" ")
                .append("FROM").append(" ")
                .append("POST P");
        String sql = sb.toString();
        Query nativeQuery = em.createNativeQuery(sql);
        System.out.println(sql);
        List<?> resultList = nativeQuery.getResultList();
        return resultList;
    }
}
