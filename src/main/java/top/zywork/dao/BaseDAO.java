package top.zywork.dao;


import top.zywork.query.PageQuery;

import java.io.Serializable;
import java.util.List;

/**
 * DAO接口，定义了常用的DAO方法，实现CRUD操作<br/>
 * 由Service调用DAO时，需要传递DO对象进来，并返回DO对象到Service<br/>
 * 创建于2017-08-23<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public interface BaseDAO {

    /**
     * 添加数据到数据库中
     * @param dataObj DO实体类
     */
    void save(Object dataObj);

    /**
     * 批量添加数据到数据库中
     * @param dataObjList DO实体类集合
     */
    void saveBatch(List<Object> dataObjList);

    /**
     * 根据主键从数据库中删除数据
     * @param id 主键ID
     */
    void removeById(Serializable id);

    /**
     * 根据多个主键批量删除数据
     * @param ids 多个主键组成的数组
     */
    void removeByIds(Serializable[] ids);

    /**
     * 根据对象更新数据库中的数据
     * @param dataObj DO实体类
     */
    void update(Object dataObj);

    /**
     * 根据对象集合批量更新数据库中的数据
     * @param dataObjList DO实体类集合
     */
    void updateBatch(List<Object> dataObjList);

    /**
     * 根据主键id查找数据
     * @param id 主键字段值
     * @return 主键字段对应记录的DO对象
     */
    Object getById(Serializable id);

    /**
     * 查找所有记录数据
     * @return 所有记录数据DO对象组成的List列表
     */
    List<Object> listAll();

    /**
     * 根据分页查询对象和条件查询对象查找数据
     * @param queryObj 条件查询对象，包括分页数据
     * @return 按照条件的分页数据DO对象所组成的List列表
     */
    List<Object> listPageByCondition(Object queryObj);

    /**
     * 根据条件查询对象计数
     * @param queryObj 条件查询对象
     * @return 按照条件查询对象的记录数
     */
    Long countByCondition(Object queryObj);

}
