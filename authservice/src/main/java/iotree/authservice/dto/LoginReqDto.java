package iotree.authservice.dto;

import lombok.Data;

@Data
public class LoginReqDto {
    private String id;
    private String password;
}
