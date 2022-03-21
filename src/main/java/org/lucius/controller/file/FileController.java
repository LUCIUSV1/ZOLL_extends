package org.lucius.controller.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Case;
import org.lucius.entity.Case_Info;
import org.lucius.entity.Snapshot_Info;
import org.lucius.entity.TwelveLead_Info;
import org.lucius.entity.Wave_Data;
import org.lucius.log.LogTemplates;
import org.lucius.mapper.CaseMapper;
import org.lucius.mapper.SnapshotMapper;
import org.lucius.mapper.TwelveLeadMapper;
import org.lucius.mapper.WaveDataMapper;
import org.lucius.service.ThreadService;
import org.lucius.utils.ControlFile;
import org.lucius.utils.FileUtil;
import org.lucius.utils.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @Author lucius
 * @Date 2021-10-19
 */
@Component
public class FileController {
    @Value("${report.server.jsonDir}")
    private String controlFileDir;
    @Value("${report.server.jsonTargetDir}")
    private String targetDir;
    @Value("${report.server.twelveleadDir}")
    private String twelveleadDir;
    @Value("${report.server.fileTargetDir}")
    private String fileTargetDir;
    @Value("${report.server.snapshotDir}")
    private String snapshotDir;
    @Value("${report.server.log}")
    private String log;

    @Autowired
    private CaseMapper caseMapper;
    @Autowired
    private WaveDataMapper waveDataMapper;
    @Autowired
    private TwelveLeadMapper twelveLeadMapper;
    @Autowired
    private SnapshotMapper snapshotMapper;

    @Autowired
    private ThreadService threadService;

    @Scheduled(fixedDelay = 5000)
    public void FileHandler() {
        String paString = controlFileDir;
        //监控文件夹
        //判断文件夹是否为空
        if (ControlFile.getDir(paString)) {
            try {
                //升序获取文件名
                File file = new File(paString);
                boolean flag = true;
                do {
                    if (file.exists()) {
                        String fileName = ControlFile.getFileListame(paString);
                        paString = controlFileDir + "/" + fileName;

                        String[] spilts = fileName.replaceAll(".json", "").split("-");
                        InputStream inputStream = new FileInputStream(new File(paString));
                        ByteArrayOutputStream result = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) != -1) {
                            result.write(buffer, 0, length);
                        }
                        result.close();
                        inputStream.close();
                        JSONObject jsonObjects = null;
                        String str = result.toString(StandardCharsets.UTF_8.name());
                        try {
                            jsonObjects = JSON.parseObject(str);
                        } catch (Exception e) {
                            str = str + "]}]}}]}]}}";
                            //处理文件异常问题
                        }
                        threadService.executorFile(jsonObjects,waveDataMapper,spilts[1]);

                        String jsonObject = getPatientInfo(str);
                        String[] info = jsonObject.split(",");
                        String targetString = targetDir + "/" + fileName;
                        QueryWrapper wrapper = new QueryWrapper();
                        wrapper.eq("caseSoleId", spilts[1]);
                        List<Case_Info> case_infos = caseMapper.selectList(wrapper);
                        Case_Info case_info = new Case_Info(spilts[0], new Date(), info[0], info[1], info[2], spilts[1]);

                        if (case_infos.size() <= 0) {
                            caseMapper.insert(case_info);
                            WebSocket webSocket = new WebSocket();
                            webSocket.onMessage("收到新报告");
                        } else {
                            caseMapper.update(case_info, wrapper);
                        }

                        //转移文件
                        copyFileUsingFileStreams(new File(paString), new File(targetString));
                        System.out.println("文件处理完成");  flag = false;
                        ControlFile.delete(paString);
                    }
                    Thread.sleep(1000);
                } while (flag);

            } catch (Exception e) {
                ControlFile.delete(paString);
                LogTemplates.getLog(log, "异常：" + e.getMessage());
                System.out.println("error");
            }

        }
    }
    @Scheduled(fixedDelay = 5000)
    public void twelveleadHandler() {
        String paString = twelveleadDir;
        //监控文件夹
        //判断文件夹是否为空
        if (ControlFile.getDir(paString)) {
            try {
                //升序获取文件名
                File file = new File(paString);
                boolean flag = true;
                do {
                    if (file.exists()) {
                        String fileName = ControlFile.getFileListame(paString);
                        paString = paString+"/"+fileName;
                        String twelveLeadId = fileName.split("_")[1];
                        String caseId = twelveLeadId.substring(twelveLeadId.length()-6,twelveLeadId.length());
                        caseId=caseId.replace(".pdf","");
                        QueryWrapper queryWrapper = new QueryWrapper();
                        queryWrapper.like("caseSerialNumber",caseId);
                        queryWrapper.last("limit 1");
                        Case_Info case_info = caseMapper.selectOne(queryWrapper);
                        TwelveLead_Info twelveLead_info = new TwelveLead_Info();
                        twelveLead_info.setCaseSerialNumber(case_info.getCaseSerialNumber());
                        twelveLead_info.setCreateTime(new Date());
                        twelveLead_info.setTwelveleadId(fileName.replace(".pdf",""));
                        String targetString = fileTargetDir +"/"+fileName;
                        twelveLead_info.setFilecontent(targetString);

                        QueryWrapper queryWrapper1 = new QueryWrapper();
                        queryWrapper1.eq("TwelveleadId",fileName.replace(".pdf",""));
                        TwelveLead_Info twelveLead_info1 = twelveLeadMapper.selectOne(queryWrapper);
                        if(twelveLead_info1==null) {
                            System.out.println("新增");
                            twelveLeadMapper.insert(twelveLead_info);
                        }
                        else {
                            System.out.println("修改");
                            twelveLeadMapper.update(twelveLead_info,queryWrapper1);
                        }
                        //转移文件
                        copyFileUsingFileStreams(new File(paString), new File(targetString));
                        System.out.println(fileName+"文件处理完成");  flag = false;
                        ControlFile.delete(paString);
                    }
                } while (flag);
            }catch (Exception e){
                e.printStackTrace();
                ControlFile.delete(paString);
                LogTemplates.getLog(log, "异常：" + e.getMessage());
                System.out.println("error");
            }
        }
    }
    @Scheduled(fixedDelay = 5000)
    public void snapShotHandler() {
        String paString = snapshotDir;
        //监控文件夹
        //判断文件夹是否为空
        if (ControlFile.getDir(paString)) {
            try {
                //升序获取文件名
                File file = new File(paString);
                boolean flag = true;
                do {
                    if (file.exists()) {
                        String fileName = ControlFile.getFileListame(paString);
                        paString = paString+"/"+fileName;
                        String snapshotId = fileName.split("_")[1];
                        String caseId = snapshotId.substring(snapshotId.length()-6,snapshotId.length());
                        caseId=caseId.replace(".pdf","");
                        QueryWrapper queryWrapper = new QueryWrapper();
                        queryWrapper.like("caseSerialNumber",caseId);
                        queryWrapper.last("limit 1");
                        Case_Info case_info = caseMapper.selectOne(queryWrapper);
                        Snapshot_Info snapshot_info = new Snapshot_Info();
                        snapshot_info.setCaseSerialNumber(case_info.getCaseSerialNumber());
                        snapshot_info.setCreateTime(new Date());
                        snapshot_info.setSnapshotId(fileName.replace(".pdf",""));
                        String targetString = fileTargetDir +"/"+fileName;
                        snapshot_info.setFilecontent(targetString);

                        QueryWrapper queryWrapper1 = new QueryWrapper();
                        queryWrapper1.eq("snapshotId",fileName.replace(".pdf",""));
                        Snapshot_Info snapshot_info1 = snapshotMapper.selectOne(queryWrapper);
                        if(snapshot_info1==null) {
                            System.out.println("新增");
                            snapshotMapper.insert(snapshot_info);
                        }
                        else {
                            System.out.println("修改");
                            snapshotMapper.update(snapshot_info,queryWrapper1);
                        }
                        //转移文件
                        copyFileUsingFileStreams(new File(paString), new File(targetString));
                        System.out.println(fileName+"文件处理完成");  flag = false;
                        ControlFile.delete(paString);
                    }
                } while (flag);
            }catch (Exception e){
                e.printStackTrace();
                ControlFile.delete(paString);
                LogTemplates.getLog(log, "异常：" + e.getMessage());
                System.out.println("error");
            }
        }
    }



