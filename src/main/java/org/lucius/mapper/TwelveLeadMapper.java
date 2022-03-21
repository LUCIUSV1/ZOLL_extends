package org.lucius.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.lucius.entity.Case_Info;
import org.lucius.entity.TwelveLead_Info;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TwelveLeadMapper extends BaseMapper<TwelveLead_Info> {

}
