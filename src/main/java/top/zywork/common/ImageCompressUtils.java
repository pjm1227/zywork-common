package top.zywork.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.zywork.common.gif.AnimatedGifEncoder;
import top.zywork.common.gif.GifDecoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图片压缩工具类<br/>
 * 创建于2019-01-02<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Slf4j
public class ImageCompressUtils {
    
    /**
     * 把图片按照指定的比例进行压缩，减小到指定的大小
     * @param bufferedImage 原图BufferedImage
     * @param scale 压缩比例，1表示原图，0.5表示一半大小
     * @return
     */
    public static BufferedImage compress(BufferedImage bufferedImage, float scale) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);
       return newBufferedImage(bufferedImage, newWidth, newHeight);
    }

    /**
     * 把图片按照指定的大小进行压缩
     * @param bufferedImage 原图BufferedImage
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @return
     */
    public static BufferedImage compress(BufferedImage bufferedImage, int newWidth, int newHeight) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double widthScale = newWidth * 1.0 / width;
        double heightScale = newHeight * 1.0 / height;
        if (newWidth < width || newHeight < height) {
            if (widthScale <= heightScale) {
                // 按照宽度比例来压缩
                newHeight = (int) (height * widthScale);
            } else {
                // 按照高度比例来压缩
                newWidth = (int) (width * heightScale);
            }
            return newBufferedImage(bufferedImage, newWidth, newHeight);
        }
        return bufferedImage;
    }

    /**
     * 以新的尺寸创建BufferedImage
     * @param bufferedImage
     * @param newWidth
     * @param newHeight
     * @return
     */
    private static BufferedImage newBufferedImage(BufferedImage bufferedImage, int newWidth, int newHeight) {
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = newImage.getGraphics();
        graphics.drawImage(bufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
        return newImage;
    }

    /**
     * 把图片按照指定的比例进行压缩，减小到指定的大小
     * @param path 原图路径
     * @param scale 压缩比例，1表示原图，0.5表示一半大小
     * @return
     */
    public static BufferedImage compress(String path, float scale) {
        BufferedImage bufferedImage = ImageUtils.getBufferedImage(path);
        if (bufferedImage != null) {
            return compress(bufferedImage, scale);
        }
        return null;
    }

    /**
     * 把图片按照指定的大小进行压缩
     * @param path 原图路径
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @return
     */
    public static BufferedImage compress(String path, int newWidth, int newHeight) {
        BufferedImage bufferedImage = ImageUtils.getBufferedImage(path);
        if (bufferedImage != null) {
            return compress(bufferedImage, newWidth, newHeight);
        }
        return null;
    }

    /**
     * 把图片按照指定的比例进行压缩，减小到指定的大小
     * @param inputStream 原图InputStream
     * @param scale 压缩比例，1表示原图，0.5表示一半大小
     * @return
     */
    public static BufferedImage compress(InputStream inputStream, float scale) {
        BufferedImage bufferedImage = ImageUtils.getBufferedImage(inputStream);
        if (bufferedImage != null) {
            return compress(bufferedImage, scale);
        }
        return null;
    }

    /**
     * 把图片按照指定的大小进行压缩
     * @param inputStream 原图InputStream
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @return
     */
    public static BufferedImage compress(InputStream inputStream, int newWidth, int newHeight) {
        BufferedImage bufferedImage = ImageUtils.getBufferedImage(inputStream);
        if (bufferedImage != null) {
            return compress(bufferedImage, newWidth, newHeight);
        }
        return null;
    }

    /**
     * 压缩gif图片到指定比例，并输出gif图片到指定的输出流
     * @param path gif图片路径
     * @param scale 压缩比例，1表示原图，0.5表示一半大小
     * @param outputStream 输出流，可以是FileOutputStream，也可以是response.getOutputStream
     */
    public static void compressGif(String path, float scale, OutputStream outputStream) {
        GifDecoder gifDecoder = getGifDecoder(path, null);
        if (gifDecoder != null) {
            writeGifToOutputStream(gifDecoder, scale, outputStream);
        }
    }

    /**
     * 压缩gif图片到指定大小，并输出gif图片到指定的输出流
     * @param path gif图片路径
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @param outputStream 输出流，可以是FileOutputStream，也可以是response.getOutputStream
     */
    public static void compressGif(String path, int newWidth, int newHeight, OutputStream outputStream) {
        GifDecoder gifDecoder = getGifDecoder(path, null);
        if (gifDecoder != null) {
            writeGifToOutputStream(gifDecoder, newWidth, newHeight, outputStream);
        }
    }

    /**
     * 压缩gif图片到指定比例，并输出gif图片到指定的输出流
     * @param inputStream gif图片输入流
     * @param scale 压缩比例，1表示原图，0.5表示一半大小
     * @param outputStream 输出流，可以是FileOutputStream，也可以是response.getOutputStream
     */
    public static void compressGif(InputStream inputStream, float scale, OutputStream outputStream) {
        GifDecoder gifDecoder = getGifDecoder(null, inputStream);
        if (gifDecoder != null) {
            writeGifToOutputStream(gifDecoder, scale, outputStream);
        }
    }

    /**
     * 压缩gif图片到指定大小，并输出gif图片到指定的输出流
     * @param inputStream gif图片输入流
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @param outputStream 输出流，可以是FileOutputStream，也可以是response.getOutputStream
     */
    public static void compressGif(InputStream inputStream, int newWidth, int newHeight, OutputStream outputStream) {
        GifDecoder gifDecoder = getGifDecoder(null, inputStream);
        if (gifDecoder != null) {
            writeGifToOutputStream(gifDecoder, newWidth, newHeight, outputStream);
        }
    }

    /**
     * 读取GifDecoder，参数二者选其一
     * @param path
     * @param inputStream
     * @return
     */
    private static GifDecoder getGifDecoder(String path, InputStream inputStream) {
        GifDecoder gifDecoder = new GifDecoder();
        int status = -1;
        if (StringUtils.isNotEmpty(path)) {
            status = gifDecoder.read(path);
        } else if (inputStream != null) {
            status = gifDecoder.read(inputStream);
        }
        if (status == GifDecoder.STATUS_OK) {
            return gifDecoder;
        } else {
            log.error("read gif error");
            return null;
        }
    }

    /**
     * 把gif图片写出到指定的输出流
     * @param gifDecoder
     * @param scale
     * @param outputStream
     */
    private static void writeGifToOutputStream(GifDecoder gifDecoder, float scale, OutputStream outputStream) {
        int frameCount = gifDecoder.getFrameCount();
        AnimatedGifEncoder animatedGifEncoder = new AnimatedGifEncoder();
        animatedGifEncoder.start(outputStream);
        animatedGifEncoder.setRepeat(gifDecoder.getLoopCount());
        for (int i = 0; i < frameCount; i++) {
            animatedGifEncoder.setDelay(gifDecoder.getDelay(i));
            BufferedImage frameImage = gifDecoder.getFrame(i);
            animatedGifEncoder.addFrame(compress(frameImage, scale));
        }
        animatedGifEncoder.finish();
    }

    /**
     * 把gif图片写出到指定的输出流
     * @param gifDecoder
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @param outputStream
     */
    private static void writeGifToOutputStream(GifDecoder gifDecoder, int newWidth, int newHeight, OutputStream outputStream) {
        int frameCount = gifDecoder.getFrameCount();
        AnimatedGifEncoder animatedGifEncoder = new AnimatedGifEncoder();
        animatedGifEncoder.start(outputStream);
        animatedGifEncoder.setRepeat(gifDecoder.getLoopCount());
        for (int i = 0; i < frameCount; i++) {
            animatedGifEncoder.setDelay(gifDecoder.getDelay(i));
            BufferedImage frameImage = gifDecoder.getFrame(i);
            animatedGifEncoder.addFrame(compress(frameImage, newWidth, newHeight));
        }
        animatedGifEncoder.finish();
    }

}
