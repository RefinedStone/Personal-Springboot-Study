# Personal-Spring-Backend-Server

개인 스프링 백앤드 서버 개발 레포지토리 입니다.

제가 배운 내용을 최대한 활용 하려고 노력하고 있습니다.<br>

## 2022 - 10 - 29 update
mySql -> h2-console세팅으로 변경 하였습니다. <br>
-> [blog: h2-console 사용하기](https://chem-en-9273.tistory.com/66) <br>
Logout 기능 구현 하였습니다(에러처리는 다시 확인 해야 합니다) <br>



### AccountService.java
```java
    // 비정상 요청을 되돌리기 위해 Transactional
    @Transactional
    public ResponseDto<?> logout(String email) throws Exception {
        var refreshToken= refreshTokenRepository.findByAccountEmail(email).orElseThrow(RuntimeException::new);
        refreshTokenRepository.delete(refreshToken);
        return ResponseDto.success("Delete Success");
    }
```
### AccountController.java

```java
@PostMapping(value = "/logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return accountService.logout(userDetails.getAccount().getEmail());

    }

```

기본적인 api를 구현 하고 난 뒤에는 코드 갈무리와, 에러 처리를 넣어야 할 것 같습니다. 최근에 FE팀과 협업 중인데,  BE서버만 개발할때와 달리 CORS에러에 직면하였습니다. 정상적으로 고쳐져야 할 문제들도 원인을 알 수 없는 이유로 FE쪽에서 헤더에 토큰값을 못받아오는 상황도 생겼습니다. 해결은 하였지만, 이 부분은 멘토분들도 이해하기 어렵다고 하더군요. 좀 더 공부 해볼 필요가 있겠습니다.

->[비정상적인 CORS에러 수정 commit내역](https://github.com/RefinedStone/Personal-Spring-Backend-Server/commit/1d62f22d4fd3045cd28100805d49ce77051fd7fa?diff=unified#diff-5931a1c89edd47981af4622fdf7dd3b4e3a8d9f821478da98de75b21ffaa44ddR49-R79)  
WebSecurityConfig.java
```java
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //cors 설정
        http.cors().configurationSource(configurationSource());
       }
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Excepti

    //cors 설정
    @Bean
    public CorsConfigurationSource configurationSource(){
    CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //헤더의 모두 승인인 "*"을 넣었지만 노출이 안되어 직접 Access_Token을 노출시키도록 하니 수정되었다.
        configuration.addExposedHeader("*");
        configuration.addExposedHeader("Access_Token");
}
```


### 앞으로 개발 할 것
좋아요 기능을 One to Many , Many To One 연관관계 설정하여 개발

저는 우선 연관관계를 무의미하게 막쓰는건 좋지 않다고 생각합니다.. 하지만 그 구조를 이해하고 공부를 하기 위해 조금은 오버스럽게 연관관계를 맺어 볼까 합니다.
최대한 다양한 기능을 활용하여 개발 해보도록 하겠습니다

## 2022 - 10 - 30 update
좋아요 기능 구현하기.

연관관계를 설정하여 구현하기로 했지만, 우선적으로 좋아요 기능을 러프하게 구현 한 뒤 해보려고 생각을 바꾸었습니다




