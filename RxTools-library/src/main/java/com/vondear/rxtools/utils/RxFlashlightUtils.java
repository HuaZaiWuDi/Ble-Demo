package com.vondear.rxtools.utils;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;

import static android.hardware.Camera.Parameters.FLASH_MODE_OFF;
import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;

/**
 * detail: 手电筒工具类
 * Created by Ttt
 */
public final class RxFlashlightUtils {

    private RxFlashlightUtils() {
    }

    // 日志TAG
    private final String TAG = RxFlashlightUtils.class.getSimpleName();

    public static RxFlashlightUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static final class LazyHolder {
        private static final RxFlashlightUtils INSTANCE = new RxFlashlightUtils();
    }

    // =

    private Camera mCamera;

    /**
     * 注册摄像头
     * @return
     */
    public boolean register() {
        try {
            mCamera = Camera.open(0);
        } catch (Throwable t) {
            return false;
        }
        if (mCamera == null) {
            return false;
        }
        try {
            mCamera.setPreviewTexture(new SurfaceTexture(0));
            mCamera.startPreview();
            return true;
        } catch (IOException e) {
            RxLogUtils.e(TAG, e);
            return false;
        }
    }

    /**
     * 注销摄像头
     */
    public void unregister() {
        if (mCamera == null) return;
        mCamera.stopPreview();
        mCamera.release();
    }

    /**
     * 打开闪光灯
     */
    public void setFlashlightOn() {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(parameters.getFlashMode())) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
        }
    }

    /**
     * 关闭闪光灯
     */
    public void setFlashlightOff() {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (Camera.Parameters.FLASH_MODE_TORCH.equals(parameters.getFlashMode())) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
        }
    }

    /**
     * 是否打开闪光灯
     * @return
     */
    public boolean isFlashlightOn() {
        if (mCamera == null) {
            return false;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        return Camera.Parameters.FLASH_MODE_TORCH.equals(parameters.getFlashMode());
    }

    /**
     * 是否支持手机闪光灯
     * @return
     */
    public static boolean isFlashlightEnable() {
        return RxUtils.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    // =

    /**
     * 打开闪光灯
     * @param camera
     */
    public void setFlashlightOn(Camera camera) {
        if (camera != null) {
            try {
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(FLASH_MODE_TORCH);
                camera.setParameters(parameter);
            } catch (Exception e){
               RxLogUtils.e(e);
            }
        }
    }

    /**
     * 关闭闪光灯
     * @param camera
     */
    public void setFlashlightOff(Camera camera) {
        if (camera != null) {
            try {
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(FLASH_MODE_OFF);
                camera.setParameters(parameter);
            } catch (Exception e){
               RxLogUtils.e(e);
            }
        }
    }

    /**
     * 是否打开了摄像头
     * @param camera
     * @return
     */
    public boolean isFlashlightOn(Camera camera) {
        if (camera == null) {
            return false;
        }
        try {
            Camera.Parameters parameters = camera.getParameters();
            return FLASH_MODE_TORCH.equals(parameters.getFlashMode());
        } catch (Exception e){
            RxLogUtils.e(e);
        }
        return false;
    }
}