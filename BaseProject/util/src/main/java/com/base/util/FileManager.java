package com.base.util;


import android.content.Context;
import androidx.annotation.NonNull;

import java.io.File;


/**
 * 缓存本地文件
 *
 * @author LiuZhi
 * @see Context#getExternalCacheDir().getAbsolutePath() 图片缓存目录
 */
public class FileManager {
    static String tag = "LocalImageManager";

    public static String dirPath = "";

    /**
     * 判断URL对应是否有本地图片
     *
     * @param url     图片路径
     * @param context 上下文
     *
     * @return true:存在，否则不存在
     */
    public static boolean hasTheFile(String url, Context context) {
        String filename = convertUrlToFileName(url);
        String dir = getDirectory(context);
        File file = new File(dir, filename);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 根据url生成文件名
     *
     * @param url http地址
     *
     * @return 文件名
     */
    public static String convertUrlToFileName(String url) {

        int index = url.lastIndexOf("/");
        if (index != -1) {
            String name = url.substring(index);
            return name;
        } else
            return url;
    }

    /**
     * @param context 上下文
     *
     * @return 获取图片缓存路径
     */
    public static String getDirectory(@NonNull Context context) {

        try {
            if (context.getExternalCacheDir() != null)
                dirPath = context.getExternalCacheDir().getAbsolutePath() + "/cache";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return dirPath;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     *
     * @return long 单位为M
     *
     * @throws Exception
     */
    public static float getFolderSize(File file) {
        float size = 0;
        File[] fileList = file.listFiles();
        if (fileList == null) {
            return 0.00f;
        }
        for(File aFileList : fileList) {
            if (aFileList.isDirectory()) {
                size = size + getFolderSize(aFileList);
            } else {
                size = size + aFileList.length();
            }
        }
        return size / 1048576;
    }

    public static void clearCache() {
        try {
            deleteDir(new File(dirPath));
        } catch (Exception ignored) {

        }
    }

    public static void clearCache(File file) {
        try {
            deleteDir(file);
        } catch (Exception ignored) {

        }
    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();//递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
