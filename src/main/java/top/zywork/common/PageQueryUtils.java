package top.zywork.common;

import org.apache.commons.lang3.StringUtils;
import top.zywork.query.PageQuery;

/**
 * 获取分页查询对象PageQuery的工具类
 * 创建于2017-08-16
 *
 * @author 王振宇
 * @version 1.0
 */
public class PageQueryUtils {

    /**
     * 默认每页显示的项数
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 通过pageNo和pageSize参数获取PageQuery对象，如果pageNo<=0，则pageNo设置为1，
     * 如果pageSize<=0，则pageSize设置为配置文件中配置的默认页大小
     * @param pageNo 页数
     * @param pageSize 每页显示个数
     * @return PageQuery对象
     */
    public static PageQuery getPageQuery(int pageNo, int pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(pageNo);
        pageQuery.setPageSize(pageSize);
        return pageQuery;
    }

    /**
     * 通过pageNo参数获取PageQuery对象，每页显示的个数为配置文件中配置的默认页大小
     * @param pageNo 页数
     * @return PageQuery对象
     */
    public static PageQuery getPageQuery(int pageNo) {
        return getPageQuery(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * 通过字符串类型的pageNO和pageSize参数获取PageQuery对象
     * @param pageNo 页数
     * @param pageSize 每页显示个数
     * @return PageQuery对象
     */
    public static PageQuery getPageQuery(String pageNo, String pageSize) {
        if (StringUtils.isNotEmpty(pageNo) && StringUtils.isNotEmpty(pageSize)) {
            return getPageQuery(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        } else if (StringUtils.isNotEmpty(pageNo)) {
            return getPageQuery(Integer.valueOf(pageNo));
        } else if (StringUtils.isNotEmpty(pageSize)) {
            return getPageQuery(1, Integer.valueOf(pageSize));
        } else {
            return getPageQuery(1);
        }
    }

}
