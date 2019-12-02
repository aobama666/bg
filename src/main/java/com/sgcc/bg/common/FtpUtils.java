package com.sgcc.bg.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.InputStream;

/**
 * Ftp 工具类
 * @author epri-xpjt
 *
 */
public class FtpUtils {

	private final static Log FtpUtilslog=LogFactory.getLog(FtpUtils.class);
	/**
	 * 国际化上传文件Ftp路径
	 */
	public static String InternationUploadPath = "/accessory/internation/";
	/**
	 * 院长信箱传文件Ftp路径
	 */
	public static String DeabmailboxUploadPath = "/accessory/deanmailbox/";
	
	/**
	 * 调查研究项目
	 */
	public static String SurveyUploadPath = "/accessory/survey/";

	/**
	 * 通知公告
	 */
	public static String NoticeUploadPath = "/accessory/notice/";
	
	/**
	 * 评标专家
	 */
	public static String EvaluationUploadPath = "/accessory/evaluation/";

	/**
	 * 优秀论文评审论文附件
	 */
	public static String PaperUploadPath = "/accessory/lunwen/";

	/**
	 * 用印管理附件
	 */
	public static String UseSealStuffPath = "/accessory/yygl/";
	
	/**
	 * 微信图片上传路径
	 */
	public static String WeChatCoverReverseUploadPath = "/cover/wechat/reverse/";

	/**
	 * 微信图片上传路径
	 */
	public static String WeChatCoverUploadPath = "/cover/wechat/";

	/**
	 * 临时文件上传路径
	 */
	public static String TempUploadPath = "/temp/";
	
	/**
	 * 报工临时文件上传路径
	 */
	public static String BgTempUploadPath = "/temp/bg/";
	
	/**
	 * 模板路径
	 */
	public static String templatePath = "/accessory/template/";

	/**
	 * 微信运维反馈路径
	 */
	public static String wechatSupportPath = "/wechatSupport/";

	public static FtpHelper getFtpHelper() {
		String ftpServer = ConfigUtils.getConfig("ftpServer");
		int ftpPort = Rtext.ToInteger(ConfigUtils.getConfig("ftpPort"), 0);
		String ftpUsername = ConfigUtils.getConfig("FtpUsername");
		String ftpPassword = ConfigUtils.getConfig("FtpPassWord");
		FtpHelper ftp = new FtpHelper(ftpServer, ftpPort, ftpUsername,
				ftpPassword);
		FtpUtilslog.info("ftp 准备连接： "+ftpServer+"/"+ftpPort+"/"+ftpUsername+"/"+ftpPassword);
		return ftp;
	}

	/**
	 * 上传文件到Ftp
	 * 
	 * @param file
	 * @param path
	 */
	public static void uploadFile(File file, String path) {
		if (null == file) {
			FtpUtilslog.error("文件不能为空！");
			return;
		}
		if (Rtext.isEmpty(path)) {
			FtpUtilslog.error("上传路径不能为空！");
			return;
		}
		FtpHelper ftp = getFtpHelper();
		if (!path.contains(".")) {
			path += file.getName();
		}
		try {
			ftp.uplodeFile(file, path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.disconnect();
		}

	}

	/**
	 * 上传文件到Ftp
	 * 
	 * @param file
	 * @param path
	 */
	public static void uploadFile(InputStream in, String path) {
		if (null == in) {
			FtpUtilslog.error("文件不能为空！");
			return;
		}
		if (Rtext.isEmpty(path)) {
			FtpUtilslog.error("上传路径不能为空！");
			return;
		}
		FtpHelper ftp = getFtpHelper();
		try {
			ftp.uplodeFile(in, path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.disconnect();
		}

	}

	/**
	 * 刪除Ftp文件
	 */
	public static void deleteFile(String path) {
		if (Rtext.isEmpty(path)) {
			FtpUtilslog.error("上传路径不能为空！");
			return;
		}

		FtpHelper ftp = getFtpHelper();
		try {
			ftp.deleteFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.disconnect();
		}

	}
	
	/**
	 * 删除指定文件夹下的所有文件
	 * @param dirName 路径
	 * @param deleteDir 是否删除文件夹 true 是 false 否
	 */
	public static void deleteAll(String path,boolean deleteDir) {
		if (Rtext.isEmpty(path)) {
			FtpUtilslog.error("路径不能为空！");
			return;
		}
		FtpHelper ftp = getFtpHelper();
		try {
			ftp.deleteAll(path,deleteDir);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.disconnect();
		}

	}
	

	/**
	 * 读取文件
	 * 
	 * @param path
	 * @return
	 */
	public static InputStream readFile(String path) {
		if (Rtext.isEmpty(path)) {
			FtpUtilslog.error("上传路径不能为空！");
			return null;
		}

		FtpHelper ftp = getFtpHelper();
		InputStream in = null;
		try {
			in = ftp.downloadFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.disconnect();
		}
		return in;
	}

/*	*//**
	 * 批量读取ftp文件并压缩
	 * 
	 * @param paths
	 * @return
	 * @throws Exception
	 *//*
	public static InputStream readBatchFile(Map<String, String> files)
			throws Exception {
		ZipHelper zipHelper = new ZipHelper();
		Set<String> keys = files.keySet();
		for (String path : keys) {
			InputStream in = readFile(path);
			if (in != null) {
				zipHelper.InsertFile(files.get(path), in);
			}
		}
		return zipHelper.getInputStream();
	}*/

	public static String getFileName(String path) {
		String name = null;
		if (!Rtext.isEmpty(path)) {
			int n = path.lastIndexOf("/");
			if (n > 0) {
				name = path.substring(n + 1);
			}
		}
		return name;
	}
	
	/**
	 * 得到ftp模块路径
	 * @param module
	 * @return
	 */
	public static String getFtpModulePath(String module) {
		if ("internation".equalsIgnoreCase(module)) {
			return FtpUtils.InternationUploadPath;
		} else if ("deanmailbox".equalsIgnoreCase(module)) {
			return FtpUtils.DeabmailboxUploadPath;
		} else if ("notice".equalsIgnoreCase(module)) {
			return FtpUtils.NoticeUploadPath;
		} else if ("wechat".equalsIgnoreCase(module)) {
			return FtpUtils.WeChatCoverUploadPath;
		} else if ("wechat_reverse".equalsIgnoreCase(module)) {
			return FtpUtils.WeChatCoverReverseUploadPath;
		} else if("evaluation".equalsIgnoreCase(module)){
			return FtpUtils.EvaluationUploadPath;
		} else if("survey".equalsIgnoreCase(module)){
			return FtpUtils.SurveyUploadPath;
		}
		else {
			return FtpUtils.TempUploadPath;
		}

	}

}
