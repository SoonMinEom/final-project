# MutsaSNS
> 회원 가입, 로그인, 인증·인가를 기반으로 게시글과 댓글의 CRUD, 마이 피드, 좋아요, 알람 기능을 구현한 SNS 웹 페이지
---
## URL
> - [Swagger](http://ec2-52-78-223-101.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/)
> - [WebPage](http://ec2-52-78-223-101.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/)
---
## ERD
![erd](./img/erd.png)
---
## 체크리스트
📃체크리스트를 잘 작성하고 싶은 나, 이기적인가요?  
- 전혀 그렇지 않답니다 희망을 가지세욤
  - 마자요 희망을가저요
  - 감사합니다  
    📃감사합니까 휴먼?  
    조심하십쇼 휴먼  

📃후후 이렇게 하면 되긴 하네ㅐ요?>
- 이렇게가 어떻게 인데요
  - 일단 띄어 쓰기 두번 해줘요  
  그리고요?  

📃 엔터 세번 해요
- 앵 그냥 엔터만 세번해도될듯?

📃봐봐 되는구만 호들갑은


  
## Endpoint

### User

#### 회원가입
`POST /api/v1/users/join`

**Request Body**
```json
{
"userName": "String",
"password": "String",
}
```

**Response Body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "userId": Integer,
        "userName": "String"
    }
}
```
<br>

#### 로그인
`POST /api/v1/users/login`

**RequestBody**
```json
{
"userName": "String",
"password": "String"
}
```

**Response Body**
```json
{
  "jwt": "String"
}
```
<br>

#### ADMIN 승급
`POST api/v1/user/{id}/role/change`

**Response Body**
```json
{
  "resultCode": "SUCCESS",
  "result": {
    "userName": "string",
    "message": "ADMIN 승급 성공"
  }  
}
```
<br>


### Post
#### 리스트 조회
`GET /api/v1/posts`

**Response Body**
```json
{
  "resultCode": "SUCCESS",
  "result": {
    "content" : [ {
      "title": "String",
      "body": "String",
      "userName": "String",
      "createdAt": "yyyy-mm-dd hh:mm:ss",
      "lastModifiedAt":  "yyyy-mm-dd hh:mm:ss",
      "id": Integer
    } ] ,
    "pageable": {
      "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
      },
      "offset": 0,
      "pageNumber": 0,
      "pageSize": 20,
      "paged": true,
      "unpaged": false
  }
}
```

#### 상세 조회
`GET /api/v1/posts/{id}`

**Response Body**
```json
{
  "id": Integer,
  "title": "String",
  "body": "String",
  "userName": "String",
  "createdAt": "yyyy-mm-dd hh:mm:ss",
  "lastModifiedAt": "yyyy-mm-dd hh:mm:ss"
}
```
<br>

#### 작성 
`POST /api/v1/posts`

**Request Body**
```json
{
    "title": "String",
    "body": "String"
}
```

**Response Body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "message": "포스트 등록 완료",
        "postId": Integer
    }
}
```

#### 수정 
`PUT /api/v1/posts/{id}`

**Request Body**
```json
{
    "title": "String",
    "body": "String"
}
```

**Response Body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "message": "포스트 수정 완료",
        "postId": Integer
    }
}
```

#### 삭제 
`DELETE /api/v1/posts/{id}`

**Response Body**
```json
{
    "resultCode": "SUCCESS",
    "result": {
        "message": "포스트 삭제 완료",
        "postId": Integer
    }
}
```
<br>