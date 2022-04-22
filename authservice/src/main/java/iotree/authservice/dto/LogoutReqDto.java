package iotree.authservice.dto;

import lombok.Data;

/**
 * /rest/logout 요청시 전송되는 데이터 클래스
 */
@Data
public class LogoutReqDto {
    private String id;
}
