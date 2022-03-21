package org.lucius.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.lucius.entity.Case_Info;
import org.lucius.entity.Snapshot_Info;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnapshotMapper extends BaseMapper<Snapshot_Info> {

}
