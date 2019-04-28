package yszx.mapper;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.sgcc.bg.yszx.bean.WLApply;
import com.sgcc.bg.yszx.mapper.ApproveMapper;
import com.sgcc.bg.yszx.service.impl.ApproveServiceImpl;

import config.TestConfig;

public class WLApplyTest {
	public static ApplicationContext ctx;
	/**
	 * 这个不能为静态方法
	 */
	private ApproveMapper mapper = ctx.getBean(ApproveMapper.class);
	
	private ApproveServiceImpl service = ctx.getBean(ApproveServiceImpl.class);
	

	@BeforeClass
	public static void initConfig(){
		ctx=TestConfig.getApplicationContext("src/main/resources/applicationContext.xml");
	}
	
	@Test
	public void addApplyAndGetId() {
		String apply_number = "YSZX_TEST_20190428";
		String apply_user = "wubin";
		Date apply_time = new Date();
		String function_type = "YSZX";
		String apply_status = "SAVE";
		String create_user = apply_user;
		String update_user = apply_user;
		
		WLApply apply = new WLApply();
		apply.setApply_number(apply_number);
		apply.setApply_user(apply_user);
		apply.setApply_time(apply_time);
		apply.setFunction_type(function_type);
		apply.setApply_status(apply_status);
		apply.setCreate_user(create_user);
		apply.setUpdate_user(update_user);
		
		mapper.addApplyAndGetId(apply);
		System.out.println("id="+apply.getId());
	}
	
	@Test
	public void updateApply() {
		String id = "8753A086ABB9865EE0536C3C550AF3FA";
		String approve_id = "approve_id_004";		
		String apply_status = "CHECK1";	
		String update_user = "wubin11";
		
		Integer rlt = mapper.updateApplyById(id, apply_status, approve_id, update_user);
		System.out.println("OK! rlt="+rlt);
	}
	
	@Test
	public void startApprove(){
		String functionType = "YSZX"; 
		String nodeName = "SAVE"; 
		String bussinessId = "3A51B99971FE422B8B15DAA6DAAEDC4E"; 
		String applyUser = "epri_mengj"; 
		
		service.startApprove(functionType, nodeName, bussinessId, applyUser);
	}
}