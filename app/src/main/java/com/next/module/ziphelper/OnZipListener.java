package com.next.module.ziphelper;

/**
 * ClassName:Zip监听接口
 *
 * @author Afton
 * @time 2023/11/21
 * @auditor
 */
public interface OnZipListener {

    /**
     * 开始
     */
    void onStart();

    /**
     * 进度回调
     *
     * @param progress 进度
     */
    void onProgress(int progress);

    /**
     * 完成
     *
     * @param success 是否成功
     */
    void onFinish(boolean success);
}