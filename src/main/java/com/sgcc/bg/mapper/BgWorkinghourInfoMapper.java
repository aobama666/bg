package com.sgcc.bg.mapper; import java.util.List; import java.util.Map; import org.springframework.stereotype.Repository;import org.apache.ibatis.annotations.Param; @Repository public interface BgWorkinghourInfoMapper {	 	 /** 		* 修改 		* @param  		* @param  		* @return 		* */ 	 public int updatabgWorkinghourInfo(			 @Param("id")String id,@Param("projectName")String projectName,			 @Param("jobContent")String jobContent,@Param("workingHour")String workingHour			 );	 /**修改	    * @param processId 		* @param 		* @param 		* @return 		* */ 	 public int commitbgWorkinghourInfo(			 @Param("id")String id,			 @Param("projectName")String projectName,			 @Param("jobContent")String jobContent,			 @Param("workingHour")String workingHour,			 @Param("processId")String processId			 );		 /** 		* 动态查询 		* @param  		* @param  		* @return 		* */ 	 public List<Map<String, Object>> selectForbgWorkinghourInfo(			 @Param("id")String id,@Param("StartData")String StartData, @Param("EndData")String EndData,			 @Param("worker")String worker,@Param("category")String category, @Param("projectName")String projectName,			 @Param("status")String status			 );	 	 /** 		* 动态查询		* @param 		* @param 		* @return 		* */ 	 public List<Map<String, Object>> selectForbgWorkinghourInfos(			 @Param("ids")List<String> ids,@Param("StartData")String StartData, @Param("EndData")String EndData,			 @Param("worker")String worker,@Param("category")String category, @Param("projectName")String projectName,			 @Param("status")String status			 );	 	 	 /** 		* 动态查询---时间 		* @param 		* @param 		* @return 		* */ 	 public List<Map<String, Object>> selectForTime(@Param("id")String id,@Param("worker")String worker,@Param("category")String category,@Param("StartData")String StartData, @Param("EndData")String EndData);	 public List<Map<String, Object>> selectCheckTime(@Param("id")String id,@Param("worker")String worker,@Param("category")String category,@Param("StartData")String StartData, @Param("EndData")String EndData);			 	 /** 		* 非工作 项目 		* @param 		* @param 		* @return 		* */ 	 public List<Map<String, Object>> selectForNoCategory(@Param("id")String id,@Param("worker")String worker,@Param("category")String category,@Param("StartData")String StartData, @Param("EndData")String EndData);			 /** 		* 查询----->状态 		* @param  		* @param  		* @return 		* */ 	 public List<Map> selectForKilebgWorkinghourInfo(@Param("id")String id  );	 /** 		* 动态查询 --->开始时间和结束时间		* @param 		* @param 		* @return 		* */ 	 public String selectForStartAndEnd(@Param("worker")String worker,@Param("category")String category,@Param("StartData")String StartData, @Param("EndData")String EndData);	 /** 		* 动态查询 --->开始时间和结束时间		* @param 		* @param 		* @return 		* */ 	 public List<Map> selectForSchedule(@Param("StartData")String StartData, @Param("EndData")String EndData);	 	 /** 		* 删除		* @param 		* @param 		* @return 		* */ 	public int deletebgWorkinghourInfo(@Param("id")String id,@Param("valid")String valid); 	 /** 	* 撤回	* @param processId 	* @param 	* @param 	* @return 	* */ 	public int backbgWorkinghourInfo(@Param("id")String id,@Param("status")String status, @Param("processId")String processId); 		/**	 * 项目工时统计----项目	 * 	 * */	 public List<Map> selectForProjectName(			 @Param("StartData")String StartData, @Param("EndData")String EndData, 			 @Param("projectName")String projectName, @Param("worker")String worker);	 	 public List<Map> selectForProjectNameXQ(			 @Param("StartData")String StartData, @Param("EndData")String EndData,    		 @Param("projectName")String projectName,  @Param("Wbsnumber")String Wbsnumber,    		 @Param("worker")String worker			 			 );	 	 /**		 * 项目工时统计----员工工时细则		 * 		 * */     public List<Map> selectForProjectNameAndWorker(    		 @Param("StartData")String StartData, @Param("EndData")String EndData,    		 @Param("projectName")String projectName, @Param("worker")String worker,    		 @Param("Wbsnumber")String Wbsnumber);     /**		 * 项目工时统计----员工工时细则详情		 * 		 * */     public List<Map> selectForWorker(     		 @Param("StartData")String StartData, @Param("EndData")String EndData,     		 @Param("projectName")String projectName, @Param("worker")String worker,     		 @Param("Wbsnumber")String Wbsnumber ,@Param("hrcode")String hrcode);     /**		 * 组织工时统计 		 * 		 * */     public String selectForDept(     		 @Param("StartData")String StartData, @Param("EndData")String EndData,     		 @Param("deptid")String deptid, @Param("ladid")String ladid,     		 @Param("worker")String worker ,@Param("category")String category,     		 @Param("Nocategory")String Nocategory);     public String selectForDepts(     		 @Param("StartData")String StartData, @Param("EndData")String EndData,     		 @Param("deptid")String deptid,  @Param("ladid")List<String> ladid ,     		 @Param("worker")String worker ,@Param("category")String category,     		 @Param("Nocategory")String Nocategory  );          public List<Map> selectForNumber(     		 @Param("StartData")String StartData, @Param("EndData")String EndData,     		 @Param("deptid")String deptid, @Param("ladid")String ladid,     		 @Param("worker")String worker ,@Param("category")String category,     		 @Param("Nocategory")String Nocategory ,  @Param("showdata")String showdata);               public List<Map<String, Object>> selectFordepts(    		 @Param("pdeptid")String pdeptid , @Param("deptid")String deptid     		      	 );     public List<Map<String, Object>> selectForUser(    		 @Param("pdeptid")String pdeptid , @Param("deptid")String deptid , @Param("useralias")String useralias,  @Param("username")String username     		      	 );     public List<Map<String, Object>> selectForUsers(    		 @Param("pdeptid")String pdeptid , @Param("deptid")List<String> deptid , @Param("useralias")String useralias,  @Param("username")String username     		      	 );     /** 	 * 查询全量组织树 	 * @param rootId 组织ID 	 * @param level 控制显示层级，如 0 显示到院 1 显示到部门 2 显示到科室 	 * @param limit 控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码 	 * @return 	 */ 	List<Map<String, Object>> getAllOrganTree(@Param("rootId")String rootId,@Param("level")String level,@Param("limit")String limit, 			 @Param("pdeptid")String pdeptid , @Param("deptid")String deptid); 	 	 public List<Map<String, Object>> selectForRole(    		 @Param("projectId")String projectId , @Param("hrcode")String hrcode     		      	 ); 	 public List<Map<String, Object>> selectForUserName(    		 @Param("userName")String userName       		      	 ); 	 public List<Map<String, Object>> selectCheckHous(			 @Param("ids")List<String> ids 			 ); 	 public List<Map<String, Object>> selectCheckWorker( 			@Param("userName")String userName   , @Param("workTime")String workTime   			 ); 	}