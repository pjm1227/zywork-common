package top.zywork.common;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaBean属性工具类<br/>
 * 创建于2017-12-18日<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class PropertyUtils {

    /**
     * 获取属性的getter方法名称
     * @param property 属性名
     * @return getter方法名
     */
    public static String getter(String property) {
        return "get" + StringUtils.capitalize(property);
    }

    /**
     * 获取属性的setter方法名称
     * @param property 属性名
     * @return setter方法名
     */
    public static String setter(String property) {
        return "set" + StringUtils.capitalize(property);
    }

    /**
     * 获取表字段对应的属性名
     * @param column 表字段名
     * @return 表字段对应的对象属性名
     */
    public static String columnToProperty(String column) {
        StringBuilder property = new StringBuilder("");
        if (column.contains("_")) {
            String[] strArray = column.split("_");
            property.append(strArray[0]);
            for (int i = 1, len = strArray.length; i < len; i++) {
                property.append(StringUtils.capitalize(strArray[i]));
            }
        } else {
            property.append(column);
        }
        return property.toString();
    }

    /**
     * 属性名转化成表字段名，如userId变成user_id
     * @param property 属性名
     * @return 属性名对应的表字段名
     */
    public static String propertyToColumn(String property) {
        List<Character> characterList = top.zywork.common.StringUtils.stringToCharList(property);
        List<Integer> upperCaseIndexes = new ArrayList<>();
        for (int i = 0, len =characterList.size(); i < len; i++) {
            if (characterList.get(i) >= 65 && characterList.get(i) <= 90) {
                upperCaseIndexes.add(i);
            }
        }
        int underLineCount = 0;
        for (Integer upperCaseIndex : upperCaseIndexes) {
            int underLineIndex = upperCaseIndex + underLineCount;
            characterList.add(underLineIndex, '_');
            characterList.set(underLineIndex + 1, (char) (characterList.get(underLineIndex + 1) + 32));
            underLineCount++;
        }
        return top.zywork.common.StringUtils.charListToString(characterList);
    }

    /**
     * 产生serialVersionId
     * @return serialVersionId
     */
    public static Long generateSerialVersionId() {
        return Long.MIN_VALUE + RandomUtils.randomNum(0, Integer.MAX_VALUE);
    }

}
