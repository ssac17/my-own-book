# :books:도서 api
도서 추천 서비스를 구현한 프로젝트입니다. (추천 기능은 추후 추가 예정)

기본적인 Create, Read, Update, Delete 기능을 구현했으며,

JWT 토큰을 사용한 로그인 기능과 토큰 인증을 한 사용자만 CRUD가 가능하도록 구현하였습니다.
<br/><br/>

## :computer:	기술 스텍
- 프레임워크: Spring Boot 3.5.5
- 언어: Java 21
- 데이터베이스: h2
- ORM: Spring Data JPA
- 빌드: Gradle

초기 데이터는 [src/main/resources/data.sql](https://github.com/ssac17/my-own-book/blob/main/src/main/resources/data.sql)에 작성하였습니다.
<br/>
<br/>
## :pushpin: 목차
[회원 가입](#회원-가입)

[로그인](#로그인)


<br/>
<br/>

## :tipping_hand_person:	유저 api

### 회원 가입
spring-boot-starter-oauth2-resource-server와 auto0:java-jwt를 추가하여 구현했습니다.

jwt토큰으로 구현한 회원가입과 로그인 기능은 [스프링 6와 스프링 부트 3로 배우는 모던 API 개발](https://wikibook.co.kr/spring-api-dev/) 책을 참고로 구현했습니다.

아래는 회원 가입, 로그인 관련 구현한 클래스입니다

[com.myownbook.api.config.SecurityConfig](https://github.com/ssac17/my-own-book/blob/main/src/main/java/com/myownbook/api/config/SecurityConfig.java)

[com/myownbook.api.service.UserService](https://github.com/ssac17/my-own-book/blob/main/src/main/java/com/myownbook/api/service/UserService.java)

[com/myownbook.api.security.JwtManager](https://github.com/ssac17/my-own-book/blob/main/src/main/java/com/myownbook/api/security/JwtManager.java)


<br/>

>요청

- URL: /users/signup
- HTTP Method: POST
- Body

```json
{
    "username": "user",
    "password": "1234"
}
```
<br/>

>응답
1. 이미 등록된 유저가 있는 경우
- 응답 코드: <span>$\color{red}400 -Bad Request$</span>

```json
{
    "code": "INVALID_PARAMETER",
    "message": "이미 등록된 유저입니다."
}
```

<img width="1074" height="474" alt="스크린샷 2025-11-22 오후 4 22 11" src="https://github.com/user-attachments/assets/ce65f40f-c940-41a8-a646-9b78f52706df" />

<br/><br/>

2. 회원 가입 성공시

-  응답 코드: <span>$\color{green}200 - OK$</span>

```json
{
    "message": "축하합니다! 회원가입 되었습니다.",
    "userId": 2,
    "username": "user",
    "refreshToken": "i7kip5cmucd7mdb410fb517v70rbqvnl3ft1vaktv4lcv3pnqcmk461olc8lhu4t4vhkr0eu8u2a85n9l4akbfvqcq8fm8jrsup8uaamalqd1hnkdkkgdsu2bunaltp5",
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiVVNFUiJdLCJpc3MiOiJPd24gQm9vayBBUEkiLCJleHAiOjE3NjM3OTcwNzQsImlhdCI6MTc2Mzc5NjE3NH0.Cge1bJ7SMZVz3MPcdvt5XjR-KE2_bZKsalhow18um1OwRqcNTfSxEidVxQdwMQWbxeP6-ToIZxiQRUgXTc_44OFYdDnUZFwsbP1pX-NdlS16VXD5Koq26UA1Lw6Q8DE9_sCVWOkMXrbi0o3j9cFei1_TMpi4-IZef7hULdBOOQeSGLbistGHtMwhuLpO6GMWrUjMKq0-sgJOj0pLltstImPMhBT4towpyBnYm-e4KJiZIzrfjNOJQ6PZJ-IS7Xp72TG_IJEDW4H--1tAGCMwj48gJk8eLD6s_DIk8a6FXFpGGaKrCiABS6nRl_1ox9QccNg0uKm0hBXU6TgypJsBo5zItXSxv_rZ9M0_g3m2fGQuxZWVnK89h1c9l3nsTP0qGEmdAicDL4vyfXBDBMnYEXiKuwX9n27baZjamxW4kh5v3DKltvvdAt8x2PqP4T-wqYjHfG4oC3QspEWX1VECx7Ly0Hczp_hJLn04IY7EvoRG-B5OPzMMeCD-MJovsFr4nUQ6I3ud5BbZaf3yMP4ZSqQNQH2KBb4pSI2aS-8rz-Ntb1ewgWWYGnt1i2wzkSHaS_d9WpkZtoUGmr2mpudUfsSeT09-vIY5TUOiXhaww5_vSJ8EcWK4D4cJ87FonNbl53W_NQgtaZ0rGhEgc9ybjYR6fMJbL7ij31WTMm9fd6M"
}
```

<img width="1061" height="691" alt="스크린샷 2025-11-22 오후 4 30 06" src="https://github.com/user-attachments/assets/ae845cea-ea68-445c-88b3-c646f2eb78d0" />

<br/><br/>

### 로그인

<br/>

>요청

- URL: /users/login<br>
- HTTP Method: POST<br>
- Body

```json
{
    "username": "user",
    "password": "1234"
}
```

<br/>

>응답


1. 등록된 사용가 없는 경우
-  응답 코드: <span>$\color{red}500 - Internal Server Error$</span><br/>

```json
{
    "code": "INTERNAL_SERVER_ERROR",
    "message": "사용자(user)를 찾을수 없습니다."
}
```

<img width="1072" height="452" alt="스크린샷 2025-11-22 오후 4 39 20" src="https://github.com/user-attachments/assets/e4c81422-df07-4f9c-8cd5-bcfefd3dc301" />

<br/><br/>

2. 로그인 성공시
-  응답 코드: <span>$\color{green}200 - OK$</span>
```json
{
    "message": "로그인 성공!",
    "userId": 2,
    "username": "user",
    "refreshToken": "r3t3u84sjatk2ib5o4db1o2ui0r11se5i4sap43fki1fmgc1hu2atqccp2n29d5ho1p6cj5peuv4o7e1397btpquq3fpst77pihruo7gjvrcbqbsnbt2u8simtt3jrah",
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiVVNFUiJdLCJpc3MiOiJPd24gQm9vayBBUEkiLCJleHAiOjE3NjM3OTg0NDgsImlhdCI6MTc2Mzc5NzU0OH0.cZzQXNCfxsRHO1dgTJWcF6GbDK7eYzzQ-nryWCwjxoSgDzGz5ov4gz-SbbJGpz-JoS3ZZx3o0Ji168jcd8Lrauq52NaMSfwsVHpCY2SBdH_Eu5BjYFIBU6YbggQGptSkS88jsy5JGho8aOlY3YJMC2WVgP3lwngtfenAp7upX9YIrlPykYfqPZPyh6CoWYsfknBl2FIcfC3C7Kqj7Hw-WavNCuDXswnRM6gyHxx1e7lgF-Nn68UoXKjKjsi5bGYpByeIxxO8-CuNNPzkyeQd9b3rJYKp1XQ8iypXYsfdbGllG_jCsDPkJTsOAJBkcZRGaOWMlizMoIRC8ZNPScL_5jByi37ubiKvT6yH5y5Jwy2msifFaDB2FSLN5e38-FNIf0cU4R9Gzhv0qjCRxNFFp5daf7aceAoeKD2_RBHQnZR_MEB4RBErOd8xTPscFhZVZqQbhTnhcOmsLw46l8bq11U0cnolSO0n_7IRWHpn4LqVxGpb1AVRXDNrhRkuK4FqiDKxFjwLt8odczeWEwxFDEgd36Nl7UkyFIyMUs91i0dkbJ08dubPbiVUKb52WM1wXz0BZWCFZCkjjfRq6Scvc6Wj7Mxq-983GB82N1NmkKMM8mhYvAQjr_CTIEpw6hvsLPIfamZ37YBXp7vSYwoFg9hsimAm3IIzdrBpjB5L9tw"
}
```

<img width="1059" height="668" alt="스크린샷 2025-11-22 오후 4 46 48" src="https://github.com/user-attachments/assets/8217bdf1-ebfb-49dc-be57-e718462b6c0e" />


<br/><br/>


