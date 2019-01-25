package top.zywork.dao;

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
     * @return 返回插入的行数
     */
    int save(Object dataObj);

    /**
     * 批量添加数据到数据库中
     * @param dataObjList DO实体类集合
     * @return 返回插入的行数
     */
    int saveBatch(List<Object> dataObjList);

    /**
     * 根据主键从数据库中删除数据
     * @param id 主键ID
     * @return 返回删除的行数
     */
    int removeById(Serializable id);

    /**
     * 根据多个主键批量删除数据
     * @param ids 多个主键组成的数组
     * @return 返回删除的行数
     */
    int removeByIds(Serializable[] ids);

    /**
     * 根据对象更新数据库中的数据
     * @param dataObj DO实体类
     * @return 返回更新的行数
     */
    int update(Object dataObj);

    /**
     * 根据对象集合批量更新数据库中的数据
     * @param dataObjList DO实体类集合
     * @return 返回更新的行数
     */
    int updateBatch(List<Object> dataObjList);

    /**
     * 根据主键id查找数据，针对单表查询
     * @param id 主键字段值
     * @return 主键字段对应记录的DO对象
     */
    Object getById(Serializable id);

    /**
     * 根据主键获取记录版本号
     * @param id 主键字段值
     * @return 记录版本号
     */
    Integer getVersionById(Serializable id);

    /**
     * 根据主体表id查找数据，针对关联表查询
     * @param id 主体表主键字段值
     * @return 关联数据的记录对应的DO集合
     */
    List<Object> listById(Serializable id);

    /**
     * 查找所有记录数据
     * @return 所有记录数据DO对象组成的List列表
     */
    List<Object> listAll();

    /**
     * 根据条件查询对象查找符合条件的所有数据
     * @param queryObj 条件查询对象
     * @return 按照条件的所有数据DO对象组成的List列表
     */
    List<Object> listAllByCondition(Object queryObj);

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
