package com.sgcc.bg.common;

import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class FileDownloadUtil {
	
	
	/**
	 * 通过ftp进行下载
	 * @param response
	 * @param filePath
	 * @param fileName
	 * @param fileSize
	 */
	public static void toFtpFileDownLoad(HttpServletResponse response, String filePath,
			String fileName, String fileSize) {
		FtpHelper ftp = ftpConnect();
		try {
			 //获取路径
			 if("/".equals(filePath.charAt(0))) filePath=filePath.substring(1);
			 InputStream is = ftp.downloadFile(FtpUtils.InternationUploadPath+filePath);
			ftp.disconnect();
			//tomcat没问题但是换成jetty后发现无法读取fileSize(格式为12.kb)
			String wordMergePath = ConfigUtils.getConfig("wordmerge_path");
			String intactPath=wordMergePath+File.separator+"linshiwenjian"+File.separator+fileName;
			System.out.println("FILE:"+intactPath);
			File file=new File(intactPath);
			if(!file.getParentFile().exists())file.getParentFile().mkdir();
			FileUtils.copyInputStreamToFile(is, file);
			fileDownload(response,file.getAbsolutePath(),fileName);
			
			
			//反馈前端
			
			// 设置响应头
			//response.reset();
			//设置文件下载显示的文件名
			//response.setHeader("Content-Disposition", "attachment; filename=\"" +new String(fileName.getBytes("utf-8"), "iso-8859-1")+ "\"");
			/*response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso-8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			//response.setContentType("application/octet-stream;charset=UTF-8");
			byte[] buffer ;
			try {
				buffer =new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) > 0) {
					response.getOutputStream().write(buffer, 0, len);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public  static FtpHelper ftpConnect() {
		//获取 ftp相关配置
		String ftpServer = ConfigUtils.getConfig("ftpServer");
		String ftpPort = ConfigUtils.getConfig("ftpPort");
		String ftpUsername = ConfigUtils.getConfig("FtpUsername");
		String ftpPassWord = ConfigUtils.getConfig("FtpPassWord");
		//打开连接
		FtpHelper ftp = new FtpHelper(ftpServer,Integer.parseInt(ftpPort),ftpUsername,ftpPassWord);
		return ftp;
	}
	
	/**
	 * 文件下载
	 * @param response
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public static void fileDownload(final HttpServletResponse response, String filePath, String fileName) throws Exception {
		byte[] buffer = new byte[1024];
		//response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("utf-8"), "iso-8859-1") + "\"");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso-8859-1"));
		OutputStream ros = response.getOutputStream();
		InputStream fis = new FileInputStream(new File(filePath));
		response.addHeader("Content-Length", "" + fis.available());
		response.setContentType("application/octet-stream;charset=UTF-8");
		try {
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				ros.write(buffer, 0, len);
			}
		} finally {
			response.flushBuffer();
			ros.close();
			fis.close();
		}
	}
	
	/**
	 * 文件下载
	 * @param response
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public static void fileDownloadFromFtp(
			final HttpServletResponse response,
			final HttpServletRequest request,
			String filePath, 
			String fileName) throws Exception {
		BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
        	response.reset();
        	response.setContentType("application/x-download");
        	response.setCharacterEncoding("UTF-8"); 
        	String userAgent = request.getHeader("user-agent").toLowerCase();  
            if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {  
                // win10 ie edge 浏览器 和其他系统的ie  
            	fileName = URLEncoder.encode(fileName, "UTF-8");  
            } else {  
                // fe  
            	fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");  
            }  
        	response.setHeader("Content-Disposition", "attachment;filename=" +fileName);
            bis = new BufferedInputStream(FtpUtils.readFile(FtpUtils.BgTempUploadPath+filePath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
      //删除已下载文件
        //TODO
  	  //FtpUtils.deleteFile(FtpUtils.BgTempUploadPath+fileName);
	}




	/**
	 * 文件下载————————论文附件使用，避免楼上方法的路径不同和ftp删除后患
	 * @param response
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public static void fileDownloadFromFtpLwAnnex(
			final HttpServletResponse response,
			final HttpServletRequest request,
			String filePath,
			String fileName) throws Exception {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			response.reset();
			response.setContentType("application/x-download");
			response.setCharacterEncoding("UTF-8");
			String userAgent = request.getHeader("user-agent").toLowerCase();
			if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {
				// win10 ie edge 浏览器 和其他系统的ie
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
				// fe
				fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
			}
			response.setHeader("Content-Disposition", "attachment;filename=" +fileName);
			bis = new BufferedInputStream(FtpUtils.readFile(filePath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
