package top.zywork.service;


import top.zywork.dto.PagerDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Service接口，定义了常用的Service方法，实现业务逻辑操作并调用DAO<br />
 * 由Controller调用Service时，需要传递DTO对象进行，并返回DTO对象到Controller<br />
 * 创建于2017-08-24<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public interface BaseService {

    /**
     * 添加数据到数据库中
     * @param dataTransferObj DTO数据传输对象
     * @return 返回插入的行数
     */
    int save(Object dataTransferObj);

    /**
     * 批量添加数据到数据库中
     * @param dataTransferObjList DTO数据传输对象集合
     * @return 返回插入的行数
     */
    int saveBatch(List<Object> dataTransferObjList);

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
     * 根据对象更新数据库中的数据<br/>
     * 为了保证在高并发状态下的数据正确性，使用了version版本号乐观锁机制来控制数据的更新<br/>
     * @param dataTransferObj DTO数据传输对象
     * @return 返回更新的行数。如果数据与原先的记录数据一致，则不更新，返回0；如果版本号小于等于数据库中的版本号，也不更新，返回0
     */
    int update(Object dataTransferObj);

    /**
     * 根据对象集合批量更新数据库中的数据
     * @param dataTransferObjList DTO数据传输对象集合
     * @return 返回更新的行数
     */
    int updateBatch(List<Object> dataTransferObjList);

    /**
     * 根据主键id查找数据
     * @param id 主键字段值
     * @return DTO数据传输对象
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
     * @return 关联数据的记录所组成的PagerDTO
     */
    PagerDTO listById(Serializable id);

    /**
     * 查找所有记录数据
     * @return DTO数据传输对象组成的PagerDTO
     */
    PagerDTO listAll();

    /**
     * 根据条件查询对象查找符合条件的所有数据
     * @param queryObj 条件查询对象
     * @return DTO数据传输对象组成的PagerDTO
     */
    PagerDTO listAllByCondition(Object queryObj);
    
    /**
     * 根据分页查询对象和条件查询对象查找数据
     * @param queryObj 条件查询对象，包括分页数据
     * @return 按照条件的分页数据DTO对象所组成的PagerDTO
     */
    PagerDTO listPageByCondition(Object queryObj);

    /**
     * 根据条件查询对象计数
     * @param queryObj 条件查询对象
     * @return 按照条件查询对象的记录数
     */
    Long countByCondition(Object queryObj);

}
