package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganStuffTreeNewMapper {
	/**
	 * 查询全量组织树
	 * @param rootId 组织ID
	 * @param level 控制显示层级，如 0 显示到院 1 显示到部门 2 显示到科室
	 * @param limit 权限控制:控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码
	 * @param funcType 权限控制:limit=yes 功能类型   YYGL BGGL
	 * @param dataSrc 数据来源：type=RLZY 人资专用 SRC = 1，其他为报工默认 SRC = 0 或 2
	 * @param userName 权限控制:登录账号
	 * @param 显示控制：show=PART 部分显示   默认为所有
	 * @return
	 */
	List<Map<String, Object>> getAllOrganTree(@Param("rootId")String rootId,@Param("level")String level,@Param("limit")String limit,
											  @Param("funcType")String funcType,@Param("dataSrc")String dataSrc,@Param("userName")String userName,
											  @Param("show")String show);
	
	/**
	 * 初始化人员树的组织，不包含人员
	 * @param rootId 组织ID
	 * @param level 控制显示层级，如 0 显示到院 1 显示到部门 2 显示到科室
	 * @param limit 权限控制:控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码
	 * @param funcType 权限控制:limit=yes 功能类型   YYGL BGGL
	 * @param dataSrc 数据来源：type=RLZY 人资专用 SRC = 1，其他为报工默认 SRC = 0 或 2
	 * @param userName 权限控制:登录账号
	 * @param 显示控制：show=PART 部分显示   默认为所有
	 * @return
	 */
	List<Map<String, Object>> initUserTree(@Param("rootId")String rootId,@Param("level")String level,@Param("limit")String limit,
										   @Param("funcType")String funcType,@Param("dataSrc")String dataSrc,@Param("userName")String userName,
										   @Param("show")String show);
	
	/**
	 * 根据组织ID（末节点），查询组织人员列表
	 * @param organId 组织ID
	 * @param level 控制显示层级，如 0 显示到院 1 显示到部门 2 显示到科室
	 * @param limit 权限控制:控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码
	 * @param funcType 权限控制:limit=yes 功能类型   YYGL BGGL
	 * @param dataSrc 数据来源：type=RLZY 人资专用 SRC = 1，其他为报工默认 SRC = 0 或 2
	 * @param userName 权限控制:登录账号
	 * @param 显示控制：show=PART 部分显示   默认为所有
	 * @return
	 */
	List<Map<String, Object>> getUserTreeByOrgan(@Param("organId")String organId,@Param("level")String level,@Param("limit")String limit,
											     @Param("funcType")String funcType,@Param("dataSrc")String dataSrc,@Param("userName")String userName,
											     @Param("show")String show);
	
	/**
	 * 根据组织ID,人员编号，人员姓名，模糊查询人员列表
	 * @param rootId  组织ID
	 * @param empCode 人员编号
	 * @param empName 人员姓名
	 * @param level 控制显示层级，如 0 显示到院 1 显示到部门 2 显示到科室
	 * @param limit 权限控制:控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码
	 * @param funcType 权限控制:limit=yes 功能类型   YYGL BGGL
	 * @param dataSrc 数据来源：type=RLZY 人资专用 SRC = 1，其他为报工默认 SRC = 0 或 2
	 * @param userName 权限控制:登录账号
	 * @param 显示控制：show=PART 部分显示   默认为所有
	 * @return
	 */
	List<Map<String, Object>> getUserTreeByUser(@Param("rootId")String rootId,@Param("empCode")String empCode,@Param("empName")String empName,
											 	@Param("level")String level,@Param("limit")String limit,
											 	@Param("funcType")String funcType,@Param("dataSrc")String dataSrc,@Param("userName")String userName,
											 	@Param("show")String show);
	
	
	
	
	/**
	 * 演示中心专用-----根据组织ID（末节点），查询组织人员列表
	 * @param organId 组织ID
	 * @return
	 */
	List<Map<String, Object>> getyszxForUserTreeByOrgan(@Param("organId")String organId);
	
	/**
	 * 当用户时部门专责 或 处室专责时，获取部门列表
	 * @param rootId 组织ID
	 * @param deptPriv 部门id列表  'xx1','xx2'
	 * @param labPriv 处室id列表  'xx1','xx2'
	 * @return
	 */
	List<Map<String, Object>> getUserOrganPrivilege(@Param("rootId")String rootId,@Param("deptPriv")String deptPriv,@Param("labPriv")String labPriv);

}
