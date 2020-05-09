package com.github.qinyou.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩方法
 *
 * @author chuang
 */
@Slf4j
public class ZipUtils {

    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩文件夹
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构
     *                         true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            close(zos);
        }
    }

    /**
     * 文件列表压缩
     *
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     */
    public static void toZip(List<File> srcFiles, OutputStream out) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            close(zos);
        }
    }

    /**
     * 多个输入流转 下载包
     * @param fileNames  文件名
     * @param inputStreams 输入流集合
     * @param out 输出流
     */
    public static void toZipByInputStream(List<String> fileNames, List<InputStream> inputStreams,OutputStream out){
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            if(fileNames.size()!=inputStreams.size()){
                for(InputStream in : inputStreams){
                    if(in!=null){
                        in.close();
                    }
                }
                throw new RuntimeException("fileNames 参数 和 inputStreams 参数 size 不一致");
            }
            InputStream in;
            for(int i=0;i<fileNames.size();i++){
                in = inputStreams.get(i);
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(fileNames.get(i)));
                int len;
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            close(zos);
        }
    }

    /**
     * 字符串输出 到 压缩文件中， datas 和 filenames size 相同 且 不为 0、不为 null
     *
     * @param datas     文本数据集合
     * @param filenames 文件名集合 （多级目录之间 / 隔开)
     * @param out       压缩文件输出
     * @throws IllegalArgumentException 参数非法
     */
    public static void toZip(List<String> datas, List<String> filenames, OutputStream out) {
        if (datas == null || filenames == null || filenames.size() != datas.size() || filenames.size() == 0 || out == null) {
            throw new IllegalArgumentException();
        }

        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (int i = 0; i < datas.size(); i++) {
                zos.putNextEntry(new ZipEntry(filenames.get(i)));
                byte[] buf = datas.get(i).getBytes(Charset.forName("UTF-8"));
                zos.write(buf);
                zos.closeEntry();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            close(zos);
        }
    }


    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构
     *                         true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), true);
                    } else {
                        compress(file, zos, file.getName(), false);
                    }

                }
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param out
     */
    private static void close(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
