package com.crs.gateway.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class UserDto {
    private boolean active;
    private int exp;
    private String user_name;
    List<String> authorities;
    List<String> scope;
    private String client_id;
}
