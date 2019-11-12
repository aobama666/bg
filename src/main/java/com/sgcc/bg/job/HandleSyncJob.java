package com.sgcc.bg.job;

import com.sgcc.bg.mapper.HandleSyncMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.service.HandleSyncService;

import ch.qos.logback.classic.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class HandleSyncJob {
    private Logger logger = (Logger) LoggerFactory.getLogger(HandleSyncJob.class);
    @Autowired
    private HandleSyncService handleSyncService;

    @Autowired
    private HandleSyncMapper handleSyncMapper;

    /**
     * 处理其他系统的接口数据
     */
    public void handleData() throws ParseException {
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

        Date date = new Date();
        Date sightcingDate ;
        Date startDate;
        Date endDate;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd");
        sightcingDate = df.parse(ds.format(date)+"12:00:00");
        if(date.getTime()<sightcingDate.getTime()){
            String amStartDate = ConfigUtils.getConfig("amStartDate");
            String amEndDate = ConfigUtils.getConfig("amEndDate");
            startDate = df.parse(ds.format(date)+amStartDate);
            endDate = df.parse(ds.format(date)+amEndDate);
        }else {
            String pmSartDate = ConfigUtils.getConfig("pmStartDate");
            String pmEndDate = ConfigUtils.getConfig("pmEndDate");
            startDate = df.parse(ds.format(date)+pmSartDate);
            endDate = df.parse(ds.format(date)+pmEndDate);
        }

        if (Rtext.ToBoolean(ConfigUtils.getConfig("DataSyncKY"))
                &&ConfigUtils.getConfig("DataSyncKY_IP").equals(localhost_ip)) {

            List<Map<String,Object>> listKY = handleSyncMapper.listKY(startDate,endDate);

            if(null != listKY && listKY.size()>0) {

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
            }else {
                logger.error("[HandleSyncJob]:科研数据同步已关闭！原因：上次数据推送失败");
            }
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

            List<Map<String,Object>> listHX = handleSyncMapper.listHX(startDate,endDate);
            if(null!= listHX && listHX.size()>0) {

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
            }else {
                logger.error("[HandleSyncJob]:横向数据同步已关闭！原因：上次数据推送失败");
            }
        }else{
            logger.error("[HandleSyncJob]:横向数据同步已关闭！请检查");
        }

/*********************************技术服务************************************************************/

        /*
         * 同步横向系统数据
         * 当配置表BG_SYS_CONFIG:DataSyncJS=true;DataSyncJS_IP=服务器IP，执行定时任务
         */
        logger.info("[HandleSyncJob]:关联系统数据同步配置：DataSyncJS="+ConfigUtils.getConfig("DataSyncJS")+";"
                +"DataSyncJS_IP="+ConfigUtils.getConfig("DataSyncJS_IP")+";"
                +"localhost_ip="+localhost_ip);
        if (Rtext.ToBoolean(ConfigUtils.getConfig("DataSyncJS"))
                &&ConfigUtils.getConfig("DataSyncJS_IP").equals(localhost_ip)) {

            List<Map<String,Object>> listJS = handleSyncMapper.listJS(startDate,endDate);
            if(null != listJS && listJS.size()>0) {

                logger.info("开始处转存技术服务数据...");
                handleSyncService.copyFromKYJS();
                logger.info("技术服务数据转存完毕！");

                /*********************************************************************************************/
                logger.info("开始同步技术服务数据到中间表...");
                handleSyncService.validateKYJS();
                logger.info("同步技术服务数据到中间表完毕！");

                /*********************************************************************************************/
                logger.info("开始根据技术服务数据更新报工系统...");
                handleSyncService.updateFromKYJS();
                logger.info("根据技术服务数据更新报工系统完毕！");
            }else {
                logger.error("[HandleSyncJob]:技术服务数据同步已关闭！原因：上次数据推送失败");
            }
        }else{
            logger.error("[HandleSyncJob]:技术服务数据同步已关闭！请检查");
        }

/*********************************清理出错信息表过期数据************************************************************/
        logger.info("开始清理出错信息表BG_SYNC_ERROR_RECORD过期数据");
        handleSyncService.cutErrorRecord();
        logger.info("出错信息表BG_SYNC_ERROR_RECORD过期数据清理完毕!");
    }
}
