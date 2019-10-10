var recommonedCommon = {
		/**
		 * 验证选择项目数量
		 * @returns {Boolean}
		 */
		validCheckedItems:function(dataArr){
			var checkedItems = dataGrid.getCheckedItems(dataArr);//获取选择项的数据
			if(checkedItems.length==0){
				messager.tip("请选择需要修改的项目！");
				return false;
			}else if(checkedItems.length>1){
				messager.tip("每次只能选择一条项目进行修改！");
				return false;
			}
			return true;
		},
		/**
		 * 推荐环节的修改功能
		 * @param awardId
		 * @param prjStatus
		 * @param achType
		 */
		getToTYGLPTUrl:function(awardId,prjStatus,achType){
//			return "/TYGLPT/ky_reward/reRecommend/newtyglToKyLoad/index.jsp?awardId="+awardId+"&prjStatus="+prjStatus+"&achType="+achType;
			return "/newtygl/rewardApply/applyForm?type="+achType+"&awardId="+awardId+"&fromDetail="+fromDetail.JLTJ;
		},
		/**
		 * 打开项目明细弹框明细页面
		 * @param awardId
		 * @param prjStatus
		 * @param prjName
		 * @param achType
		 * @param detailType
		 * @param callBackFunc
		 */
		openViewToKy:function(awardId,prjStatus,prjName,achType,detailType,callBackFunc){
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:42px;">项目明细</h4>',
				area:['90%','90%'],
				fixed:false,//不固定
				maxmin:true,
//				content:'/TYGLPT/ky_reward/reRecommend/newtyglToKyRead/index.jsp?awardId='+awardId+'&achType='+achType+'&prjStatus='+prjStatus+'&prjName='+prjName+'&fromDetail='+detailType,
				content:'/newtygl/recommoned/projectDetail?awardId='+awardId+'&type='+achType+'&fromDetail='+detailType,
				end:callBackFunc
			});
		},
		/**
		 * 奖励推荐 形式审查  影响力评定环节提交时验证项目信息
		 * @param p_items		项目数据  包含PRJ_STATUS/EXAMIN_STATUS  AWARD_ID/PAPER_ID/...  RESULT_NAME/PAPER_NAME/...
		 * @param p_subOrRefuse submit/refuse
		 * @param p_fromDetail  B/D/Q
		 * @param p_achType		10003102/3/4/5/6/7
		 * @param callBackFunc  回调函数
		 * @returns {Boolean}
		 */
		validSubmitInfo:function(p_items,p_subOrRefuse,p_fromDetail,p_achType,callBackFunc){
			var paramsData = {};
			paramsData.ITEMS = p_items;
			paramsData.SUBMIT_REFUSE = p_subOrRefuse;
			paramsData.STATUS_TYPE = p_fromDetail;
			paramsData.ACH_TYPE = p_achType;
			var validResult = true;
			$.ajax({
				type:"post",
				url:"/newtygl/recommoned/validPrjInfo",
				cache:false,
				async:false,//同步
				dataType:"json",
				contentType: 'application/json',
		        data:JSON.stringify(paramsData),
				success:function(p_context){
					var result = p_context.RESULT;
					var info = p_context.INFO;
					if(result=="REFRESH"){//强制刷新
						alert(info);
						callBackFunc();
						validResult = false;
					}else if(result=="FALSE"){
						alert(info);
						validResult = false;
					}
				}
			});
			return validResult;
		},
		/**
		 * 提交或者退回数据  修改项目状态
		 * @param p_items
		 * @param p_subOrRefuse
		 * @param p_fromDetail
		 * @param p_achType
		 * @param callBack
		 */
		submitOrRefuse:function(p_items,p_subOrRefuse,p_fromDetail,p_achType,callBack){
			var paramsData = {};
			paramsData.ITEMS = p_items;
			paramsData.SUBMIT_REFUSE = p_subOrRefuse;
			paramsData.FROM_DETAIL = p_fromDetail;
			paramsData.ACH_TYPE = p_achType;
			$.ajax({
				type:"post",
				url:"/newtygl/recommoned/changeItemsStatus",
				cache:false,
				dataType:"text",
				contentType: 'application/json',
		        data:JSON.stringify(paramsData),
				success:function(p_context){
					if(p_context=="SUCCESS"){
						alert("操作成功");
					}else{
						alert("操作出错，请联系管理员！");
					}
					callBack();
				}
			});
		}
		
};

var netVote = {
		/**
		 * 验证选择项目数量
		 * @returns {Boolean}
		 */
		validCheckedItems:function(dataArr){
			var checkedItems = dataGrid.getCheckedItems(dataArr);//获取选择项的数据
			if(checkedItems.length==0){
				messager.tip("请选择需要提交的项目！");
				return false;
			}
			return true;
		}
};
