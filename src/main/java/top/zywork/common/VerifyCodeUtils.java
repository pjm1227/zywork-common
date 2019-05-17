package top.zywork.common;

import top.zywork.enums.RandomCodeEnum;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 验证码工具类，生成验证码和验证码图片<br/>
 *
 * 创建于2017-12-10<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class VerifyCodeUtils {

    private static Integer width = 100;
    private static Integer height = 40;
    private static Integer codeLength = 5;
    private static Integer lineCount = 6;

    /**
     * 初始化验证码图片生成选项，如果指定值给的是null，则表示使用默认值。如果不调用此方法，所有选项都使用默认值
     * @param width 默认值100
     * @param height 默认值40
     * @param codeLength 默认值5
     * @param lineCount 默认值6
     */
    public static void initOptions(Integer width, Integer height, Integer codeLength, Integer lineCount) {
        if (width != null) {
            VerifyCodeUtils.width = width;
        }
        if (height != null) {
            VerifyCodeUtils.height = height;
        }
        if (codeLength != null) {
            VerifyCodeUtils.codeLength = codeLength;
        }
        if (lineCount != null) {
            VerifyCodeUtils.lineCount = lineCount;
        }
    }

    /**
     * 生成随机验证码
     * @return
     */
    public static String generateCode() {
        return RandomUtils.randomCode(RandomCodeEnum.MIX_CODE, codeLength);
    }

    /**
     * 生成随机验证码图片
     * @param verifyCode
     * @return
     */
    public static BufferedImage generateImage(String verifyCode) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(ColorUtils.backColor());
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(new Color(0, 0, 0));
        graphics.setFont(new Font("黑体", Font.BOLD + Font.ITALIC, 20));
        graphics.drawString(verifyCode, 20, height / 2 + 10);
        for (int i = 0; i < lineCount; i++) {
            graphics.setColor(ColorUtils.backColor());
            graphics.drawLine(RandomUtils.randomNum(0, width), RandomUtils.randomNum(0, height), RandomUtils.randomNum(0, width), RandomUtils.randomNum(0, width));
        }
        return bufferedImage;
    }

}
