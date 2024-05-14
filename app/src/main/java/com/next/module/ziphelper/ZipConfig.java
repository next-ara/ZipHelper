package com.next.module.ziphelper;

/**
 * ClassName:Zip配置类
 *
 * @author Afton
 * @time 2023/11/21
 * @auditor
 */
public class ZipConfig {

    public static final String TAG = "ZipConfig";

    private static ZipConfig zipConfig;

    //是否开启日志
    private boolean isOpenLog = false;

    //回调频率
    private long callBackRate = 1000;

    public static ZipConfig getInstance() {
        if (zipConfig == null) {
            zipConfig = new ZipConfig();
        }
        return zipConfig;
    }

    private ZipConfig() {
    }

    /**
     * 设置是否开启日志
     *
     * @param openLog 是否开启日志
     * @return Zip配置对象
     */
    public ZipConfig setOpenLog(boolean openLog) {
        isOpenLog = openLog;

        return this;
    }

    /**
     * 设置回调频率
     *
     * @param callBackRate 回调频率
     * @return Zip配置对象
     */
    public ZipConfig setCallBackRate(long callBackRate) {
        this.callBackRate = callBackRate;
        return this;
    }

    public boolean isOpenLog() {
        return isOpenLog;
    }

    public long getCallBackRate() {
        return callBackRate;
    }
}