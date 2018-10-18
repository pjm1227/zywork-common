package top.zywork.common;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static SchedulerFactory schedulerFactory;

    public static final String DEFAULT_JOB_GROUP = "zywork_job_group";
    public static final String DEFAULT_TRIGGER_GROUP = "zywork_trigger_group";

    public static final String DATA_KEY = "data";

    static {
        schedulerFactory = new StdSchedulerFactory();
    }

    /**
     * 启动一个作业，并指定相应的作业组名称和触发器及触发器组名称
     * @param jobClassName 作业类的全限定名
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     * @param cronExpression cron表达式
     */
    @SuppressWarnings("unchecked")
    public static void startJob(String jobClassName, String jobName, String jobGroup, String triggerName,
                              String triggerGroup, String cronExpression) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            Class jobClass = Class.forName(jobClassName);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException | ClassNotFoundException e) {
            logger.error("start job {} error: {}", jobClassName, e.getMessage());
        }
    }

    /**
     * 启动一个作业，并使用默认的作业组和触发器组名称
     * @param jobClassName 作业类的全限定名
     * @param jobName 作业名称
     * @param triggerName 触发器名称
     * @param cronExpression cron表达式
     */
    public static void startJob(String jobClassName, String jobName, String triggerName, String cronExpression) {
        startJob(jobClassName, jobName, DEFAULT_JOB_GROUP, triggerName, DEFAULT_TRIGGER_GROUP, cronExpression);
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
     */
    @SuppressWarnings({"unchecked"})
    public static void startJob(String jobClassName, String jobName, String jobGroup, Object jobData,
                                String triggerName, String triggerGroup, String cronExpression) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
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
        } catch (SchedulerException | ClassNotFoundException e) {
            logger.error("start job {} with data error: {}", jobClassName, e.getMessage());
        }
    }

    /**
     * 启动一个作业，并使用默认的作业组和触发器组名称
     * @param jobClassName 作业类的全限定名
     * @param jobName 作业名称
     * @param jobData 传递给job的数据所组成的JavaBean
     * @param triggerName 触发器名称
     * @param cronExpression cron表达式
     */
    public static void startJob(String jobClassName, String jobName, Object jobData, String triggerName, String cronExpression) {
        startJob(jobClassName, jobName, DEFAULT_JOB_GROUP, jobData, triggerName, DEFAULT_TRIGGER_GROUP, cronExpression);
    }

    /**
     * 暂停一个作业
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     */
    public static void pauseJob(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            logger.error("pause job {} error: {}", jobName, e.getMessage());
        }
    }

    /**
     * 暂停一个作业，默认作业组中
     * @param jobName 作业名称
     */
    public static void pauseJob(String jobName) {
        pauseJob(jobName, DEFAULT_JOB_GROUP);
    }

    /**
     * 恢复一个作业
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     */
    public static void resumeJob(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
            }
        } catch (SchedulerException e) {
            logger.error("resume job {} error: {}", jobName, e.getMessage());
        }
    }

    /**
     * 恢复一个作业，默认作业组中
     * @param jobName 作业名称
     */
    public static void resumeJob(String jobName) {
        resumeJob(jobName, DEFAULT_JOB_GROUP);
    }

    /**
     * 暂停一个触发器
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     */
    public static void pauseTrigger(String triggerName, String triggerGroup) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey);
            }
        } catch (SchedulerException e) {
            logger.error("pause trigger {} error: {}", triggerName, e.getMessage());
        }
    }

    /**
     * 暂停一个触发器，默认触发器组中
     * @param triggerName 触发器名称
     */
    public static void pauseTrigger(String triggerName) {
        pauseTrigger(triggerName, DEFAULT_TRIGGER_GROUP);
    }

    /**
     * 恢复一个触发器
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     */
    public static void resumeTrigger(String triggerName, String triggerGroup) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.resumeTrigger(triggerKey);
            }
        } catch (SchedulerException e) {
            logger.error("resume trigger {} error: {}", triggerName, e.getMessage());
        }
    }

    /**
     * 恢复一个触发器，默认触发器组
     * @param triggerName 触发器名称
     */
    public static void resumeTrigger(String triggerName) {
        resumeTrigger(triggerName, DEFAULT_TRIGGER_GROUP);
    }

    /**
     * 停止一个作业，此时会从Scheduler中移除此作业，不能使用restartJob重启作业
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     */
    public static void stopJob(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey(jobName, jobGroup);
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
            }
        } catch (SchedulerException e) {
            logger.error("stop job {} error: {}", jobName, e.getMessage());
        }
    }

    /**
     * 停止一个作业，默认作业组及触发器组
     * @param jobName 作业名称
     * @param triggerName 触发器名称
     */
    public static void stopJob(String jobName, String triggerName) {
        stopJob(jobName, DEFAULT_JOB_GROUP, triggerName, DEFAULT_TRIGGER_GROUP);
    }

    /**
     * 重启一个正在运行的作业，如果是已经停止的作业，不可被重启
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     */
    @SuppressWarnings({"unchecked"})
    public static void restartJob(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                List<Trigger> triggerList = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggerList) {
                    scheduler.rescheduleJob(trigger.getKey(), trigger);
                }
            }
        } catch (SchedulerException e) {
            logger.error("restart running job {} error: {}", jobName, e.getMessage());
        }
    }

    /**
     * 重启一个作业，默认作业组中
     * @param jobName 作业名称
     */
    public static void restartJob(String jobName) {
        restartJob(jobName, DEFAULT_JOB_GROUP);
    }

    /**
     * 停止所有作业，并关闭Scheduler
     */
    public static void stopAllJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            logger.error("stop all jobs error: {}", e.getMessage());
        }
    }

    /**
     * 修改一个正在运行的作业的cron表达式并重新调度
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     * @param cronExpression cron表达式
     */
    public static void modifyJob(String jobName, String jobGroup, String triggerName, String triggerGroup, String cronExpression) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey(jobName, jobGroup);
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(jobKey)) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.scheduleJob(jobDetail, TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build());
            }
        } catch (SchedulerException e) {
            logger.error("modify job {} with new cron expression {} error: {}", jobName, cronExpression, e.getMessage());
        }
    }

    /**
     * 修改一个正在运行的作业的cron表达式并重新调度，默认作业组和触发器组
     * @param jobName 作业名称
     * @param triggerName 触发器名称
     * @param cronExpression cron表达式
     */
    public static void modifyJob(String jobName, String triggerName, String cronExpression) {
        modifyJob(jobName, DEFAULT_JOB_GROUP, triggerName, DEFAULT_TRIGGER_GROUP, cronExpression);
    }

    /**
     * 删除一个作业
     * @param jobName 作业名称
     * @param jobGroup 作业组名称
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器组名称
     */
    public static void deleteJob(String jobName, String jobGroup,  String triggerName, String triggerGroup) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey(jobName, jobGroup);
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            logger.error("delete job {} error: {}", jobName, e.getMessage());
        }
    }

    /**
     * 删除一个作业，默认作业组和触发器组
     * @param jobName 作业名称
     * @param triggerName 触发器名称
     */
    public static void deleteJob(String jobName, String triggerName) {
        deleteJob(jobName, DEFAULT_JOB_GROUP, triggerName, DEFAULT_TRIGGER_GROUP);
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

}
