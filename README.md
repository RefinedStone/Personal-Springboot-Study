# Personal-Spring-Backend-Study

개인 스프링 백앤드 서버 학습 레포지토리 입니다.

제가 배운 내용을 최대한 복습 하려고 노력하고 있습니다.

이 레퍼지토리는 학습의 개념을 가지고 있어서, 효율성이나 성능보다는 실험적 시도에 대한 내용들이 포함 되어 있습니다.

그 과정에서 클래스나 메소드 단계에서, 코드가 통일 되어 있지 않고 산만한 느낌을 주는 부분도 많습니다.



## ERD

현재 ERD diagram 입니다.
![image](https://user-images.githubusercontent.com/113455892/201755410-b7d9b153-3c58-4e43-9e58-d70948aefe48.png)



## Trouble Shooting

트러블 슈팅 내역 입니다.


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


## 2022 - 11 - 09 Update

오늘은 enum 타입을 도입 해보려고 합니다. enum을 쓰는 이유는 가독성이나 인스턴스 생성을 막기 위해서 정도 입니다.

### PostType.java
```java
public enum PostType {
    IMAGE,TEXT
}
```
enum 클래스를 만드는건 상당히 간단합니다. 저렇게 나열 형태로 선언 하면 되고, Convention은 전부 대문자로 쓰는것입니다.


### PostRequestDto.java
```java
public class PostRequestDto {
    private String title;
    private String contents;
    // JsonValue 어노테이션을 통해 타입을 받아준다
    @JsonValue private PostType type;
}
```
### Post.java
```java
    @Enumerated(EnumType.STRING)
    private PostType postType;
```
Entity에는 annotation만 주의 하면 됩니다.

오늘은 Enum타입을 매우 간단히 구현 해보았습니다. 


### 앞으로 올릴 내용

최근에 항해 99 최종 프로젝트를 진행중이라 굉장히 바쁜 와중입니다. QuaryDsl에 대해 공부중인데, 백엔드 공부 레포지토리에도 그 내용을 활용하여 코드를 작성하고 싶네요

두번째는 쿼리 최적화를 위한 즉시로딩과 지연로딩에 관해서 코드를 작성해보려고 합니다



## 2022 - 11 - 12 Update

오늘은 Amazon S3를 통해 유저 프로필 사진 update 기능을 추가 하였습니다.

S3를 이용한 이미지 업로드 기능은 사실 프론트에서 하는게 맞습니다.. 효율적으론 그렇지만.. 프론트엔드 협업분들에게 모든것을 요구 할 수는 없기에, 프로젝트에 썼던 경험이 있습니다.

그때의 기억을 되살려서 코드를 작성중입니다.

S3 기본세팅은 저의 레포지토리에서 참고 하여 사용하였습니다
https://github.com/RefinedStone/aws-s3-setting-success.git

### AccountController.java
```java
 //내 프로필 편집하기
    @PatchMapping
    public ResponseDto<?> editMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart AccountReqDto accountReqDto, MultipartHttpServletRequest request) throws IOException {
        MultipartFile file = request.getFile("profileImg");
        return accountService.editMyInfo(userDetails.getAccount(),accountReqDto, file);
    }
```


### AccountService.java
```java
   public ResponseDto<?> editMyInfo(Account account, AccountReqDto accountReqDto, MultipartFile file) throws IOException {
        // 아이디 존재 여부 체크
        accountRepository.findById(account.getId()).orElseThrow(RuntimeException::new);
        //img 업로드 & return 값 받기
        Map<String, String> profile = s3UploadUtil.upload(file, "profile");
        // Account repo에 url 및 key 저장
        account.update(accountReqDto,profile);
        return ResponseDto.success("Profile edited");
    }
```

### Account.java
```java
public void update(AccountReqDto accountReqDto, Map<String,String> urlMap) {
        this.email = accountReqDto.getEmail();
        this.password = accountReqDto.getPassword();
        this.nickname = accountReqDto.getNickname();
        this.imgUrl = urlMap.get("url");
        this.imgKey = urlMap.get("key");
    }
}
```

아직 AccountService 단에서 수정해줘야 할 부분들이 많습니다.  detail하게 변경해야 할 부분은 많아 보입니다.


### Update 할 내용
1.앞전에 말했던 QuaryDsl을 적용하려고 준비중입니다.
2.이미지를 list로 넣는 방법을 post 기능에 추가하려고 합니다.


## 2022 - 11 - 13 update

오늘은 IntelliJ의 기능 중 하나인 DATABASE에서 erd를 그리는 기능을 도입 해 보았습니다. 학습용 레퍼지토리라 늘 설계가 변형이 이루어 지고 있엇는데, 이 방식을 도입하면 늘 최신상태의 ERD를 유지 할 수 있을것 같습니다.
ERD 세팅은 제 블로그에 있습니다

-> [blog: 자동 ERD 생성](https://chem-en-9273.tistory.com/77) <br>


현재 ERD 구조를 보겠습니다
![image](https://user-images.githubusercontent.com/113455892/201513867-824196b9-4cc2-4c25-899a-76a6918958c0.png)

 

## 2022 - 11 -15 update

오늘은 Entity에 연관관계 ManyToOne OneToMany에 fetchtype을 Lazy로 변경하였습니다. 그 이유는 당연히 코드 안정성과 최적화를 위해서 입니다.


우선적으로 모든 경우에 Lazy로 걸어주고, 나중에 fetch join이나 기타 방법으로 풀어주는것이 정론이라 저도 그 방법을 따르기로 했습니다.

물론 OneToMany의 경우에는 default가 Lazy이기 때문에 건들지 않고, ManyToOne인경우에만 설정을 해주면 됩니다.

예를들면,

### Post.java
```java
    @JsonIgnore //JPA 순환참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_Id")
    private Account account;

    //One post to Many comment
    @OneToMany(mappedBy = "post") // default = lazy
    private List<Comment> comment;
```


이런 형태로 수정하면 됩니다.

앞으로의 코드는 이 Lazy에 맞춰  n+1 문제를 발생시키지 않는 방향으로 이루어질 예정입니다.





오늘의 두번째 update 입니다

Post와 Comment 연관관계를 확고히 하는 코드 리팩토링을 하였습니다.

간단한 부분이라 코드는 생략하고

변화된 ERD를 보겠습니다


### ERD 수정
![image](https://user-images.githubusercontent.com/113455892/201755410-b7d9b153-3c58-4e43-9e58-d70948aefe48.png)


## 2022 - 12 -13 update

오늘은 이메일 인증 서비스를 구현 해보았습니다. 물론 RestApi 스타일을 상정하고 구현하였습니다.

interface JavaMailSender를 기본적으로 이용합니다.

이 인터페이스는 package org.springframework.mail.javamail에 존재합니다. 즉 스프링프레임워크에서 제공하고 있습니다.

가장 핵심인 EmailingService의 로직을 간단히 설명 해보겠습니다.

로직은 간단합니다

랜덤 인증코드 생성 -> 메일 양식 작성 -> 실제 코드 발송


위에서 말씀드린 JavaMailSender가 실질적으로 메일을 보내는 기능이라고 생각하면 됩니다.


### EmailingService.java
```java


@Service
@RequiredArgsConstructor
public class EmailingService {

    private final AccountRepository accountRepository;
    //mailSender 인증용
    private final JavaMailSender emailSender;
    private String authNum;


    //랜덤 인증 코드 생성
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
    }

    //메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        createCode(); //인증 코드 생성
        String setFrom = "chem.en.9273@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String toEmail = email; //받는 사람
        String title = "Veloce 개인 스프링 서버 입니다"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText("<div style=\"margin:100px;\">" +
                "<h1> Veloce 인증번호 입니다.</h1>\n<br>" +
                "<p> 아래 코드를 회원가입 창으로 돌아가 입력해주세요.</p>\n<br>" +
                "<h2>인증번호 : " + authNum + "</h2>" +
                "<br/>\n" +
                "</div>", "utf-8", "html");

        return message;
    }

    //실제 메일 전송
    public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {
        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);
        //실제 메일 전송
        emailSender.send(emailForm);
        return authNum; //인증 코드 반환
    }


}
```

적용모습

![image](https://user-images.githubusercontent.com/113455892/207448402-610f3f36-21da-4f29-9b43-01be0ef9c977.png)

![image](https://user-images.githubusercontent.com/113455892/207448460-bbaf52fc-1463-45e5-9736-79f3bfe4f26a.png)


postman에서 response로 주는 값과, 실제 이메일이 온 값이 일치하는지를 확인하는 로직이 클라이언트단에서 있으면, 메일 인증 기능이 잘 작동 할 것입니다.

또한, 이 기능은 기본 이메일 회원가입의 무분별함을 막기 위한 장치라고 볼 수 있습니다.

email을 통한 회원가입기능 이외에, 구글,카카오등 OAuth 로그인을 구현해볼까 합니다.


### Update 할 내용
1.구글, 카카오 OAuth로그인 구현하기



## 2022-12-14 update

application.properties에는 민감한 정보들이 저장되는 경우가 많습니다. 이 경우에 github라던가에 .gitignore를 통해 올리지 않을 수 있겠지만, CI-CD라던가 다양한 니즈에서 암호화가 필요합니다.

그래서 이번에는 암호화를 할 수 있는 Jasypt를 적용시켜 보았습니다.

좀 더 자세한 사항은 저의 블로그에 기록 되어 있습니다.

블로그 가기https://chem-en-9273.tistory.com/101

적용 모습
![image](https://user-images.githubusercontent.com/113455892/207447806-9ac63606-ec4f-4fed-9f71-11de355e2438.png)

## 2023-1-08 update

@RequestParam 대해 학습해봅시다.

https://chem-en-9273.tistory.com/145

## 2023-1-09 update

JPA와 NativeSQL에 대해 고민해보고, 직접 구현해봅시다.(1)

https://chem-en-9273.tistory.com/139

## 2023-1-11 update

JPA와NativeSQL에 대해 고민해보고, 직접 구현해봅시다.(2)

https://chem-en-9273.tistory.com/140
