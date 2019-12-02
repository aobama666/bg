package com.sgcc.bg.lunwen.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UploadUtil {

    private static Logger log = LoggerFactory.getLogger(UploadUtil.class);

    /**
     * 页面上传一个文件至本地服务指定文件夹下,论文上传正在使用,不可随意修改,上传单个文件可以使用此方法,如为多个请使用下一个方法
     * @param path   服务端临时保存路径
     * @param request
     * @return
     */
    public static String uploadFileForLocal(String path,HttpServletRequest request){
        String fileName = "";
        //获取解析器
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断是否是文件
        if(resolver.isMultipart(request)){
            //转换
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)(request);
            //获取文件名称
            Iterator<String> its = multipartHttpServletRequest.getFileNames();
            while(its.hasNext()){
                //根据文件名称取文件
                MultipartFile file = multipartHttpServletRequest.getFile(its.next());
                fileName = file.getOriginalFilename();
                String localPath = path+fileName;
                //创建一个新的文件对象，参数为保存路径
                File newFile = new File(localPath);
                if(newFile.getParentFile() != null || !newFile.getParentFile().isDirectory()){
                    //创建父文件夹，如果不存在的话
                    if(!newFile.exists()){
                        newFile.getParentFile().mkdirs();
                    }
                }
                //上传文件到指定文件夹
                try {
                    file.transferTo(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("upload file："+fileName);
            }
        }
        return fileName;
    }



    /**
     * 页面上传至本地服务指定文件夹下,上传多个文件,返回多个文件名称
     * @param path   服务端临时保存路径
     * @param request
     * @return
     */
    public static List<String> uploadFileForLocalMore(String path,HttpServletRequest request){
        List<String> fileNames = new ArrayList<>();
        //获取解析器
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断是否是文件
        if(resolver.isMultipart(request)){
            String fileName = "";
            //转换
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)(request);
            //获取文件名称
            Iterator<String> its = multipartHttpServletRequest.getFileNames();
            while(its.hasNext()){
                //根据文件名称取文件
                MultipartFile file = multipartHttpServletRequest.getFile(its.next());
                fileName = file.getOriginalFilename();
                String localPath = path+fileName;
                //创建一个新的文件对象，参数为保存路径
                File newFile = new File(localPath);
                if(newFile.getParentFile() != null || !newFile.getParentFile().isDirectory()){
                    //创建父文件夹，如果不存在的话
                    if(!newFile.exists()){
                        newFile.getParentFile().mkdirs();
                    }
                }
                //上传文件到指定文件夹
                try {
                    file.transferTo(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("上传文件:{}",fileName);
                fileNames.add(fileName);
            }
        }
        return fileNames;
    }
}
