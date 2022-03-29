# service

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
