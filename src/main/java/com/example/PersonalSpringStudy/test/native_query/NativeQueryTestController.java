package com.example.PersonalSpringStudy.test.native_query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/api")

@RequiredArgsConstructor
@RestController
public class NativeQueryTestController {
    private final NativeQueryTestJpaRepository nativeQueryTestJpaRepository;
    private final NativeQueryRepository nativeQueryRepository;



    //@Query(value = "SELECT P.POST_ID FROM POST P", nativeQuery = true)
//    @Transactional(readOnly = true)
    @GetMapping("/test/v1/query")
    public List<?> testQuery1() {
        return nativeQueryTestJpaRepository.findNativeQuery1();
    }

    //Query(value = "SELECT P.POST_ID FROM POST P", nativeQuery = true))
//    @Transactional(readOnly = true)
    @GetMapping("/test/v2/query")
    public List<?> testQuery2() {
        return nativeQueryTestJpaRepository.findNativeQuery2("SELECT P.POST_ID FROM POST P");
    }

    // Native SQL method를 작성하기 위해 새로운 repository를 만들어야 한다.
//    @Transactional(readOnly = true)
    @GetMapping("/test/v3/query")
    public List<?> testQuery3() {
        return nativeQueryRepository.nativeQuery();
    }

    //Querydsl과 Native SQL을  JPArepository 하나로 이용하도록 변경
//    @Transactional(readOnly = true)
    @GetMapping("/test/v4/query")
    public List<?> testQuery4() {
        return nativeQueryTestJpaRepository.nativeQuery();
    }

    /*참고
    * @Transactional(readOnly = true)을 메소드마다 붙인 이유
    * 1. 읽기 메소드이기 때문이다.
    * 2. 복잡한 쿼리의 대다수가 DB에서 데이터의 읽기를 위한 경우가 많다
    * 3. Class에 어노테이션을 붙이는것이 아닌 메소드 위에 붙이는 것은, 코드철학적 개념이다
    * -> 지금은 학습개념상 service와 controller를 분리 하지 않았지만,
    * -> 실제로는 @Transactional은 serivce method에 붙인다.
    * -> Service method에 @Transactional(read=only)가 붙어있는것만 봐도,
    * -> 이것은 수정변경이 없는 조회용 메소드임을 인지할수있다.
    * */

}
