package iotree.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Filter를 통과하는 요청들을 로그에 남긴다.
 * <p>
 * 아래와 예시와 같은 형태로 간단한 로그를 남긴다.
 * - Request 로그 예
 * => dfa28fbc-43: /rest/hello
 * - Response 로그 예
 * <= dfa28fbc-43: /rest/hello [200 OK]
 */
@Component
@Slf4j
class LoggerGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggerGatewayFilterFactory.Config> {

    public LoggerGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String requestId = request.getId();
            String requestPath = request.getPath().toString();

            log.info("=> {}: {}", requestId, requestPath);

            //Custom Post Filter
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() ->
                            log.info("<= {}: {} [{}]", requestId, requestPath, exchange.getResponse().getStatusCode())
                    ));
        };
    }

    @Data
    public static class Config {
    }
}