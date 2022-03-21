package org.lucius.utils;


import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


/**
 *
 * @Author lucius
 * @Date 2021-06-09
 */
public class FileUtil {

    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
    public static String getPdfText(String pathString) {
        String result = null;
        FileInputStream is = null;
        PDDocument document = null;
        try {
            is = new FileInputStream(pathString);
            PDFParser parser = new PDFParser(new RandomAccessBuffer(is));
            parser.parse();
            document = parser.getPDDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            result = stripper.getText(document);
            if (is != null) {
                is.close();
                is = null;
            }
            if (document != null) {
                document.close();
                document = null;
            }
            return result;
        } catch (FileNotFoundException e) {

            // TODO Auto-generated catch block
//			e.printStackTrace();
            return "";
        } catch (Exception e) {

            // TODO Auto-generated catch block
//			e.printStackTrace();
            return "";
        }

    }



}
