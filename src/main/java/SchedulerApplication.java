import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import scheduler.CronJobScheduler;
import scheduler.MyJob;

import java.util.Timer;

public class SchedulerApplication {

    public static void main(String[] args) throws Exception {
/*
        Timer time = new Timer();
        CronJobScheduler scheduler = new CronJobScheduler();
        time.schedule(scheduler, 0, 10000);*/

        //Create instance of factory
        SchedulerFactory schedulerFactory=new StdSchedulerFactory();

        //Get schedular
        Scheduler scheduler= schedulerFactory.getScheduler();

        //Create JobDetail object specifying which Job you want to execute
        JobDetail jobDetail= JobBuilder.newJob(MyJob.class).withIdentity("myJob","group1").build();


        //Associate Trigger to the Job
        CronTrigger trigger=TriggerBuilder.newTrigger()
                                            .withIdentity("trigger1","group1")
                                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 * * * ?"))
                                            .forJob("myJob","group1")
                                            .build();
                //new CronTrigger("cronTrigger","myJob1","0 0/1 * * * ?");

        //Pass JobDetail and trigger dependencies to schedular
        scheduler.scheduleJob(jobDetail,trigger);

        //Start schedular
        scheduler.start();
    }
}
