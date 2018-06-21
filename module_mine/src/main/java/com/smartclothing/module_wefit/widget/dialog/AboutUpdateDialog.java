package com.smartclothing.module_wefit.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.dfu.DfuService;
import com.smartclothing.module_wefit.net.net.RetrofitService;
import com.vondear.rxtools.boradcast.B;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.roundprogressbar.RxTextRoundProgressBar;

import java.io.File;

import io.reactivex.functions.Function;
import lab.wesmartclothing.wefit.netlib.rx.FileDownLoadObserver;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;
import okhttp3.ResponseBody;

/**
 * @author zzp
 */
public class AboutUpdateDialog extends BaseDialog implements View.OnClickListener {
    private QMUIRelativeLayout frContent;
    private LinearLayout ll_close;
    private TextView tv_update_tip;
    private RxTextRoundProgressBar progressbar;
    private String filePath;
    private Context mContext;
    private boolean mustUpdate;
    private final String Dir = "/Timetofit/";
    private final String fileName = "weSmartClothing" + ".zip";

    public AboutUpdateDialog(Context context, String filePath, boolean mustUpdate) {
        super(context);
        super.setContentView(R.layout.dialog_recharge_tip);

        mContext = context;
        this.mustUpdate = mustUpdate;
        Window window = getWindow();
        WindowManager.LayoutParams attributesParams = window.getAttributes();
        attributesParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributesParams.dimAmount = 0.4f;
        setCanceledOnTouchOutside(false);
        int screenWidth = window.getWindowManager().getDefaultDisplay().getWidth();
        int windowWidth = (int) (screenWidth * 0.9f);
        window.setLayout(windowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setCanceledOnTouchOutside(!mustUpdate);

        ll_close = findViewById(R.id.ll_close);
        ll_close.setOnClickListener(this);
        frContent = findViewById(R.id.fr_content);
        tv_update_tip = findViewById(R.id.tv_update_tip);
        progressbar = findViewById(R.id.mRxTextRoundProgressBar);
        progressbar.setMax(100);
        progressbar.setProgressText("正在升级");
        progressbar.setProgress(0);
        progressbar.setTextProgressSize(dp2px(10));
        progressbar.setProgressBackgroundColor(context.getResources().getColor(R.color.lineColor));
        progressbar.setProgressColor(context.getResources().getColor(R.color.colorTheme));


        //下载文件固件升级文件
//        if (!RxDataUtils.isNullString(filePath))
//            downLoadFile(filePath);
//        else {
//            final String dirName = Environment.getExternalStorageDirectory() + Dir + fileName;
//        startMyDFU(new File(dirName));
//        }
        //演示使用本地DFU文件
        startMyDFU(null);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                DfuServiceListenerHelper.unregisterProgressListener(mContext, listenerAdapter);
                B.broadUpdate(mContext, BleKey.ACTION_DFU_STARTING, BleKey.EXTRA_DFU_STARTING, false);
            }
        });
    }

    private void downLoadFile(String filePath) {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.downLoadFile(filePath))
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody body) throws Exception {
                        return mFileDownLoadObserver.saveFile(body, Dir, fileName);
                    }
                })
                .subscribe(mFileDownLoadObserver);
    }


    FileDownLoadObserver<File> mFileDownLoadObserver = new FileDownLoadObserver<File>() {
        @Override
        public void onDownLoadSuccess(File o) {
            tv_update_tip.setText("下载文件成功");
            startMyDFU(o);
        }

        @Override
        public void onDownLoadFail(Throwable throwable) {
            tv_update_tip.setText("下载文件失败:" + throwable.toString());
        }

        @Override
        public void onProgress(int progress, long total) {
            tv_update_tip.setText("正在下载文件：" + progress + "%");
        }
    };


    private void startMyDFU(File o) {
        if (!BleTools.getInstance().isConnect()) {
            tv_update_tip.setText("未连接设备，请连上设备再重试");
            return;
        }

//        if (o == null || !o.exists() || o.getAbsolutePath().equals("") || !o.getAbsolutePath().endsWith(".zip")) {
//            tv_update_tip.setText("升级文件有误");
//            return;
//        }

        final DfuServiceInitiator starter = new DfuServiceInitiator(BleTools.getInstance().getBleDevice().getMac())
                .setDeviceName(BleTools.getInstance().getBleDevice().getName());
        starter.setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
        starter.setZip(R.raw.nrf52832_xxaa_app_7);
//        starter.setZip(o.getPath());
        starter.start(mContext, DfuService.class);
    }

    //拦截返回按钮
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) return mustUpdate;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void show() {
        DfuServiceListenerHelper.registerProgressListener(mContext, listenerAdapter);
        super.show();
    }


    private final DfuProgressListenerAdapter listenerAdapter = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnected(String deviceAddress) {
            super.onDeviceConnected(deviceAddress);
            RxLogUtils.d("onDeviceConnected：" + deviceAddress);
        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            super.onDeviceDisconnected(deviceAddress);
            RxLogUtils.d("onDeviceDisconnected：" + deviceAddress);
        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            super.onDfuAborted(deviceAddress);
            RxLogUtils.d("onDfuAborted：" + deviceAddress);
        }

        @Override
        public void onDfuProcessStarted(String deviceAddress) {
            super.onDfuProcessStarted(deviceAddress);
            RxLogUtils.d("onDfuProcessStarted");
        }

        @Override
        public void onDfuProcessStarting(String deviceAddress) {
            super.onDfuProcessStarting(deviceAddress);
            RxLogUtils.d("onDfuProcessStarting");
            tv_update_tip.setText("正在升级，请稍后...");
            B.broadUpdate(mContext, BleKey.ACTION_DFU_STARTING, BleKey.EXTRA_DFU_STARTING, true);
        }

        @Override
        public void onDeviceConnecting(String deviceAddress) {
            super.onDeviceConnecting(deviceAddress);
            RxLogUtils.d("onDeviceConnecting");
        }

        @Override
        public void onDeviceDisconnecting(String deviceAddress) {
            super.onDeviceDisconnecting(deviceAddress);
            RxLogUtils.d("onDeviceDisconnecting");
        }

        @Override
        public void onDfuCompleted(String deviceAddress) {
            super.onDfuCompleted(deviceAddress);
            B.broadUpdate(mContext, BleKey.ACTION_DFU_STARTING, BleKey.EXTRA_DFU_STARTING, false);
            RxLogUtils.d("onDfuCompleted");
//            RxToast.success("升级完成");
            tv_update_tip.setText("升级完成");
            setCanceledOnTouchOutside(true);
        }

        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            super.onEnablingDfuMode(deviceAddress);
            RxLogUtils.d("onEnablingDfuMode");
        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            super.onError(deviceAddress, error, errorType, message);
            RxLogUtils.e("onError:" + message);
            B.broadUpdate(mContext, BleKey.ACTION_DFU_STARTING, BleKey.EXTRA_DFU_STARTING, false);
//            startMyDFU();
            tv_update_tip.setText("升级失败,请重试");
            setCanceledOnTouchOutside(true);
        }

        @Override
        public void onFirmwareValidating(String deviceAddress) {
            super.onFirmwareValidating(deviceAddress);
            RxLogUtils.d("onFirmwareValidating");
        }

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            super.onProgressChanged(deviceAddress, percent, speed, avgSpeed, currentPart, partsTotal);
            RxLogUtils.d("onProgressChanged:" + "percent:" + percent + "----" + speed + "----avgSpeed:" + avgSpeed + "-----currentPart:" + currentPart + "------prtsTotal:" + partsTotal);
            progressbar.setProgress(percent);
            progressbar.setProgressText((int) progressbar.getProgress() + "%");
        }
    };


    @Override
    public void setContentView(View view) {
        frContent.removeAllViews();
        frContent.addView(view, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams lp) {
        frContent.removeAllViews();
        frContent.addView(view, lp);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(getContext(), layoutResID, null);
        setContentView(view);
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ll_close) {
            dismiss();
        } else {
        }
    }

    public static int dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}