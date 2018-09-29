package com.sgcc.bg.model;

import java.util.Date;

/**
 * 文件实体类 对应数据库 tb_common_attachment
 * 
 * @author epri-xpjt
 * 
 */
public class FileEntity {
	private String id; // 主键id
	private String originname; // 文件上传前原始名
	private String uploadname; // 文件上传后保存名
	private String extname; // 文件类型
	private String filepath; // 文件下载地址
	private long filesize; // 文件大小 单位：byte
	private String username; // 上传该文件用户
	private Date CREATEDATE; // 文件创建时间
	private String soucre; // 文件来源 （哪个模块上传的，用途）
	private int download_num; // 下载次数

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOriginname() {
		return originname;
	}

	public void setOriginname(String originname) {
		this.originname = originname;
	}

	public String getUploadname() {
		return uploadname;
	}

	public void setUploadname(String uploadname) {
		this.uploadname = uploadname;
	}

	public String getExtname() {
		return extname;
	}

	public void setExtname(String extname) {
		this.extname = extname;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCREATEDATE() {
		return CREATEDATE;
	}

	public void setCREATEDATE(Date createdate) {
		CREATEDATE = createdate;
	}

	public String getSoucre() {
		return soucre;
	}

	public void setSoucre(String soucre) {
		this.soucre = soucre;
	}

	public int getDownload_num() {
		return download_num;
	}

	public void setDownload_num(int download_num) {
		this.download_num = download_num;
	}

}
