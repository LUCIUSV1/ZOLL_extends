package org.lucius.controller.client;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lucius.Result.Result;
import org.lucius.entity.Case_Info;
import org.lucius.mapper.CaseMapper;
import org.lucius.service.RedisService;
import org.lucius.utils.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author lucius
 * @Date 2021-06-08
 */
@RestController
@CrossOrigin
@Component
@PropertySource(value = "application.yml")
@RequestMapping("/client")
@Api(tags = "case接口")
public class MsgController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CaseMapper caseMapper;

    @RequestMapping("/addCase")
    public String AddCases(){


        return "保存完成";
    }
    @ApiOperation("Case列表获取接口")
    @RequestMapping("/getCase")
    public Result getCases(){

        List<Case_Info> case_infos = caseMapper.getCaseList();

        Map<String, Object> map = new HashMap<>();
        map.put("caseList", case_infos);
        return new Result(200, "获取case列表成功", map);
    }

}
