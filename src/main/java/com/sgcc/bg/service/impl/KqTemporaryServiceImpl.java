package com.sgcc.bg.service.impl;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sgcc.bg.common.*;
import com.sgcc.bg.mapper.BgKqTemporaryMapper;
import com.sgcc.bg.model.Temporary;
import com.sgcc.bg.service.KqTemporaryService;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KqTemporaryServiceImpl implements KqTemporaryService {


	@Autowired
	private BgKqTemporaryMapper kqTemporaryMapper;

	private static org.slf4j.Logger Logger = LoggerFactory.getLogger(KqTemporaryServiceImpl.class);
	@Override
	public String  addTemporary(String beginDate, String endDate) {
		Gson   gson=new Gson();
		Date  date=new Date();
        Map<String, String> map = new HashMap<>();
		Logger.info("############# 调用:http://10.85.61.208:28233/zhywSi/WorkTimeService/getWorkTime接口 ##############");
		Logger.info("############# 参数:beginDate"+ beginDate +"endDate"+endDate+"##############");
		String    path="http://10.85.61.208:28233/zhywSi/WorkTimeService/getWorkTime";
		String    data="beginDate="+beginDate+"&endDate="+endDate+"&key=f91f33c46f6f49a7be2d7012fef9ac57";
		String    kqDate=  HttpUtil.interfaceUtil(path,data);
		Logger.info("############# 返回值 ##############");
		Map<String ,String>  kqDataMap=gson.fromJson(kqDate,Map.class);
		String     result=kqDataMap.get("result");
		String     message=kqDataMap.get("message");
		Logger.info("############# 返回值 result："+result+"##############");
		Logger.info("############# 返回值 message："+message+"##############");
		if("success".equals(result)){
			List<Map<String ,String>>  list=gson.fromJson(gson.toJson(kqDataMap.get("data")),List.class);
			if(!list.isEmpty()){
					String oncecode = Rtext.getUUID();
					List<Map<String ,String>>	temporaryList=kqTemporaryMapper.selectForAll();
                    if(temporaryList.isEmpty()){
						Logger.info("############# 临时表无数据##############");
						try {
							Logger.info("############# 临时表数据的录入开始##############");
							addTemporary(list, oncecode, date);
							Logger.info("############# 临时表数据的录入结束##############");
						}catch (Exception e1) {  //数据进入临时表异常处理机制
							Logger.info("############# 临时表录入数据异常，删除该批次是数据##############");
							kqTemporaryMapper.deleteByOneCode(oncecode);
							e1.printStackTrace();
						}
					}else {
						Logger.info("############# 临时表有数据##############");
						String	onceCode=temporaryList.get(1).get("ONCECODE");
						try {
						Logger.info("############# 临时表数据的录入开始##############");
						kqTemporaryMapper.updataForStatus("0",onceCode);
						addTemporary(list,oncecode,date);
						Logger.info("############# 临时表数据的录入结束##############");
						}catch (Exception e1) {  //数据进入临时表异常处理机制
							Logger.info("############# 临时表录入数据异常，还原上批次的数据##############");
							kqTemporaryMapper.updataForStatus("1",onceCode);
							e1.printStackTrace();
						}
						Logger.info("############# 临时表删除上一批次是数据##############");
						kqTemporaryMapper.deleteByOneCode(onceCode);
					}
                Logger.info("############# 查询临时表最新的数据开始##############");
				List<Map<String ,String>>	historyLists=kqTemporaryMapper.selectForAll();
                Logger.info("############# 查询临时表最新的数据结束##############");
                Logger.info("############# 最新临时表数据录入历史记录表开始##############");
                String  res=addHistoryInfo(historyLists,date);
                Logger.info("############# 最新临时表数据录入历史记录表结束##############");
                if("200".equals(res)){
                    Logger.info("############# 最新临时表数据录入业务表开始##############");
					String   kqRes=addKqEmpInfo(historyLists,date,beginDate,endDate);
                    Logger.info("############# 最新临时表数据录入业务表开始##############");
					if(kqRes.equals("200")){
                        Logger.info("############# 数据同步结束  ##############");
                        map.put("msg","数据同步成功");
                        map.put("code","200");
                        return JSON.toJSONString(map);
                    }else {
                        map.put("msg","数据录入业务表有误，请联系管理员");
                        map.put("code","201");
                        return JSON.toJSONString(map);
                    }
				}else {
                    map.put("msg","数据录入历史记录表有误，请联系管理员");
                    map.put("code","201");
                    return JSON.toJSONString(map);
                }
			}else {
                map.put("msg","该时间无数据，请稍后同步！");
                map.put("code","201");
                return JSON.toJSONString(map);
			}
		}else {
            Logger.info("############# 返回值 message："+message+"##############");
            map.put("msg",message);
            map.put("code","201");
            return JSON.toJSONString(map);
		}


	}
	/**
	 * 添加考勤临时表数据录入
	 * @param list ,oncecode,date
	 * @return
	 */
    public   void    addTemporary(List<Map<String,String>> list,String oncecode,Date date){
		for(Map<String,String>  map:list){
			String   empCode=map.get("empCode");
			String   empName=map.get("empName");
			String   fullTime=map.get("fullTime");
			String   overTime=map.get("overTime");
			String   beginDates=map.get("beginDate");
			String   endDates=map.get("endDate");
			Temporary  temporary=new Temporary();
			temporary.setEmpCode(empCode);
			temporary.setEmpName(empName);
			temporary.setFullTime(fullTime);
			temporary.setOverTime(overTime);
			temporary.setBeginDate(beginDates);
			temporary.setEndDate(endDates);
			temporary.setOnceCode(oncecode);
			temporary.setStatus("1");
			temporary.setCreateTime(date);
			kqTemporaryMapper.addTemporary(temporary);
		}
	}
	/**
	 * 添加考勤数据进入历史记录表
	 * @param historyList ,oncecode,date
	 * @return
	 */
	public   String    addHistoryInfo(List<Map<String ,String>> historyList,Date date ){
		String   res="";
		try {
			for(Map<String,String>  map:historyList){
				String empCode = map.get("EMP_CODE")==null?"":map.get("EMP_CODE").toString();
				String empName = map.get("EMP_NAME")==null?"":map.get("EMP_NAME").toString();
				String fullTime = map.get("FULL_TIME")==null?"":map.get("FULL_TIME").toString();
				String overTime = map.get("OVER_TIME")==null?"":map.get("OVER_TIME").toString();
				String beginDate = map.get("BEGIN_DATE")==null?"":map.get("BEGIN_DATE").toString();
				String endDate = map.get("END_DATE")==null?"":map.get("END_DATE").toString();
				Temporary  temporary=new Temporary();
				temporary.setEmpCode(empCode);
				temporary.setEmpName(empName);
				temporary.setFullTime(fullTime);
				temporary.setOverTime(overTime);
				temporary.setBeginDate(beginDate);
				temporary.setEndDate(endDate);
				temporary.setCreateTime(date);
				kqTemporaryMapper.addHistoryInfo(temporary);
			}
		  res="200";
		}catch (Exception e1) {  //数据进入历史表异常处理机制
			Logger.info("############# 历史表录入数据异常，删除该批次的数据##############");
			kqTemporaryMapper.deleteHistoryByOneCode(date);
			res="201";
			e1.printStackTrace();
		}
	    return res;
	}

    public   String    addKqEmpInfo(List<Map<String ,String>> historyList,Date date,String begindate,String enddate ){
        String   res="";
        try {
			kqTemporaryMapper.updataKqEmpForDate("0","1",begindate,enddate);
            for(Map<String,String>  map:historyList){
				String onceCode = Rtext.getUUID();
                String empCode = map.get("EMP_CODE")==null?"":map.get("EMP_CODE").toString();
                String empName = map.get("EMP_NAME")==null?"":map.get("EMP_NAME").toString();
                String fullTime = map.get("FULL_TIME")==null?"":map.get("FULL_TIME").toString();
                String overTime = map.get("OVER_TIME")==null?"":map.get("OVER_TIME").toString();
                String beginDate = map.get("BEGIN_DATE")==null?"":map.get("BEGIN_DATE").toString();
                String endDate = map.get("END_DATE")==null?"":map.get("END_DATE").toString();
                Temporary  temporary=new Temporary();
                temporary.setEmpCode(empCode);
                temporary.setEmpName(empName);
                temporary.setFullTime(fullTime);
                temporary.setOverTime(overTime);
                temporary.setBeginDate(beginDate);
                temporary.setEndDate(endDate);
                temporary.setCreateTime(date);
				temporary.setOnceCode(onceCode);
				temporary.setStatus("1");
                kqTemporaryMapper.addKqEmpInfo(temporary);
            }
            res="200";
        }catch (Exception e1) {  //数据进入业务表异常处理机制
            Logger.info("############# 业务表录入数据异常，删除该批次的数据##############");
            kqTemporaryMapper.updataKqEmpForDate("1","0",begindate,enddate);
            res="201";
            e1.printStackTrace();
        }
        if(res.equals("200")){
            kqTemporaryMapper.deleteKqEmpByDate("0",begindate,enddate);
        }else {
            kqTemporaryMapper.deleteKqEmpByDate("1",begindate,enddate);
        }
        return res;
    }
	@Override
	public String selectForKqInfo(HttpServletRequest request) {
		String beginDate = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
		String endDate = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
		String empName = request.getParameter("empName" ) == null ? "" : request.getParameter("empName").toString(); //类型
		int page=Integer.parseInt(request.getParameter("page"));
		int limit=Integer.parseInt(request.getParameter("limit"));
		Page<?> page2 = PageHelper.startPage(page, limit);
		List<String>  idslist =new  ArrayList<String>();
		kqTemporaryMapper.selectForKqInfo(beginDate,endDate,empName,idslist);
		long total = page2.getTotal();
		List<Map<String, String>> list = (List<Map<String, String>>) page2.getResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("items", list);
		map.put("totalCount", total);
		String jsonStr= JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}

	@Override
	public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
		String beginDate = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
		String endDate = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
		String empName = request.getParameter("empName" ) == null ? "" : request.getParameter("empName").toString(); //类型
		String idsStr = request.getParameter("selectList")==null?"":request.getParameter("selectList").trim();

		List<String>  idslist =new  ArrayList<String>();
		if(idsStr!=""){
			String [] strings=idsStr.split(",");
			for(int i=0;i<strings.length;i++){
				String num=strings[i];
				idslist.add(num);
			}
		}
		//获取Excel数据信息
		List<Map<String, Object>> valueList = new ArrayList<Map<String,Object>>();
		valueList=kqTemporaryMapper.selectForKqInfo(beginDate,endDate,empName,idslist);
		Object[][] title = {
				{ "员工姓名", "EMP_NAME" },
				{ "项目编号","EMP_CODE"},
				{ "开始时间","BEGIN_DATE"},
				{ "结束时间", "END_DATE" },
				{ "全勤时长(天)","FULL_TIME"},
				{ "加班时长(天)","OVER_TIME"},
				{ "同步时间","CREATE_TIME"}
		};

		ExportExcelHelper.getExcel(response, "考勤报表-"+ DateUtil.getDays(), title, valueList, "normal");
		return "";
	}

}
