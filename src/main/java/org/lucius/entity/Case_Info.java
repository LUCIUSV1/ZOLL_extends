package org.lucius.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author lucius
 * @Date 2021-06-17
 */
@Data
@TableName("Case_info")
public class Case_Info {
    @TableField("caseSerialNumber")
    public String  caseSerialNumber;
    @TableField("caseSaveTime")
    public Date caseSaveTime;
    @TableField("patientId")
    public String  patientId;
    @TableField("gender")
    public String  gender;
    @TableField("caseSoleId")
    public String  caseSoleId;
    @TableField("age")
    public String  age;
    public Case_Info(){}
    public Case_Info(String caseSerialNumber, Date caseSaveTime, String patientId, String gender,String age, String caseSoleId){
        this.caseSaveTime=caseSaveTime;
        this.caseSerialNumber =caseSerialNumber;
        this.patientId =patientId;
        this.age =age;
        this.gender = gender;
        this.caseSoleId = caseSoleId;

    }
}
