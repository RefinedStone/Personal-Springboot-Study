package com.example.PersonalSpringStudy.test.native_query;

import com.example.PersonalSpringStudy.post.Post;

import java.util.List;


public interface NativeQueryTestRepositoryCustom {


    //Native SQL method
    List<?> nativeQuery();

    //QueryDsl method
    Post testQuery();

    //catch err
    Long cathErr();
}
