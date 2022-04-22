# authservice

## OAuth 주의사항

- provider에 따라서 서비스 domain을 등록하는 경우(Naver)도 있으며 이 경우에는 로그인 요청하는 domain은 
  서버에 등록된 domain이어야 한다.
- 대부분의 provider는 OAuth의 마지막 redirect-uri를 등록하도록 한다. 따라서 OAuth 설정의 
  redirect-uri 값은 반드시 서버에 등록된 값으로 설정해야 한다.
- 마지막 인증 성공 후 발행되는 JWT 쿠키는 인증을 요청한 domain 주소에 설정이 된다.
  - proxy나 gateway를 통과하더라도 브라우저에서(주소창에 표시된) 요청된 domain에 설정된다.
  - 일반적으로 마지막 handler에서 로그인 후 이동할 페이지로 redirect를 하게 되는데
    이 때 주소도 역시 로그인을 요청한 domain과 같은 domain이어야 쿠키가 적용되고 정상동작하게 된다.

## Spring Boot Project

### Mybatis 설정

- 가장 먼저 Datasource를 정확히 설정해줘야 정상적으로 실행된다.
- 아직 DB가 준비되지 않은 상태라면 Gradle에서 mybatis dependency를 임시로 주석처리한다.

### Logback 설정

- console, rolling file appender만 추가되어 있으며 로그의 경로 설정만 환경에 맞게 수정하면 된다.

### AJP/1.3 설정

- Apache를 전면에 두고 apache와 통신을 하는 구성이라면 APJ/1.3 프로토콜로 요청을 처리할 수 있도록 구성하는 게 필요하지만 직접 HTTP 요청을 처리하는 구성이라면 이 설정은 필요없다.
- 만약 이 설정이 필요없다면 Ajp13Config.java 는 삭제해도 좋다. application.properties에서 tomcat.ajp.xxx 설정들도 모두 삭제해도 좋다.

### Controller Mapping 예시

- GET /rest/hello
- POST /rest/sample
  - {} 빈 객체를 요청에 전달하면 응답 객체가 내려온다.

## JWT 구현

최초에는 실제 인증 없이 JWT 토큰을 발행하도록 구현되어있다. 따라서 아무 값이나 입력하더라도 모두 인증을
통과한다.

- 인증이 정상적으로 동작하도록 하기 위해서는 AuthService 인터페이스를 구현해야 한다.
  - AuthServiceImpl 클래스가 샘플 구현체로 추가되어 있다.
- Client에서는 아래 3가지 API를 이용하여 인증을 구현한다.
  - /rest/auth
  - /rest/reauth
  - /rest/logout
- 각 API의 상세 전송 내용은 DTO 클래스들을 참조하도록 되어있으므로 DTO 클래스를 상황에 맞게 정의하면 된다.
- AuthService의 주석을 참조하여 구현해보면 좀 더 명확하게 이해할 수 있다.

## Quasar Framework

Quasar Framework 구성은 '[ProjectRoot]/front' 에 생성된다.

- 최초 프로젝트 생성 후에 front 폴더에서 `yarn` 명령을 실행해준다.
- 기본적인 quasar 개발 환경은 이미 구성되어 있으며 아래 명령으로 개발을 진행한다.
  - `yarn`
    - 필요한 js 모듈들을 설치한다.
  - `quasar dev`
    - front 수정 작업이 실시간 반영되는 개발 서버를 띄운다.
  - `quasar build`
    - 최종 배포를 위한 front 소스 코드를 빌드한다. 참고로 빌드되는 경로는 '../src/main/resources/static' 이다.
- 설정 관련한 사항은 'quasar.config.js' 파일을 참조하도록 한다.

## Spring OAuth2 Implementations

### Prerequisite

- Google, Naver, Kakao 연동 예시가 포함되어 있으므로 사용하려는 인증 사이트에 미리 등록 절차가 되어있어야 한다.

### OAuth2 적용

- Spring Security OAuth2 를 이용하여 적용함.
- application-oauth.yml 파일에 관련 설정이 작성되어있다.

