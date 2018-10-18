package top.zywork.common;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.constant.BPMNConstants;

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
     * 根据流程名部署指定流程，流程保存在BPMNConstant定义的BPMN常量目录中（resources/processes）<br/>
     * 流程文件和流程图打包成zip文件
     *
     * @param repositoryService RepositoryService实例
     * @param processName       流程名
     * @return 流程定义对象
     */
    public Deployment deploy(RepositoryService repositoryService, String processName) {
        InputStream inputStream = null;
        ZipInputStream zipInputStream = null;
        try {
            inputStream = new FileInputStream(FileUtils.getBPMNDir() + processName + BPMNConstants.SUFFIX_ZIP);
            zipInputStream = new ZipInputStream(inputStream);
            return repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
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
     * 列出所有流程部署，按照部署时间降序排列
     *
     * @param repositoryService RepositoryService实例
     * @return 所有流程部署
     */
    public List<Deployment> listDeployments(RepositoryService repositoryService) {
        return repositoryService.createDeploymentQuery().orderByDeploymenTime().desc().list();
    }

    /**
     * 列出所有流程部署的最近版本
     *
     * @param repositoryService RepositoryService实例
     * @return 所有流程部署对象列表
     */
    public List<Deployment> listLatestDeployments(RepositoryService repositoryService) {
        return repositoryService.createDeploymentQuery().latest().list();
    }

    /**
     * 列出指定流程的流程部署，按照部署时间倒序排列
     *
     * @param repositoryService RepositoryService实例
     * @param processName       流程名
     * @return 指定流程的流程部署对象列表
     */
    public List<Deployment> listDeployments(RepositoryService repositoryService, String processName) {
        return repositoryService.createDeploymentQuery().deploymentName(processName).orderByDeploymenTime().list();
    }

    /**
     * 获取指定流程名的最新版本的流程部署
     *
     * @param repositoryService RepositoryService实例
     * @param processName       流程名
     * @return 指定流程名的最新版本的流程部署对象
     */
    public Deployment getLatestDeployment(RepositoryService repositoryService, String processName) {
        List<Deployment> deploymentList = listDeployments(repositoryService, processName);
        return (deploymentList != null && deploymentList.size() > 0) ? deploymentList.get(0) : null;
    }

    /**
     * 根据流程名删除流程部署
     *
     * @param repositoryService RepositoryService实例
     * @param processName       流程名
     */
    public void removeDeployment(RepositoryService repositoryService, String processName) {
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().processDefinitionKey(processName).list();
        for (Deployment deployment : deploymentList) {
            repositoryService.deleteDeployment(deployment.getId(), true);
        }
    }

    /**
     * 列出所有流程定义，按照版本降序排列
     *
     * @param repositoryService RepositoryService实例
     * @return 所有流程定义对象列表
     */
    public List<ProcessDefinition> listProcessDefinitions(RepositoryService repositoryService) {
        return repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc().list();
    }

    /**
     * 列出所有流程定义的最新版本
     *
     * @param repositoryService RepositoryService实例
     * @return 所有流程定义对象列表
     */
    public List<ProcessDefinition> listLatestProcessDefinitions(RepositoryService repositoryService) {
        return repositoryService.createProcessDefinitionQuery().latestVersion().list();
    }

    /**
     * 列出指定流程的流程定义，按照版本降序排列
     *
     * @param repositoryService RepositoryService实例
     * @param processName       流程名
     * @return 指定流程的流程定义对象列表
     */
    public List<ProcessDefinition> listProcessDefinitions(RepositoryService repositoryService, String processName) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionName(processName)
                .orderByProcessDefinitionVersion().desc().list();
    }

    /**
     * 获取指定流程名的最新版本的流程定义
     *
     * @param repositoryService RepositoryService实例
     * @param processName       流程名
     * @return 指定流程名的最新版本的流程定义对象
     */
    public ProcessDefinition getLatestProcessDefinition(RepositoryService repositoryService, String processName) {
        List<ProcessDefinition> processDefinitionList = listProcessDefinitions(repositoryService, processName);
        return (processDefinitionList != null && processDefinitionList.size() > 0) ? processDefinitionList.get(0) : null;
    }

    /**
     * 启动流程，并且只能启动一个，如果已经有启动的流程，则不能再次启动流程
     *
     * @param runtimeService RuntimeService实例
     * @param processKey     流程key
     * @return 流程实例
     */
    public ProcessInstance startOneProcess(RuntimeService runtimeService, String processKey) {
        List<ProcessInstance> processInstanceList = listProcessInstances(runtimeService, processKey);
        return (processInstanceList != null && processInstanceList.size() > 0) ? null : runtimeService.startProcessInstanceByKey(processKey);
    }

    /**
     * 启动流程，并且只能启动一个，如果已经有启动的流程，则不能再次启动流程
     *
     * @param runtimeService RuntimeService实例
     * @param processKey     流程key
     * @param variables      流程变量
     * @return 流程实例
     */
    public ProcessInstance startOneProcess(RuntimeService runtimeService, String processKey, Map<String, Object> variables) {
        List<ProcessInstance> processInstanceList = listProcessInstances(runtimeService, processKey);
        return (processInstanceList != null && processInstanceList.size() > 0) ? null : runtimeService.startProcessInstanceByKey(processKey, variables);
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
    public ProcessInstance startOneProcess(IdentityService identityService, RuntimeService runtimeService, String userIdentity, String processKey) {
        List<ProcessInstance> processInstanceList = listProcessInstances(runtimeService, userIdentity, processKey);
        if (processInstanceList != null && processInstanceList.size() > 0) {
            return null;
        } else {
            identityService.setAuthenticatedUserId(String.valueOf(userIdentity));
            return runtimeService.startProcessInstanceByKey(processKey);
        }
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
    public ProcessInstance startOneProcess(IdentityService identityService, RuntimeService runtimeService, String userIdentity, String processKey, Map<String, Object> variables) {
        List<ProcessInstance> processInstanceList = listProcessInstances(runtimeService, userIdentity, processKey);
        if (processInstanceList != null && processInstanceList.size() > 0) {
            return null;
        } else {
            identityService.setAuthenticatedUserId(String.valueOf(userIdentity));
            return runtimeService.startProcessInstanceByKey(processKey, variables);
        }
    }

    /**
     * 启动流程
     *
     * @param runtimeService RuntimeService实例
     * @param processKey     流程key
     * @return 流程实例
     */
    public ProcessInstance startProcess(RuntimeService runtimeService, String processKey) {
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
    public ProcessInstance startProcess(RuntimeService runtimeService, String processKey, Map<String, Object> variables) {
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
    public ProcessInstance startProcess(IdentityService identityService, RuntimeService runtimeService, String userIdentity, String processKey) {
        identityService.setAuthenticatedUserId(String.valueOf(userIdentity));
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
    public ProcessInstance startProcess(IdentityService identityService, RuntimeService runtimeService, String userIdentity, String processKey, Map<String, Object> variables) {
        identityService.setAuthenticatedUserId(String.valueOf(userIdentity));
        return runtimeService.startProcessInstanceByKey(processKey, variables);
    }

    /**
     * 根据流程名称获取已经启动的所有流程实例
     *
     * @param runtimeService RuntimeService实例
     * @param processName    流程名称
     * @return 流程实例列表
     */
    public List<ProcessInstance> listProcessInstances(RuntimeService runtimeService, String processName) {
        return runtimeService.createProcessInstanceQuery().processDefinitionName(processName).list();
    }

    /**
     * 根据用户标识和流程名称获取已经启动的所有流程实例
     *
     * @param runtimeService RuntimeService实例
     * @param userIdentity   启动流程的用户标识
     * @param processName    流程名称
     * @return 流程实例列表
     */
    public List<ProcessInstance> listProcessInstances(RuntimeService runtimeService, String userIdentity, String processName) {
        return runtimeService.createProcessInstanceQuery().processDefinitionName(processName).startedBy(userIdentity).list();
    }

    /**
     * 根据指派的用户对象列出所有任务，按任务时间正序排列
     *
     * @param taskService  TaskService实例
     * @param userIdentity 任务指派人用户标识
     * @return 指定用户的所有任务组成的列表
     */
    public List<Task> listAssigneeTasks(TaskService taskService, String userIdentity) {
        return taskService.createTaskQuery().taskAssignee(userIdentity).orderByTaskCreateTime().asc().list();
    }

    /**
     * 根据候选人列出所有任务，按任务时间正序排列
     *
     * @param taskService  TaskService实例
     * @param userIdentity 任务候选人用户标识
     * @return 指定候选人的所有任务
     */
    public List<Task> listCandidateUserTasks(TaskService taskService, String userIdentity) {
        return taskService.createTaskQuery().taskCandidateUser(userIdentity).orderByTaskCreateTime().asc().list();
    }

    /**
     * 根据候选角色列出所有任务，按任务时间正序排列
     *
     * @param taskService  TaskService实例
     * @param roleIdentity 候选的角色或组标识
     * @return 指定候选角色或组的的所有任务
     */
    public List<Task> listCandidateGroupTasks(TaskService taskService, String roleIdentity) {
        return taskService.createTaskQuery().taskCandidateGroup(roleIdentity).orderByTaskCreateTime().asc().list();
    }

    /**
     * 根据流程名获取流程定义对应的png图片
     *
     * @param repositoryService RepositoryService实例
     * @param processName       流程名
     * @return 原始流程图片
     */
    public InputStream getDiagramPNG(RepositoryService repositoryService, String processName) {
        ProcessDefinition processDefinition = getLatestProcessDefinition(repositoryService, processName);
        if (processDefinition != null) {
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
     * @param processName       流程名
     * @return 标识出当前任务节点的流程图
     */
    public InputStream generateDiagramPNG(ProcessEngine processEngine, RuntimeService runtimeService, RepositoryService repositoryService, String processInstanceId, String processName) {
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
        List<String> activityIds = new ArrayList<>();
        for (Execution execution : executions) {
            activityIds.addAll(runtimeService.getActiveActivityIds(execution.getId()));
        }
        if (activityIds.size() > 0) {
            ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
            return new DefaultProcessDiagramGenerator().generateDiagram(
                    repositoryService.getBpmnModel(getLatestProcessDefinition(repositoryService, processName).getId()),
                    "png",
                    activityIds,
                    Collections.emptyList(),
                    processEngineConfiguration.getActivityFontName(),
                    processEngineConfiguration.getLabelFontName(),
                    processEngineConfiguration.getAnnotationFontName(),
                    processEngineConfiguration.getClassLoader(),
                    1.0
            );
        } else {
            return null;
        }
    }

    /**
     * 查看所有历史流程实例，按执行完的时间倒序排列
     *
     * @param historyService HistoryService实例
     * @return 所有历史流程实例
     */
    public List<HistoricProcessInstance> listHistoricProcessInstances(HistoryService historyService) {
        return historyService.createHistoricProcessInstanceQuery().orderByProcessInstanceEndTime().desc().list();
    }

    /**
     * 查看指定流程名的所有历史流程实例，按执行完的时间倒序排列
     *
     * @param historyService HistoryService实例
     * @param processName    流程名
     * @return 所有历史流程实例
     */
    public List<HistoricProcessInstance> listHistoricProcessInstances(HistoryService historyService, String processName) {
        return historyService.createHistoricProcessInstanceQuery().processDefinitionName(processName).orderByProcessInstanceEndTime().desc().list();
    }

    /**
     * 查看某个用户启动的所有历史流程实例，按执行完的时间倒序排列
     *
     * @param historyService HistoryService实例
     * @return 所有历史流程实例
     */
    public List<HistoricProcessInstance> listUserHistoricProcessInstances(HistoryService historyService, String userIdentity) {
        return historyService.createHistoricProcessInstanceQuery().startedBy(userIdentity).orderByProcessInstanceEndTime().desc().list();
    }

    /**
     * 查看某个用户启动的指定流程名的所有历史流程实例，按执行完的时间倒序排列
     *
     * @param historyService HistoryService实例
     * @param processName    流程名
     * @return 所有历史流程实例
     */
    public List<HistoricProcessInstance> listHistoricProcessInstances(HistoryService historyService, String processName, String userIdentity) {
        return historyService.createHistoricProcessInstanceQuery().processDefinitionName(processName)
                .startedBy(userIdentity).orderByProcessInstanceEndTime().desc().list();
    }

}
