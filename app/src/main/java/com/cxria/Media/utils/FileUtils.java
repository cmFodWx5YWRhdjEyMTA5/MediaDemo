package com.cxria.Media.utils;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by yukun on 17-9-29.
 */

public class FileUtils {
    public final static String PHOTO_PATH = "mnt/sdcard/MediaCamera";

    // 创建并保存图片文件
    public static void createFile(){
        File mFile = new File(PHOTO_PATH);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
    }
    public static String getPhotoFileName() {
        createFile();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'LOCK'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
}
