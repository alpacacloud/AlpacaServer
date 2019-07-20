package com.alpaca.infrastructure.core.utils.file;

import org.springframework.util.StringUtils;

import java.io.*;

/**
 * @author Lichenw
 * Created on 2019/3/25
 */
public class FileHelper {

    protected FileHelper() {
    }
    public final static String PATH_SPLIT_CHARTER = "/";

    /**
     * 判断文件是否为图片
     *
     * @param ext
     * @return
     */
    public static boolean isImage(String ext) {
        switch (ext.toLowerCase()) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
            case "tif":
                return true;
            default:
                return false;
        }
    }

    /**
     * 校验Shp文件是否完整 .dbf .shp .shx
     *
     * @param ext
     * @return
     */
    public static boolean isShape(String ext) {
        switch (ext.toLowerCase()) {
            case "dbf":
            case "shp":
            case "shx":
                return true;
            default:
                return false;
        }
    }

    /**
     * file to byte
     *
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * byte to file
     *
     * @param filePath
     * @return
     */
    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String BuildFolderPath(String fileId){
        StringBuilder pathBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(fileId)) {
            if (fileId.length() > 0) {
                pathBuilder.append(fileId.substring(0, 1));
                pathBuilder.append(PATH_SPLIT_CHARTER);
            }
            if (fileId.length() > 1) {
                pathBuilder.append(fileId.substring(1, 2));
                pathBuilder.append(PATH_SPLIT_CHARTER);
            }
            if (fileId.length() > 2) {
                pathBuilder.append(fileId.substring(2, 3));
                pathBuilder.append(PATH_SPLIT_CHARTER);
            }
        }

        return pathBuilder.toString();
    }

}
