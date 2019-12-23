package com.example.frame.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author danbin
 * @date 2017/12/18
 * @email android1410danbin@163.com
 */
public class MD5Utils {
    private final static String DIGEST_ALGORITHM_MD5 = "MD5";

    /**
     * 用来将字节转换成 16 进制表示的字符
     */
    private final static String[] HEX_DIGITS = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
            "f"};

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            try {
                while ((len = in.read(buffer, 0, 1024)) != -1) {
                    digest.update(buffer, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }


    /**
     * MD5加密算法的具体实现
     *
     * @param src 需要加密的字符串
     * @return 加密后的结果字符串, 32位, 小写
     */
    public static String getStringMD5(String src) {
        String result = null;
        try {
            result = new String(src);
            result = byteArrayToHexString(MessageDigest.getInstance(
                    DIGEST_ALGORITHM_MD5).digest(result.getBytes()));
        } catch (Exception err) {
            err.printStackTrace();
        }

        return result.toLowerCase();
    }

    public static String byteArrayToHexString(byte[] digest) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            result.append(byteToHexString(digest[i]));
        }
        return result.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    public static byte[] stringToByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        }
        return result;
    }

}
