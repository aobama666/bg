package com.sgcc.bg.job;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.service.HandleSyncService;

import ch.qos.logback.classic.Logger;


@Service
public class HandleSyncJob {
    private Logger logger = (Logger) LoggerFactory.getLogger(HandleSyncJob.class);
    @Autowired
    private HandleSyncService handleSyncService;

    /**
     * 处理其他系统的接口数据
     */
    public void handleData() {
        String localhost_ip = QuartzJob.getLocalIP()==null?"xxx":QuartzJob.getLocalIP();
/**********************************科研***********************************************************/
        /*
         * 同步科研系统数据
         *
         * 当配置表BG_SYS_CONFIG:DataSyncKY=true;DataSyncKY_IP=服务器IP，执行定时任务
         */
        logger.info("[HandleSyncJob]:关联系统数据同步配置：DataSyncKY="+ConfigUtils.getConfig("DataSyncKY")+";"
                +"DataSyncKY_IP="+ConfigUtils.getConfig("DataSyncKY_IP")+";"
                +"localhost_ip="+localhost_ip);
        if (Rtext.ToBoolean(ConfigUtils.getConfig("DataSyncKY"))
                &&ConfigUtils.getConfig("DataSyncKY_IP").equals(localhost_ip)) {

            logger.info("开始处转存研系统数据...");
            handleSyncService.copyFromKY();
            logger.info("科研系统数据转存完毕！");

            /*********************************************************************************************/
            logger.info("开始同步科研系统数据到中间表...");
            handleSyncService.validateKY();
            logger.info("同步科研系统数据到中间表完毕！");

            /*********************************************************************************************/
            logger.info("开始根据科研系统数据更新报工系统...");
            handleSyncService.updateFromKY();
            logger.info("根据科研系统数据更新报工系统完毕！");

        }else{
            logger.error("[HandleSyncJob]:科研数据同步已关闭！请检查");
        }


/*********************************横向************************************************************/
        /*
         * 同步横向系统数据
         * 当配置表BG_SYS_CONFIG:DataSyncHX=true;DataSyncHX_IP=服务器IP，执行定时任务
         */
        logger.info("[HandleSyncJob]:关联系统数据同步配置：DataSyncHX="+ConfigUtils.getConfig("DataSyncHX")+";"
                +"DataSyncHX_IP="+ConfigUtils.getConfig("DataSyncHX_IP")+";"
                +"localhost_ip="+localhost_ip);
        if (Rtext.ToBoolean(ConfigUtils.getConfig("DataSyncHX"))
                &&ConfigUtils.getConfig("DataSyncHX_IP").equals(localhost_ip)) {

            logger.info("开始处转存横向系统数据...");
            handleSyncService.copyFromHX();
            logger.info("横向系统数据转存完毕！");

            /*********************************************************************************************/
            logger.info("开始同步横向系统数据到中间表...");
            handleSyncService.validateHX();
            logger.info("同步横向系统数据到中间表完毕！");

            /*********************************************************************************************/
            logger.info("开始根据横向系统数据更新报工系统...");
            handleSyncService.updateFromHX();
            logger.info("根据横向系统数据更新报工系统完毕！");
        }else{
            logger.error("[HandleSyncJob]:横向数据同步已关闭！请检查");
        }

/*********************************清理出错信息表过期数据************************************************************/
        logger.info("开始清理出错信息表BG_SYNC_ERROR_RECORD过期数据");
        handleSyncService.cutErrorRecord();
        logger.info("出错信息表BG_SYNC_ERROR_RECORD过期数据清理完毕!");
    }

}
