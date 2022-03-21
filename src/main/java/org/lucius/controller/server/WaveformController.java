package org.lucius.controller.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.lucius.Result.Result;
import org.lucius.utils.FileUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @Author lucius
 * @Date 2021-06-08
 */
@RestController
@CrossOrigin
public class WaveformController {

        @RequestMapping("/waveform")
     public Result getWaveF(){
//    public static void main(String[] args) {
        String path = "E:\\test\\hl7\\1\\2.json";
//        String path = "D:\\test.data\\xml\\AR16K022298-60bfb40ca84f11b0a70d822a16022298.json";
        String content = FileUtil.readFileContent(path);

        JSONObject jsonObject = JSON.parseObject(content);

        String ZOLL = jsonObject.getString("ZOLL");

        JSONObject FullDisclosure = JSON.parseObject(ZOLL);
        String FullDisclosures = FullDisclosure.getString("FullDisclosure");
        //替换首尾字符
//        FullDisclosures = trimFirstAndLastChar(FullDisclosures,"[");
//        FullDisclosures = trimFirstAndLastChar(FullDisclosures,"]");

        JSONArray FullDisclosureRecord = JSON.parseArray(FullDisclosures);

            JSONObject jsonArrays = FullDisclosureRecord.getJSONObject(0);
        String list = jsonArrays.getString("FullDisclosureRecord");

        List<String> waveList = new ArrayList<>();
        JSONArray FullDisclosureRecords = JSON.parseArray(list);

        int j = 0;
        String result = "";

        //获取波形节点
        for (int i = 0; i < FullDisclosureRecords.size(); i++) {
            String test = FullDisclosureRecords.getJSONObject(i).getString("ContinWaveRec");
            if (test != null) {
                j++;
                result = FullDisclosureRecords.getJSONObject(i).getString("ContinWaveRec");
//                waveList.add("["+result+"]");
                waveList.add(result);
            }

        }
        List<String> unList = new ArrayList<>(); //波形数组
        List<String> waList = new ArrayList<>(); //节律数组
        List<String> back = new ArrayList<>(); //备用数组
        List<String> I = new ArrayList<>(); //备用数组
        List<String> II = new ArrayList<>(); //备用数组
        List<String> III = new ArrayList<>(); //备用数组
        List<String> aVL = new ArrayList<>(); //备用数组
        List<String> avR = new ArrayList<>(); //备用数组
        List<String> avF = new ArrayList<>(); //备用数组
        List<String> V1 = new ArrayList<>(); //备用数组
        List<String> V2 = new ArrayList<>(); //备用数组
        List<String> V3 = new ArrayList<>(); //备用数组
        List<String> V4 = new ArrayList<>(); //备用数组
        List<String> V5 = new ArrayList<>(); //备用数组
        List<String> V6 = new ArrayList<>(); //备用数组
        List<List<String>> IList = new ArrayList<>(); //备用数组

        Map<String,List<String>> ecgList = new HashMap<>();
        //for
        for (int w = 0;w<waveList.size();w++) {
            String waveform = waveList.get(w).toString();
            JSONObject waveforms = JSON.parseObject(waveform);
            String waveformList = waveforms.getString("Waveform");
            String StdHdr = waveforms.getString("StdHdr");
            JSONObject StdHdrs = JSON.parseObject(StdHdr);
//            波形重复标记
            String ElapsedTime = StdHdrs.getString("ElapsedTime");
//            System.out.println(ElapsedTime);
            JSONArray wformList = JSON.parseArray(waveformList);
            //for
            for(int wf = 0;wf<wformList.size();wf++) {
                String WaveRec = wformList.getJSONObject(wf).getString("WaveRec");
                JSONObject WaveRecs = JSON.parseObject(WaveRec);
                String WaveTypeRec = WaveRecs.getString("WaveTypeVar");

                switch (WaveTypeRec){

                    case "12-Lead I":
                        String UnpackedSamples = WaveRecs.getString("UnpackedSamples");
//                    System.out.println(UnpackedSamples);
                        if(UnpackedSamples.length()>2) {
                            UnpackedSamples = trimFirstAndLastChar(UnpackedSamples, "[");
                            UnpackedSamples = trimFirstAndLastChar(UnpackedSamples, "]");

                            String[] uns = UnpackedSamples.split(",");
                            for (String s :
                                    uns) {
                                I.add(s);
                            }
//                            IList.add(I);

                        }
                        break;
                    case "12-Lead II":
                        String UnpackedSamples1 = WaveRecs.getString("UnpackedSamples");
//                    System.out.println(UnpackedSamples);
                        if(UnpackedSamples1.length()>2) {
                            UnpackedSamples1 = trimFirstAndLastChar(UnpackedSamples1, "[");
                            UnpackedSamples1 = trimFirstAndLastChar(UnpackedSamples1, "]");

                            String[] uns = UnpackedSamples1.split(",");
                            for (String s :
                                    uns) {
                                II.add(s);
                            }
                        }
                        break;
                    case "12-Lead III":
                        String UnpackedSamples2 = WaveRecs.getString("UnpackedSamples");
//                    System.out.println(UnpackedSamples);
                        if(UnpackedSamples2.length()>2) {
                            UnpackedSamples2 = trimFirstAndLastChar(UnpackedSamples2, "[");
                            UnpackedSamples2 = trimFirstAndLastChar(UnpackedSamples2, "]");

                            String[] uns = UnpackedSamples2.split(",");
                            for (String s :
                                    uns) {
                                III.add(s);
                            }
                        }
                        break;
                    case "12-Lead aVL":
                        String UnpackedSamples3 = WaveRecs.getString("UnpackedSamples");
//                    System.out.println(UnpackedSamples);
                        if(UnpackedSamples3.length()>2) {
                            UnpackedSamples3 = trimFirstAndLastChar(UnpackedSamples3, "[");
                            UnpackedSamples3 = trimFirstAndLastChar(UnpackedSamples3, "]");

                            String[] uns = UnpackedSamples3.split(",");
                            for (String s :
                                    uns) {
                                aVL.add(s);
                            }
                        }
                        break;
                    case "12-Lead aVR":
                        String UnpackedSamples4 = WaveRecs.getString("UnpackedSamples");
//                    System.out.println(UnpackedSamples);
                        if(UnpackedSamples4.length()>2) {
                            UnpackedSamples4 = trimFirstAndLastChar(UnpackedSamples4, "[");
                            UnpackedSamples4 = trimFirstAndLastChar(UnpackedSamples4, "]");

                            String[] uns = UnpackedSamples4.split(",");
                            for (String s :
                                    uns) {
                                avR.add(s);
                            }
                        }
                        break;
                    case "12-Lead aVF":
                        break;
                    case "12-Lead V1":
                        break;
                    case "12-Lead V2":
                        break;
                    case "12-Lead V3":
                        break;
                    case "12-Lead V4":
                        break;
                    case "12-Lead V5":
                        break;
                    case "12-Lead V6":
                        break;
                }




            }
//            System.out.println(unList);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("I", I);
        map.put("II", II);
        map.put("III", III);
        map.put("aVR", avR);

//        map.put("waveList", waList);
        return new Result(200, "数据解析完成", map);
//        return waveList;
    }

    public static boolean useList(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
        }
    public static String trimFirstAndLastChar(String str, String element) {
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do {
            int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
            int endIndex = str.lastIndexOf(element) + 1 == str.length() ? str.lastIndexOf(element) : str.length();
            str = str.substring(beginIndex, endIndex);
            beginIndexFlag = (str.indexOf(element) == 0);
            endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
        } while (beginIndexFlag || endIndexFlag);
        return str;
    }

    public String getWaveform() throws Exception {
        String path = "E:\\test\\hl7\\1\\AR16K022298-60bfb40ca84f11b0a70d822a16022298.json";
        String content = FileUtil.readFileContent(path);

        JSONObject jsonObject = JSON.parseObject(content);

        String ZOLL = jsonObject.getString("ZOLL");

        System.out.println(1);
        return content;
    }


    @RequestMapping("/getHttp")
    public String getHttp() {
        String d = doHttpPost("{\n" +
                "    \"caseSerialNumber\": \"AR16K022298\",\n" +
                "    \"caseSaveTime\": \"2021-06-08 16:44:12\",\n" +
                "    \"patientId\": \"患者1234\",\n" +
                "    \"gender\": \"男\"\n" +
                "}", "http://192.168.0.108:10002/client/getCase");
        return d;
    }


    public static String doHttpPost(String xmlInfo, String URL) {
//        System.out.println("发起的数据:" + xmlInfo);
        byte[] xmlData = xmlInfo.getBytes();
        InputStream instr = null;
        java.io.ByteArrayOutputStream out = null;
        try {
            java.net.URL url = new URL(URL);
            URLConnection urlCon = url.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("content-Type", "application/json");
            urlCon.setRequestProperty("charset", "utf-8");
            urlCon.setRequestProperty("Content-length",
                    String.valueOf(xmlData.length));
//            System.out.println(String.valueOf(xmlData.length));
            DataOutputStream printout = new DataOutputStream(
                    urlCon.getOutputStream());
            printout.write(xmlData);
            printout.flush();
            printout.close();
            instr = urlCon.getInputStream();
            byte[] bis = IOUtils.toByteArray(instr);
            String ResponseString = new String(bis, "UTF-8");
            if ((ResponseString == null) || ("".equals(ResponseString.trim()))) {
//                System.out.println("返回空");
            }
//            System.out.println("返回数据为:" + ResponseString);90982182
            return ResponseString;

        } catch (Exception e) {
            System.out.println(2);
            e.printStackTrace();
            return "0";
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (instr != null) {
                    instr.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
//                System.out.println(1);
                return "0";
            }
        }

    }
}
