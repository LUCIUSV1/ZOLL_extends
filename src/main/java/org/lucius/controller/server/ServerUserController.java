package org.lucius.controller.server;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lucius.Result.Result;
import org.lucius.entity.User_Info;
import org.lucius.log.LogTemplates;
import org.lucius.mapper.UserMapper;
import org.lucius.utils.Base64SecurityUtils;
import org.lucius.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lucius
 * @Date 2021-06-02
 */
@RestController
@CrossOrigin
@Component
@PropertySource(value = "application.yml")
@RequestMapping("/server")
@Api(tags = "服务端接口")
public class ServerUserController {
    @Autowired
    private UserMapper userMapper;

    @Value("${report.server.log}")
    private String log;
    /***
     * 服务端登录接口
     * 入参： 用户名  + 密码
     * **/
    @ApiOperation("服务端登录接口")
    @RequestMapping("/loginS/{userName}/{userPwd}")
    public Result login(@PathVariable String userName,
                        @PathVariable String userPwd){
        LogTemplates.getLog(log,"------------------执行登录请求---------------------");
//         用户名
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_name",userName);
//        用户名+密码
        QueryWrapper wrappers = new QueryWrapper();
        wrappers.eq("user_name",userName);

        try {
            userPwd = Base64SecurityUtils.encryptBASE64(userPwd);
        } catch (Exception e) {
            LogTemplates.getLog(log,userName+"-"+userPwd+"  登录失败，用户名或密码错误。");
            LogTemplates.getLog(log,"------------------执行完毕---------------------");
            Map<String ,Object> map = new HashMap<>();
            map.put("token", null);
            map.put("userName", null);
            return new Result(500, "用户名或密码错误，请重新输入", map);
        }
        wrappers.eq("user_pwd",userPwd);
        wrappers.eq("user_remark","server");
        List<User_Info> user_info = userMapper.selectList(wrapper);
        User_Info user_infos = userMapper.selectOne(wrappers);

        if(user_info.size()>0) {
            if(user_infos!=null){
                LogTemplates.getLog(log,userName+"-"+userPwd+"  登录成功");
                LogTemplates.getLog(log,"------------------执行完毕---------------------");
                Map<String ,Object> map = new HashMap<>();
                map.put("token", JwtUtils.createJwt(userName,userPwd));
                map.put("userName", userName);
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

    /***
     * 服务端token验证接口
     * 入参：
     * **/
    @ApiOperation("token验证")
    @RequestMapping("/checkToken")
    public Result checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        String admin = request.getHeader("admin");

        try {
            String user = JwtUtils.parseJwt(token);

            if (!user.equals(admin)) {
                Map<String, Object> map = new HashMap<>();
                map.put("token", null);
                map.put("userName", null);
                return new Result(403, "当前token已失效", map);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("token", null);
                map.put("userName", null);
                return new Result(200, "验证通过", map);
            }
        }catch (Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("token", null);
            map.put("userName", null);
            return new Result(403, "当前token已失效", map);
        }
    }
}
