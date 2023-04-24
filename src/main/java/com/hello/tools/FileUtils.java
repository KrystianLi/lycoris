package com.hello.tools;

import org.apache.commons.codec.binary.Base64;
import java.io.*;

public class FileUtils {
    /**
     * 读取文件返回字节数组
     * @param filePath 文件路径
     * @return
     */
    public static byte[] toByteArray(String filePath){

        ByteArrayOutputStream bos=null;
        BufferedInputStream in = null;
        try {
            File f = new File(filePath);
            if(f.exists()){
                in = new BufferedInputStream(new FileInputStream(f));
                bos = new ByteArrayOutputStream((int) f.length());

                int buf_size = 1024;
                byte[] buffer = new byte[buf_size];
                int len = 0;
                while (-1 != (len = in.read(buffer, 0, buf_size))) {
                    bos.write(buffer, 0, len);
                }
                //return bos.toByteArray();
            }

        } catch (IOException e) {
            System.out.println("toByteArray() Exception");
        } finally {
            try {
                in.close();
                bos.close();
            } catch (IOException e) {
                System.out.println("toByteArray() Exception");
            }
        }
        return bos.toByteArray();
    }

    /**
     * 文件base64 编码
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static String encodeFile(String filePath){
        byte[] readFileToByteArray = toByteArray(filePath);
        return Base64.encodeBase64String(readFileToByteArray);
    }


}
