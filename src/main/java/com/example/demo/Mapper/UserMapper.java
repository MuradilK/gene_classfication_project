package com.example.demo.Mapper;

import com.example.demo.Entitys.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO userhub (username, password, emailAdd,state) values( #{username},#{password},#{emailAdd},#{state} )")
    int addUser(UserInfo user);

    @Select("select username,password,emailAdd,state from userhub where username=#{username}")
    UserInfo  findUserByNameAndPassWord(String username);

    @Update("update userhub set state=#{state} where username=#{username}")
    int setStateByUsername(boolean state,String username);
}