package top.zywork.common;

import lombok.extern.slf4j.Slf4j;
import top.zywork.enums.MIMETypeEnum;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * 图片处理相关工具类<br/>
 * 创建于2017-10-31<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Slf4j
public class ImageUtils {

    /**
     * 根据指定的图片路径，获取缓冲图片
     * @param imagePath 图片路径
     * @return 缓冲图对象，如果出现异常则返回null
     */
    public static BufferedImage getBufferedImage(String imagePath) {
        try (InputStream inputStream = new FileInputStream(new File(imagePath))) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            log.error("read image input stream error: {}, path: {}", e.getMessage(), imagePath);
        }
        return null;
    }

    /**
     * 根据图片输入流获取缓冲图片，不会关闭输入流参数
     * @param inputStream 图片输入流
     * @return 缓冲图对象
     */
    public static BufferedImage getBufferedImage(InputStream inputStream) {
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            log.error("read image from input stream error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 根据图片的字节数组数据生成缓冲图
     * @param imageData 图片的字节数组数据
     * @return 缓冲图对象
     */
    public static BufferedImage getBufferedImage(byte[] imageData) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            log.error("read image from byte array input stream error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 把指定路径的图片填充到指定的输出流，不会关闭输入输出流参数
     * @param imagePath 图片路径
     * @param out 输出流
     */
    public static void writeToOut(String imagePath, OutputStream out) {
        BufferedImage bufferedImage = getBufferedImage(imagePath);
        if (bufferedImage != null) {
            try {
                ImageIO.write(bufferedImage, FileUtils.getExt(imagePath), out);
            } catch (IOException e) {
                log.error("write image with file path {} to output stream error: {}", imagePath, e.getMessage());
            }
        }
    }

    /**
     * 根据输入流把图片内容读填充到指定的输出流，不会关闭输入输出流参数
     * @param inputStream 图片输入流
     * @param out 输出流
     * @param imageType 图片类型枚举
     */
    public static void writeToOut(InputStream inputStream, OutputStream out, MIMETypeEnum imageType) {
        BufferedImage bufferedImage = getBufferedImage(inputStream);
        if (bufferedImage != null) {
            try {
                ImageIO.write(bufferedImage, imageType.getValue(), out);
            } catch (IOException e) {
                log.error("write image with type {} to output stream error: {}", imageType.getExt(), e.getMessage());
            }
        }
    }

    /**
     * 根据指定的图片路径，获取图片对应的字节数组数据
     * @param imagePath 图片路径
     * @return 图片对应的字节数组数据
     */
    public static byte[] getImageData(String imagePath) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            writeToOut(imagePath, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("byte array output stream error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 根据图片输入流获取图片的字节数组数据，不会关闭输入流参数
     * @param inputStream 图片输入流
     * @param imageType 图片类型枚举
     * @return 图片对应的字节数组数据
     */
    public static byte[] getImageData(InputStream inputStream, MIMETypeEnum imageType) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            writeToOut(inputStream, outputStream, imageType);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("byte array output stream error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 根据图片路径获取图片的类型枚举
     * @param imagePath 图片路径
     * @return 图片类型枚举
     */
    public static MIMETypeEnum getImageType(String imagePath) {
        if (imagePath.endsWith(MIMETypeEnum.PNG.getExt())) {
            return MIMETypeEnum.PNG;
        } else if (imagePath.endsWith(MIMETypeEnum.JPG.getExt())) {
            return MIMETypeEnum.JPG;
        } else if (imagePath.endsWith(MIMETypeEnum.JPEG.getExt())) {
            return MIMETypeEnum.JPEG;
        } else if (imagePath.endsWith(MIMETypeEnum.GIF.getExt())) {
            return MIMETypeEnum.GIF;
        } else if (imagePath.endsWith(MIMETypeEnum.BMP.getExt())) {
            return MIMETypeEnum.BMP;
        }
        return null;
    }

    /**
     * 把字节数组的图片数据保存到指定路径的图片
     * @param imageData 图片数据
     * @param imagePath 图片路径
     * @param imageType 图片类型枚举
     */
    public static void saveImage(byte[] imageData, String imagePath, MIMETypeEnum imageType) {
        saveImage(imageData, imagePath, imageType.getValue());
    }

    /**
     * 把字节数组的图片数据保存到指定路径的图片
     * @param imageData 图片数据
     * @param imagePath 图片路径
     * @param imageType 图片类型字符串，如png
     */
    public static void saveImage(byte[] imageData, String imagePath, String imageType) {
        File imageFile = new File(imagePath);
        BufferedImage bufferedImage = getBufferedImage(imageData);
        if (bufferedImage != null) {
            try {
                ImageIO.write(bufferedImage, imageType, imageFile);
            } catch (IOException e) {
                log.error("save image to path {} with type {} error: {}", imagePath, imageType, e.getMessage());
            }
        }
    }

    /**
     * 把缓冲图片保存到指定路径的文件中
     * @param bufferedImage 缓冲图片
     * @param imagePath 图片路径
     * @param imageType 图片类型
     */
    public static void saveImage(BufferedImage bufferedImage, String imagePath, MIMETypeEnum imageType) {
        File imageFile = new File(imagePath);
        try {
            ImageIO.write(bufferedImage, imageType.getValue(), imageFile);
        } catch (IOException e) {
            log.error("save image to path {} with type {} error: {}", imagePath, imageType.getValue(), e.getMessage());
        }
    }

    /**
     * 从指定的图片url获取图片InputStream
     * @param urlString
     * @return
     */
    public static InputStream getInputStreamFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            return urlConnection.getInputStream();
        } catch (IOException e) {
            log.error("get image input stream from url error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 从指定的图片url获取BufferedImage
     * @param urlString
     * @return
     */
    public static BufferedImage getBufferedImageFromUrl(String urlString) {
        try {
            return ImageIO.read(new URL(urlString));
        } catch (IOException e) {
            log.error("get buffered image from url error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 根据图片输入流获取图片格式，如jpg, png，gif等
     * @param inputStream
     * @return
     */
    public static String getImageFormat(InputStream inputStream) {
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream)) {
            Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
            while (iterator.hasNext()) {
                ImageReader reader = iterator.next();
                return reader.getFormatName();
            }
        } catch (IOException e) {
            log.error("get image format from input stream error: {}", e.getMessage());
        }
        return null;
    }
    /**
     * 根据图片输入流获取图片类型枚举
     * @param inputStream
     * @return
     */
    public static MIMETypeEnum getImageTypeEnum(InputStream inputStream) {
        String type = getImageFormat(inputStream);
        if (type != null) {
            if (MIMETypeEnum.JPEG.getValue().equalsIgnoreCase(type)) {
                return MIMETypeEnum.JPEG;
            } else if (MIMETypeEnum.PNG.getValue().equalsIgnoreCase(type)) {
                return MIMETypeEnum.PNG;
            } else if (MIMETypeEnum.GIF.getValue().equalsIgnoreCase(type)) {
                return MIMETypeEnum.GIF;
            } else if (MIMETypeEnum.BMP.getValue().equalsIgnoreCase(type)) {
                return MIMETypeEnum.BMP;
            }
        }
        return null;
    }

}
