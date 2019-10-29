package com.sgcc.bg.lunwen.util;

import java.io.File;

public class FIleDeleteUtil {

    /**
     * 删除文件夹或者文件------------请调用该方法， 删除文件或者文件夹下的所有文件
     */
    public static void deleteFolderOrFile(File file){
        deleteAllFile(file);//删除所有文件
        file.delete();//删除空文件夹
    }

    /**
     * 删除文件-------------子方法
     */
    public static void deleteAllFile(File file){
        String path = file.getPath();
        if(!file.isDirectory()){
            file.delete();
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for(String tempStr : tempList){
            if(path.endsWith(File.separator)){
                temp = new File(path + tempStr);
            }else{
                temp = new File(path + File.separator + tempStr);
            }
            if(temp.isFile()){
                temp.delete();
            }
            if(temp.isDirectory()){
                deleteAllFile(new File(path +"/"+ tempStr));
                deleteFolderOrFile(new File(path +"/"+ tempStr));
            }
        }
    }


}
