package iotree.authservice.mapper;

import iotree.authservice.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthMapper {
    UserVo getUserById(String id);
    List<String> getUserAuthorities(String id);
    int createOAuthUser(UserVo userVo);
    int createUserData(UserVo userVo);
    int addAuthority(String id, String authority);
}
