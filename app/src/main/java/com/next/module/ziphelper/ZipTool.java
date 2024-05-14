package com.next.module.ziphelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ClassName:Zip工具类
 *
 * @author Afton
 * @time 2023/11/21
 * @auditor
 */
public class ZipTool {

    /**
     * 压缩文件
     *
     * @param srcFiles      待压缩的文件对象列表
     * @param targetFile    压缩后目标文件对象
     * @param onZipListener 压缩监听接口
     */
    public static void zip(ArrayList<File> srcFiles, File targetFile, OnZipListener onZipListener) {
        LogTool.i(ZipConfig.TAG, "开始压缩");
        ZipCompress zipCompress = new ZipCompress();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onZipListener.onProgress(zipCompress.getProgress());
            }
        }, 0, ZipConfig.getInstance().getCallBackRate());

        new Thread(() -> {
            try {
                onZipListener.onStart();
                zipCompress.zip(srcFiles, targetFile.getPath());
                timer.cancel();
                timer.purge();
                onZipListener.onProgress(100);
                onZipListener.onFinish(true);
                LogTool.i(ZipConfig.TAG, "压缩完成");
            } catch (Exception e) {
                timer.cancel();
                timer.purge();
                onZipListener.onFinish(false);
                LogTool.e(ZipConfig.TAG, "压缩失败：" + e.toString());
            }
        }).start();
    }

    /**
     * 解压文件
     *
     * @param zipFile       待解压文件对象
     * @param targetDir     解压后目标目录对象
     * @param onZipListener 解压监听接口
     */
    public static void unZip(File zipFile, File targetDir, OnZipListener onZipListener) {
        LogTool.i(ZipConfig.TAG, "开始解压");
        ZipDeCompress zipDeCompress = new ZipDeCompress();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onZipListener.onProgress(zipDeCompress.getProgress());
            }
        }, 0, ZipConfig.getInstance().getCallBackRate());

        new Thread(() -> {
            try {
                onZipListener.onStart();
                zipDeCompress.unZip(zipFile, targetDir.getPath());
                timer.cancel();
                timer.purge();
                onZipListener.onProgress(100);
                onZipListener.onFinish(true);
                LogTool.i(ZipConfig.TAG, "解压完成");
            } catch (Exception e) {
                timer.cancel();
                timer.purge();
                onZipListener.onFinish(false);
                LogTool.e(ZipConfig.TAG, "解压失败：" + e.toString());
            }
        }).start();
    }
}