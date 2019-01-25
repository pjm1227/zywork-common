package top.zywork.service;

import top.zywork.common.BeanUtils;
import top.zywork.common.ReflectUtils;
import top.zywork.dao.BaseDAO;
import top.zywork.dto.PagerDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseService接口的抽象实现类，所有Service类中只需要再次实现一些额外定义的接口方法<br/>
 * 在Service实现类中需要把具体的DAO通过Resource或Autowired注解和setBaseDAO方法注入进来<br/>
 * 同时需要把DO和DTO类通过PostConstruct注解和init方法注入进来<br/>
 * 
 * 创建于2017-12-02
 *
 * @author 王振宇
 * @version 1.0
 */
public abstract class AbstractBaseService implements BaseService {

    private BaseDAO baseDAO;
    private Class<?> doClass;
    private Class<?> dtoClass;

    @Override
    public int save(Object dataTransferObj) {
        return baseDAO.save(BeanUtils.copy(dataTransferObj, doClass));
    }

    @Override
    public int saveBatch(List<Object> dataTransferObjList) {
        return baseDAO.saveBatch(BeanUtils.copyList(dataTransferObjList, doClass));
    }

    @Override
    public int removeById(Serializable id) {
        return baseDAO.removeById(id);
    }

    @Override
    public int removeByIds(Serializable[] ids) {
        return baseDAO.removeByIds(ids);
    }

    @Override
    public int update(Object dataTransferObj) {
        Object idObj = ReflectUtils.getPropertyValue(dataTransferObj, "id");
        Serializable id = idObj instanceof Long ? (Long) idObj : idObj instanceof Integer ? (Integer) idObj : idObj instanceof String ? (String) idObj : 0;
        Integer version = baseDAO.getVersionById(id);
        version = version == null ? 1 : version;
        ReflectUtils.setPropertyValue(dataTransferObj, "version", version + 1);
        return baseDAO.update(BeanUtils.copy(dataTransferObj, doClass));
    }

    @Override
    public int updateBatch(List<Object> dataTransferObjList) {
        return baseDAO.updateBatch(BeanUtils.copyList(dataTransferObjList, doClass));
    }

    @Override
    public Object getById(Serializable id) {
        Object doObject = baseDAO.getById(id);
        if (doObject != null) {
            return BeanUtils.copy(doObject, dtoClass);
        }
        return null;
    }

    @Override
    public Integer getVersionById(Serializable id) {
        return baseDAO.getVersionById(id);
    }

    @Override
    public PagerDTO listById(Serializable id) {
        PagerDTO pagerDTO = new PagerDTO();
        List<Object> doObjList = baseDAO.listById(id);
        List<Object> dtoObjList = new ArrayList<>();
        if (doObjList != null && doObjList.size() > 0) {
            dtoObjList = BeanUtils.copyList(doObjList, dtoClass);
        }
        pagerDTO.setRows(dtoObjList);
        pagerDTO.setTotal((long) dtoObjList.size());
        return pagerDTO;
    }

    @Override
    public PagerDTO listAll() {
        PagerDTO pagerDTO = new PagerDTO();
        List<Object> doObjList = baseDAO.listAll();
        List<Object> dtoObjList = new ArrayList<>();
        if (doObjList != null && doObjList.size() > 0) {
            dtoObjList = BeanUtils.copyList(doObjList, dtoClass);
        }
        pagerDTO.setRows(dtoObjList);
        pagerDTO.setTotal((long) dtoObjList.size());
        return pagerDTO;
    }

    @Override
    public PagerDTO listAllByCondition(Object queryObj) {
        PagerDTO pagerDTO = new PagerDTO();
        List<Object> dtoObjList = new ArrayList<>();
        List<Object> doObjList = baseDAO.listAllByCondition(queryObj);
        if (doObjList != null && doObjList.size() > 0) {
            dtoObjList = BeanUtils.copyList(doObjList, dtoClass);
        }
        pagerDTO.setRows(dtoObjList);
        pagerDTO.setTotal((long) dtoObjList.size());
        return pagerDTO;
    }

    @Override
    public PagerDTO listPageByCondition(Object queryObj) {
        PagerDTO pagerDTO = new PagerDTO();
        Long count = baseDAO.countByCondition(queryObj);
        pagerDTO.setTotal(count);
        if (count > 0) {
            List<Object> doObjList = baseDAO.listPageByCondition(queryObj);
            pagerDTO.setRows(BeanUtils.copyList(doObjList, dtoClass));
        } else {
            pagerDTO.setRows(new ArrayList<>());
        }
        return pagerDTO;
    }

    @Override
    public Long countByCondition(Object queryObj) {
        return baseDAO.countByCondition(queryObj);
    }

    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    public Class<?> getDoClass() {
        return doClass;
    }

    public Class<?> getDtoClass() {
        return dtoClass;
    }

    public void init(Class<?> doClass, Class<?> dtoClass) {
        this.doClass = doClass;
        this.dtoClass = dtoClass;
    }
}
