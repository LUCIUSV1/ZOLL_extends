package org.lucius.utils;



import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

/**
 * 非对称加密工具类
 * @Author lucius
 * @Date 2021-06-02
 */
public class Base64SecurityUtils {
    /**
     * Base64
     *
     */
    public static void base64(String str) {
        byte[] bytes = str.getBytes();
        //Base64 加密
        String encoded = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Base 64 加密后：" + encoded);
        //Base64 解密
        byte[] decoded = Base64.getDecoder().decode(encoded);
        String decodeStr = new String(decoded);
        System.out.println("Base 64 解密后：" + decodeStr);

    }


    /**
     * BASE64解密
     * @throws Exception
     */
    public static String decryptBASE64(String key) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(key);
        String decodeStr = new String(decoded);
        return decodeStr;
    }

    /**
     * BASE64加密
     */
    public static String encryptBASE64(String str) throws Exception {
        byte[] bytes = str.getBytes();
        //Base64 加密
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }


}
