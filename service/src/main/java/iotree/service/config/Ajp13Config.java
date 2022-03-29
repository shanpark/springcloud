package iotree.service.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Apache와의 연결을 위한 AJP/1.3 프로토콜 Connector를 설정한다.
 * application.yml 파일에서 tomcat.ajp.enabled 속성을 true로 설정해야 동작한다.
 * Jar로 단독 실행 시에는 의미가 있지만 War로 다른 Tomcat에 배포를 하는 경우에는 disable 시키거나 삭제하도록 한다.
 *
 * 만약 AJP/1.3 연결이 필요없다면 이 클래스는 삭제한다. 이 경우 tomcat.ajp.enabled, tomcat.ajp.protocol,
 * tomcat.ajp.port 속성도 사용하지 않으므로 application.yml 파일에서 삭제하도록 한다.
 * 
 * AJP Connector를 추가하는 것이므로 기존 8080 포트의 Connector도 동작한다. 만약 8080 포트가 필요없다면
 * server.port 속성을 설정하여 다른 포트 번호를 사용하도록 설정하는 게 안전한다.
 */
@Configuration
public class Ajp13Config {

    @Value("${tomcat.ajp.enabled:false}")
    private boolean ajpEnabled;

    @Value("${tomcat.ajp.protocol:AJP/1.3}")
    private String ajpProtocol;

    @Value("${tomcat.ajp.port:8009}")
    private int ajpPort;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return factory -> {
            if (ajpEnabled) {
                Connector ajpConnector = new Connector(ajpProtocol);
                ajpConnector.setPort(ajpPort);
                ajpConnector.setSecure(false);
                ajpConnector.setAllowTrace(false);
                ajpConnector.setScheme("http");
                ((AbstractAjpProtocol<?>) ajpConnector.getProtocolHandler()).setSecretRequired(false);
                factory.addAdditionalTomcatConnectors(ajpConnector);
            }
        };
    }
}