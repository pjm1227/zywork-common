package top.zywork.common;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.enums.CharsetEnum;

import java.io.*;
import java.util.List;

/**
 * 输入输出工具类<br />
 * 创建于2017-11-01
 *
 * @author 王振宇
 * @version 1.0
 */
public class IOUtils {

    private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);

    /**
     * 获取指定文件内的所有字符文本内容
     * @param path 指定文件的路径
     * @param charsetName 编码名称
     * @return 文件的所有文本内容
     */
    public static String getText(String path, String charsetName) {
        try {
            return getText(new FileInputStream(path), charsetName);
        } catch (FileNotFoundException e) {
            logger.error("file {} not found error: {}", path, e.getMessage());
        }
        return null;
    }

    /**
     * 以UTF-8编码读取文件内的字符文本内容
     * @param path
     * @return
     */
    public static String getText(String path) {
        return getText(path, CharsetEnum.UTF8.getValue());
    }

    /**
     * 获取指定输入流的所有字符文本内容，不会关闭输入流参数
     * @param inputStream 字符文件输入流
     * @param charsetName 编码名称
     * @return 文件的所有文本内容
     */
    public static String getText(InputStream inputStream, String charsetName) {
        StringBuilder text = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
            String str;
            while ((str = reader.readLine()) != null) {
                text.append(str).append("\r\n");
            }
        } catch (IOException e) {
            logger.error("read text file from input stream error: {}", e.getMessage());
        } finally {
            if (reader !=  null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("buffered reader close error: {}", e.getMessage());
                }
            }
        }
        return text.toString();
    }

    /**
     * 使用UTF-8编码读取指定输入流中的字符文本内容
     * @param inputStream
     * @return
     */
    public static String getText(InputStream inputStream) {
        return getText(inputStream, CharsetEnum.UTF8.getValue());
    }

    /**
     * 写出文本内容到指定路径的文件
     * @param text 字符串内容
     * @param filePath 文件路径
     * @param charsetName 编码名称
     */
    public static void writeText(String text, String filePath, String charsetName) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(filePath), charsetName));
            bufferedWriter.write(text);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                logger.error("write text to file error: {}", e.getMessage());
            }
        }
    }

    /**
     * 使用UTF-8默认编码写出文本内容到指定路径的文件
     * @param text 字符串内容
     * @param filePath 文件路径
     */
    public static void writeText(String text, String filePath) {
        writeText(text, filePath, CharsetEnum.UTF8.getValue());
    }

    /**
     * 获取指定路径文件的字节数组
     * @param path 文件路径
     * @return 文件对应的字节数组数据
     */
    public static byte[] getData(String path) {
        try {
            return getData(new FileInputStream(new File(path)));
        } catch (FileNotFoundException e) {
            logger.error("file {} not found error: {}", path, e.getMessage());
        }
        return null;
    }

    /**
     * 获取输入流对应的字节数组，不会关闭输入流参数
     * @param inputStream 输入流
     * @return 输入流对应的字节数组数据
     */
    public static byte[] getData(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        inputToOutput(inputStream, outputStream);
        byte[] bytes = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            logger.error("byte array output stream close error: {}", e.getMessage());
        }
        return bytes;
    }

    /**
     * 把指定路径文件的数据放入到指定输出流，不会关闭输出流参数
     * @param path 文件路径
     * @param outputStream 输出流对象
     */
    public static void inputToOutput(String path, OutputStream outputStream) {
        try {
            InputStream inputStream = new FileInputStream(new File(path));
            inputToOutput(inputStream, outputStream);
            inputStream.close();
        } catch (IOException e) {
            logger.error("read file {} to output stream error: {}", path, e.getMessage());
        }
    }

    /**
     * 把输入流的数据放入到指定输出流，不会关闭输入输出流参数
     * @param inputStream 输入流
     * @param outputStream 输出流
     */
    public static void inputToOutput(InputStream inputStream, OutputStream outputStream) {
        byte[] bytes = new byte[1024];
        try {
            for (int length; (length = inputStream.read(bytes)) != -1;) {
                outputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            logger.error("read input stream to output stream error: {}", e.getMessage());
        }
    }


    /**
     * 字节输出流转化为字节输入流
     * @param byteArrayOutputStream
     * @return
     */
    public static ByteArrayInputStream outputToInput(ByteArrayOutputStream byteArrayOutputStream) {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /**
     * 把JSON文件读入到指定的对象中
     * @param path json文件路径
     * @param tClass 指定的对象类
     * @param <T>
     * @return
     */
    public static <T> T readJsonFileToObject(String path, Class<T> tClass) {
        return readJsonStrToObject(getText(path), tClass);
    }

    /**
     * 把JSON文件读入到指定的对象中，返回List
     * @param path json文件路径
     * @param tClass 指定的对象类
     * @param <T>
     * @return
     */
    public static <T> List<T> readJsonFileToList(String path, Class<T> tClass) {
        return readJsonStrToList(getText(path), tClass);
    }

    /**
     * 把JSON字符串读入到指定的对象中
     * @param jsonStr json字符中
     * @param tClass 指定的对象类
     * @param <T>
     * @return
     */
    public static <T> T readJsonStrToObject(String jsonStr, Class<T> tClass) {
        return JSON.parseObject(jsonStr, tClass);
    }

    /**
     * 把JSON字符串读入到指定的对象中，返回List
     * @param jsonStr json字符中
     * @param tClass 指定的对象类
     * @param <T>
     * @return
     */
    public static <T> List<T> readJsonStrToList(String jsonStr, Class<T> tClass) {
        return JSON.parseArray(jsonStr, tClass);
    }


    /**
     * 把JSON 输入流读入到指定的对象中
     * @param inputStream json InputStream
     * @param tClass 指定的对象类
     * @param <T>
     * @return
     */
    public static <T> T readJsonInputStreamToObject(InputStream inputStream, Class<T> tClass) {
        return readJsonStrToObject(getText(inputStream, CharsetEnum.UTF8.getValue()), tClass);
    }

    /**
     * 把JSON 输入流读入到指定的对象中，返回List
     * @param inputStream json InputStream
     * @param tClass 指定的对象类
     * @param <T>
     * @return
     */
    public static <T> List<T> readJsonInputStreamToList(InputStream inputStream, Class<T> tClass) {
        return readJsonStrToList(getText(inputStream, CharsetEnum.UTF8.getValue()), tClass);
    }

}
