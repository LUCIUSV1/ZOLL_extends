package org.lucius.log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: lucius
 * @Date :  2020-12-15
 **/
public class LogTemplates {


    public static void getLog(String addr,String msg){
        String folder = addr;
        DirectoryExists(folder);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = simpleDateFormat.format(date);
        String logFileName = folder+"/"+formatDate+".log";
        FileExists(logFileName);
        //开始执行写入
        Date date1 = new Date();
        String content = simpleDateFormat1.format(date1)+"    "+msg+"\r\n";

        try {
            FileWriterAppend(logFileName, content);
//            System.out.println("写入完成");
        }catch (Exception e){
            System.out.println("写入异常");
            }
    }

    //  判断文件是否存在
    public static void FileExists(String file){
        try {
            File file1 = new File(file);
            if (!file1.exists()) {

                new File(file).createNewFile();
                System.out.println("文件已创建--" + file);
            }
//            、、System.out.println("文件不存在--" + file);
        }catch (Exception e){
            System.out.println("io流文件异常");
        }
    }
    //判断文件夹是否为空
    public static void DirectoryExists(String file1) {
        File file = new File(file1);
        if (!file.exists() && !file.isDirectory()) {
//            System.out.println("//不存在");
            file.mkdir();
            System.out.println("文件夹已创建");
        } else {
//            System.out.println("//目录存在");
        }
    }
    //执行写入
    public static void FileWriterAppend(String fileName,String msg) throws Exception{
        File file = new File(fileName);
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(file,true);
        fileWriter.write(msg);
        fileWriter.close();
    }
}
