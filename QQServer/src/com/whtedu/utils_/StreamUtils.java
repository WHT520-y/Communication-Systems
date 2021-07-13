package com.whtedu.utils_;

import java.io.*;

/*
 *@author 文帅帅
 *@version 1.0
 */
public class StreamUtils {

    /**
     *  将字节输入流转成字节数组
     * @param is 传入字节输入流
     * @return 返回字节数组
     * @throws IOException
     */
    public static byte[] streamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream bis = new ByteArrayOutputStream();
        int data = 0;
        byte[] buff = new byte[1024];
        while ((data = is.read(buff)) != -1){
            bis.write(buff,0,data);
        }
        byte[] arr = bis.toByteArray();
        bis.close();
        return arr;
    }

    public static String streamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null){
            builder.append(line + "\r\n");
        }
        return new String(builder);
    }
}
