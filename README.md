# 프로젝트 소개

여러가지 카테고리로 소통하는 온라인 커뮤니티 노리터입니다.

React + Springboot


# 개발환경

- BACK-END
    - JAVA JDK11
    - springboot 2.7.12
    - gradle
    - h2 (development)
    - MySQL (production)
    - spring data jpa
    - spring security
- FRONT-END
    - typescript
    - react
    - styled-components
- DEPLOY
    - git
    - aws ec2
    - aws rds
    - nginx
- TOOLS
    - intellij
    - VSC


# 구현 기능

- 사용자 관리
    - 구글 로그인
    - 프로필 변경 기능
    - 프로필에서 작성한 최근 질문 및 답변 확인
- 게시판 CRUD
    - 게시글 작성, 수정, 삭제 기능
    - 댓글 작성, 수정, 삭제 기능
    - 게시글 검색 기능 (키워드 별, 카테고리 별 검색)
- 게시글 조회 수 확인 기능
- 최근 3일 이내 조회 수 상위 5개 질문글 노출 기능

# 프론트엔드 서버 접근 URL

| url         | name       | description |
|-------------|------------| --- |
| /           | 홈          |  |
| /login      | 로그인 페이지    | 소셜 로그인을 위한 링크 페이지 |
| /posts      | 게시글 검색 페이지 | 게시글 검색 및 조회수 높은 질문을 확인할 수 있는 페이지 |
| /posts/{id} | 게시글 상세페이지  | 게시글 상세 페이지, 댓글 작성 가능 |
| /user/{id}  | 사용자 프로필    |  |


# REST API 명세서

### REQUEST

- 요청 데이터를 body에 넣어 요청해야 하는 경우 Header의 “Content-Type”을 “application/json”으로 설정하여 요청한다.
- 유저의 권한이 필요한 요청의 경우 발급 받은 “JSESSIONID”쿠키를 포함하여 요청한다.

### REQUEST OBJECT

- 사용자 수정

| name     | type | description | required |
|----------| --- | --- | --- |
| username | String | 닉네임 | O |
- 게시글 등록/수정

| name         | type | description | required |
|--------------| --- |---------| --- |
| title        | String | 제목      | O |
| content      | String | 내용      | O |
| categoryName | String | 카테고리    | O |
- 댓글 등록/수정

| name             | type | description    | required |
|------------------| --- |----------------| --- |
| postId | Long | 댓글을 등록할 게시글 번호 | O |
| content          | String | 답변 내용          | O |

### RESPONSE

| name | type | description |
| --- | --- | --- |
| data | 답변 데이터 객체 별로 상이(아래 객체 설명 참조) | 요청한 데이터 |
| error | Error | 에러 발생 시 에러 응답 객체 |

### DATA OBJECT

- 사용자

| name         | type | description | required |
|--------------| --- |-------------| --- |
| id           | Long | 사용자 id      | O |
| username     | String | 닉네임         | O |
| email        | String | 이메일         | O |
| profileImgUrl | String | 프로필이미지 URL  | X |
- 댓글

| name         | type | description | required |
|--------------| --- |-------------| --- |
| id           | Long | 게시글 번호      | O |
| categoryName | String | 카테고리 이름     | O |
| title        | String | 게시글 제목      | O |
| content      | String | 게시글 내용      | O |
| writer       | String | 작성자 닉네임     | O |
| writerId     | Long | 게시글 작성자 id  | O |
| views        | Integer | 조회수         | O |
- 댓글

| name     | type | description | required |
|----------| --- |-------------| --- |
| id       | Long | 댓글 id       | O |
| content  | String | 내용          | O |
| writer   | String | 댓글 작성자 닉네임  | O |
| writerId | Long | 댓글 작성자 id   | O |
- 게시글 리스트(페이징)

| name  | type      | description | required |
|-------|-----------| --- | --- |
| posts | List<게시글> | 조회된 질문 목록 | O |
| hasNext | boolean   | 다음 페이지 유무 | O |
- 답변 리스트(페이징)

| name    | type     | description | required |
|---------|----------|-------------| --- |
| replies | List<댓글> | 조회된 댓글 목록   | O |
| hasNext | boolean  | 다음 페이지 유무   | O |

### ERROR OBJECT

| name | type | description | required |
| --- | --- | --- | --- |
| errorCode | String | 에러 코드 | O |
| message | String | 에러 메시지 | O |

### ERROR CODE

| CODE | description |
| --- | --- |
| SERVER | 서버 오류 |
| DB | DB관련 오류 |
| CLIENT | 클라이언트 오류 |

### SUMMARY
| Method | URI                          | Description             | request                                                                       | credentials | response detail             |
| --- |------------------------------|-------------------------|-------------------------------------------------------------------------------| --- |-----------------------------|
| POST | /api/v1/post                 | 게시글 등록                  | json: 게시글 등록/수정                                                               | O | 201 CREATED / json: 게시글     |
| GET | /api/v1/post/{id}            | 게시글 조회                  |                                                                               |  | 200 OK / data: 게시글          |
| PUT | /api/v1/post/{id}            | 게시글 수정                  | json: 게시글 등록/수정                                                               | O | 202 ACCEPTED / data: 게시글    |
| DELETE | /api/v1/post/{id}            | 게시글 삭제                  |                                                                               | O | 200 OK                      |
| GET | /api/v1/post/category        | 게시글 카테고리                |                                                                               |  | 200 OK / data: 카테고리         |
| GET | /api/v1/post/top5            | 오늘 포함 최근 3일 기준 조회수 top5 |                                                                               |  | 200 OK / data: List<게시글>    |
| POST | /api/v1/reply                | 댓글 등록                   | json: 댓글 등록/수정                                                                | O | 201 CREATED / data: 댓글      |
| GET | /api/v1/reply                | 게시글에 대한 답변              | parameters: questionNo(Integer), page(Integer), size(Integer)                 |  | 200 OK / data: 댓글 리스트(페이징)  |
| PUT | /api/v1/reply/{id}           | 게시글 수정                  | json: 댓글 등록/수정                                                                | O | 202 ACCEPTED / data: 댓글     |
| DELETE | /api/v1/reply/{id}           | 게시글 삭제                  |                                                                               | O | 200 OK                      |
| GET | /api/v1/user                 | 세션에 담긴 로그인  된 사용자 정보    |                                                                               | O | 200 OK / data: 사용자          |
| GET | /api/v1/user/{id}            | 사용자 프로필                 |                                                                               |  | 200 OK / data: 사용자          |
| PUT | /api/v1/user/{id}            | 사용자 프로필 수정              | json: 유저 수정                                                                   | O | 202 ACCEPTED / data: 사용자    |
| DELETE | /api/v1/user/{id}            | 사용자 정보 삭제(탈퇴)           |                                                                               | O | 200 OK                      |
| GET | /api/v1/user/{id}/post/top3  | 사용자 게시글 최신 top3         |                                                                               |  | 200 OK / data: List<게시글>    |
| GET | /api/v1/user/{no}/reply/top3 | 사용자 댓글 최신 top3          |                                                                               |  | 200 OK / data: List<댓글>     |
| GET | /api/v1/search/post          | 검색된 게시글 리스트             | parameters: categoryName(String), word(String), page( Integer), size(Integer) |  | 200 OK / data: 게시글 리스트(페이징) |

### TROUBLESHOOTING
https://alabaster-caravel-721.notion.site/12ce94f0864544269cc64553fb688fbd

