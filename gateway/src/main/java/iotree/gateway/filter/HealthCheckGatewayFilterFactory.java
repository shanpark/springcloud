package iotree.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HealthCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<HealthCheckGatewayFilterFactory.Config> {

    public HealthCheckGatewayFilterFactory() {
        super(HealthCheckGatewayFilterFactory.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        };
    }

    @Data
    public static class Config {
    }
}
