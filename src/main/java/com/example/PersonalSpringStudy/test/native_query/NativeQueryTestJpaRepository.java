package com.example.PersonalSpringStudy.test.native_query;

import com.example.PersonalSpringStudy.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NativeQueryTestJpaRepository extends JpaRepository<Post, Long>, NativeQueryTestRepositoryCustom {
    @Query(value = "SELECT P.POST_ID FROM POST P", nativeQuery = true)
    List<Long> findNativeQuery1();
    //인터페이스에 String을 선언하면 final 값으로 '상수(constant)'가 된다.
    //결국, @Query로는 Native SQL의 동적 쿼리를 만들 수 없다.
    // query에 native SQL을 넣고 @Param("query")로 변수화 해보려고 했지만 실패
    String finalStringValue = "SELECT P.POST_ID FROM POST P";
    @Query(value = finalStringValue, nativeQuery = true)
    List<Long> findNativeQuery2(@Param("query") String sb);
}
