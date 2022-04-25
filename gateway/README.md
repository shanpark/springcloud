# gateway

- 기본 설정값은 아래와 같다.

```yml
spring:
  application:
    name: TREE-GATEWAY
  cloud:
    gateway:
      routes:
        - id: HEALTH-CHECK
          uri: lb://TREE-GATEWAY # madatory지만 filter에서 완료되기 때문에 의미 없음.
          predicates:
            - Path=/healthcheck
          filters:
            - name: HealthCheck

        ...

        - id: SOME-SERVICE
          uri: lb://SOME-SERVICE
          predicates:
            - Path=/rest/**
          filters:
            - name: Logger
            - name: JwtAuth
              args:
                authorities: ROLE_USER

gateway:
  filter:
    jwt-auth:
      cookie-name: jwt
      secret-key: Ke5r!tW*teiif3xm6d#nh_afeijfv39ru!68h-r!ewijfsajif8ewjh5dw2m2dwk # authservice가 제공하는 key값과 같아야 한다.
      header-name: X-Auth
      jwt-claims: sub, name

server:
  port: 9001

eureka:
  client:
    service-url:
      defaultZone: http://192.168.1.156:8761/eureka, http://192.168.1.103:8761/eureka

management:
  endpoints:
    web:
      exposure:
        include: info, health, metrics

logging:
  config: classpath:logback.xml
```

- gateway 는 기본적으로 route 정보가 필요하므로 각 서버의 application.yml 은 최소한 아래와 같은 설정이 필요하다.

```yml
spring:
  cloud:
    gateway:
      routes:
        - id: HEALTH-CHECK
          uri: lb://GATEWAY # madatory지만 filter에서 완료되기 때문에 의미 없음.
          predicates:
            - Path=/healthcheck
          filters:
            - name: HealthCheck

        ...

        - id: SOME-SERVICE
          uri: lb://SOME-SERVICE
          predicates:
            - Path=/rest/**
          filters:
            - name: Logger
            - name: JwtAuth
              args:
                authorities: ROLE_USER

gateway:
  filter:
    jwt-auth:
      cookie-name: jwt
      secret-key: Ke5r!tW*teiif3xm6d#nh_afeijfv39ru!68h-r!ewijfsajif8ewjh5dw2m2dwk
      header-name: X-Auth
      jwt-claims: sub, name

eureka:
  client:
    service-url:
      defaultZone: http://192.168.1.156:8761/eureka, http://192.168.1.103:8761/eureka
```

- route 정보, 필터 설정, discovery 서버 설정 값들을 환경에 맞게 설정해준다.
- Filter는 아래 3가지 기본으로 제공한다.
  - HealthCheck: health check 를 위해서 특정 경로에 OK를 반환하는 필터. /actuator/health 가 제공되기 때문에 굳이 사용할 필요는 없다.
  - Logger: 지정된 경로에 대한 access 로그를 남긴다. 로그가 필요한 경우에만 filter 에 추가한다.
  - JwtAuth: 쿠키에서 정상적인 JWT 가 없는 경우 access 를 제한한다. 추가로 authority 를 설정할 수 있다.
    - authority: 통과시킬 role을 지정한다. 지정하지 않으면 권한 검사를 하지 않고 모두 통과 시킨다.
- JwtAuth 필터는 Customizing 을 위한 설정이 필요하다.
  - cookie-name: JWT 가 전달되는 쿠키 이름.
  - secret-key: JWT 검증을 위한 secret 키.
  - header-name: downstream 으로 JWT 에서 뽑아낸 정보를 전달할 header 이름. 설정하지 않으면 전달 안함.
  - jwt-claims: JWT 에서 뽑아낼 정보 필드. JSON 포맷의 문자열을 base64로 encoding하여 위에서 지정한 header로 전송.
- JwtAuth 필터를 통과하지 못하는 경우 401 Unauthorized 와 함께 아래와 같은 형태로 응답이 내려온다.
  ```JSON
  {
    "code": -3,
    "message": "Access with invalid authority."          
  }
  ```