package top.zywork.common;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.constant.BPMNConstants;
import top.zywork.vo.PagerVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * Activiti工具类<br/>
 * 创建于2017-10-17<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class ActivitiUtils {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiUtils.class);

    /**
     * 部署流程
     * @param repositoryService
     * @param processPath
     * @param processName
     * @param processKey
     * @return
     */
    public static Deployment deployByPath(RepositoryService repositoryService, String processPath, String processName, String processKey) {
        InputStream inputStream = null;
        ZipInputStream zipInputStream = null;
        try {
            inputStream = new FileInputStream(processPath);
            zipInputStream = new ZipInputStream(inputStream);
            return repositoryService.createDeployment().addZipInputStream(zipInputStream).name(processName).key(processKey).deploy();
        } catch (IOException e) {
            logger.error("deploy process {} error: {}", processName, e.getMessage());
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (zipInputStream != null) {
                    zipInputStream.close();
                }
            } catch (IOException e) {
                logger.error("input stream close error: {}", e.getMessage());
            }
        }
    }

    /**
     * 部署流程
     * @param repositoryService
     * @param processDir 流程文件目录
     * @param processName
     * @param processKey
     * @return
     */
    public static Deployment deployByName(RepositoryService repositoryService, String processDir, String processName, String processKey) {
        return deployByPath(repositoryService, processDir + File.separator+ processName + BPMNConstants.SUFFIX_ZIP, processName, processKey);
    }

    /**
     * 列出所有流程部署，按照部署时间降序排列
     *
     * @param repositoryService RepositoryService实例
     * @param offset 开始索引
     * @param limit 个数
     * @return 所有流程部署
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listDeployments(RepositoryService repositoryService, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        long total = deploymentQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<Deployment>
            pagerVO.setRows((List) deploymentQuery.orderByDeploymenTime().desc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 列出指定流程的流程部署，按照部署时间倒序排列
     *
     * @param repositoryService RepositoryService实例
     * @param processName       流程名
     * @param offset 开始索引
     * @param limit 个数
     * @return 指定流程的流程部署对象列表
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listDeployments(RepositoryService repositoryService, String processName, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery().deploymentNameLike(processName);
        long total = deploymentQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<Deployment>
            pagerVO.setRows((List) deploymentQuery.orderByDeploymenTime().desc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 获取指定流程名的最新版本的流程部署
     *
     * @param repositoryService RepositoryService实例
     * @param processKey       流程名
     * @return 指定流程名的最新版本的流程部署对象
     */
    public static Deployment getLatestDeployment(RepositoryService repositoryService, String processKey) {
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().deploymentKey(processKey).latest().list();
        return (deploymentList != null && deploymentList.size() > 0) ? deploymentList.get(0) : null;
    }

    /**
     * 根据流程名删除所有流程部署
     *
     * @param repositoryService RepositoryService实例
     * @param processKey       流程名
     */
    public static int removeAllDeployment(RepositoryService repositoryService, String processKey) {
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().processDefinitionKeyLike(processKey).list();
        int count = deploymentList.size();
        for (Deployment deployment : deploymentList) {
            repositoryService.deleteDeployment(deployment.getId(), true);
        }
        return count;
    }

    /**
     * 根据流程名删除除最新部署之外的流程部署
     *
     * @param repositoryService RepositoryService实例
     * @param processKey       流程名
     */
    public static int removeOldDeployment(RepositoryService repositoryService, String processKey) {
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().processDefinitionKeyLike(processKey).orderByDeploymentId().desc().list();
        int count = deploymentList.size();
        if (deploymentList.size() > 0) {
            deploymentList.remove(0);
            for (Deployment deployment : deploymentList) {
                repositoryService.deleteDeployment(deployment.getId(), true);
            }
        }
        return count == 0 ? 0 : count - 1;
    }

    /**
     * 列出所有流程定义，按照版本降序排列
     *
     * @param repositoryService RepositoryService实例
     * @param offset 开始索引
     * @param limit 个数
     * @return 所有流程定义对象列表
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listProcessDefinitions(RepositoryService repositoryService, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        long total = processDefinitionQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<ProcessDefinition>
            pagerVO.setRows((List) processDefinitionQuery.orderByProcessDefinitionVersion().desc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 列出指定流程的流程定义，按照版本降序排列
     *
     * @param repositoryService RepositoryService实例
     * @param processName       流程名
     * @param offset 开始索引
     * @param limit 个数
     * @return 指定流程的流程定义对象列表
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listProcessDefinitions(RepositoryService repositoryService, String processName, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().processDefinitionNameLike(processName);
        long total = processDefinitionQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<ProcessDefinition>
            pagerVO.setRows((List) processDefinitionQuery.orderByProcessDefinitionVersion().desc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 获取指定流程名的最新版本的流程定义
     *
     * @param repositoryService RepositoryService实例
     * @param processKey       流程名
     * @return 指定流程名的最新版本的流程定义对象
     */
    public static ProcessDefinition getLatestProcessDefinition(RepositoryService repositoryService, String processKey) {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().list();
        return (processDefinitionList != null && processDefinitionList.size() > 0) ? processDefinitionList.get(0) : null;
    }

    /**
     * 启动流程，并且只能启动一个，如果已经有启动的流程，则不能再次启动流程
     *
     * @param runtimeService RuntimeService实例
     * @param processKey     流程key
     * @return 流程实例
     */
    public static ProcessInstance startOneProcess(RuntimeService runtimeService, String processKey) {
        long totalProcessInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey).count();
        return totalProcessInstance > 0 ? null : runtimeService.startProcessInstanceByKey(processKey);
    }

    /**
     * 启动流程，并且只能启动一个，如果已经有启动的流程，则不能再次启动流程
     *
     * @param runtimeService RuntimeService实例
     * @param processKey     流程key
     * @param variables      流程变量
     * @return 流程实例
     */
    public static ProcessInstance startOneProcess(RuntimeService runtimeService, String processKey, Map<String, Object> variables) {
        long totalProcessInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey).count();
        return totalProcessInstance > 0 ? null : runtimeService.startProcessInstanceByKey(processKey, variables);
    }

    /**
     * 用户启动流程，并且只能启动一个，如果已经有启动的流程，则不能再次启动流程
     *
     * @param identityService IdentityService实例
     * @param runtimeService  RuntimeService实例
     * @param userIdentity    启动流程的用户标识
     * @param processKey      流程key
     * @return 流程实例
     */
    public static ProcessInstance startOneProcess(IdentityService identityService, RuntimeService runtimeService, String userIdentity, String processKey) {
        long totalProcessInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey).startedBy(userIdentity).count();
        if (totalProcessInstance > 0) {
            return null;
        }
        identityService.setAuthenticatedUserId(userIdentity);
        return runtimeService.startProcessInstanceByKey(processKey);
    }

    /**
     * 用户启动流程，可附加流程变量，并且只能启动一个，如果已经有启动的流程，则不能再次启动流程
     *
     * @param identityService IdentityService实例
     * @param runtimeService  RuntimeService实例
     * @param userIdentity    启动流程的用户标识
     * @param processKey      流程key
     * @param variables       流程变量
     * @return 流程实例
     */
    public static ProcessInstance startOneProcess(IdentityService identityService, RuntimeService runtimeService, String userIdentity, String processKey, Map<String, Object> variables) {
        long totalProcessInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey).startedBy(userIdentity).count();
        if (totalProcessInstance > 0) {
            return null;
        }
        identityService.setAuthenticatedUserId(userIdentity);
        return runtimeService.startProcessInstanceByKey(processKey, variables);
    }

    /**
     * 启动流程
     *
     * @param runtimeService RuntimeService实例
     * @param processKey     流程key
     * @return 流程实例
     */
    public static ProcessInstance startProcess(RuntimeService runtimeService, String processKey) {
        return runtimeService.startProcessInstanceByKey(processKey);
    }

    /**
     * 启动流程
     *
     * @param runtimeService RuntimeService实例
     * @param processKey     流程key
     * @param variables      流程变量
     * @return 流程实例
     */
    public static ProcessInstance startProcess(RuntimeService runtimeService, String processKey, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(processKey, variables);
    }

    /**
     * 用户启动流程
     *
     * @param identityService IdentityService实例
     * @param runtimeService  RuntimeService实例
     * @param userIdentity    启动流程的用户标识
     * @param processKey      流程key
     * @return 流程实例
     */
    public static ProcessInstance startProcess(IdentityService identityService, RuntimeService runtimeService, String userIdentity, String processKey) {
        identityService.setAuthenticatedUserId(userIdentity);
        return runtimeService.startProcessInstanceByKey(processKey);
    }

    /**
     * 用户启动流程，可附加流程变量
     *
     * @param identityService IdentityService实例
     * @param runtimeService  RuntimeService实例
     * @param userIdentity    启动流程的用户标识
     * @param processKey      流程key
     * @param variables       流程变量
     * @return 流程实例
     */
    public static ProcessInstance startProcess(IdentityService identityService, RuntimeService runtimeService, String userIdentity, String processKey, Map<String, Object> variables) {
        identityService.setAuthenticatedUserId(userIdentity);
        return runtimeService.startProcessInstanceByKey(processKey, variables);
    }

    /**
     * 根据流程名称获取已经启动的所有流程实例
     *
     * @param runtimeService RuntimeService实例
     * @param offset 开始索引
     * @param limit 个数
     * @return 流程实例列表
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listProcessInstances(RuntimeService runtimeService, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        long total = processInstanceQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<ProcessInstance>
            pagerVO.setRows((List) processInstanceQuery.listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 根据流程名称获取已经启动的所有流程实例
     *
     * @param runtimeService RuntimeService实例
     * @param processKey    流程名称
     * @param offset 开始索引
     * @param limit 个数
     * @return 流程实例列表
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listProcessInstances(RuntimeService runtimeService, String processKey, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey);
        long total = processInstanceQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<ProcessInstance>
            pagerVO.setRows((List) processInstanceQuery.listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 根据用户标识和流程名称获取已经启动的所有流程实例
     *
     * @param runtimeService RuntimeService实例
     * @param userIdentity   启动流程的用户标识
     * @param offset 开始索引
     * @param limit 个数
     * @return 流程实例列表
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listUserProcessInstances(RuntimeService runtimeService, String userIdentity, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().startedBy(userIdentity);
        long total = processInstanceQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<ProcessInstance>
            pagerVO.setRows((List) processInstanceQuery.listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 根据用户标识和流程名称获取已经启动的所有流程实例
     *
     * @param runtimeService RuntimeService实例
     * @param userIdentity   启动流程的用户标识
     * @param processKey    流程名称
     * @param offset 开始索引
     * @param limit 个数
     * @return 流程实例列表
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listUserProcessInstances(RuntimeService runtimeService, String userIdentity, String processKey, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey).startedBy(userIdentity);
        long total = processInstanceQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<ProcessInstance>
            pagerVO.setRows((List) processInstanceQuery.listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 查询所有待办任务
     * @param taskService
     * @param offset
     * @param limit
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listTasks(TaskService taskService, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        TaskQuery taskQuery = taskService.createTaskQuery();
        long total = taskQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<Task>
            pagerVO.setRows((List) taskQuery.orderByTaskCreateTime().asc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 根据指派的用户对象列出所有任务，按任务时间正序排列
     *
     * @param taskService  TaskService实例
     * @param userIdentity 任务指派人用户标识
     * @param offset 开始索引
     * @param limit 个数
     * @return 指定用户的所有任务组成的列表
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listAssigneeTasks(TaskService taskService, String userIdentity, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userIdentity);
        long total = taskQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<Task>
            pagerVO.setRows((List) taskQuery.orderByTaskCreateTime().asc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 根据候选人列出所有任务，按任务时间正序排列
     *
     * @param taskService  TaskService实例
     * @param userIdentity 任务候选人用户标识
     * @param offset 开始索引
     * @param limit 个数
     * @return 指定候选人的所有任务
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listCandidateUserTasks(TaskService taskService, String userIdentity, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateUser(userIdentity);
        long total = taskQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<Task>
            pagerVO.setRows((List) taskQuery.orderByTaskCreateTime().asc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 根据候选角色列出所有任务，按任务时间正序排列
     *
     * @param taskService  TaskService实例
     * @param roleIdentities 候选的角色或组标识
     * @param offset 开始索引
     * @param limit 个数
     * @return 指定候选角色或组的的所有任务
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listCandidateGroupTasks(TaskService taskService, List<String> roleIdentities, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateGroupIn(roleIdentities);
        long total = taskQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<Task>
            pagerVO.setRows((List) taskQuery.orderByTaskCreateTime().asc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 执行指定的任务
     * @param taskService
     * @param taskId
     */
    public static void executeTask(TaskService taskService, String taskId) {
        taskService.complete(taskId);
    }

    /**
     * 执行带参任务
     * @param taskService
     * @param taskId
     * @param variables
     */
    public static void executeTask(TaskService taskService, String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    /**
     * 根据流程名获取流程定义对应的png图片
     *
     * @param repositoryService RepositoryService实例
     * @param processKey       流程名
     * @param version 流程版本号
     * @return 原始流程图片
     */
    public static InputStream getDiagramPNG(RepositoryService repositoryService, String processKey, Integer version) {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).processDefinitionVersion(version).list();
        if (processDefinitionList != null && processDefinitionList.size() > 0) {
            ProcessDefinition processDefinition = processDefinitionList.get(0);
            return repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
        } else {
            return null;
        }
    }

    /**
     * 根据流程实例编号和流程名称动态生成流程图片，可标识出当前任务节点
     *
     * @param processEngine     ProcessEngine实例
     * @param runtimeService    RuntimeService实例
     * @param repositoryService RepositoryService实例
     * @param processInstanceId 流程实例编号
     * @return 标识出当前任务节点的流程图
     */
    public static InputStream generateDiagramPNG(ProcessEngine processEngine, RuntimeService runtimeService, RepositoryService repositoryService, String processInstanceId) {
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).list();
        if (processInstanceList != null && processInstanceList.size() > 0) {
            ProcessInstance processInstance = processInstanceList.get(0);
            List<String> activityIds = new ArrayList<>();
            for (Execution execution : executions) {
                activityIds.addAll(runtimeService.getActiveActivityIds(execution.getId()));
            }
            if (activityIds.size() > 0) {
                ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
                return new DefaultProcessDiagramGenerator().generateDiagram(
                        repositoryService.getBpmnModel(processInstance.getProcessDefinitionId()),
                        "png",
                        activityIds,
                        Collections.emptyList(),
                        processEngineConfiguration.getActivityFontName(),
                        processEngineConfiguration.getLabelFontName(),
                        processEngineConfiguration.getAnnotationFontName(),
                        processEngineConfiguration.getClassLoader(),
                        1.0
                );
            }
        }
        return null;
    }

    /**
     * 查看所有历史流程实例，按执行完的时间倒序排列
     *
     * @param historyService HistoryService实例
     * @param offset 开始索引
     * @param limit 个数
     * @return 所有历史流程实例
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listHistoricProcessInstances(HistoryService historyService, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        long total = historicProcessInstanceQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<HistoricProcessInstance>
            pagerVO.setRows((List) historicProcessInstanceQuery.orderByProcessInstanceEndTime().desc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 查看指定流程名的所有历史流程实例，按执行完的时间倒序排列
     *
     * @param historyService HistoryService实例
     * @param processKey    流程名
     * @param offset 开始索引
     * @param limit 个数
     * @return 所有历史流程实例
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listHistoricProcessInstances(HistoryService historyService, String processKey, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processKey);
        long total = historicProcessInstanceQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<HistoricProcessInstance>
            pagerVO.setRows((List) historicProcessInstanceQuery.orderByProcessInstanceEndTime().desc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 查看某个用户启动的所有历史流程实例，按执行完的时间倒序排列
     *
     * @param historyService HistoryService实例
     * @param offset 开始索引
     * @param limit 个数
     * @return 所有历史流程实例
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listUserHistoricProcessInstances(HistoryService historyService, String userIdentity, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().startedBy(userIdentity);
        long total = historicProcessInstanceQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<HistoricProcessInstance>
            pagerVO.setRows((List) historicProcessInstanceQuery.orderByProcessInstanceEndTime().desc().listPage(offset, limit));
        }
        return pagerVO;
    }

    /**
     * 查看某个用户启动的指定流程名的所有历史流程实例，按执行完的时间倒序排列
     *
     * @param historyService HistoryService实例
     * @param processKey    流程名
     * @return 所有历史流程实例
     */
    @SuppressWarnings({"unchecked"})
    public static PagerVO listUserHistoricProcessInstances(HistoryService historyService, String processKey, String userIdentity, int offset, int limit) {
        PagerVO pagerVO = new PagerVO(0L, null);
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processKey).startedBy(userIdentity);
        long total = historicProcessInstanceQuery.count();
        if (total > 0) {
            pagerVO.setTotal(total);
            // List<HistoricProcessInstance>
            pagerVO.setRows((List) historicProcessInstanceQuery.orderByProcessInstanceEndTime().desc().listPage(offset, limit));
        }
        return pagerVO;
    }

}
