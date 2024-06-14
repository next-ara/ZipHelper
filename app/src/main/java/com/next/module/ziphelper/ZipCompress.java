package com.next.module.ziphelper;

import com.next.module.file2.File2;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ClassName:Zip压缩类
 *
 * @author Afton
 * @time 2023/11/24
 * @auditor
 */
public class ZipCompress {

    //默认编码格式
    public static final String ENCODING_DEFAULT = "UTF-8";
    //默认缓冲区大小
    public static final int BUFFER_SIZE_DIFAULT = 1024;

    //进度
    private int progress = 0;

    //压缩包总大小
    private long totalSize = 0;

    //当前已压缩大小
    private long currentSize = 0;

    /**
     * 生成ZIP压缩包
     *
     * @param srcFiles   待压缩的文件对象列表
     * @param targetFile 生成的压缩包文件对象
     * @throws Exception
     */
    public void zip(ArrayList<File2> srcFiles, File2 targetFile) throws Exception {
        File2[] files = new File2[srcFiles.size()];
        for (int i = 0; i < srcFiles.size(); i++) {
            files[i] = srcFiles.get(i);
        }

        this.zip(files, targetFile, ENCODING_DEFAULT);
    }

    /**
     * 生成ZIP压缩包
     *
     * @param srcFiles   待压缩的文件对象数组
     * @param targetFile 生成的压缩包文件对象
     * @throws Exception
     */
    public void zip(File2[] srcFiles, File2 targetFile) throws Exception {
        this.zip(srcFiles, targetFile, ENCODING_DEFAULT);
    }

    /**
     * 生成ZIP压缩包
     *
     * @param srcFiles   待压缩的文件对象数组
     * @param targetFile 生成的压缩包文件对象
     * @param encoding   编码格式
     * @throws Exception
     */
    public void zip(File2[] srcFiles, File2 targetFile, String encoding) throws Exception {
        //计算压缩包总大小
        this.totalSize = this.getTotalSize(srcFiles);
        LogTool.i(ZipConfig.TAG, "文件总大小：" + this.totalSize);

        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(targetFile.openOutputStream()));
        zipOut.setEncoding(encoding);
        for (int i = 0; i < srcFiles.length; i++) {
            File2 file = srcFiles[i];
            this.doZipFile(zipOut, file);
        }

        zipOut.flush();
        zipOut.close();
    }

    /**
     * 执行压缩文件
     *
     * @param zipOut 压缩输出流
     * @param file   文件对象
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void doZipFile(ZipOutputStream zipOut, File2 file) throws IOException {
        if (file.isFile()) {
            BufferedInputStream bis = new BufferedInputStream(file.openInputStream());
            ZipEntry entry = new ZipEntry(file.getName());
            zipOut.putNextEntry(entry);
            byte[] buff = new byte[BUFFER_SIZE_DIFAULT];
            int size;
            while ((size = bis.read(buff, 0, buff.length)) != -1) {
                this.currentSize += size;
                //更新进度
                this.progress = (int) (this.currentSize * 100 / this.totalSize);
                LogTool.i(ZipConfig.TAG, "已压缩：" + this.currentSize);
                LogTool.i(ZipConfig.TAG, "压缩进度：" + this.progress + "%");
                zipOut.write(buff, 0, size);
            }
            zipOut.closeEntry();
            bis.close();
        } else {
            File2[] subFiles = file.listFiles();
            for (File2 subFile : subFiles) {
                this.doZipFile(zipOut, subFile);
            }
        }
    }

    /**
     * 获取压缩包总大小
     *
     * @param srcFiles 待压缩的文件对象数组
     * @return
     */
    private long getTotalSize(File2[] srcFiles) {
        if (this.totalSize == 0) {
            for (File2 file : srcFiles) {
                //递归获取压缩包大小
                this.totalSize += this.getSize(file);
            }
        }

        return this.totalSize;
    }

    /**
     * 获取文件/文件夹大小
     *
     * @param file 文件对象
     * @return
     */
    private long getSize(File2 file) {
        long size = 0;
        if (file.isFile()) {
            size = file.length();
        } else {
            File2[] subFiles = file.listFiles();
            for (File2 subFile : subFiles) {
                size += this.getSize(subFile);
            }
        }

        return size;
    }

    /**
     * 获取压缩进度
     *
     * @return 进度
     */
    public int getProgress() {
        return this.progress;
    }
}