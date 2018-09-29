package com.sgcc.bg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 * @author Administrator
 * @date 2017-09-12
 */
public class Dept implements Serializable {

    /** null */
    private String deptid;

    /** null */
    private String deptname;

    /** null */
    private String hrdeptcode;

    /** null */
    private String pdeptid;

    /** null */
    private String phrdeptcode;

    /** null */
    private String pdeptname;

    /** null */
    private String organid;

    /** null */
    private String porganid;

    /** null */
    private BigDecimal src;

    /** null */
    private String origid;

    /** null */
    private BigDecimal deptorder;

    /** null */
    private BigDecimal deptstatus;

    /** null */
    private BigDecimal type;

    /** null */
    private BigDecimal ptype;

    /** null */
    private Integer wechatid;

    /** null */
    private Integer syncstatus;

    /** null */
    private Integer status;

    /** null */
    private String itemcode;

    /** null */
    private String errormsg;

    /** null */
    private Integer errorcode;

    /** null */
    private Date datecreated;

    /** null */
    private BigDecimal deptsort;

    private static final long serialVersionUID = 1L;

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname == null ? null : deptname.trim();
    }

    public String getHrdeptcode() {
        return hrdeptcode;
    }

    public void setHrdeptcode(String hrdeptcode) {
        this.hrdeptcode = hrdeptcode == null ? null : hrdeptcode.trim();
    }

    public String getPdeptid() {
        return pdeptid;
    }

    public void setPdeptid(String pdeptid) {
        this.pdeptid = pdeptid == null ? null : pdeptid.trim();
    }

    public String getPhrdeptcode() {
        return phrdeptcode;
    }

    public void setPhrdeptcode(String phrdeptcode) {
        this.phrdeptcode = phrdeptcode == null ? null : phrdeptcode.trim();
    }

    public String getPdeptname() {
        return pdeptname;
    }

    public void setPdeptname(String pdeptname) {
        this.pdeptname = pdeptname == null ? null : pdeptname.trim();
    }

    public String getOrganid() {
        return organid;
    }

    public void setOrganid(String organid) {
        this.organid = organid == null ? null : organid.trim();
    }

    public String getPorganid() {
        return porganid;
    }

    public void setPorganid(String porganid) {
        this.porganid = porganid == null ? null : porganid.trim();
    }

    public BigDecimal getSrc() {
        return src;
    }

    public void setSrc(BigDecimal src) {
        this.src = src;
    }

    public String getOrigid() {
        return origid;
    }

    public void setOrigid(String origid) {
        this.origid = origid == null ? null : origid.trim();
    }

    public BigDecimal getDeptorder() {
        return deptorder;
    }

    public void setDeptorder(BigDecimal deptorder) {
        this.deptorder = deptorder;
    }

    public BigDecimal getDeptstatus() {
        return deptstatus;
    }

    public void setDeptstatus(BigDecimal deptstatus) {
        this.deptstatus = deptstatus;
    }

    public BigDecimal getType() {
        return type;
    }

    public void setType(BigDecimal type) {
        this.type = type;
    }

    public BigDecimal getPtype() {
        return ptype;
    }

    public void setPtype(BigDecimal ptype) {
        this.ptype = ptype;
    }

    public Integer getWechatid() {
        return wechatid;
    }

    public void setWechatid(Integer wechatid) {
        this.wechatid = wechatid;
    }

    public Integer getSyncstatus() {
        return syncstatus;
    }

    public void setSyncstatus(Integer syncstatus) {
        this.syncstatus = syncstatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode == null ? null : itemcode.trim();
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg == null ? null : errormsg.trim();
    }

    public Integer getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }

    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    public BigDecimal getDeptsort() {
        return deptsort;
    }

    public void setDeptsort(BigDecimal deptsort) {
        this.deptsort = deptsort;
    }
}