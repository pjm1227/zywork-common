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

    private static final Integer WIDTH = 100;
    private static final Integer HEIGHT = 40;
    private static final Integer CODE_LENGTH = 5;
    private static final Integer LINE_COUNT = 6;

    /**
     * 生成随机验证码
     * @return
     */
    public static String generateCode() {
        return RandomUtils.randomCode(RandomCodeEnum.MIX_CODE, CODE_LENGTH);
    }

    /**
     * 生成随机验证码图片
     * @param verifyCode
     * @return
     */
    public static BufferedImage generateImage(String verifyCode) {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(ColorUtils.backColor());
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        graphics.setColor(new Color(0, 0, 0));
        graphics.setFont(new Font("黑体", Font.BOLD + Font.ITALIC, 20));
        graphics.drawString(verifyCode, 20, HEIGHT / 2 + 10);
        for (int i = 0; i < LINE_COUNT; i++) {
            graphics.setColor(ColorUtils.backColor());
            graphics.drawLine(RandomUtils.randomNum(0, WIDTH), RandomUtils.randomNum(0, HEIGHT), RandomUtils.randomNum(0, WIDTH), RandomUtils.randomNum(0, WIDTH));
        }
        return bufferedImage;
    }

}
