# 🎖️온라인 강의 플랫폼

## 1. 기획서

### 프로젝트 개요

본 프로젝트는 Spring Boot 기반의 마이크로서비스 아키텍처를 적용한 온라인 강의 플랫폼입니다. 강의 영상 업로드, 검색, 구매 및 수강 기능을 제공하며, 수강생과 강사로 구분된 서비스를 제공합니다.

### 목표 및 범위

**목표**: 온라인 강의 시장에 최적화된 확장 가능하고 유지보수가 용이한 플랫폼 구축

**범위**:
- **포함**: 사용자 인증, 강의 등록 및 조회, 결제, 동영상 스트리밍, 마이페이지
- **제외**: 실시간 스트리밍 기능, 모바일 앱 구현

### 타겟 사용자

- **일반 수강생**: 강의를 검색하고 구매 및 수강하는 사용자
- **강사**: 강의를 등록하고 강의 수익을 확인할 수 있는 사용자

### 주요 기능 목록

1. **회원가입 및 로그인 (JWT 인증)**
2. **강의 목록 조회 및 검색**
3. **강의 상세 페이지**
4. **강의 구매 및 결제 처리**
5. **강의 영상 스트리밍**
6. **강사 전용 강의 업로드/수정/삭제**

### 기술 스택

- **백엔드**: Spring Boot, Spring Cloud (Config, Eureka, Gateway), Spring Security, Spring Data JPA
- **프론트엔드**: React, Vite, Axios, CSS
- **DB**: MySQL
- **인프라**: AWS EC2, S3, RDS, CloudFront

### 마이크로서비스 구조

