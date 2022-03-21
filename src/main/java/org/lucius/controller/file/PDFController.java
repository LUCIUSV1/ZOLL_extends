package org.lucius.controller.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.lucius.Result.Result;
import org.lucius.entity.Case_Info;
import org.lucius.entity.Snapshot_Info;
import org.lucius.entity.TwelveLead_Info;
import org.lucius.mapper.CaseMapper;
import org.lucius.mapper.SnapshotMapper;
import org.lucius.mapper.TwelveLeadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "报告数据接口")
public class PDFController {
    @Autowired
    private CaseMapper caseMapper;
    @Autowired
    private TwelveLeadMapper twelveLeadMapper;
    @Autowired
    private SnapshotMapper snapshotMapper;


    @ApiOperation("获取12导联报告")
    @GetMapping("/getTwelveLeadList")
    public Result getTwelveLeadList(@Param("caseSerialNumber")String caseSerialNumber) {
        List<TwelveLead_Info> twelveLead_infos = new ArrayList<>();

        if("null".equals(caseSerialNumber)){
            twelveLead_infos=  twelveLeadMapper.selectList(null);
        }else {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("caseSerialNumber", caseSerialNumber);
            twelveLead_infos=  twelveLeadMapper.selectList(queryWrapper);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("twelveLeads", twelveLead_infos);
        return new Result(200, "获取12导报告列表成功", map);
    }
    @ApiOperation("获取快照报告")
    @GetMapping("/getSnapshotList")
    public Result getSnapshotList(@Param("caseSerialNumber")String caseSerialNumber) {
        List<Snapshot_Info> snapshot_infos = new ArrayList<>();
        if("null".equals(caseSerialNumber)){
            snapshot_infos=  snapshotMapper.selectList(null);
        }else {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("caseSerialNumber", caseSerialNumber);
            snapshot_infos=  snapshotMapper.selectList(queryWrapper);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("snapshots", snapshot_infos);
        return new Result(200, "获取快照报告列表成功", map);
    }
}
