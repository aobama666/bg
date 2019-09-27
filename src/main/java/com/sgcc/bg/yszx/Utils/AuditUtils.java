package com.sgcc.bg.yszx.Utils;

import java.util.HashMap;
import java.util.Map;

public class AuditUtils {
	private Map getPublishData(long processInstId,long workitemid,String username,long nextActInstID,String processName,String actDefName){
		Map map = new HashMap();
//		map.put("flowid", "tygl_bpm_demo");
//		map.put("taskid", Rtext.toString(processInstId));
//		map.put("precessid",Rtext.toString(workitemid));
//		map.put("userid", username);
//		map.put("contentType", "2");
//		map.put("content",
//				"../../../bpmdemo/rest/demo/send?dmwfid=" + workitemid+"&processInstId="+processInstId+"&actid="+Rtext.toString(nextActInstID));
//		map.put("auditCatalog", "BPM测试");
//		map.put("auditTitle", "BPM测试-"+processName+"-"+actDefName);
//		map.put("remarkFlag", "0");
//		map.put("auditOrigin", "tygl");
//		map.put("key", "DOTRl5HgPHQ2iz2iCy");
//		map.put("operate", "insertTask");
//		bpmdemoControllerlog.info("获取mq所需的待办Map完成，数据为："+Rtext.toGson(map));
		return map;
	}
}
