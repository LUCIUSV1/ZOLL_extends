package org.lucius.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author lucius
 * @Date 2021-06-17
 */
@Data
@TableName("snapshot_info")
@AllArgsConstructor
@NoArgsConstructor
public class Snapshot_Info {
    @TableField("id")
    public int  id;
    @TableField("snapshotId")
    public String snapshotId;
    @TableField("caseSerialNumber")
    public String  caseSerialNumber;
    @TableField("createTime")
    public Date  createTime;
    @TableField("filecontent")
    public String  filecontent;

}
