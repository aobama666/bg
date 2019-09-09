package com.sgcc.bg.yygl.pojo;

/**
 * 封装对数据库的访问，查询列表，用印申请材料
 */
public class YyApplyAnnexDAO {

    private String uuid;
    private String useSealFileName;
    private String useSealFileLink;
    private String proofFileName;
    private String proofFileLink;
    private String useSealAmount;
    private String remark;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUseSealFileName() {
        return useSealFileName;
    }

    public void setUseSealFileName(String useSealFileName) {
        this.useSealFileName = useSealFileName;
    }

    public String getUseSealFileLink() {
        return useSealFileLink;
    }

    public void setUseSealFileLink(String useSealFileLink) {
        this.useSealFileLink = useSealFileLink;
    }

    public String getProofFileName() {
        return proofFileName;
    }

    public void setProofFileName(String proofFileName) {
        this.proofFileName = proofFileName;
    }

    public String getProofFileLink() {
        return proofFileLink;
    }

    public void setProofFileLink(String proofFileLink) {
        this.proofFileLink = proofFileLink;
    }

    public String getUseSealAmount() {
        return useSealAmount;
    }

    public void setUseSealAmount(String useSealAmount) {
        this.useSealAmount = useSealAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
