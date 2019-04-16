package com.sgcc.bg.job;

import ch.qos.logback.classic.Logger;
import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.service.SyncDataForZHService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SyncZhDataBase {
    private Logger logger = (Logger) LoggerFactory.getLogger(SyncZhDataBase.class);

    @Autowired
    private SyncDataForZHService syncDataForZHService;

    /**
     * 需要从综合系统同步数据包括并且顺序如下所示：
     * 1.新增组织
     * 2.部门排序
     * 3.处室排序
     * 4.员工排序
     * 5.日历班次
     * 6.人员关系变更
     * 7.部门类型
     */
    public void syncBaseDataForZH(){
        logger.info("[SyncZhDataBase]综合系统同步数据配置JDBC_URL"+ ConfigUtils.getConfig("jdbc_syncForZH_url")+
                ",JDBC_USERNAMW" + ConfigUtils.getConfig("jdbc_syncForZH_username")+
                ",JDBC_PASSWORD"+ ConfigUtils.getConfig("jdbc_syncForZh_password"));

        String localhost_ip = QuartzJob.getLocalIP()==null?"xxx":QuartzJob.getLocalIP();

        //获取执行同步任务的时间
        String time = DateUtil.getTime();
        if (Rtext.ToBoolean(ConfigUtils.getConfig("DataSync01"))
                &&ConfigUtils.getConfig("DataSync01_IP").equals(localhost_ip)) {
            logger.info("[SyncZhDataBase]执行同步任务的时间"+time);
            try {
                /*********************************同步新增组织***********************************************************/
                Map<String, String> stringMap = syncDataForZHService.syncNewOrganForZH(time, "admin");
                if("0".equals(stringMap.get("status"))){
                    System.out.println("错误信息："+stringMap.get("message"));
                    return;
                }
                /*******************************************************************************************************/
                logger.info("新增组织更新完成");
                logger.info("综合系统部门排序开始同步");
                /*********************************同步部门排序**********************************************************/
                Map<String, String> stringMap1 = syncDataForZHService.syncDeptSortForZH(time, "admin");
                if("0".equals(stringMap1.get("status"))){
                    System.out.println("错误信息："+stringMap1.get("message"));
                    return;
                }
                logger.info("综合系统部门排序结束同步");
                /*******************************************************************************************************/
                logger.info("处室排序开始同步");
                /**********************************处室排序************************************************************/
                Map<String, String> stringMap2 = syncDataForZHService.syncPartSortForZH(time, "admin");
                if("0".equals(stringMap2.get("status"))){
                    System.out.println("错误信息："+stringMap2.get("message"));
                    return;
                }
                /******************************************************************************************************/
                logger.info("综合系统处室排序同步结束");
                logger.info("综合系统人员排序同步开始");
                /*********************************人员排序************************************************************/
                Map<String, String> stringMap3 = syncDataForZHService.syncEmpSortForZh(time, "admin");
                if("0".equals(stringMap3.get("status"))){
                    System.out.println("错误信息："+stringMap3.get("message"));
                    return;
                }
                /******************************************************************************************************/
                logger.info("综合系统人员排序同步结束");
                logger.info("综合系统同步日历开始");
                /*********************************日历班次*************************************************************/
                Map<String, String> stringMap4 = syncDataForZHService.syncScheduleForZH(time, "admin");
                if("0".equals(stringMap4.get("status"))){
                    System.out.println("错误信息："+stringMap4.get("message"));
                    return;
                }
                /******************************************************************************************************/
                logger.info("综合系统同步日历班次结束");
                logger.info("综合系统同步人员关系变更开始");
                /*********************************人员关系变更********************************************************/
                Map<String, String> stringMap5 = syncDataForZHService.syncEmpRelationForZH(time, "admin");
                if("0".equals(stringMap5.get("status"))){
                    System.out.println("错误信息："+stringMap5.get("message"));
                    return;
                }
                /*****************************************************************************************************/
                logger.info("综合系统同步人员关系变更结束");
                logger.info("综合系统同步部门类型开始");
                /********************************部门类型排序*********************************************************/
                Map<String, String> stringMap6 = syncDataForZHService.syncDeptTypeForZH(time, "admin");
                if("0".equals(stringMap6.get("status"))){
                    System.out.println("错误信息："+stringMap6.get("message"));
                    return;
                }
                /*****************************************************************************************************/
                logger.info("综合系统同步部门类型结束");
            } catch (Exception e) {
                //程序执行过程中出现异常，进行捕捉并中断程序
                e.printStackTrace();

            }

        }else{
            logger.error("[syncBaseDataForZH]:综合数据同步已关闭！请检查");
        }

    }

}
