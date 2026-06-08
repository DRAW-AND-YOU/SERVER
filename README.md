
<img width="854" height="288" alt="image" src="https://github.com/user-attachments/assets/a72a5eac-ba87-4629-8ada-003d52695743" />

# DRAW&YOU

그림·일기 기반의 감정 분석 및 치유 경험을 제공하는 **AI 웹 서비스**입니다.  
AI 는 사용자가 직접 그린 그림 또는 업로드한 이미지를 분석하고, 
개인 맞춤형 치유 코스와 힐링 콘텐츠를 추천합니다. 


개발기간 2025.09.01 ~ 2025.12

-----


# Project Architecture

<img width="2971" height="1618" alt="drawandyou_architecture drawio" src="https://github.com/user-attachments/assets/bea0bdb3-415a-4550-b134-797bf3a08da5" />

-------

# 기술 스택 

| Category | Technology |
|----------|------------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.5.5 |
| **Database** | MySQL, Redis |
| **ORM** | Spring Data JPA, QueryDSL 5.0 |
| **Authentication** | Spring Security, OAuth2, JWT |
| **API Docs** | Swagger (springdoc-openapi 2.8.8) |
| **Cloud Storage** | AWS S3 |
| **HTTP Client** | Spring WebFlux (WebClient) |
| **Build Tool** | Gradle |
| **Deployment** | Github Actions , AWS ECR , AWS EC2 |

-----

# 제공 기능 

### 🟩 그림 분석 
- AI 기반 그림 감정 분석
- 분석 결과 저장 및 히스토리 관리
- 맞춤형 콘텐츠 추천

### 🟩 치유 프로그램 
- 개인 맞춤형 치유 코스 제공
- 일별 코스 진행 및 완료 관리
- 프로그램 진행 상태 추적

### 🟩 감정 일기 
- 일기 작성 및 관리
- 캘린더 기반 일기 조회

### 🟩 커뮤니티 
- 게시글 작성/조회/수정/삭제
- 무한 스크롤 기반 피드
- 좋아요 및 댓글 기능
- 인기 게시글 조회

### 🟩 사용자 관리 
- 회원가입/로그인 (일반, OAuth2)
- 마이페이지
- 대시보드 통계

-----

# API 문서

```
https://api.drawandyou.com/swagger-ui/index.html
```

-----

# Database Design (ERD)
(database 설계 초기에는 jpa 연관관계 매핑을 사용하였으나, 추후에는 연관관계 매핑을 진행하지 않는 방향으로 수정)

<img width="2020" height="1262" alt="DRAW AND YOU" src="https://github.com/user-attachments/assets/ac842fd3-bf88-4243-bd82-d99b5390e923" />
