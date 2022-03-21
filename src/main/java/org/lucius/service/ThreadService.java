package org.lucius.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.lucius.entity.Wave_Data;
import org.lucius.mapper.WaveDataMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author lucius
 * @Date 2022-01-11
 */
@Component
public class ThreadService {

    //    在线程池执行，不影响原有主线程
    @Async("taskExecutor")
    public void executorFile(JSONObject jsonObjects , WaveDataMapper waveDataMapper,String s1) {
        long startime= System.currentTimeMillis();
        JSONArray jsonArray = jsonObjects
                .getJSONObject("ZOLL")
                .getJSONArray("FullDisclosure")
                .getJSONObject(0)
                .getJSONArray("FullDisclosureRecord");
        Wave_Data wave_data = null;
        for (Object o : jsonArray) {
            if (o.toString().startsWith("{\"ContinWaveRec\":{\"StdHdr\":")) {
                JSONObject wavObject = JSON.parseObject(o.toString());
//                               第一个波形节点时间
                Integer elapsedTime = wavObject.getJSONObject("ContinWaveRec")
                        .getJSONObject("StdHdr")
                        .getInteger("ElapsedTime");
                wave_data = new Wave_Data();
                wave_data.setCaseId(s1);
                wave_data.setWavedata(wavObject.toString());
                wave_data.setHeadtime(String.valueOf(elapsedTime));
                LambdaQueryWrapper<Wave_Data> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Wave_Data::getHeadtime,elapsedTime);
                lambdaQueryWrapper.eq(Wave_Data::getCaseId,s1);
                if(waveDataMapper.selectOne(lambdaQueryWrapper)==null) {
                    waveDataMapper.insert(wave_data);
                }
            }
        }
        long endtime= System.currentTimeMillis()-startime;
        System.out.println("执行时间"+endtime+"ms");
    }
//    @Async("taskExecutor")
//    public void test2() {
//        try {
//            Thread.sleep(2000);
//            System.out.println("测试线程2");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
