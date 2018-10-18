package top.zywork.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

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
     * @return 文件的所有文本内容
     */
    public static String getText(String path) {
        try {
            return getText(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            logger.error("file {} not found error: {}", path, e.getMessage());
        }
        return null;
    }

    /**
     * 获取指定输入流的所有字符文本内容，不会关闭输入流参数
     * @param inputStream 字符文件输入流
     * @return 文件的所有文本内容
     */
    public static String getText(InputStream inputStream) {
        StringBuilder text = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            while ((str = reader.readLine()) != null) {
                text.append(str);
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

}
