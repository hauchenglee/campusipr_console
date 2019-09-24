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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
//        List<Patent> patentList = new ArrayList<>();
//        Patent patent1 = patentService.getById("007d5e7b54f0497a9b3252573d04594b");
//        patentList.add(patent1);
        log.info("scheduled -> auto sync patent size: " + patentList.size());
        for (Patent patent : patentList) {
            patent.setAdmin(admin);
            Map<String, Patent> mergeMap = patentService.syncPatentDataBySchedule(patent);
            if (mergeMap != null && !mergeMap.isEmpty()) {
                String dbPatentId = "";
                Patent editPatent = new Patent();
                for (Map.Entry<String, Patent> entry : mergeMap.entrySet()) {
                    dbPatentId = entry.getKey();
                    editPatent = entry.getValue();
                }
                patentService.mergeDiffPatent(dbPatentId, editPatent, admin, editPatent.getBusiness());
            }
        }
        long end = System.nanoTime();
        long total = end - start;
        double second = (double) total / 1_000_000_000.0;
        double minutes = second / 60.0;
        log.info("total auto sync patent spend time (seconds): " + second);
        log.info("total auto sync patent spend time (minutes): " + minutes);
    }
}