### OAuth2 테스트

- 8081로 접속해서 테스트하더라도 OAuth 후에는 결국 8080으로 리다이렉트가 된다.
- 따라서 OAuth 테스트 전에는 'yarn build' 명령을 실행하고나서 수행한다.
- 8080으로 리다이렉트 되더라도 주소창에서 다시 8081로 바꾸고 진행하면 문제 없다.

### Flyway

#### Prerequisite

- 개발 환경의 운영체제에 맞는 flyway CLI 버전을 설치해야 한다.(DB 담당자만 설치)
- Spring 코드나 Gradle을 통해서 자동으로 작업되도록 하는 건 상용 서비스 시에는 위험하므로 반드시 CLI를 사용한다.

#### Flyway 적용

- `flyway.conf` 파일 설정 확인 및 수정.
  - flyway.driver={JDBC 드라이버 클래스}
  - flyway.url={JDBC URL}
  - flyway.user={DB 접속 계정}
  - flyway.password={DB 접속 계정 비밀번호}
  - flyway.locations=filesystem:flyway
  - 나머지 필드는 가능한 한 그대로 두도록 한다.
  - 위 정보는 항상 개발환경에 맞춰놓고 상용 적용 시에만 잠시 바꿔 적용 후 다시 개발환경으로 맞춰놓을 것.
- `./flyway` 폴더에 sql 파일 작성.
  - 버전은 V1부터 시작한다. (파일 prefix 부분)
  - 개발 중에는 1~2개의 sql로 초기화 작업을 끝내고 상용 배포가 시작되면 그 후부터는
    이력관리가 될 수 있도록 관리한다.

#### Flyway 명령어

- 참고: [https://flywaydb.org/documentation/usage/commandline/](https://flywaydb.org/documentation/usage/commandline/)
- 모든 flyway 명령은 (`flyway.conf`가 있는)Project_Root에서 실행하도록 한다.
- **info**

  - 현재 DB에 적용된 작업 상태 정보 출력.
  - 아직 적용되지 않는 migration이 있으면 Pending으로 표시.
  - 이전에 실패한 migraion이 있으면 Failed로 표시.

  ```bash
  flyway info
  ```

- **migrate**

  - 아직 적용되지 않은 migraion 작업들 적용.
  - 차례대로 migraion을 적용하다가 실패하는 작업이 발생하면 거기서 작업을 멈추고 실패 이력을 `flyway_schema_history` 테이블에 기록한다.
  - 작업 도중 실패한 경우 repair 명령으로 실패 이력을 처리하기 전에는 다시 migration을 시작할 수 없다.

  ```bash
  flyway migrate
  ```

- **repair**

  - 실패한 이력을 삭제하고 잘못된 정보(checksum)들을 수정하여 다시 migraion을 수행할 수 있도록 한다.
  - repair를 해도 해결이 안된다면 `flyway_schema_history` 테이블에서 문제가 있는 row를 삭제하고 다시 migrate를 실행해본다.

  ```bash
  flyway repair
  ```

- **validate**

  - 현재 migraion 파일(.sql)들이 DB에 적용된 상태와 일치하는 상태인지 판단하기 위해서 사용한다.
  - migration 파일을 한 글자라도 수정하면 checksum이 바뀌게 되어 DB에 적용된 상태와 달라지기 때문에 파일의 수정 여부를 알 수 있다.
  - 한 번 migraion을 수행하면 반드시 형상 관리 시스템에 commit하여 의도치 않은 수정사항을 복구할 수 있도록 해야 한다.

  ```bash
  flyway validate
  ```

- **~~clean~~**
  - clean 명령은 **절대 사용 금지**. DB 스키마의 모든 개체(테이블, 뷰, 프로시져 등)들을 삭제한다.
- **~~baseline~~**, **~~undo~~**
  - baseline은 migration 작업 시 자동으로 이루어지도록 설정되어있기 때문에 사용하지 않는다.
  - undo는 undo 작업을 위한 sql 파일을 만들지 않을 것이므로 사용하지 않는다.
