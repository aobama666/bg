package sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.sgcc.bg.job.HandleSyncJob;
import com.sgcc.bg.mapper.BgWorkinghourInfoMapper;
import com.sgcc.bg.service.HandleSyncService;
import com.sgcc.bg.service.SyncService;

import ch.qos.logback.classic.Logger;
import config.TestConfig;


public class syncTest {
	public static ApplicationContext ctx;
	public HandleSyncService handleSyncService = ctx.getBean(HandleSyncService.class);
	public BgWorkinghourInfoMapper bgMapper = ctx.getBean(BgWorkinghourInfoMapper.class);

	@BeforeClass
	public static void initConfig(){
		ctx=TestConfig.getApplicationContext("src/main/resources/applicationContext.xml");
	}
	@Test
	public void sync() {
		System.out.println("------start-----");
		Logger logger = (Logger) LoggerFactory.getLogger(HandleSyncJob.class);
		
		
		logger.info("开始处转存研系统中间表...");
		handleSyncService.copyFromKY();
		logger.info("科研系统中间表转存完毕！");
		
/*********************************************************************************************/
		logger.info("开始处理科研系统中间表...");
		handleSyncService.validateKY();
		logger.info("科研系统中间表处理完毕！");
		
/*********************************************************************************************/	
		logger.info("开始根据科研系统数据更新报工系统...");
		handleSyncService.updateFromKY();
		logger.info("根据科研系统数据更新报工系统完毕！");
		
/**********************************科研***********************************************************/
/*********************************横向************************************************************/

//		logger.info("开始处转存横向系统中间表...");
//		handleSyncService.copyFromHX();
//		logger.info("横向系统中间表转存完毕！");
		
/*********************************************************************************************/
//		logger.info("开始处理横向系统中间表...");
//		handleSyncService.validateHX();
//		logger.info("横向系统中间表处理完毕！");
		
/*********************************************************************************************/	
//		logger.info("开始根据横向系统数据更新报工系统...");
//		handleSyncService.updateFromHX();
//		logger.info("根据横向系统数据更新报工系统完毕！");	
		System.out.println("------end-----");
	}
	
	@Test
	public void testMapper() {
		List<Map<String,Object>> list = bgMapper.getWorkingHourInfoByDateAndType("epri_gukai", "2019-01-07", "2019-01-13", null, new String[]{"NP","CG","BP"});
		System.out.println(list);
		
		List<Map<String,Object>> list1 = bgMapper.getBPByDateAndIsRelated("epri_gukai", "2019-01-07", "2019-01-13", "0");
		System.out.println(list1);
		
		/*List<String> idList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			if(map.get("")){
				
			}
			idList
		}*/
		List<Map<String,Object>> list2 = bgMapper.getBPByWorkingHourInfo(list);
		
		//String workHours = bgMapper.getWorkHoursById(list);
		System.out.println(list2);
		
	}
}
