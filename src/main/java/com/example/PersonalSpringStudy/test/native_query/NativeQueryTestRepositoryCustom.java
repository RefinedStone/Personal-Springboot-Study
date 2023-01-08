package com.example.PersonalSpringStudy.test.native_query;

import com.example.PersonalSpringStudy.post.Post;

import java.util.List;

public interface NativeQueryTestRepositoryCustom {
    //QueryDsl method
    Post testQuery1();

    //Native SQL method
    List<?> nativeQuery();
}
