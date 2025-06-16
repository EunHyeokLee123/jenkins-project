package com.playdata.gatewayservice.filter;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory <GlobalFilter.Config>{


      public GlobalFilter() {
          super(Config.class);
      }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchage , chain) ->{
            ServerHttpRequest request = exchage.getRequest();
            ServerHttpResponse response = exchage.getResponse();
            if (config.isPreLogger()){
              log.info("Request URI: {}",request.getURI());
            }
            return chain.filter(exchage).then(Mono.fromRunnable(()->{
                 if (config.isPostLogger()){
                     log.info("Gloal Post Filter active! response code: {}",response.getStatusCode());
                 }
            }));
        } ;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Config {

        private String baseMessage;
        private  boolean preLogger;
        private  boolean postLogger;

    }
}
