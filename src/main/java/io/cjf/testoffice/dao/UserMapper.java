package io.cjf.testoffice.dao;

import io.cjf.testoffice.po.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

//    custom
    List<User> selectAll();

    List<User> selectByIds(@Param("userIds") List<Long> userIds);
}