package top.zywork.common;

import java.awt.*;

/**
 * 颜色工具类<br/>
 *
 * 创建于2017-12-10<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class ColorUtils {

    public static final Color[] BACK_COLORS = {
            new Color(0, 102, 153),
            new Color(0, 153, 153),
            new Color(0, 204, 153),
            new Color(0, 255, 153),
            new Color(51, 0, 153),
            new Color(51, 51, 153),
            new Color(51, 102, 153),
            new Color(51, 153, 153),
            new Color(51, 204, 153),
            new Color(51, 255, 153),
            new Color(102, 0, 153),
            new Color(102, 51, 153),
            new Color(102, 102, 153),
            new Color(102, 153, 153),
            new Color(102, 204, 153),
            new Color(102, 255, 153),
            new Color(153, 0, 153),
            new Color(153, 51, 153),
            new Color(153, 102, 153),
            new Color(153, 153, 153),
            new Color(153, 204, 153),
            new Color(153, 255, 153),
            new Color(204, 0, 153),
            new Color(204, 51, 153),
            new Color(204, 102, 153),
            new Color(204, 153, 153),
            new Color(204, 204, 153),
            new Color(204, 255, 153),
            new Color(255, 0, 153),
            new Color(255, 51, 153),
            new Color(255, 102, 153),
            new Color(255, 153, 153),
            new Color(255, 204, 153),
            new Color(255, 255, 153),
    };

    /**
     * 生成随机颜色
     * @return
     */
    public static Color randomColor() {
        return new Color(RandomUtils.randomNum(0, 256), RandomUtils.randomNum(0, 256), RandomUtils.randomNum(0, 256));
    }

    /**
     * 随机获取BACK_COLORS中定义的背景色
     * @return
     */
    public static Color backColor() {
        return BACK_COLORS[RandomUtils.randomNum(0, BACK_COLORS.length)];
    }

}
