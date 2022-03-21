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
@TableName("wave_info")
public class Wave_Info{
    @TableField("wave_id")
    private Integer Wave_id;
    @TableField("WaveCaseId")
    private String WaveCaseId;
    @TableField("waveLocation")
    private String waveLocation;
    @TableField("waveSerialNumber")
    private String waveSerialNumber;
    @TableField("WaveTime")
    private Date WaveTime;

}
