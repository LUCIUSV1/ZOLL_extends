package org.lucius.controller.client;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lucius.Result.Result;
import org.lucius.entity.User_Info;

import org.lucius.log.LogTemplates;
import org.lucius.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lucius
 * @Date 2021-03-22
 */
@RestController
@CrossOrigin
@Component
@PropertySource(value = "application.yml")
@RequestMapping("/client")
@Api(tags = "客户端服务接口")
public class ClientUserController {

    @Autowired
    private UserMapper userMapper;

    @Value("${report.server.log}")
    private String log;

    @ApiOperation("客户端登录接口")
    @RequestMapping("/loginC/{userName}/{userPwd}")
    public Result login(@PathVariable String userName,
                        @PathVariable String userPwd){

//        LogTemplates.getLog(log,"");
        LogTemplates.getLog(log,"------------------执行登录请求---------------------");
//         用户名
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_name",userName);

//        用户名+密码
        QueryWrapper wrappers = new QueryWrapper();
        wrappers.eq("user_name",userName);
        wrappers.eq("user_pwd",userPwd);
        List<User_Info> user_info = userMapper.selectList(wrapper);
        User_Info user_infos = userMapper.selectOne(wrappers);

        if(user_info.size()>0) {
            if(user_infos!=null){
                LogTemplates.getLog(log,userName+"-"+userPwd+"  登录成功");
                LogTemplates.getLog(log,"------------------执行完毕---------------------");
                Map<String ,Object> map = new HashMap<>();
                map.put("token", null);
                map.put("userName", null);

                return new Result(200, "登录成功", map);
            }
            else{
                LogTemplates.getLog(log,userName+"-"+userPwd+"  登录失败，用户名或密码错误。");
                LogTemplates.getLog(log,"------------------执行完毕---------------------");
                Map<String ,Object> map = new HashMap<>();
                map.put("token", null);
                map.put("userName", null);
                return new Result(500, "用户名或密码错误，请重新输入", map);
            }
        }else{
            LogTemplates.getLog(log,userName+"-"+userPwd+"  登录失败，当前用户名错误或不存在。");
            LogTemplates.getLog(log,"------------------执行完毕---------------------");
            Map<String ,Object> map = new HashMap<>();
            map.put("token", null);
            map.put("userName", null);
            return new Result(401, "当前用户名错误或不存在", map);
        }
    }
}
