package com.crs.gateway.dto;

import lombok.Data;
import org.springframework.web.server.ServerWebExchange;

@Data
public class AuthorizationResponse {
    ServerWebExchange serverWebExchange;
    Boolean isValid = false;
}
