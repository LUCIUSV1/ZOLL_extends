package org.lucius.controller.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.corba.se.spi.ior.ObjectKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.lucius.Result.Result;
import org.lucius.Result.ReturnResult;
import org.lucius.config.RedisCache;
import org.lucius.entity.Case_Info;
import org.lucius.entity.Wave_Data;
import org.lucius.entity.Wave_Info;
import org.lucius.mapper.CaseMapper;
import org.lucius.mapper.WaveDataMapper;
import org.lucius.mapper.WaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.lucius.Result.ErrorCode.QUERY_EXIST;


@RestController
@Slf4j
@Api(tags = "波形数据接口")
public class HeartLineController {
    @Autowired
    private RedisCache redisCache;
    @Value("${report.server.jsonTargetDir}")
    private String targetDir;
    private static Integer FINAL_TIME = 0;
    @Autowired
    private WaveMapper waveMapper;
    @Autowired
    private WaveDataMapper waveDataMapper;

    /***
     * 获取波形数据
     * @param time
     * @param caseId
     * @return
     */
    @ApiOperation("获取波形数据")
    @GetMapping("/getLine")
    public ReturnResult test(Integer time,String caseId){
        //根据 时间 获取指定段的数据
        LambdaQueryWrapper<Wave_Data> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Wave_Data::getCaseId,caseId);
        queryWrapper.gt(Wave_Data::getHeadtime,time);
        queryWrapper.select(Wave_Data::getWavedata);
        queryWrapper.last("limit 1");
        Wave_Data wave_data = waveDataMapper.selectOne(queryWrapper);
        if(wave_data!=null){
            return ReturnResult.success(JSONObject.parseObject(wave_data.getWavedata()));
        }else{
            return ReturnResult.failed(QUERY_EXIST.getCode(),QUERY_EXIST.getMsg());
        }
//
//
//
//        //旧数据查询条件
//        QueryWrapper wrapper  = new QueryWrapper();
//        wrapper.eq("waveCaseId",file.split("-")[1]);
//        String waveCaseid = file.split("-")[1];
//        wrapper.eq("waveSerialNumber",file.split("-")[0]);
//        //拼接新对象
//        Wave_Info wave_info1 = new Wave_Info();
//        wave_info1.setWaveCaseId(file.split("-")[1]);
//        wave_info1.setWaveSerialNumber(file.split("-")[0]);
//
//        file = targetDir+"\\"+file+".json";
//        InputStream inputStream = new FileInputStream(new File(file));
//
//        ByteArrayOutputStream result = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = inputStream.read(buffer)) != -1) {
//            result.write(buffer, 0, length);
//        }
//        String str = result.toString(StandardCharsets.UTF_8.name());
//
//        //根据条件查询数据 第一次应为空
//        Wave_Info wave_info = waveMapper.selectOne(wrapper);
//
//        wave_info1.setWaveLocation(file);
//        wave_info1.setWaveTime(new Date());
//
//        //回放数据处理
////      为1说明在初次浏览页面
//        if(status.equals("1")) {
//            if (wave_info == null) {
//                //执行新增
//                waveMapper.insert(wave_info1);
//            } else {
//                //根据条件进行修改
//                QueryWrapper wrapper1 = new QueryWrapper();
//                wrapper1.eq("waveCaseId", waveCaseid);
//                waveMapper.update(wave_info1, wrapper1);
//            }
//        }
//        return getJsonObject(time, str);
    }
    @ApiOperation("获取患者信息")
    @GetMapping("/getPatientInfo")
    public Object getPatientInfos(String file) throws IOException {
        file = targetDir+"/"+file+".json";
        InputStream inputStream = new FileInputStream(new File(file));

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String str = result.toString(StandardCharsets.UTF_8.name());
        return getPatientInfo(str);
    }
    @Autowired
    private CaseMapper caseMapper;

    @ApiOperation("获取趋势数据")
    @GetMapping("/getTrendRpt")
    public Object getTrendRpt(String file,Integer times) throws IOException {
        file = targetDir+"/"+file+".json";
        InputStream inputStream = new FileInputStream(new File(file));

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String str = result.toString(StandardCharsets.UTF_8.name());
        return getTrendRptData(str,times);
    }
    @ApiOperation("获取初始时间")
    @GetMapping("/getTime")
    public Integer getTime()    {
        return FINAL_TIME;
    }
    @ApiOperation("获取数据回放列表")
    @GetMapping("/getRecordList")
    public Result getRecordList() {
        List<Case_Info> case_infos = caseMapper.selectList(null);
        Map<String, Object> map = new HashMap<>();
        map.put("waveLists", case_infos);
        return new Result(200, "获取case列表成功", map);
    }

    /***
     * 获取case起始时间
     * @param caseId
     * @return
     */
    @ApiOperation("获取文件中起始时间")
    @GetMapping("/getRecordTime")
    public ReturnResult getRecordTime(String caseId) {
        LambdaQueryWrapper<Wave_Data> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Wave_Data::getCaseId,caseId);
        queryWrapper.select(Wave_Data::getHeadtime);
        queryWrapper.last("limit 1");
        Wave_Data wave_data = waveDataMapper.selectOne(queryWrapper);
        if(wave_data!=null){
            return ReturnResult.success(wave_data.getHeadtime());
        }else{
            return ReturnResult.failed(QUERY_EXIST.getCode(),QUERY_EXIST.getMsg());
        }
    }
    @ApiOperation("获取文件中所有时间")
    @GetMapping("/getAllTime")
    public List<Times> getAllTime(String file) throws IOException {
        file = targetDir+"/"+file+".json";
        InputStream inputStream = new FileInputStream(new File(file));
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String str = result.toString(StandardCharsets.UTF_8.name());
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(str);
        } catch (Exception e) {
            str = str + "]}]}}";
            jsonObject = JSON.parseObject(str);
        }
        Map<String,Object> map = new LinkedHashMap<>();
        List<Times> list = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject
                    .getJSONObject("ZOLL")
                    .getJSONArray("FullDisclosure")
                    .getJSONObject(0)
                    .getJSONArray("FullDisclosureRecord");
            for (Object o : jsonArray) {
                if (o.toString().startsWith("{\"ContinWaveRec\":{\"StdHdr\":")) {
                    JSONObject wavObject = JSON.parseObject(o.toString());
                    Integer elapsedTime = wavObject.getJSONObject("ContinWaveRec")
                            .getJSONObject("StdHdr")
                            .getInteger("ElapsedTime");
                    Times times = new Times();
                    times.setTime(wavObject.getJSONObject("ContinWaveRec")
                            .getJSONObject("StdHdr")
                            .getString("DevDateTime").replaceAll("T"," "));
                    times.setTimes(elapsedTime);
                    list.add(times);
                }

            }
            return list;

        } catch (Exception e) {
            return  null;
        }

    }
    private JSONObject getJsonObject(Integer time, String str) {
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
                if (o.toString().startsWith("{\"ContinWaveRec\":{\"StdHdr\":")) {
                    JSONObject wavObject = JSON.parseObject(o.toString());
                    Integer elapsedTime = wavObject.getJSONObject("ContinWaveRec")
                            .getJSONObject("StdHdr")
                            .getInteger("ElapsedTime");

                    if (elapsedTime>time) {
                        log.info("读取第" + elapsedTime + "秒的数据");
//                        FINAL_TIME = time;
                        return wavObject;
                    }
                }
            }
//            FINAL_TIME = 2;
            return new JSONObject();
        } catch (Exception e) {
            return new JSONObject();
        }
    }
    private JSONObject getPatientInfo(String str) {
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
                    return wavObject;
                }
            }
            FINAL_TIME = 2;
            return new JSONObject();
        } catch (Exception e) {
            return new JSONObject();
        }
    }
    private JSONObject getTrendRptData(String str,Integer time) {
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
                if (o.toString().startsWith("{\"TrendRpt\"")) {
                    JSONObject wavObject = JSON.parseObject(o.toString());
                    Integer elapsedTime = wavObject.getJSONObject("TrendRpt")
                            .getJSONObject("StdHdr")
                            .getInteger("ElapsedTime");
                    if (elapsedTime>time) {

                        return wavObject;
                    }
                }
            }
            FINAL_TIME = 2;
            return new JSONObject();
        } catch (Exception e) {
            return new JSONObject();
        }
    }
}
class Times{
    private String time;
    private Integer times;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
