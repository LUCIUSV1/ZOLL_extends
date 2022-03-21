package org.lucius.Result;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author lucius
 * @Date 2021-03-22
 */

//接口返回类
@Data
public class Result {
    private  Integer code;
    private String msg;
    private Map<String ,Object> diagnose;



    public Result(Integer code, String msg, Map<String,Object> diagnose){
        this.code = code;
        this.msg = msg;
        this.diagnose =diagnose;
    }
}
