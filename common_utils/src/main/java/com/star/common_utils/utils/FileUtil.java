package com.star.common_utils.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author xueshanshan
 * @date 2018/12/11
 */
public class FileUtil {
    private FileUtil() {
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹路径
     * @return 返回是否创建成功
     */
    public static boolean makeDirIfNotExist(String path) {
        File rootDirFile = new File(path);
        if (!rootDirFile.exists()) {
            return rootDirFile.mkdirs();
        }
        return false;
    }

    /**
     * 创建文件
     *
     * @param filepath 文件路径
     * @return 创建的文件
     */
    public static File createFileIfNotExist(String filepath) {
        File file = new File(filepath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            LogUtil.e("createFileIfNotExist filepath: " + filepath, e);
        }
        return file;
    }

    /**
     * 递归删除文件
     *
     * @param file 要删除的文件
     */
    public static void deleteFiles(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFiles(f);
                }
            }
            file.delete();
        }
    }
}