1. **Auth-Service**: 사용자 인증 및 권한 관리 (JWT 기반)
2. **User-Service**: 사용자 정보 관리 (수강생, 강사, 관리자)
3. **Course-Service**: 강의 등록, 조회, 수정, 삭제
4. **Order-Service**: 강의 구매 및 결제 내역 관리
5. **Streaming-Service**: S3 기반 강의 영상 스트리밍 처리
6. **Notification-Service**: 결제 성공, 강의 승인 등 이벤트 알림
7. **Gateway-Service**: API 게이트웨이 (라우팅, 인증 필터)
8. **Config-Service**: 공통 설정 관리 (Spring Cloud Config)
9. **Eureka-Service**: 서비스 디스커버리
![제목 없는 다이어그램 drawio (2)](https://github.com/user-attachments/assets/d0e9b721-18c8-4484-942d-d547c937fea4)
## 2. 요구사항 정의서

### 2-1. 기능 요구사항

#### 사용자 서비스
| ID | 요구사항명 | 내용 | 담당자 | 우선순위 |
| --- | --- | --- | --- | --- |
| US-01 | 회원가입 | 사용자 정보를 입력받아 회원가입 처리 및 DB 저장 | 나석후 | 높음 |
| US-02 | 로그인 | 사용자 입력 정보와 DB정보를 대조하여 로그인 처리 | 나석후 | 높음 |
| US-04 | 마이페이지  | 본인페이지들어가서 비밀번호 변경 구현  | 나석후 | 중간 |
 

#### 결제 서비스
| ID | 요구사항명 | 내용 | 담당자 | 우선순위 |
| --- | --- | --- | --- | --- |
| OD-01 | 강의 담기 | 사용자가 결제 전 장바구니에 강의를 담을 수 있음 | 김현지 | 중간 |
| OD-02 | 담은 강의 삭제 | 사용자가 장바구니에 담은 강의를 삭제 | 김현지 | 중간 |
| OD-03 | 구매 결제 | 사용자가 강의를 구매하고 결제 | 김현지 | 높음 |
| OD-04 | 결제 취소 | 사용자가 결제한 강의를 결제 취소 | 김현지 | 중간 |

#### 강의 서비스
| ID | 요구사항명 | 내용 | 담당자 | 우선순위 |
| --- | --- | --- | --- | --- |
| COR-01 | 강의 검색 | 사용자가 수강할 수 있는 강의 조회 | 김지원 | 높음 |
| COR-02 | 강의 추가 | 강사가 강의 콘텐츠를 업로드 | 김지원 | 높음 |
| COR-03 | 강의 수정 | 강사가 본인이 등록한 강의를 수정 | 김지원 | 중간 |
| COR-04 | 강의 삭제 | 강사가 본인이 등록한 강의를 삭제 | 김지원 | 중간 |

#### 댓글 서비스
| ID | 요구사항명 | 내용 | 담당자 | 우선순위 |
| --- | --- | --- | --- | --- |
| PO-01 | 댓글 추가 | 사용자가 강의에 댓글을 남김 | 이은혁 | 높음 |
| PO-02 | 댓글 수정 | 사용자가 본인이 남긴 댓글을 수정 | 이은혁 | 중간 |
| PO-03 | 댓글 삭제 | 사용자가 본인이 남긴 댓글을 삭제 | 이은혁 | 중간 |

### 2-2. 비기능 요구사항(예시/성능, 보안, 확장성 등 시스템의 품질 속성)
| ID | 요구사항명 | 내용 | 담당자 | 우선순위 |
| --- | --- | --- | --- | --- |
| NF-01 | 서버 이용량 | 시스템은 동시에 100명의 사용자를 처리할 수 있어야 한다. | - | 중간 |
| NF-02 | 로딩시간 | 페이지 로딩 시간은 3초 이내여야 한다. | - | 높음 |
| NF-03 | DB 저장형식 | 모든 비밀번호는 암호화되어 저장되어야 한다. | - | 높음 |

### 2-3. 제약사항(예시/개발 과정에서 고려해야 할 제한사항)
| ID | 요구사항명 | 내용 | 담당자 | 우선순위 |
| --- | --- | --- | --- | --- |
| C-01 | AWS 서비스(EC2, S3)만을 사용하여 배포해야 한다. | - | 중간 |
| C-02 | 마이크로서비스 간 통신은 REST API를 통해 이루어져야 한다. | - | 중간 |

## 3. 인터페이스 설계서

### 3-1. API 설계

### 🔸사용자 서비스 API
  1. **회원가입**
     - URL: `POST /api/users`
     - Request Body:
       ```json
       {
         "email": "user@example.com",
         "password": "password123",
         "name": "홍길동"
       }
       ```
     - Response (성공 - 201 Created):
       ```json
       {
         "id": "12345",
         "email": "user@example.com",
         "name": "홍길동"
       }
       ```
  
  2. **로그인**
     - URL: `POST /api/auth/login`
     - Request Body:
       ```json
       {
         "email": "user@example.com",
         "password": "password123"
       }
       ```
     - Response (성공 - 200 OK):
       ```json
       {
         "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
         "user": {
           "id": "12345",
           "email": "user@example.com",
           "name": "홍길동"
         }
       }
       ```

### 🔸강의 서비스 API
  1. **강의 목록 조회**
     - URL: `GET /api/course-service/courses/all`
     - Response (성공 - 200 OK):
       ```json
       [
         {
             "productId": 2,
             "productName": "일타강사 류현진의 기초 Git 강의!   (수정본)",
             "description": "Git에 기초를 배울 수 있는 매우 훌륭한 강의입니다!\n\n\n(내용이 수정되었습니다!)",
             "price": 35000,
             "userId": 10,
             "category": "Git",
             "active": true,
             "filePath": "https://www.youtube.com/watch?v=Fley6IFhlC8&t=5s",
             "username": "류현진"
         },
         {
             "productId": 5,
             "productName": "일타강사 류현진의 기초 HTML 강의!",
             "description": "초보자도 손쉽게 이해할 수 있는 HTML 특강입니다.",
             "price": 25000,
             "userId": 10,
             "category": "HTML/CSS",
             "active": true,
             "filePath": "https://www.youtube.com/watch?v=FV32OM3B49c&list=PLI33CnBTx2MYe0rqJ2nMSbfUqLmWIJtaV",
             "username": "류현진"
         }
         ]
       ```
  
  2. **강의 상세 조회**
     - URL: `GET /course-service/courses/info/{courseID}`
     - Response (성공 - 200 OK):
       ```json
       {
         "productId": 2,
         "productName": "일타강사 류현진의 기초 Git 강의!",
         "description": "Git에 기초를 배울 수 있는 매우 훌륭한 강의입니다!\n\n\n(내용이 수정되었습니다!)",
         "price": 35000,
         "userId": 10,
         "category": "Git",
         "active": true,
         "filePath": "https://www.youtube.com/watch?v=Fley6IFhlC8&t=5s",
         "username": "류현진"
       }
       ```
  
  3. **강의 등록 (강사 전용)**
    - URL: `POST /course-service/courses/createCourse`
    - Request Body:
      ```json
      {
        "productName": "Spring Boot 입문",
        "description": "Spring Boot 기본 개념 강의입니다.",
        "price": 15000,
        "category": "Spring",
        "filePath": "https://www.youtube.com/watch?v=Fley6IFhlC8&t=8s&ab_channel=%EC%A1%B0%EC%BD%94%EB%94%A9JoCoding"
      }
      ```
    - Response (성공 - 201 Created):
    
      ```json
      {
        "productId": "12",
        "productName": "Spring Boot 입문",
        "description": "Spring Boot 기본 개념 강의입니다.",
        "price": 15000,
        "userId": "12",
        "category": "Spring",
        "active": true,
        "filePath": "https://www.youtube.com/watch?v=Fley6IFhlC8&t=8s&ab_channel=%EC%A1%B0%EC%BD%94%EB%94%A9JoCoding",
        "username": "정승제"
      }
      ```
     

### 🔸댓글 서비스 API
  1. **강의 질문 달기**
     - URL: `POST /api/post/create`
     - Request Body:
       ```json
       {
         "userId": "12345",
         "title": "강의에 대한 질문이 있습니다!",
         "content": "강의에 대한 질문에 대한 내용!"
       }
       ```
     - Response (성공 - 201 Created):
       ```json
       {
         "id": "101",
         "userId": "12345",
         "content": "이 강의 정말 유익했어요!",
         "createdAt": "2025-05-12T10:00:00"
       }
       ```
  
  2. **강사 댓글에 답글 달기**
     - URL: `POST /api/post/comment/create`
     - Request Body:
       ```json
       {
         "userId": "67890",  // 강사 ID
         "content": "감사합니다! 더 많은 강의 준비 중입니다."
       }
       ```
     - Response (성공 - 201 Created):
       ```json
       {
         "id": "201",
         "userId": "67890",  // 강사 ID
         "content": "감사합니다! 더 많은 강의 준비 중입니다.",
         "createdAt": "2025-05-12T10:15:00"
       }
       ```


### 🔸결제 서비스 API
  1. **강의 구매**
     - URL: `POST /api/order/create`
     - Request Body:
       ```json
       [
          {"productId": 3},
          {"productId": 4}
       ]
       ```
     - Response (성공 - 201 Created):
       ```json
       {
          "statusCode": 201,
          "statusMessage": "정상 주문 완료",
          "result": [
              {
                  "id": 9,
                  "userId": 2,
                  "userEmail": "do@naver.com",
                  "productId": 3,
                  "orderStatus": "ORDERED",
                  "orderDate": "2025-05-18"
              },
              {
                  "id": 10,
                  "userId": 2,
                  "userEmail": "do@naver.com",
                  "productId": 4,
                  "orderStatus": "ORDERED",
                  "orderDate": "2025-05-18"
              }
          ]
       }
       ```
       
  2. **강의 구매 취소**
     - URL: `POST /api/order/cancel/4`
     - Response (성공 - 201 Created):
       ```json
       {
          "statusCode": 200,
          "statusMessage": "주문 취소 완료",
          "result": 10
       }
       ```
       
 3. **강의 구매 조회 (학생용)**
     - URL: `POST /api/order/my-order`
     - Response (성공 - 201 Created):
       ```json
       {
           "statusCode": 200,
           "statusMessage": "정상 조회 완료",
           "result": [
               {
                   "id": 2,
                   "userEmail": "do@naver.com",
                   "userId": 2,
                   "orderStatus": "CANCELED",
                   "productId": 2,
                   "productName": "일타강사 류현진의 기초 Git 강의!   (수정본)",
                   "orderDate": "2025-05-15",
                   "category": "Git",
                   "filePath": "https://www.youtube.com/watch?v=Fley6IFhlC8&t=5s",
                   "active": true
               },
               ...
           ]
       }
       ```

    4. **강의 구매 조회 (강사용)**
     - URL: `POST /api/order/my-course-order`
     - Response (성공 - 201 Created):
       ```json
        {
            "statusCode": 200,
            "statusMessage": "전체 주문 내역 조회 완료",
            "result": [
                {
                    "id": 2,
                    "userEmail": "do@naver.com",
                    "userId": 2,
                    "orderStatus": "CANCELED",
                    "productId": 2,
                    "productName": "일타강사 류현진의 기초 Git 강의!   (수정본)",
                    "orderDate": "2025-05-15",
                    "category": "Git",
                    "filePath": "https://www.youtube.com/watch?v=Fley6IFhlC8&t=5s",
                    "active": true
                },
                ...
            ]
        }
       ```

### 3-2. 서비스 간 통신 설계

1. **강의 서비스 -> 결제 서비스**
   - 목적: 강의 구매 시 결제 처리
   - 통신 방식: REST API 호출
   - 엔드포인트: `POST /api/orders`

2. **결제 서비스 -> 알림 서비스**
   - 목적: 결제 완료 후 사용자에게 알림 발송
   - 통신 방식: 비동기 메시지 (이벤트 발행)
   - 이벤트 타입: `OrderCompletedEvent`

3. **강의 서비스 -> 댓글 서비스**
   - 목적: 강의에 대한 댓글과 답글 조회 및 작성
   - 통신 방식: REST API 호출
   - 엔드포인트:
     - 댓글 조회: `GET /api/lectures/{lectureId}/comments`
     - 댓글 달기: `POST /api/lectures/{lectureId}/comments`
     - 답글 달기: `POST /api/comments/{commentId}/replies`

4. **결제 서비스 -> 강의 서비스**
   - 목적: 환불 시 강의 수강권 해지
   - 통신 방식: REST API 호출
   - 엔드포인트: `DELETE /api/lectures/{lectureId}/enrollments/{userId}`
    


### 3-3. UI 화면 설계

  ### 📌 **회원가입 화면**
  - **구성 요소**:
    - 이메일 입력 필드
    - 비밀번호 입력 필드
    - 이름 입력 필드
    - 회원가입 버튼
  - **사용자 인터랙션**:
    - 사용자는 이메일, 비밀번호, 이름을 입력하고 "회원가입" 버튼을 클릭하여 회원가입을 진행합니다.
  
  ### 📌 **강의 목록 화면**
  - **구성 요소**:
    - 강의 목록 (강의 제목, 가격, 짧은 설명)
    - 강의 검색 필터 (카테고리, 키워드)
    - 각 강의를 클릭하면 강의 상세 페이지로 이동
  - **사용자 인터랙션**:
    - 사용자는 강의를 클릭하여 강의 상세 페이지로 이동하거나, 검색 필터를 사용하여 원하는 강의를 찾을 수 있습니다.
  
  ### 📌 **강의 상세 페이지**
  - **구성 요소**:
    - 강의 제목, 설명, 가격
    - 강의 영상 스트리밍 (S3 기반)
    - "구매하기" 버튼
    - 댓글 목록
    - 댓글 작성 입력 필드
    - 강사 댓글 답글 작성 필드
  - **사용자 인터랙션**:
    - 사용자는 강의 영상을 보고 "구매하기" 버튼을 클릭하여 강의를 구매할 수 있습니다.
    - 사용자는 강의에 댓글을 달 수 있습니다.
    - 강사는 자신이 올린 강의에 달린 댓글에 답글을 달 수 있습니다.
   
  ### 📌 **강사 강의 등록 화면**
  - **구성 요소**:
    - 강의 제목 입력 필드
    - 강의 설명 입력 필드
    - 강의 가격 입력 필드
    - 강의 url 입력 필드
    - 카테고리 선택
    - "강의 등록" 버튼
  - **사용자 인터랙션**:
    - 사용자(강사)는 강의 제목, 설명, 가격, url을 입력하고 "강의 등록" 버튼을 클릭하여 강의 등록을 진행합니다.
   
  ### 📌 **마이페이지 > 구매 강의 목록 화면**
  - **구성 요소**:
    - 수강중인 강의 리스트
    - "수강 취소" 버튼(조건부 노출)
  - **사용자 인터랙션**:
    - 사용자는 각 강의 옆에 있는 "수강 취소" 버튼을 눌러 구매한 강의의 수강을 철회할 수 있습니다.
   

### 3.4 사용자 흐름도
  
  🔹 A[로그인한 사용자] --> B[강의 목록 조회] --> C[강의 구매] --> D[수강 시작] --> E[댓글 작성] --> F[강사 답글 작성]  
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 ㄴ--> G[강의 취소 요청] --> H[환불 처리 완료]  
  
 🔹 A[로그인한 강사] --> I[강의 등록] --> J[강의 상세 페이지 생성] --> K[강의 삭제 요청] --> L[강의 삭제]  
     

## 4. 소스코드

### 4-1. Frontend Source
[👉🏻 프론트엔드 코드](https://github.com/EunHyeokLee123/msa-project-front)

## 5. 테스트 케이스

### ✅ 백엔드 테스트 케이스 (Spring Boot 등)

| 테스트 ID | 기능 | 테스트 내용 | 입력 값 예시 | 예상 결과 |
|-----------|------|--------------|---------------|--------------|
| BE-001 | 회원가입 | 유효한 회원 정보로 가입 시도 | 이메일, 비밀번호, 이름 | 201 Created, 회원 DB 저장됨 |
| BE-002 | 로그인 | 잘못된 비밀번호로 로그인 | 존재하는 이메일, 틀린 비번 | 401 Unauthorized |
| BE-003 | 강의 등록 | 관리자가 강의 등록 시 | 유효한 강의 정보 | 200 OK, DB에 강의 저장 |
| BE-004 | 강의 목록 조회 | 전체 강의 리스트 조회 | 없음 | 200 OK, 강의 리스트 JSON 반환 |
| BE-005 | 강의 검색 | 키워드 포함 강의 검색 | keyword=“Java” | 200 OK, Java 포함 강의만 반환 |
| BE-006 | 장바구니 추가 | 수강생이 강의를 장바구니에 담음 | 강의 ID, 사용자 ID | 200 OK, 장바구니에 저장 |
| BE-007 | 결제 처리 | 강의 결제 API 호출 | 사용자 토큰, 결제 정보 | 200 OK, 결제 완료 |
| BE-008 | 마이페이지 조회 | 본인 수강 목록 요청 | 로그인된 사용자 | 200 OK, 구매한 강의 리스트 |
| BE-009 | 비회원 접근 제한 | 인증 없이 강의 업로드 시도 | 없음 | 403 Forbidden |
| BE-010 | 페이징 기능 | 강의 리스트 페이지 요청 | page=0&size=12 | 200 OK, 12개 강의만 반환 |
| BE-011 | 질문 생성 | 강의에 대한 질문 생성 요청 | 강의 ID, 제목, 내용 | 201 Created, 질문 정보 DB 저장됨 |
| BE-012 | 댓글 생성 | 질문에 대한 댓글 생성 요청 | 질문 ID, 내용 | 201 Created, 댓글 정보 DB 저장됨 |
| BE-013 | 질문 조회 | 강의에 대한 정보 조회 시 모든 질문 조회 | 강의 ID | 200 OK, 질문 리스트와 댓글 수 JSON 반환 |
| BE-014 | 댓글 조회 | 질문에 대한 모든 댓글 조회 | 질문 ID | 200 OK, 댓글 리스트 JSON 반환 |
| BE-015 | 질문 삭제 | 질문 삭제 요청 | 질문 ID, 이메일 | 200 OK |
| BE-016 | 댓글 삭제 | 댓글 삭제 요청 | 댓글 ID, 이메일 | 200 OK |
| BE-017 | 댓글 및 질문 삭제 가능 확인 | 댓글 혹은 질문 삭제 요청 시 본인이 작성한 것인지 확인 | 댓글(질문) ID, 이메일 | 401 Unauthorized |


---

### ✅ 프론트엔드 테스트 케이스 (React 등)

| 테스트 ID | 기능 | 테스트 내용 | 시나리오 | 기대 결과 |
|-----------|------|--------------|-----------|--------------|
| FE-001 | 회원가입 폼 | 모든 필수 항목 입력 후 제출 | 이메일, 비밀번호 입력 → 제출 | 회원가입 완료 메시지 |
| FE-002 | 로그인 페이지 | 로그인 성공 → 토큰 저장 | 로그인 정보 입력 후 제출 | 메인 페이지로 리다이렉트 |
| FE-003 | 검색 기능 | 검색어 입력 후 Enter | “Spring” 입력 후 Enter | 검색 결과 페이지로 이동 |
| FE-004 | 강의 상세 진입 | 강의 카드 클릭 | 강의 목록 → 강의 선택 | 상세 페이지 이동 및 정보 표시 |
| FE-005 | 장바구니 추가 버튼 | 장바구니 버튼 클릭 | 로그인 상태에서 버튼 클릭 | “장바구니에 추가됨” 알림 |
| FE-006 | 강의 업로드 폼 | 입력 누락 후 제출 | 제목 없이 제출 | “제목을 입력하세요” 에러 |
| FE-007 | 반응형 UI | 모바일 화면 접속 | 강의 카드 UI 축소 | 모바일에서 깨지지 않음 |
| FE-008 | Pagination | 페이지 버튼 클릭 | 다음 페이지 클릭 | 강의 목록 12개 갱신 |
| FE-009 | 마이페이지 이동 | 상단 메뉴 클릭 | 사용자 이름 클릭 → 마이페이지 | 수강 강의 리스트 표시 |
| FE-010 | 인증된 요청 처리 | 로그인 없이 수강 클릭 | 수강 버튼 클릭 | 로그인 페이지로 이동 |
| FE-011 | 질문 생성 폼 | 제목, 내용 항목 입력 후 제출 | 질문 생성 버튼 클릭 | 질문 생성 완료 메시지 |
| FE-012 | 댓글 생성 폼 | 내용 항목 입력 후 제출 | 댓글 생성 버튼 클릭 | 댓글 생성 완료 메시지 |


## 6. 테스트 케이스 결과

### ✅ 백엔드 테스트 케이스 결과표

| 테스트 ID | 기능 | 테스트 내용 | 실제 결과 | 상태 |
|-----------|------|--------------|--------------|--------|
| BE-001 | 회원가입 | 유효한 회원 정보로 가입 시도 | 201 Created | ✅ 통과 |
| BE-002 | 로그인 | 잘못된 비밀번호로 로그인 | 200 OK | ✅ 통과 |
| BE-003 | 강의 등록 | 관리자가 강의 등록 시 | 200 OK | ✅ 통과 |
| BE-004 | 강의 목록 조회 | 전체 강의 리스트 조회 | 200 OK | ✅ 통과 |
| BE-005 | 강의 검색 | 키워드 포함 강의 검색 | 빈 목록 반환 | ✅ 통과 |
| BE-006 | 장바구니 추가 | 수강생이 강의 장바구니 담기 | 200 OK | ✅ 통과 |
| BE-007 | 결제 처리 | 결제 API 호출 | 200 OK | ✅ 통과 |
| BE-008 | 마이페이지 조회 | 본인 수강 목록 요청 | 200 OK | ✅ 통과 |
| BE-009 | 비회원 접근 제한 | 인증 없이 강의 업로드 시도 | 403 Forbidden | ✅ 통과 |
| BE-010 | 페이징 기능 | page=0&size=12 요청 | 12개 강의 반환 | ✅ 통과 |
| BE-011 | 질문 생성 | 강의에 대한 질문 생성 요청 | 201 Created | ✅ 통과 |
| BE-012 | 댓글 생성 | 질문에 대한 댓글 생성 요청 | 201 Created | ✅ 통과 |
| BE-013 | 질문 조회 | 강의에 대한 정보 조회 시 모든 질문 조회 | 200 OK | ✅ 통과 |
| BE-014 | 댓글 조회 | 질문에 대한 모든 댓글 조회 | 200 OK | ✅ 통과 |
| BE-015 | 질문 삭제 | 질문 삭제 요청 | 200 OK | ✅ 통과 |
| BE-016 | 댓글 삭제 | 댓글 삭제 요청 | 200 OK | ✅ 통과 |
| BE-017 | 댓글 및 질문 삭제 가능 확인 | 댓글 혹은 질문 삭제 요청 시 본인이 작성한 것인지 확인 | 401 Unauthorized | ✅ 통과 |

---

### ✅ 프론트엔드 테스트 케이스 결과표

| 테스트 ID | 기능 | 테스트 내용 | 실제 결과 | 상태 |
|-----------|------|--------------|--------------|--------|
| FE-001 | 회원가입 폼 | 필수 항목 입력 후 제출 | 성공 메시지 출력 | ✅ 통과 |
| FE-002 | 로그인 페이지 | 로그인 후 토큰 저장 | 메인 페이지 이동 | ✅ 통과 |
| FE-003 | 검색 기능 | 검색어 입력 후 Enter | 결과 페이지 이동 | ✅ 통과 |
| FE-004 | 강의 상세 진입 | 강의 카드 클릭 | 상세 페이지 표시 | ✅ 통과 |
| FE-005 | 장바구니 추가 버튼 | 장바구니 버튼 클릭 | 알림 표시 | ✅ 통과 |
| FE-006 | 강의 업로드 폼 | 입력 누락 후 제출 | 경고 표시됨 | ✅ 통과 |
| FE-007 | 반응형 UI | 모바일 화면 접속 | UI 정상 표시 | ✅ 통과 |
| FE-008 | Pagination | 페이지 버튼 클릭 | 강의 12개 갱신 | ✅ 통과 |
| FE-009 | 마이페이지 이동 | 상단 메뉴 클릭 | 리스트 표시 | ✅ 통과 |
| FE-010 | 인증된 요청 처리 | 비로그인 상태에서 수강 클릭 | 로그인 페이지 이동 | ✅ 통과 |
| FE-011 | 질문 생성 폼 | 제목, 내용 항목 입력 후 제출 | 질문 생성 완료 메시지 | ✅ 통과 |
| FE-012 | 댓글 생성 폼 | 내용 항목 입력 후 제출 | 댓글 생성 완료 메시지 | ✅ 통과 |




