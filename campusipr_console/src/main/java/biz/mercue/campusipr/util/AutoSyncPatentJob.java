package biz.mercue.campusipr.util;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.service.AdminService;
import biz.mercue.campusipr.service.PatentService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AutoSyncPatentJob implements Job {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    PatentService patentService;

    @Autowired
    AdminService adminService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long start = System.nanoTime();
        log.info("hello world");
        String systemAdmin = Constants.SYSTEM_ADMIN;
        Admin admin = adminService.getById(systemAdmin);
        List<Patent> patentList = patentService.getPatentList();
        for (Patent patent : patentList) {
            patent.setAdmin(admin);
            patentService.syncPatentDataBySchedule(patent);
        }
        long end = System.nanoTime();
        long total = end - start;
        double second = (double) total / 1_000_000_000.0;
        double minutes = second / 60.0;
        log.info("total auto sync patent spend time (seconds): " + second);
        log.info("total auto sync patent spend time (minutes): " + minutes);
    }
}
