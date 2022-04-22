package iotree.authservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVo {
    private String id; // token에서 subject로 내려간다.
    private String password; // can be null if oauth user.
    private Boolean oauth; // is oauth user
    private Date created;

    private String name; // 이 필드는 편의상 token에 포함된다.
    private String cellphone;

    public void erasePersonalInfo() {
        created = null;
        password = null;
        cellphone = null;
    }
}
