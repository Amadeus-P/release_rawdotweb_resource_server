## 첫 프로젝트(미배포)

개발기간 <br/>
1차 24.07 ~ 24.11 (4.5개월) <br/>
2차 24.12 ~ <br/>

개발 인원 1명

프로젝트 진행도(24년 12월 기준) <br/>
리소스 서버  <br/>
등록, 조회, 수정, 삭제 (80%)

UI <br/>
등록, 조회 페이지 (40%)


개발환경 <br/>
Vue3(CSR, Composition API) Nuxt3(SSR + SEO) <br/>
Spring Boot 3.3.4(Resourse Server) <br/>
JPA(Hibernate-구현체, Criteria-동적쿼리) <br/>

하이브리드 앱(웹 기반 앱 제작 = 웹 + 앱) <br/>

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
