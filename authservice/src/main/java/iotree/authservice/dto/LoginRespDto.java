package iotree.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import iotree.authservice.vo.UserVo;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRespDto {
    private int code = 0;
    private String message;
    private UserVo user;
}
