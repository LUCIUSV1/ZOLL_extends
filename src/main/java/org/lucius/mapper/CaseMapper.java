package org.lucius.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.lucius.entity.Case_Info;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseMapper extends BaseMapper<Case_Info> {

    @Select("select * from case_info where  TO_DAYS(caseSaveTime) = TO_DAYS(NOW()) order by caseSaveTime desc")
    List<Case_Info> getCaseList();


}
