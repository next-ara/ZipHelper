package com.next.module.ziphelper;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * ClassName:Zip解压类
 *
 * @author Afton
 * @time 2023/11/24
 * @auditor
 */
public class ZipDeCompress {

    //默认缓冲区大小
    public static final int BUFFER_SIZE_DIFAULT = 1024;

    //进度
    private int progress = 0;

    //压缩包总大小
    private long totalSize = 0;

    //当前已解压大小
    private long currentSize = 0;

    /**
     * 解压ZIP包
     *
     * @param zipFile       压缩包文件对象
     * @param targetDirPath 目标解压缩路径
     * @throws IOException
     * @throws Exception
     */
    public void unZip(File zipFile, String targetDirPath) throws IOException, Exception {
        this.totalSize = this.getZipSize(zipFile.getPath());
        LogTool.i(ZipConfig.TAG, "解压后文件总大小：" + this.totalSize);

        //先删除，后添加
        if (new File(targetDirPath).exists()) {
            new File(targetDirPath).delete();
        }
        new File(targetDirPath).mkdirs();

        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> entries = zip.getEntries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (zipEntry.isDirectory()) {
            } else {
                String zipEntryName = zipEntry.getName();
                if (zipEntryName.contains("../")) {
                    throw new Exception("unsecurity zipfile");
                } else {
                    if (zipEntryName.indexOf(File.separator) > 0) {
                        String zipEntryDir = zipEntryName.substring(0, zipEntryName.lastIndexOf(File.separator) + 1);
                        String unzipFileDir = targetDirPath + File.separator + zipEntryDir;
                        File unzipFileDirFile = new File(unzipFileDir);
                        if (!unzipFileDirFile.exists()) {
                            unzipFileDirFile.mkdirs();
                        }
                    }

                    String newFilePath = targetDirPath + File.separator + zipEntryName;
                    File file = new File(newFilePath);

                    int i = 1;
                    int index = file.getName().lastIndexOf(".");
                    while (file.exists()) {
                        i++;
                        if (index < 0) {
                            file = new File(newFilePath + "(" + i + ")");
                        } else {
                            file = new File(targetDirPath + File.separator + zipEntryName.substring(0, index) + "(" + i + ")" + zipEntryName.substring(index));
                        }
                    }

                    InputStream is = zip.getInputStream(zipEntry);
                    FileOutputStream fos = new FileOutputStream(file.getPath());
                    byte[] buff = new byte[BUFFER_SIZE_DIFAULT];
                    int size;
                    while ((size = is.read(buff)) > 0) {
                        this.currentSize += size;
                        //更新进度
                        this.progress = (int) (this.currentSize * 100 / this.totalSize);
                        LogTool.i(ZipConfig.TAG, "已解压：" + this.currentSize);
                        LogTool.i(ZipConfig.TAG, "解压进度：" + this.progress + "%");
                        fos.write(buff, 0, size);
                    }

                    fos.flush();
                    fos.close();
                    is.close();
                }
            }
        }
    }

    /**
     * 获取zip文件大小
     *
     * @param filePath zip文件路径
     * @return 大小
     */
    public long getZipSize(String filePath) {
        if (this.totalSize == 0) {
            ZipFile f;
            try {
                f = new ZipFile(filePath);
                Enumeration<ZipEntry> en = f.getEntries();
                while (en.hasMoreElements()) {
                    this.totalSize += en.nextElement().getSize();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this.totalSize;
    }

    /**
     * 获取解压进度
     *
     * @return 进度
     */
    public int getProgress() {
        return this.progress;
    }
}