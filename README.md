# :books:도서 추천 api
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
[**회원 가입**](#회원-가입) | [**로그인**](#로그인)

[**전체 조회**](#전체-조회) | [**id, isbn으로 도서 조회**](#id-isbn으로-도서-조회) | [**도서 등록**](#도서-등록) | [**도서 수정**](#도서-수정) | [**도서 삭제**](#도서-삭제)


<br/>
<br/>

## :tipping_hand_person:	유저 api

### 회원 가입
`spring-boot-starter-oauth2-resource-server`와 `com.auth0:java-jwt`를 추가하여 구현했습니다.

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
<div align="right">
    
[**:pushpin: 목차로**](#pushpin-목차)

</div>


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

<div align="right">
    
[**:pushpin: 목차로**](#pushpin-목차)

</div>


## :books:도서 api

<br/>


조회는 비 로그인으로 조회 가능하도록 구현하였습니다.

Pageable 인터페이스로 페이징처리 하였으며, 페이지당 5건, 생성된 id를 기준으로 내림차순으로 정렬하였습니다. 

공통적으로 전체 조회, 단건 조회시 rest api의 구현 조건인 Hateoas를 만족하기 위해 `spring-boot-starter-hateoas`를 추가 하여 구현 했습니다.

RepresentationModelAssemblerSupport 추상 클래스를 상속받아 자신, 모든 책, update, delete url을 응답에 담았습니다.

아래는 도서 CURD 관련 구현한 주요 클래스입니다

[com.myownbook.api.controller.BookController](https://github.com/ssac17/my-own-book/blob/main/src/main/java/com/myownbook/api/controller/BookController.java)

[com.myownbook.api.service.BookService](https://github.com/ssac17/my-own-book/blob/main/src/main/java/com/myownbook/api/service/BookService.java)

[com.myownbook.api.controller.BookResponseDTOAssembler](https://github.com/ssac17/my-own-book/blob/main/src/main/java/com/myownbook/api/controller/BookResponseDTOAssembler.java)

[com.myownbook.api.config.ImageConfig](https://github.com/ssac17/my-own-book/blob/main/src/main/java/com/myownbook/api/config/ImageConfig.java)

<br/>

### 전체 조회

<br/>

>요청
- URL: /books
- HTTP Method: GET

<br/>

>응답

-  응답 코드: <span>$\color{green}200 - OK$</span>

```json
{
    "_embedded": {
        "bookResponseDTOList": [
            {
                "id": 10,
                "title": "주술회전 30 더블특장판",
                "author": "아쿠타미 게게",
                "isbn": "979-114-280-355-0",
                "publicationDate": "2025",
                "category": "CARTOON",
                "recommend": 4,
                "image": {
                    "id": 10,
                    "imagePath": "/static/image/주술회전_30_더블특장판.jpeg",
                    "thumbnailPath": ""
                },
                "user": {
                    "id": 1,
                    "username": "sky",
                    "role": "ROLE_USER"
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/books/id/10"
                    },
                    "all-books": {
                        "href": "http://localhost:8080/books"
                    },
                    "update-book": {
                        "href": "http://localhost:8080/books/10"
                    },
                    "delete-book": {
                        "href": "http://localhost:8080/books/10"
                    }
                }
            },
                ... 생략
            {
                "id": 6,
                "title": "첫 여름, 완주",
                "author": "김금희",
                "isbn": "979-119-722-198-9",
                "publicationDate": "2025",
                "category": "NOVEL",
                "recommend": 4,
                "image": {
                    "id": 6,
                    "imagePath": "/static/image/첫_여름_완주.jpeg",
                    "thumbnailPath": ""
                },
                "user": {
                    "id": 1,
                    "username": "sky",
                    "role": "ROLE_USER"
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/books/id/6"
                    },
                    "all-books": {
                        "href": "http://localhost:8080/books"
                    },
                    "update-book": {
                        "href": "http://localhost:8080/books/6"
                    },
                    "delete-book": {
                        "href": "http://localhost:8080/books/6"
                    }
                }
            }
        ]
    },
    "_links": {
        "first": {
            "href": "http://localhost:8080/books?page=0&size=5&sort=id,desc"
        },
        "self": {
            "href": "http://localhost:8080/books"
        },
        "next": {
            "href": "http://localhost:8080/books?page=1&size=5&sort=id,desc"
        },
        "last": {
            "href": "http://localhost:8080/books?page=1&size=5&sort=id,desc"
        }
    },
    "page": {
        "size": 5,
        "totalElements": 10,
        "totalPages": 2,
        "number": 0
    }
}
```

<img width="1064" height="802" alt="스크린샷 2025-11-23 오후 3 57 08" src="https://github.com/user-attachments/assets/e6f0bc45-cb61-4ee0-a52e-0ece3813407d" />

<br/><br/>

<div align="right">
    
[**:pushpin: 목차로**](#pushpin-목차)

</div>

### id isbn으로 도서 조회


id로 조회, isbn 조회는 단순 조회로 postman 캡처본만 첨부 하겠습니다.

<br/>

>응답


<img width="1073" height="885" alt="스크린샷 2025-11-23 오후 4 04 40" src="https://github.com/user-attachments/assets/1ff24f10-c123-4973-a651-3fc29dd1a063" />

<br/>

<img width="1071" height="893" alt="스크린샷 2025-11-23 오후 4 05 10" src="https://github.com/user-attachments/assets/e6845f66-d917-4ed7-8707-9c7064749401" />

<br/><br/>

### 도서 등록

도서 등록 경우 로그인 후 가능 하므로 Bearer Token을 넣고 요청합니다.
도서 이미지를 추가 하기 위해 form 데이터로 요청합니다.

<br/>

>요청

- URL: /books/add
- HTTP Method: POST
- Bearer Token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiVVNFUiJdLCJpc3Mi...
- form

```from
title='공허의 시대',
author='조남호',
publicationDate=2025,
category=HOBBY,
recommend=2
file=공허의_시대.jpeg
```

<br/>

>응답
-  응답 코드: <span>$\color{green}200 - OK$</span>

```json
{
    "id": 11,
    "title": "공허의 시대",
    "author": "조남호",
    "isbn": "978-890-129-700-2",
    "publicationDate": "2025",
    "category": "HOBBY",
    "recommend": 2,
    "image": {
        "id": 11,
        "imagePath": "/static/image/공허의_시대_9de1d9a4-2496-4432-9c0a-166d8aa85fae.jpeg",
        "thumbnailPath": ""
    },
    "user": {
        "id": 2,
        "username": "user",
        "role": "ROLE_USER"
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/books/id/11"
        },
        "all-books": {
            "href": "http://localhost:8080/books"
        },
        "update-book": {
            "href": "http://localhost:8080/books/11"
        },
        "delete-book": {
            "href": "http://localhost:8080/books/11"
        }
    }
}
```


<img width="1071" height="934" alt="스크린샷 2025-11-23 오후 4 28 34" src="https://github.com/user-attachments/assets/692c0a46-b713-48ae-9e99-1751929ad9fd" />


<br/><br/>

>이미지 추가 확인
<img width="406" height="147" alt="스크린샷 2025-11-23 오후 4 28 46" src="https://github.com/user-attachments/assets/16ddb8e8-4321-4bfb-a6ed-6196ff322904" />

<br/>
<br/>

응답 받은 imagePath 호출시 이미지를 불러올수 있습닏다.

<br/>

<img width="1070" height="649" alt="스크린샷 2025-11-23 오후 4 33 06" src="https://github.com/user-attachments/assets/cbab9594-e7c1-4f67-a377-1b51476ad162" />

<br/><br/>

<div align="right">
    
[**:pushpin: 목차로**](#pushpin-목차)

</div>


### 도서 수정


>요청

- URL: books/{id}
- HTTP Method: PATCH
- Bearer Token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiVVNFUiJdLCJpc3Mi...
- Body

```json
{
    "title": "제목 수정",
    "author": "저자 수정",
    "category": "HOBBY",
    "isbn": "978-890-129-700-2",
    "publicationDate": "2025",
    "recommend": 3
}
```

</br>

>응답
-  응답 코드: <span>$\color{green}200 - OK$</span>

```json
{
    "id": 1,
    "title": "제목 수정",
    "author": "저자 수정",
    "isbn": "978-890-129-700-2",
    "publicationDate": "2025",
    "category": "HOBBY",
    "recommend": 3,
    "image": {
        "id": 1,
        "imagePath": "/static/image/하늘과_바람과_별과_시.jpeg",
        "thumbnailPath": ""
    },
    "user": {
        "id": 1,
        "username": "sky",
        "role": "ROLE_USER"
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/books/id/1"
        },
        "all-books": {
            "href": "http://localhost:8080/books"
        },
        "update-book": {
            "href": "http://localhost:8080/books/1"
        },
        "delete-book": {
            "href": "http://localhost:8080/books/1"
        }
    }
}
```

<img width="1070" height="1024" alt="스크린샷 2025-11-23 오후 7 41 42" src="https://github.com/user-attachments/assets/c3640854-6ae2-48ea-8b38-a22a0b59fe6f" />

<br/><br/>

<div align="right">
    
[**:pushpin: 목차로**](#pushpin-목차)

</div>

### 도서 삭제

도서 삭제시에는 Body를 반환하지 않고 header에 location으로 돌아갈 도서 전체 조회 url을 넣어 주었습니다

>요청

- URL: books/{id}
- HTTP Method: DELETE
- Bearer Token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiVVNFUiJdLCJpc3Mi...

>응답
-  응답 코드: <span>$\color{green}204 - No Content$</span>

<img width="1075" height="497" alt="스크린샷 2025-11-23 오후 7 48 17" src="https://github.com/user-attachments/assets/1ab3a8a8-ca74-4efa-b871-68d9d85515bd" />


<br/><br/>

<div align="right">
    
[**:pushpin: 목차로**](#pushpin-목차)

</div>












