package com.sgcc.bg.job;

import ch.qos.logback.classic.Logger;
import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.service.KqTemporaryService;
import java.util.Date;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyncKqInfoJob {
    private Logger logger = (Logger) LoggerFactory.getLogger(SyncKqInfoJob.class);

    @Autowired
    private KqTemporaryService kqTemporaryService;

    public void syncBaseDataForKq(){

        String localhost_ip = QuartzJob.getLocalIP()==null?"xxx":QuartzJob.getLocalIP();

        logger.info("[syncKQ]:考勤数据同步配置：DataSyncKQ="+ ConfigUtils.getConfig("DataSyncKQ")+";"
                +"DataSyncKQ_IP="+ConfigUtils.getConfig("DataSyncKQ_IP")+";"
                +"localhost_ip="+localhost_ip);
        if (Rtext.ToBoolean(ConfigUtils.getConfig("DataSyncKQ"))
                &&ConfigUtils.getConfig("DataSyncKQ_IP").equals(localhost_ip)) {

            //获取执行同步任务的时间
            logger.info("考勤同步的定时任务开始");
            Date   BeginDate=DateUtil.getBeginDayOfLastMonth();
            Date   EndDate=DateUtil.getEndDayOfLastMonth();
            String  BeginDates=DateUtil.getFormatDateString(BeginDate,"yyyy-MM-dd");
            String  EndDates=DateUtil.getFormatDateString(EndDate,"yyyy-MM-dd");
            kqTemporaryService.addTemporary(BeginDates,EndDates);
            logger.info("考勤同步的定时任务结束");
        }else{
            logger.error("[syncKQ]:科研数据同步已关闭！请检查");
        }
    }

}
