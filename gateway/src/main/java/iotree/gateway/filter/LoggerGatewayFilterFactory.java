package iotree.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
class LoggerGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggerGatewayFilterFactory.Config> {

    public LoggerGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (config.enabled) {
                ServerHttpRequest request = exchange.getRequest();
                String requestId = request.getId();
                String requestPath = request.getPath().toString();

                log.info("=> {}: {}", requestId, requestPath);

                //Custom Post Filter
                return chain.filter(exchange)
                        .then(Mono.fromRunnable(() ->
                                log.info("<= {}: {} [{}]", requestId, requestPath, exchange.getResponse().getStatusCode())
                        ));
            } else {
                return chain.filter(exchange);
            }
        };
    }

    @Data
    public static class Config {
        private boolean enabled;
    }
}