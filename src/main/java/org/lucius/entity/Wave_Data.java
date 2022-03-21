package org.lucius.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author lucius
 * @Date 2021-07-06
 */
@Data
@TableName("wave_data")
public class Wave_Data {
    @TableField("id")
    private Long id;
    @TableField("caseId")
    private String caseId;
    @TableField("headtime")
    private String headtime;
    @TableField("wavedata")
    private String wavedata;
    @TableField("nexttime")
    private String nexttime;

}
