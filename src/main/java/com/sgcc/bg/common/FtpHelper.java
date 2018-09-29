package com.sgcc.bg.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.FileTransferOutputStream;

/**
 * Ftp 工具类
 * 
 * @author admin
 *
 */
public class FtpHelper {

	private FileTransferClient ftp;

	public FtpHelper() {

	}

	/**
	 * 初始化Ftp信息
	 * 
	 * @param ftpServer
	 *            ftp服务器地址
	 * @param ftpPort
	 *            Ftp端口号
	 * @param ftpUsername
	 *            ftp 用户名
	 * @param ftpPassword
	 *            ftp 密码
	 */
	public FtpHelper(String ftpServer, int ftpPort, String ftpUsername,
			String ftpPassword) {
		connect(ftpServer, ftpPort, ftpUsername, ftpPassword);
	}

	/**
	 * 连接到ftp
	 * 
	 * @param ftpServer
	 *            ftp服务器地址
	 * @param ftpPort
	 *            Ftp端口号
	 * @param ftpUsername
	 *            ftp 用户名
	 * @param ftpPassword
	 *            ftp 密码
	 */
	public void connect(String ftpServer, int ftpPort, String ftpUsername,
			String ftpPassword) {
		ftp = new FileTransferClient();
		if("erp".equalsIgnoreCase(ftpUsername)){
			ftp.getAdvancedSettings().setControlEncoding("gbk");
		}else{
			ftp.getAdvancedSettings().setControlEncoding("utf-8");
		}

		try {
			ftp.setRemoteHost(ftpServer);
			ftp.setRemotePort(ftpPort);
			ftp.setUserName(ftpUsername);
			ftp.setPassword(ftpPassword);

			ftp.connect();

		} catch (Exception e) {
			e.printStackTrace();
			ftp = null;
		}
	}

	/**
	 * 更改ftp路径
	 * @param ftp
	 * @param dirName
	 * @return
	 */
	public boolean checkDirectory(FileTransferClient ftp, String dirName) {
		boolean _flag = false;
		try {
			FTPFile[] files = ftp.directoryList();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDir()) {
					if (dirName.equalsIgnoreCase(files[i].getName())) {
						_flag = true;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		return _flag;
	}

	/**
	 * 断开ftp链接
	 */
	public void disconnect() {
		try {
			if (ftp.isConnected()) {
				ftp.disconnect();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 读取ftp文件流
	 * @param filePath ftp文件路径
	 * @return
	 * @throws Exception
	 */
	public InputStream downloadFile(String filePath) throws Exception {
		InputStream inputStream = null;

		String fileName = "";
		filePath = StringUtils.removeStart(filePath, "/");
		int len = filePath.lastIndexOf("/");
		if (len == -1) {
			if (filePath.length() > 0) {
				fileName = filePath;
			} else {
				throw new Exception("没有输入文件路径");
			}
		} else {
			fileName = filePath.substring(len + 1);

			String type = filePath.substring(0, len);
			String[] typeArray = type.split("/");
			for (int i = 0; i < typeArray.length; i++) {
				ftp.setContentType(FTPTransferType.BINARY);
				ftp.changeDirectory(typeArray[i]);
			}
		}

		byte[] data = ftp.downloadByteArray(fileName);
		inputStream = new ByteArrayInputStream(data);
		return inputStream;
	}

	/**
	 * 上传文件到ftp
	 * 
	 * @param file
	 *            文件对象
	 * @param filePath
	 *            上传的路径
	 * @throws Exception
	 */
	public void uplodeFile(File file, String filePath) throws Exception {
		InputStream inStream = new FileInputStream(file);
		uplodeFile(inStream, filePath);
	}

	/**
	 * 上传文件到ftp
	 * 
	 * @param inStream
	 *            上传的文件流
	 * @param filePath
	 *            上传路径
	 * @throws Exception
	 */
	public void uplodeFile(InputStream inStream, String filePath)
			throws Exception {
		if (inStream == null) {
			return;
		}

		String fileName = "";
		filePath = StringUtils.removeStart(filePath, "/");
		int len = filePath.lastIndexOf("/");
		if (len == -1) {
			if (filePath.length() > 0) {
				fileName = filePath;
			} else {
				throw new Exception("没有输入文件路径");
			}
		} else {
			fileName = filePath.substring(len + 1);

			String type = filePath.substring(0, len);
			String[] typeArray = type.split("/");
			for (int i = 0; i < typeArray.length; i++) {
				if (!checkDirectory(ftp, typeArray[i]))
					ftp.createDirectory(typeArray[i]);
				ftp.changeDirectory(typeArray[i]);
			}
		}

		FileTransferOutputStream os = ftp.uploadStream(fileName);
		byte[] bytes = new byte[1024];
		int c;
		while ((c = inStream.read(bytes)) != -1) {
			os.write(bytes, 0, c);
		}
		inStream.close();
		os.close();
		inStream = null;
		os = null;
	}

	/**
	 * 删除ftp文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @throws Exception
	 */
	public void deleteFile(String filePath) throws Exception {
		String fileName = "";
		filePath = StringUtils.removeStart(filePath, "/");
		int len = filePath.lastIndexOf("/");
		if (len == -1) {
			if (filePath.length() > 0) {
				fileName = filePath;
			} else {
				throw new Exception("没有输入文件路径");
			}
		} else {
			fileName = filePath.substring(len + 1);

			String type = filePath.substring(0, len);
			String[] typeArray = type.split("/");
			for (int i = 0; i < typeArray.length; i++) {
				if (checkDirectory(ftp, typeArray[i])) {
					ftp.changeDirectory(typeArray[i]);
				}
			}
		}

		ftp.deleteFile(fileName);
	}
	
	/**
	 * 删除指定文件夹下的所有文件
	 * @param dirName 路径
	 * @param deleteDir 是否删除文件夹 true 是
	 */
	public void deleteAll(String dirName,boolean deleteDir){
		dirName = StringUtils.removeStart(dirName, "/");
		dirName = StringUtils.removeEnd(dirName, "/");
		try {
			ftp.changeDirectory(dirName);
			FTPFile[] files= ftp.directoryList();
			for (FTPFile ftpFile : files) {
				if (ftpFile.isDir()) {
					String curPath=dirName+"/"+ftpFile.getName();
					deleteAll(curPath,deleteDir);
				}else{
					ftp.deleteFile(ftpFile.getName());
				}
			}
			//删除该文件夹
			if(deleteDir){
				ftp.deleteDirectory(dirName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		try {
			// 上传文件到ftp
			//FtpHelper ftp = new FtpHelper("10.85.60.50", 21, "tygl", "1");
			//System.out.println(ftp);
			/*File file = new File("d:\\data\\1.xls");
			ftp.uplodeFile(file, "aa/bb/aa.xls");
			ftp.disconnect();*/

			// 从ftp下载文件
			FtpHelper ftp = new FtpHelper("10.85.60.103", 21, "tygl", "kpAc6Mzk1M");
			 //InputStream is = ftp.downloadFile("/accessory/internation/e446b028-7168-45d8-9d2b-2522d92e2c72.xls");
			ftp.deleteAll("/temp/bg/", false);
			ftp.disconnect();
			 System.out.println("结束！");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
