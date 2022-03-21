package org.lucius.Result;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author lucius
 * @Date 2022-01-10
 */
@Data
@AllArgsConstructor
public class ReturnResult {
    private int code;
    private String msg;
    private Object object;

    public static ReturnResult success(Object o){
        return new ReturnResult(200,"成功",o);
    }
    public static ReturnResult failed(int code,String msg){
        return new ReturnResult(code,msg,null);
    }

}
