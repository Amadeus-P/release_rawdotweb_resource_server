
## 프로젝트

-- 개발기간<br/>
24.07 ~

<br/>
-- 개발 인원 <br/>
1명
<br/>

-- 개발환경<br/>
Spring Boot 3.3.4<br/>
MariaDB

라이브러리: JPA, ModelMapper, Lombok<br/>
보안: JWT 사용<br/>

## 모놀로식 Layered 하이브리드 아키텍처 <br/>
<pre>
SIWA_Project
├── .idea
├── .mvn
├── .vscode
├── rawdotweb
├── src
│   └── main
│       └── java
│           └── com.main.web.siwa
│               ├── admin
│               │   ├── category
│               │   │   ├── controller
│               │   │   ├── dto
│               │   │   ├── service
│               │   ├── member
│               │   │   ├── controller
│               │   │   ├── dto
│               │   │   ├── service
│               │   └── website
│               │       ├── controller
│               │       ├── dto
│               │       ├── service
│               ├── auth
│               │   ├── controller
│               │   ├── dto
│               │   ├── service
│               ├── config
│               ├── entity
│               ├── error
│               ├── filter
│               ├── ghost
│               │   ├── controller
│               │   ├── dto
│               │   ├── service
│               ├── member
│               │   ├── comment
│               │   │   ├── controller
│               │   │   ├── dto
│               │   │   ├── service
│               │   ├── profile
│               │   │   ├── controller
│               │   │   ├── dto
│               │   │   ├── service
│               │   └── website
│               │       ├── controller
│               │       ├── dto
│               │       ├── service
│               ├── repository
│               ├── utility
│               └── websiteImage
│                   ├── controller
│                   ├── dto
│                   ├── service
└── SiwaProjectApplication

</pre>


## 홈페이지(미배포)
https://www.rawdotweb.com
