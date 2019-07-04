package com.sgcc.bg.lunwen.util;

import com.sgcc.bg.common.Rtext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

public class DownLoadUtil {
    private static Logger log = LoggerFactory.getLogger(DownLoadUtil.class);

    /**
     * 模板通用文件夹
     */
    public static final String TEMPLATE_FILE_PATH = "files/";
    /**
     * 论文附件下载文件夹
     */
    public static final String LW_PAPER_ANNEX_PATH = "files/lw_annex";
    /**
     * 论文信息模板
     */
    public static final String LW_PAPER_FILE_NAME = "论文信息模板.xls";


    /**
     * 下载工具类，request，response从控制层获取，文件路径和文件名称自定义
     * @param request
     * @param response
     * @param filePath
     */
    public void downLoadFile(HttpServletRequest request, HttpServletResponse response , String filePath , String fileName){
        OutputStream outp = null;
        InputStream in = null;
        try {
            request.setCharacterEncoding("utf-8");
            //考虑到页面参数添加还不如直接在controller层传递来的方便，取消从请求参数中获取
            //fileName = request.getParameter("fileName").trim();
            in = this.getClass().getClassLoader().getResourceAsStream(filePath + fileName);
            log.info("the filename is " + fileName);
            log.info(
                    "the wanted file's path is " + this.getClass().getClassLoader().getResource(filePath + fileName));
            response.reset();
            response.setContentType("application/x-download");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            outp = response.getOutputStream();
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) > 0) {
                outp.write(b, 0, i);
            }
            outp.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (outp != null) {
                try {
                    outp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                outp = null;
            }
        }
    }

    public static void main(String[] args) {
        for (int i=0;i<10;i++){
            System.out.println(Rtext.getUUID());
        }
//        String[] as = null;
//        String a = "asdfasdfa";
//        as = new String[]{a};
//        System.out.println(as);
    }
}
