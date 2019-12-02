package com.sgcc.bg.lunwen.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * @author mingliao
 * @date 2019/6/27
 * @description TODO
 */
public class ZipUtil {
        /**
         *
         * @param inputByte
         *            待解压缩的字节数组
         * @return 解压缩后的字节数组
         * @throws IOException
         */
        public static byte[] uncompress(byte[] inputByte) throws IOException {
            int len = 0;
            Inflater infl = new Inflater();
            infl.setInput(inputByte);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] outByte = new byte[1024];
            try {
                while (!infl.finished()) {
                    // 解压缩并将解压缩后的内容输出到字节输出流bos中
                    len = infl.inflate(outByte);
                    if (len == 0) {
                        break;
                    }
                    bos.write(outByte, 0, len);
                }
                infl.end();
            } catch (Exception e) {
                //
            } finally {
                bos.close();
            }
            return bos.toByteArray();
        }

        /**
         * 压缩.
         *
         * @param inputByte
         *            待压缩的字节数组
         * @return 压缩后的数据
         * @throws IOException
         */
        public static byte[] compress(byte[] inputByte) throws IOException {
            int len = 0;
            Deflater defl = new Deflater(1);//这里可以设置压缩的比,看实际情况进行修改
            defl.setInput(inputByte);
            defl.finish();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] outputByte = new byte[1024];
            try {
                while (!defl.finished()) {
                    // 压缩并将压缩后的内容输出到字节输出流bos中
                    len = defl.deflate(outputByte);
                    bos.write(outputByte, 0, len);
                }
                defl.end();
            } finally {
                bos.close();
            }
            return bos.toByteArray();
        }

        /**
         * @param sourceFileName  源文件
         * @param zipFileName   压缩后文件
         * @throws Exception
         */
        public static void zip(String sourceFileName,String zipFileName) throws Exception
        {
            //创建zip输出流
            ZipOutputStream out = new ZipOutputStream( new FileOutputStream(zipFileName));
            //创建缓冲输出流
            BufferedOutputStream bos = new BufferedOutputStream(out);
            File sourceFile = new File(sourceFileName);
            //调用函数
            compress(out,bos,sourceFile,sourceFile.getName());
            bos.close();
            out.close();
        }


        public static void compress(ZipOutputStream out,BufferedOutputStream bos,File sourceFile,String base) throws Exception
        {
            //如果路径为目录（文件夹）
            if(sourceFile.isDirectory())
            {
                //取出文件夹中的文件（或子文件夹）
                File[] flist = sourceFile.listFiles();
                if(flist.length==0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                {
                    System.out.println(base + File.pathSeparator);
                    out.putNextEntry(new ZipEntry(base + File.pathSeparator));
                }
                else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                {
                    for(int i=0;i<flist.length;i++)
                    {
                        compress(out,bos,flist[i],base+File.separator+flist[i].getName());
                    }
                }
            }
            else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            {
                out.putNextEntry( new ZipEntry(base) );
                FileInputStream fos = new FileInputStream(sourceFile);
                BufferedInputStream bis = new BufferedInputStream(fos);

                int tag;
                System.out.println(base);
                //将源文件写入到zip文件中
                while((tag=bis.read())!=-1)
                {
                    bos.write(tag);
                }
                bis.close();
                fos.close();
            }
        }

        /**
         * zip解压
         * @param srcFile        zip源文件
         * @param destDirPath     解压后的目标文件夹
         * @throws RuntimeException 解压失败会抛出运行时异常
         */

        public static void unZip(File srcFile, String destDirPath) throws RuntimeException {
            long start = System.currentTimeMillis();
            // 判断源文件是否存在
            if (!srcFile.exists()) {
                throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
            }
            // 开始解压
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(srcFile, Charset.forName("gbk"));
                Enumeration<?> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    // 如果是文件夹，就创建个文件夹
                    if (entry.isDirectory()) {
                        String dirPath = destDirPath + "/" + entry.getName();
                        File dir = new File(dirPath);
                        dir.mkdirs();
                    } else {
                        // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                        File targetFile = new File(destDirPath + "/" + entry.getName());
                        // 保证这个文件的父文件夹必须要存在
                        if(!targetFile.getParentFile().exists()){
                            targetFile.getParentFile().mkdirs();
                        }
                        targetFile.createNewFile();
                        // 将压缩文件内容写入到这个文件中
                        InputStream is = zipFile.getInputStream(entry);
                        FileOutputStream fos = new FileOutputStream(targetFile);
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        // 关流顺序，先打开的后关闭
                        fos.close();
                        is.close();
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println("解压完成，耗时：" + (end - start) +" ms");
            } catch (Exception e) {
                throw new RuntimeException("unzip error from ZipUtils", e);
            } finally {
                if(zipFile != null){
                    try {
                        zipFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        public static void main(String[] args) {

            try {
                //压缩
//                zip("D:\\20190611_mingliao\\评分规则.PNG","D:\\20190611_mingliao\\评分规则.zip");
                //解压
                unZip(new File("D:\\20190611_mingliao\\IDEA_workspace\\epri\\default1-113005\\bg_new_v20180929\\src\\main\\webapp\\upload\\lunwen\\999.zip"),
                        "D:\\20190611_mingliao\\IDEA_workspace\\epri\\default1-113005\\bg_new_v20180929\\src\\main\\webapp\\upload\\lunwen\\1A3DF7797B8F47D0B33B03F9A9C1A14D");
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

    }