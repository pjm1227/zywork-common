package top.zywork.common;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.enums.CharsetEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 二维码工具类<br/>
 *
 * 创建于2018-12-29<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class QrCodeUtils {

    private static final Logger logger = LoggerFactory.getLogger(QrCodeUtils.class);

    private static final int QR_COLOR = 0x000000;

    private static final int BG_WHITE = 0xFFFFFF;

    private static final double LOGO_SCALE = 0.18;

    private static final int LOGO_WHITE_MARGIN = 2;

    private static final int REMARK_PADDING = 20;

    private static Map<EncodeHintType, Object> encodeHintsMap;

    private static Map<DecodeHintType, Object> decodeHintsMap;

    static {
        encodeHintsMap = new HashMap<>();
        encodeHintsMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        encodeHintsMap.put(EncodeHintType.CHARACTER_SET, CharsetEnum.UTF8.getValue());
        encodeHintsMap.put(EncodeHintType.MARGIN, 1);

        decodeHintsMap = new HashMap<>();
        decodeHintsMap.put(DecodeHintType.CHARACTER_SET, CharsetEnum.UTF8.getValue());
    }

    /**
     * 生成二维码BufferedImage
     * @param text 二维码字符串内容
     * @param width 二维码图片宽度
     * @param height 二维码图片高度
     * @return
     */
    public static BufferedImage generateQrCode(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, encodeHintsMap);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            logger.error("qr code encode error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成二维码BufferedImage
     * @param text 二维码字符串内容
     * @param width 二维码图片宽度
     * @param height 二维码图片高度
     * @param remark 二维码图片描述
     * @param remarkColor 二维码图片描述颜色
     * @param remarkFont 二维码图片描述字体
     * @return
     */
    public static BufferedImage generateQrCode(String text, int width, int height, String remark, Color remarkColor, Font remarkFont) {
        return generateQrCode(text, width, height, remark, remarkColor, remarkFont);
    }

    /**
     * 生成带logo图片的二维码BufferedImage
     * @param text 二维码字符串内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param logo logo图片文件
     * @return
     */
    public static BufferedImage generateQrCode(String text, int width, int height, File logo) {
        try {
            return generateQrCode(text, width, height, ImageIO.read(logo), null, null, null);
        } catch (IOException e) {
            logger.error("read image file error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成带logo图片的二维码BufferedImage
     * @param text 二维码字符串内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param logo logo图片文件
     * @param remark 二维码图片描述
     * @param remarkColor 二维码图片描述颜色
     * @param remarkFont 二维码图片描述字体
     * @return
     */
    public static BufferedImage generateQrCode(String text, int width, int height, File logo, String remark, Color remarkColor, Font remarkFont) {
        try {
            return generateQrCode(text, width, height, ImageIO.read(logo), remark, remarkColor, remarkFont);
        } catch (IOException e) {
            logger.error("read image file error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成带logo图片的二维码BufferedImage
     * @param text 二维码字符串内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param logoUrl logo图片url
     * @return
     */
    public static BufferedImage generateQrCode(String text, int width, int height, String logoUrl) {
        try {
            return generateQrCode(text, width, height, ImageIO.read(new URL(logoUrl)), null, null, null);
        } catch (IOException e) {
            logger.error("read image from url error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成带logo图片的二维码BufferedImage
     * @param text 二维码字符串内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param remark 二维码图片描述
     * @param remarkColor 二维码图片描述颜色
     * @param remarkFont 二维码图片描述字体
     * @param logoUrl logo图片url
     * @return
     */
    public static BufferedImage generateQrCode(String text, int width, int height, String remark, Color remarkColor, Font remarkFont, String logoUrl) {
        try {
            return generateQrCode(text, width, height, ImageIO.read(new URL(logoUrl)), remark, remarkColor, remarkFont);
        } catch (IOException e) {
            logger.error("read image from url error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成带logo图片的二维码BufferedImage
     * @param text 二维码字符串内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param logoImage logo图片BufferedImage
     * @param remark 二维码图片描述
     * @param remarkColor 二维码图片描述颜色
     * @param remarkFont 二维码图片描述字体
     * @return
     */
    public static BufferedImage generateQrCode(String text, int width, int height, BufferedImage logoImage, String remark, Color remarkColor, Font remarkFont) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, encodeHintsMap);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            List<String> multiRows = null;
            int strLineHeight = 0;
            int stringHeight = 0;
            if (StringUtils.isNotEmpty(remark)) {
                FontMetrics fontMetrics = graphics.getFontMetrics(remarkFont);
                strLineHeight = fontMetrics.getHeight();
                multiRows = AwtStringUtils.multiRows(remark, width - REMARK_PADDING * 2, fontMetrics);
                stringHeight = multiRows.size() * strLineHeight;
                // 重新生成缓冲图及获取Graphics对象
                bufferedImage = new BufferedImage(width, height + stringHeight + REMARK_PADDING, BufferedImage.TYPE_INT_RGB);
                graphics = bufferedImage.getGraphics();
            }
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bufferedImage.setRGB(i, j, bitMatrix.get(i, j) ? QR_COLOR : BG_WHITE);
                }
            }
            if (multiRows != null) {
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, height, width, stringHeight + REMARK_PADDING);
                graphics.setColor(remarkColor);
                graphics.setFont(remarkFont);
                for (int i = 0, size = multiRows.size(); i < size; i++) {
                    graphics.drawString(multiRows.get(i), REMARK_PADDING, height + REMARK_PADDING  + i * strLineHeight);
                }
            }
            if (logoImage != null) {
                int logoWidth = (int) (width * LOGO_SCALE);
                int logoHeight = (int) (height * LOGO_SCALE);
                int logoX = width / 2 - logoWidth / 2;
                int logoY = height / 2 - logoHeight / 2;
                graphics.setColor(Color.WHITE);
                graphics.fillRect(logoX - LOGO_WHITE_MARGIN, logoY - LOGO_WHITE_MARGIN, logoWidth + LOGO_WHITE_MARGIN * 2, logoHeight + LOGO_WHITE_MARGIN * 2);
                graphics.drawImage(logoImage, logoX , logoY, logoWidth, logoHeight, null);
            }
            return bufferedImage;
        } catch (WriterException e) {
            logger.error("qr code encode error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成并保存二维码图片到指定文件
     * @param text 二维码字符串内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    public static void saveQrCode(String text, int width, int height, String filePath, String fileName) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, encodeHintsMap);
            MatrixToImageWriter.writeToPath(bitMatrix, FileUtils.getExt(fileName), FileSystems.getDefault().getPath(filePath, fileName));
        } catch (WriterException | IOException e) {
            logger.error("qr code encode error: {}", e.getMessage());
        }
    }

    /**
     * 生成并保存二维码图片到指定文件
     * @param text 二维码字符串内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param remark 二维码图片描述
     * @param remarkColor 二维码图片描述颜色
     * @param remarkFont 二维码图片描述字体
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    public static void saveQrCode(String text, int width, int height, String remark, Color remarkColor, Font remarkFont, String filePath, String fileName) {
        try {
            BufferedImage bufferedImage = generateQrCode(text, width, height, remark, remarkColor, remarkFont, null);
            if (bufferedImage != null) {
                ImageIO.write(bufferedImage, FileUtils.getExt(fileName), new File(filePath, fileName));
            }
        } catch (IOException e) {
            logger.error("qr code encode error: {}", e.getMessage());
        }
    }

    /**
     * 生成并保存带logo图片二维码图片到指定文件
     * @param text 二维码字符串内容
     * @param width 二维码宽度
     * @param logo logo图片文件
     * @param height 二维码高度
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    public static void saveQrCode(String text, int width, int height, File logo, String filePath, String fileName) {
        try {
            BufferedImage bufferedImage = generateQrCode(text, width, height, logo);
            if (bufferedImage != null) {
                ImageIO.write(bufferedImage, FileUtils.getExt(fileName), new File(filePath, fileName));
            }
        } catch (IOException e) {
            logger.error("qr code encode error: {}", e.getMessage());
        }
    }

    /**
     * 生成并保存带logo图片二维码图片到指定文件
     * @param text 二维码字符串内容
     * @param width 二维码宽度
     * @param logo logo图片文件
     * @param height 二维码高度
     * @param remark 二维码图片描述
     * @param remarkColor 二维码图片描述颜色
     * @param remarkFont 二维码图片描述字体
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    public static void saveQrCode(String text, int width, int height, File logo, String remark, Color remarkColor, Font remarkFont, String filePath, String fileName) {
        try {
            BufferedImage bufferedImage = generateQrCode(text, width, height, logo, remark, remarkColor, remarkFont);
            if (bufferedImage != null) {
                ImageIO.write(bufferedImage, FileUtils.getExt(fileName), new File(filePath, fileName));
            }
        } catch (IOException e) {
            logger.error("qr code encode error: {}", e.getMessage());
        }
    }

    /**
     * 解析二维码
     * @param image 二维码BufferedImage
     * @return
     */
    public static String decodeQrCode(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new MultiFormatReader().decode(bitmap, decodeHintsMap);
            return result != null ? result.getText() : null;
        } catch (NotFoundException e) {
            logger.error("decode qr code error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 解析指定路径的二维码
     * @param imgPath 二维码图片路径
     * @return
     */
    public static String decodeQrCode(String imgPath) {
        try {
            return decodeQrCode(ImageIO.read(new File(imgPath)));
        } catch (Exception e) {
            logger.error("read image from path error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 解析指定url的二维码图片
     * @param imgUrl 二维码图片url
     * @return
     */
    public static String decodeQrCodeFromUrl(String imgUrl) {
        try {
            return decodeQrCode(ImageIO.read(new URL(imgUrl)));
        } catch (Exception e) {
            logger.error("read image from url error: {}", e.getMessage());
        }
        return null;
    }

}
