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

Entity의 @Getter 가 작동 안하는 오류가 발생하였다. 모든 멤버변수가 그런것이 아니라.. Boolean 타입의 변수인 likeCheck만 작동이 되질 않았다.

그래서, 따로 Getter와 Setter를 생성해주었다.

https://github.com/RefinedStone/Personal-Spring-Backend-Server/commit/6eee321598ef614710ad19571500d6e034443c4f#diff-8f94b52d357c4a980a3e0f8b65de107957af302d609e744b0a7acca618507d73R44-R51


Dto를 사용하는 이유에 대해서는 학습하였지만, like와 같이 true,false 한 값만 받아 오는 부분에서도 굳이 dto를 써야 하는것인가.. 하는 의문이 들었다.

물론, 배운대로 dto를 사용하여 코드를 작성 하였지만, 이런경우에 까지 굳이 써야 하는가에 대한 고민은 계속되고 있다.

### LikesService.java
```java
 //좋아요 등록
    @Transactional
    public boolean createLikes(Account account, Long postId, LikesRequestDto likesRequestDto) {
        
        postRepository.findById(postId).orElseThrow(RuntimeException::new);
        
        var r = likesRepository.findByAccountAndPostId(account, postId);
        if (r.isPresent()) {
            Likes likes = r.get();
            return (boolean) likes.setLikeCheck(!(likesRequestDto.getLikeCheck()));
        } else {
            Likes likes = new Likes(account, postId, likesRequestDto);
            likesRepository.save(likes);
            return (boolean) likes.getLikeCheck();
        }
    }
````

큰 로직은 2개로 나뉜다. 지금 좋아요가 레포지토리에 존재 하는지 안하는지로 나뉜다.

최근 프론트엔드와 많은 협업을 거치는 과정에서 느끼는게 있다.. 프론트에서 요청관리하기 쉽도록 response와 db table을 설계 해야 한다는 것이다

지금은 최소기능만 구현하도록 작성하느라  return 값을 boolean 타입으로 보내 주는데 이것이 과연 좋은 방법인가에 대한 의문이 있다.

효율성과 편리성에 대한 고민은 연관관계 관련 코드를 작성하고 난 뒤에 다시 해볼려고 한다.


## 2022 - 10 -31 update

최근 람다와 스트림에 대해 다시 공부를 하고 있습니다. 제 학습 레포지토리에도 스트림을 적용 시켜볼만한 코드가 있을까 하다가 좋은 부분을 발견하여 적용 시켜 보았습니다.

### AccountService.java
```java
->변경 전
    // 모든 글 읽어오기
    public List<PostResponseDto> getAllpost() {
        var postLists = postRepository.findAllByOrderByCreatedAtDesc();
        var postDtoLists = new ArrayList<PostResponseDto>();
        for (Post postList : postLists) {
            postDtoLists.add(new PostResponseDto(postList));
        }
      return postDtoLists;
    }
->변경 후
    // 모든 글 읽어오기
    public List<PostResponseDto> getAllpost() {
        var postLists = postRepository.findAllByOrderByCreatedAtDesc();
        var postDtoLists = postLists.stream().map(x->new PostResponseDto(x)).collect(Collectors.toList());
        return postDtoLists;
    }

```

당장의 코드에선 크게 이득은 없습니다 하지만..stream과 lamda의 학습의 관점에서는 도움이 되었다고 생각은 합니다.

또한 새로운 기법을 적용하다 보니 활용방법이 떠오르기도 하네요. 

예컨대, DB에서 명확하게 한번에 걸러 오지 않았다면, stream의 필터라던지 distinct등을 이용해서 원하는 값만 리스트로 뽑아낸다던지 등의 무궁무진한 방법이 있을 것 같습니다.

좀 더 어려운 로직에서 빛을 발할것 같아 기대가 됩니다. 더욱 stream과 lamda식을 깊게 파보아야 겠습니다

--추가 연습김에 극단적으로 코드를 줄여 보았습니다.

```java
    public List<PostResponseDto> getAllpost() {
        var postDtoLists = postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).collect(Collectors.toList());
        return postDtoLists;
    }
```

## 2022 -11 -02 update

항상 개발중에 느끼는 지점은 response의 관리가 어렵다 입니다. response에 넣을 값들을 dto를 통해 보내주는데, 무한히 많은 dto를 만들기에는 코드 재
사용성이 떨어집니다. 그래서 이 부분에 대한 연구를 해보았습니다.

첫번째는, 익명클래스를 이용하는 방법입니다.


### PostService.java
```java
public Post getOnePost(Account account) {
        Post post = postRepository.findById(10L).orElseThrow(RuntimeException::new);
        return new Post(post) {
            //Response에 nickname2라는 필드명을 추가하고 싶다!
            public String nickname2 = account.getNickname();
        };
    }
```


두번째 방법은 바로 dto단에서 어노테이션을 달아주고 생성자를 새로 만드는 방법입니다.

```java
@Getter
public class PostResponseDto {
    //null값이 들어온다면 이 필드를 무시하고 싶다!!
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
    
    //항상 nickname을 보여주고 싶진 않으니, 생성자를 추가하여 관리!!
    public PostResponseDto(Post post, Account account) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.email = post.getEmail();
        this.urlToString = post.getUrlToString();
        this.nickname = account.getNickname();
    }


}
```


결과만 말씀드리면 굉장히 짧지만.. 저의 짧은 지식으론 여기까지가 통용되는 방법인것 같습니다. 


## 2022 - 11 - 03 update

post에 좋아요의 갯수를 세는 api를 구현 하였습니다. 토글방식으로 구현 하였으며, 약간은 로직상 로스가 있다고 판단이 되네요. 추후에 좀더 발전 시켜 보아야 겠습니다.


### LikeService.java
```java
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
```

### Post.java
```java
 //초기값이 null이므로 0L로 초기값을 준다!
 @Column(nullable = true)
 private Long likesLength = 0L;
    
//라이크의 갯수를 추가하는 메소드
public void setLikesLength(boolean likesType) {
        this.likesLength = (likesType) ? this.likesLength + 1L : this.likesLength - 1L;
    }
```




