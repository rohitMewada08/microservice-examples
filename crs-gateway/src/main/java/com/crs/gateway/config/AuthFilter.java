package com.crs.gateway.config;

import com.crs.gateway.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus)  {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        response.writeWith(Mono.just(new DefaultDataBufferFactory().wrap(err.getBytes())));
        //response.writeWith(Mono.error(new Error(err)));
        return response.setComplete();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(exchange.getRequest().getURI().getRawPath().contains("/v3/api-docs/"))
                return chain.filter(exchange);

            ServerHttpRequest request = exchange.getRequest();

            if(exchange.getRequest().getURI().getRawPath().contains("/v3/api-docs/"))
                return chain.filter(exchange);

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return this.onError(exchange, "Missing authorization information", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                return this.onError(exchange, "Incorrect authorization structure", HttpStatus.UNAUTHORIZED);
            }
            return  webClientBuilder.build()
                    .post()
                    .uri("http://auth-service/oauth/check_token?token=" + parts[1])
                    .headers(headers -> headers.setBasicAuth("client", "secret"))
                    .exchangeToMono(clientResponse -> {
                        System.out.println("clientResponse -> " + clientResponse);
                        if(clientResponse.statusCode().isError()) {
                            return this.onError(exchange, "Incorrect authorization structure", HttpStatus.UNAUTHORIZED);
                        } else {
                             clientResponse.bodyToMono(UserDto.class).flatMap( userDto -> {
                                 exchange.getRequest()
                                         .mutate()
                                         .header("X-auth-user-id", String.valueOf(userDto.getUser_name()));
                                return exchange.getResponse().setComplete();
                             });
                        }
                        return chain.filter(exchange);
                    });
        };
    }

    public static class Config {
        // empty class as I don't need any particular configuration
    }

}
