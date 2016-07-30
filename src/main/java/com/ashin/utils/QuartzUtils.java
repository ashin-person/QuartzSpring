package com.ashin.utils;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzUtils {
	
	@Autowired
	private Scheduler scheduler;
	/**
	 * 开始一个simpleSchedule()调度(创建一个新的定时任务)
	 * @param jName
	 * @param jGroup
	 * @param cron
	 * @param tName
	 * @param tGroup
	 * @param c
	 */
	public void startScheduler(String jName,String jGroup,String cron,
			String tName,String tGroup,Class c){
		try {
			//1.创建一个jobDetail实例
			JobDetail jobDetail = JobBuilder.newJob(c).withIdentity(jName, jGroup).build();
			//2.创建Trigger
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(tName, tGroup)
					.startNow().withSchedule(cronScheduleBuilder).build();
			//3.任务调度执行
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 开始定时任务
	 */
	public void startJob(){
		try {
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 暂停任务
	 * @param jName job的名字
	 * @param jGroup job所在的组
	 */
	public void pauseJob(String jName,String jGroup){
		JobKey jobKey = JobKey.jobKey(jName,jGroup);
		try {
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 恢复Job
	 * @param name  job名字
	 * @param group  job组名
	 */
	public void resumeJob(String name, String group){
		JobKey jobKey = JobKey.jobKey(name,group);
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 更新任务表达式
	 * @param name  trigger名字
	 * @param group  trigger组名
	 * @param cron  cron时间表达式
	 */
	public void rescheduleJob(String name, String group,String cron) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(name,group);
			// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 表达式调度构建器    "0/2 * * * * ?"
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
