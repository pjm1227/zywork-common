package top.zywork.common;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用quartz的作业调度工具类，包含启动，停止，重启，修改作业等方法<br/>
 *
 * 创建于2018-01-16<br/>
 *
 * @author 王振宇
 * @version 1.0
 *
 */
public class SchedulerUtils {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerUtils.class);

    private static Scheduler scheduler;

    public static final String DEFAULT_JOB_GROUP = "zywork_job_group";
    public static final String DEFAULT_TRIGGER_GROUP = "zywork_trigger_group";

    public static final String DATA_KEY = "data";

    /**
     * 使用quartz初始化Schedulery对象
     */
    public static void initScheduler() {
        if (scheduler == null) {
            try {
                scheduler = new StdSchedulerFactory().getScheduler();
            } catch (SchedulerException e) {
                logger.error("SchedulerFactory getScheduler error: {}", e.getMessage());
            }
        }
    }

    /**
     * 通过Spring-scheduler的SchedulerFactoryBean初始化Scheduler对象
     * @param schedulerFactoryBean
     */
    public static void initScheduler(SchedulerFactoryBean schedulerFactoryBean) {
        if (scheduler == null) {
            scheduler = schedulerFactoryBean.getScheduler();
        }
    }

    /**
     * 启动一个作业，并指定相应的作业组名称和触发器及触发器组名称
     * @param jobClassName 作业类的全限定名
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     * @param cronExpression cron表达式
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean startJob(String jobClassName, String jobName, String jobGroup, String triggerName,
                              String triggerGroup, String cronExpression) {
        try {
            Class jobClass = Class.forName(jobClassName);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException | ClassNotFoundException e) {
            logger.error("start job {} error: {}", jobClassName, e.getMessage());
            return false;
        }
    }

    /**
     * 启动一个作业，并使用默认的作业组和触发器组名称
     * @param jobClassName 作业类的全限定名
     * @param jobName 作业名称
     * @param triggerName 触发器名称
     * @param cronExpression cron表达式
     * @return
     */
    public static boolean startJob(String jobClassName, String jobName, String triggerName, String cronExpression) {
        return startJob(jobClassName, jobName, DEFAULT_JOB_GROUP, triggerName, DEFAULT_TRIGGER_GROUP, cronExpression);
    }

    /**
     * 启动一个作业，并指定相应的作业组名称和触发器及触发器组名称
     * @param jobClassName 作业类的全限定名
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @param jobData 传递给job的数据所组成的JavaBean
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     * @param cronExpression cron表达式
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public static boolean startJob(String jobClassName, String jobName, String jobGroup, Object jobData,
                                String triggerName, String triggerGroup, String cronExpression) {
        try {
            Class jobClass = Class.forName(jobClassName);
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(DATA_KEY, jobData);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).setJobData(jobDataMap).withIdentity(jobName, jobGroup).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException | ClassNotFoundException e) {
            logger.error("start job {} with data error: {}", jobClassName, e.getMessage());
            return false;
        }
    }

    /**
     * 启动一个作业，并使用默认的作业组和触发器组名称
     * @param jobClassName 作业类的全限定名
     * @param jobName 作业名称
     * @param jobData 传递给job的数据所组成的JavaBean
     * @param triggerName 触发器名称
     * @param cronExpression cron表达式
     * @return
     */
    public static boolean startJob(String jobClassName, String jobName, Object jobData, String triggerName, String cronExpression) {
        return startJob(jobClassName, jobName, DEFAULT_JOB_GROUP, jobData, triggerName, DEFAULT_TRIGGER_GROUP, cronExpression);
    }

    /**
     * 暂停一个作业
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @return
     */
    public static boolean pauseJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("pause job {} error: {}", jobName, e.getMessage());
            return false;
        }
    }

    /**
     * 暂停一个作业，默认作业组中
     * @param jobName 作业名称
     * @return
     */
    public static boolean pauseJob(String jobName) {
        return pauseJob(jobName, DEFAULT_JOB_GROUP);
    }

    /**
     * 恢复一个作业
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @return
     */
    public static boolean resumeJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("resume job {} error: {}", jobName, e.getMessage());
            return false;
        }
    }

    /**
     * 恢复一个作业，默认作业组中
     * @param jobName 作业名称
     * @return
     */
    public static boolean resumeJob(String jobName) {
        return resumeJob(jobName, DEFAULT_JOB_GROUP);
    }

    /**
     * 暂停一个触发器
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     * @return
     */
    public static boolean pauseTrigger(String triggerName, String triggerGroup) {
        try {
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey);
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("pause trigger {} error: {}", triggerName, e.getMessage());
            return false;
        }
    }

    /**
     * 暂停一个触发器，默认触发器组中
     * @param triggerName 触发器名称
     * @return
     */
    public static boolean pauseTrigger(String triggerName) {
        return pauseTrigger(triggerName, DEFAULT_TRIGGER_GROUP);
    }

    /**
     * 恢复一个触发器
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     * @return
     */
    public static boolean resumeTrigger(String triggerName, String triggerGroup) {
        try {
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.resumeTrigger(triggerKey);
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("resume trigger {} error: {}", triggerName, e.getMessage());
            return false;
        }
    }

    /**
     * 恢复一个触发器，默认触发器组
     * @param triggerName 触发器名称
     * @return
     */
    public static boolean resumeTrigger(String triggerName) {
        return resumeTrigger(triggerName, DEFAULT_TRIGGER_GROUP);
    }

    /**
     * 停止一个作业，此时会从Scheduler中移除此作业，不能使用restartJob重启作业
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     * @return
     */
    public static boolean stopJob(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("stop job {} error: {}", jobName, e.getMessage());
            return false;
        }
    }

    /**
     * 停止一个作业，默认作业组及触发器组
     * @param jobName 作业名称
     * @param triggerName 触发器名称
     * @return
     */
    public static boolean stopJob(String jobName, String triggerName) {
        return stopJob(jobName, DEFAULT_JOB_GROUP, triggerName, DEFAULT_TRIGGER_GROUP);
    }

    /**
     * 重启一个正在运行的作业，如果是已经停止的作业，不可被重启
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public static boolean restartJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                List<Trigger> triggerList = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggerList) {
                    scheduler.rescheduleJob(trigger.getKey(), trigger);
                }
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("restart running job {} error: {}", jobName, e.getMessage());
            return false;
        }
    }

    /**
     * 重启一个作业，默认作业组中
     * @param jobName 作业名称
     * @return
     */
    public static boolean restartJob(String jobName) {
        return restartJob(jobName, DEFAULT_JOB_GROUP);
    }

    /**
     * 停止所有作业，并关闭Scheduler
     * @return
     */
    public static boolean stopAllJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("stop all jobs error: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 修改一个正在运行的作业的cron表达式并重新调度
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     * @param cronExpression cron表达式
     * @return
     */
    public static boolean modifyJob(String jobName, String jobGroup, String triggerName, String triggerGroup, String cronExpression) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(jobKey)) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.scheduleJob(jobDetail, TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build());
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("modify job {} with new cron expression {} error: {}", jobName, cronExpression, e.getMessage());
            return false;
        }
    }

    /**
     * 修改一个正在运行的作业的cron表达式并重新调度，默认作业组和触发器组
     * @param jobName 作业名称
     * @param triggerName 触发器名称
     * @param cronExpression cron表达式
     * @return
     */
    public static boolean modifyJob(String jobName, String triggerName, String cronExpression) {
        return modifyJob(jobName, DEFAULT_JOB_GROUP, triggerName, DEFAULT_TRIGGER_GROUP, cronExpression);
    }

    /**
     * 删除一个作业
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     * @return
     */
    public static boolean deleteJob(String jobName, String jobGroup,  String triggerName, String triggerGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(jobKey);
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("delete job {} error: {}", jobName, e.getMessage());
            return false;
        }
    }

    /**
     * 删除一个作业，默认作业组和触发器组
     * @param jobName 作业名称
     * @param triggerName 触发器名称
     * @return
     */
    public static boolean deleteJob(String jobName, String triggerName) {
        return deleteJob(jobName, DEFAULT_JOB_GROUP, triggerName, DEFAULT_TRIGGER_GROUP);
    }

    /**
     * 获取作业的参数所组成的JavaBean对象
     * @param jobExecutionContext 作业执行上下文对象
     * @return 传递给作业的数据所组成的JavaBean对象
     */
    public static Object getJobData(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        return jobDataMap != null ? jobDataMap.get(DATA_KEY) : null;
    }

    /**
     * 获取所有正在运行的任务
     * @return
     */
    public static List<JobExecutionContext> executingJobs() {
        try {
            return scheduler.getCurrentlyExecutingJobs();
        } catch (SchedulerException e) {
            logger.error("get executing jobs error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取所有正在运行的任务名称
     * @return
     */
    public static List<String> executingJobNames() {
        List<JobExecutionContext> jobExecutionContextList = executingJobs();
        if (jobExecutionContextList == null) {
            return null;
        }
        List<String> jobNames = new ArrayList<>();
        for (JobExecutionContext jobExecutionContext : jobExecutionContextList) {
            JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
            jobNames.add(jobKey.getName());
        }
        return jobNames;
    }

}
