package com.sgcc.bg.yygl.pojo;

import com.sgcc.bg.yygl.bean.YyApplyAnnex;

import java.io.File;

/**
 * 申请材料-前端传值
 */
public class YyApplyAnnexVo {
    /**
     * 用印申请基本信息主键
     */
    private String applyId;
    /**
     * 用印材料
     */
    private File useSealFile;
    /**
     * 佐证材料
     */
    private File proofFile;
    /**
     * 用印文件份数
     */
    private String useSealAmount;
    /**
     * 备注
     */
    private String remark;


    public YyApplyAnnex toYyApplyAnnex(){
        YyApplyAnnex y = new YyApplyAnnex();
        y.setApplyId(getApplyId());
        y.setRemark(getRemark());
        y.setUseSealAmount(getUseSealAmount());
        return y;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public File getUseSealFile() {
        return useSealFile;
    }

    public void setUseSealFile(File useSealFile) {
        this.useSealFile = useSealFile;
    }

    public File getProofFile() {
        return proofFile;
    }

    public void setProofFile(File proofFile) {
        this.proofFile = proofFile;
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
