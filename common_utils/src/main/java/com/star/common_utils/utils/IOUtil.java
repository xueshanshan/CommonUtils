package com.star.common_utils.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xueshanshan
 * @date 2018/12/11
 */
public class IOUtil {
    private IOUtil() {
    }

    /**
     * 关闭流
     *
     * @param closeable 流
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LogUtil.e("close", e);
            }
        }
    }

    /**
     * 往文件里面写对象
     *
     * @param s        要被写入的对象
     * @param filePath 要被写入对象的文件名
     */
    public static void writeObject(Serializable s, String filePath) {
        writeObject(s, new File(filePath));
    }

    /**
     * 往文件里面写对象
     *
     * @param s    要被写入的对象
     * @param file 要被写入对象的文件
     */
    public static void writeObject(Serializable s, File file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(s);
            oos.close();
        } catch (IOException e) {
            LogUtil.e("writeObject", e);
        } finally {
            close(oos);
            close(fos);
        }
    }

    /**
     * 读取文件转成字符串
     *
     * @param filePath 文件路径
     * @return 读取后的字符串结果
     */
    public static String readFileToString(String filePath) {
        BufferedReader reader = null;
        try {
            FileInputStream input = new FileInputStream(filePath);
            reader = new BufferedReader(new InputStreamReader(input));
            String line;
            final List<String> buffer = new LinkedList<String>();
            while ((line = reader.readLine()) != null) {
                buffer.add(line);
            }
            return TextUtils.join("\n", buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(reader);
        }
        return "";
    }

    /**
     * 向文件中写内容
     *
     * @param filepath 被写入的文件
     * @param content  被写入的内容
     */
    public static void writeStringToFile(String filepath, String content) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "utf-8"));
            writer.write(content);
        } catch (IOException e) {
            LogUtil.e("writeStringToFile", e);
        } finally {
            close(writer);
        }
    }
}
