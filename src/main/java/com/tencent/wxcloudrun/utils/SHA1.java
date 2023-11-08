package com.tencent.wxcloudrun.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: lantaimin
 * @version: 1.0
 * @date: 2023-11-08 14:06:59
 **/
public class SHA1 {
    public static String encode(String str) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] bt = str.getBytes();
            sha1.update(bt);
            return bytes2Hex(sha1.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