//获取文件中第一个时间节点
     int getWaveTime(String result,int time){
         JSONObject jsonObject = JSON.parseObject(result);
         JSONArray jsonArray = jsonObject
                 .getJSONObject("ZOLL")
                 .getJSONArray("FullDisclosure")
                 .getJSONObject(0)
                 .getJSONArray("FullDisclosureRecord");
         int value =0;
         for (Object o : jsonArray) {
             if (o.toString().startsWith("{\"ContinWaveRec\":{\"StdHdr\":")) {
                 JSONObject wavObject = JSON.parseObject(o.toString());
                 Integer elapsedTime = wavObject.getJSONObject("ContinWaveRec")
                         .getJSONObject("StdHdr")
                         .getInteger("ElapsedTime");
                 if(time==0){
                     value=elapsedTime;
                     break;
                 }else{
                     if(elapsedTime>time){
                         value=elapsedTime;
                         break;
                     }
                 }
             }
         }
         return value;
     }
    JSONObject getWaveWaveByFirstTime(String result,int firstTime){
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray jsonArray = jsonObject
                .getJSONObject("ZOLL")
                .getJSONArray("FullDisclosure")
                .getJSONObject(0)
                .getJSONArray("FullDisclosureRecord");
        JSONObject waveData=null;
        for (Object o : jsonArray) {
            if (o.toString().startsWith("{\"ContinWaveRec\":{\"StdHdr\":")) {
                JSONObject wavObject = JSON.parseObject(o.toString());
                Integer elapsedTime = wavObject.getJSONObject("ContinWaveRec")
                        .getJSONObject("StdHdr")
                        .getInteger("ElapsedTime");
                if(elapsedTime==firstTime) {
                    waveData = wavObject;
                    break;
                }
            }
        }
        return waveData;
    }
    public static void copyFileUsingFileStreams(File source, File dest) throws Exception {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, bytesRead);
            }

        } finally {
            input.close();
            output.close();
            source.delete();
        }
    }
    private String getPatientInfo(String str) {
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(str);
        } catch (Exception e) {
            str = str + "]}]}}";
            jsonObject = JSON.parseObject(str);
        }
        try {
            JSONArray jsonArray = jsonObject
                    .getJSONObject("ZOLL")
                    .getJSONArray("FullDisclosure")
                    .getJSONObject(0)
                    .getJSONArray("FullDisclosureRecord");
            for (Object o : jsonArray) {
                if (o.toString().startsWith("{\"PatientInfo\"")) {
                    JSONObject wavObject = JSON.parseObject(o.toString());
                    JSONObject PatientData = wavObject.getJSONObject("PatientInfo").getJSONObject("PatientData");
                    return PatientData.getString("PatientId") + "," + PatientData.getString("Sex") + "," + PatientData.getString("Age");
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}
