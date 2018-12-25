package top.zywork.service;

import top.zywork.common.BeanUtils;
import top.zywork.common.ExceptionUtils;
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
        try {
            return baseDAO.save(BeanUtils.copy(dataTransferObj, doClass));
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public int saveBatch(List<Object> dataTransferObjList) {
        try {
            return baseDAO.saveBatch(BeanUtils.copyList(dataTransferObjList, doClass));
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public int removeById(Serializable id) {
        try {
            return baseDAO.removeById(id);
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public int removeByIds(Serializable[] ids) {
        try {
            return baseDAO.removeByIds(ids);
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public int update(Object dataTransferObj) {
        try {
            Object idObj = ReflectUtils.getPropertyValue(dataTransferObj, "id");
            Serializable id = idObj instanceof Long ? (Long) idObj : idObj instanceof Integer ? (Integer) idObj : idObj instanceof String ? (String) idObj : 0;
            Integer version = baseDAO.getVersionById(id);
            version = version == null ? 1 : version;
            ReflectUtils.setPropertyValue(dataTransferObj, "version", version + 1);
            return baseDAO.update(BeanUtils.copy(dataTransferObj, doClass));
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public int updateBatch(List<Object> dataTransferObjList) {
        try {
            return baseDAO.updateBatch(BeanUtils.copyList(dataTransferObjList, doClass));
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public Object getById(Serializable id) {
        try {
            Object doObject = baseDAO.getById(id);
            Object dtoObject = null;
            if (doObject != null) {
                dtoObject = BeanUtils.copy(doObject, dtoClass);
            }
            return dtoObject;
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public Integer getVersionById(Serializable id) {
        try {
            return baseDAO.getVersionById(id);
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public PagerDTO listById(Serializable id) {
        PagerDTO pagerDTO = new PagerDTO();
        try {
            List<Object> doObjList = baseDAO.listById(id);
            List<Object> dtoObjList = new ArrayList<>();
            if (doObjList != null && doObjList.size() > 0) {
                dtoObjList = BeanUtils.copyList(doObjList, dtoClass);
            }
            pagerDTO.setRows(dtoObjList);
            pagerDTO.setTotal((long) dtoObjList.size());
            return pagerDTO;
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public PagerDTO listAll() {
        PagerDTO pagerDTO = new PagerDTO();
        try {
            List<Object> doObjList = baseDAO.listAll();
            List<Object> dtoObjList = new ArrayList<>();
            if (doObjList != null && doObjList.size() > 0) {
                dtoObjList = BeanUtils.copyList(doObjList, dtoClass);
            }
            pagerDTO.setRows(dtoObjList);
            pagerDTO.setTotal((long) dtoObjList.size());
            return pagerDTO;
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
    }

    @Override
    public PagerDTO listPageByCondition(Object queryObj) {
        PagerDTO pagerDTO = new PagerDTO();
        try {
            Long count = baseDAO.countByCondition(queryObj);
            pagerDTO.setTotal(count);
            if (count > 0) {
                List<Object> doObjList = baseDAO.listPageByCondition(queryObj);
                pagerDTO.setRows(BeanUtils.copyList(doObjList, dtoClass));
            } else {
                pagerDTO.setRows(new ArrayList<>());
            }
            return pagerDTO;
        } catch (RuntimeException e) {
            throw ExceptionUtils.serviceException(e);
        }
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
