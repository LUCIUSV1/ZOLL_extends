package org.lucius.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lucius.entity.User_Info;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User_Info> {

}
