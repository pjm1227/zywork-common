package top.zywork.common;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AWT字符串工具类<br/>
 * 创建于2018-12-29<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class AwtStringUtils {

    /**
     * 根据字符串内容，字符串每行限制的最大宽度和字体相关信息，获取换行后的多个字符串
     * @param content
     * @param maxWidth
     * @param fontMetrics
     * @return
     */
    public static List<String> multiRows(String content, int maxWidth, FontMetrics fontMetrics) {
        List<String> multiRows = new ArrayList<>();
        int contentWidth = fontMetrics.stringWidth(content);
        if (contentWidth <= maxWidth) {
            multiRows.add(content);
        } else {
            char[] charArray = content.toCharArray();
            int lineWidth = 0;
            int lineStart = 0;
            int count = 0;
            for (char aCharArray : charArray) {
                int charWidth = fontMetrics.charWidth(aCharArray);
                lineWidth += charWidth;
                count++;
                if (lineWidth >= maxWidth) {
                    multiRows.add(String.valueOf(charArray, lineStart, count));
                    lineWidth = 0;
                    lineStart += count;
                    count = 0;
                }
                if (lineStart + count >= charArray.length) {
                    multiRows.add(String.valueOf(charArray, lineStart, count));
                }
            }
        }
        return multiRows;
    }

    /**
     * 根据行数及字体信息获取所有字符串内容的高度
     * @param rows
     * @param fontMetrics
     * @return
     */
    public static int getStringLinesHeight(int rows, FontMetrics fontMetrics) {
        return rows * fontMetrics.getHeight();
    }

}
