package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import lab.wesmartclothing.wefit.flyso.ble.BleKey;
import lab.wesmartclothing.wefit.flyso.ble.BleTools;
import com.vondear.rxtools.boradcast.B;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.roundprogressbar.RxTextRoundProgressBar;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.dfu.DfuService;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.FileDownLoadObserver;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

/**
 * @author zzp
 */
public class AboutUpdateDialog extends RxDialog {
    @BindView(R.id.tv_progress)
    TextView mTvProgress;
    @BindView(R.id.mRxTextRoundProgressBar)
    RxTextRoundProgressBar mMRxTextRoundProgressBar;
    @BindView(R.id.tv_update_tip)
    TextView mTvUpdateTip;
    @BindView(R.id.fr_content)
    QMUIRelativeLayout mFrContent;
    @BindView(R.id.loadView)
    RxLoadingView mLoadView;

    private String filePath;
    private Context mContext;
    private boolean mustUpdate;
    private final String Dir = "/Timetofit/";
    private String fileName = "weSmartClothing" + ".zip";

    private BLEUpdateListener mBLEUpdateListener;

    public void setBLEUpdateListener(BLEUpdateListener BLEUpdateListener) {
        mBLEUpdateListener = BLEUpdateListener;
    }

    public interface BLEUpdateListener {
        void success();

        void fail();
    }

    public AboutUpdateDialog(Context context, String filePath, boolean mustUpdate) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_recharge_tip, null);
        super.setContentView(view);
        ButterKnife.bind(this, view);

        mContext = context;
        this.mustUpdate = mustUpdate;

        this.setCanceledOnTouchOutside(false);
        setFullScreenWidth();


        mMRxTextRoundProgressBar.setMax(100);
        mMRxTextRoundProgressBar.setProgressText("");
        mMRxTextRoundProgressBar.setTextProgressSize(dp2px(10));
        mTvUpdateTip.setTypeface(MyAPP.typeface);

        //下载文件固件升级文件
        if (!RxDataUtils.isNullString(filePath))
            downLoadFile(filePath);

//        //演示使用本地DFU文件
//        startMyDFU(null);

        setOnDismissListener(dialog -> {
            DfuServiceListenerHelper.unregisterProgressListener(mContext, listenerAdapter);
            B.broadUpdate(mContext, BleKey.ACTION_DFU_STARTING, BleKey.EXTRA_DFU_STARTING, false);
        });
    }

    private void downLoadFile(String filePath) {
        int lastIndexOf = filePath.lastIndexOf("/");
        fileName = filePath.substring(lastIndexOf + 1, filePath.length());
        RxLogUtils.i("文件名：" + fileName);

        RxManager.getInstance().doLoadDownSubscribe(NetManager.getApiService().downLoadFile(filePath))
                .map(body -> mFileDownLoadObserver.saveFile(body, Dir, fileName))
                .subscribe(mFileDownLoadObserver);
    }

    FileDownLoadObserver<File> mFileDownLoadObserver = new FileDownLoadObserver<File>() {
        @Override
        public void onDownLoadSuccess(File o) {
            mTvUpdateTip.setText("下载文件成功");
            startMyDFU(o);
        }

        @Override
        public void onDownLoadFail(Throwable throwable) {
            mTvUpdateTip.setText("下载文件失败:" + throwable.toString());
            RxLogUtils.e(throwable.toString());
            setCanceledOnTouchOutside(true);
        }

        @Override
        public void onProgress(int progress, long total) {
            mTvUpdateTip.setText("正在下载文件：" + progress + "%");
        }
    };


    private void startMyDFU(File o) {
        if (!BleTools.getInstance().isConnect()) {
            mTvUpdateTip.setText("未连接设备，请连上设备再重试");
            setCanceledOnTouchOutside(true);
            return;
        }

        if (o == null || !o.exists() || o.getAbsolutePath().equals("") || !o.getAbsolutePath().endsWith(".zip")) {
            mTvUpdateTip.setText("升级文件有误");
            setCanceledOnTouchOutside(true);
            return;
        }

        final DfuServiceInitiator starter = new DfuServiceInitiator(BleTools.getInstance().getBleDevice().getMac())
                .setDeviceName(BleTools.getInstance().getBleDevice().getName());
        starter.setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
//        starter.setZip(R.raw.nrf52832_xxaa_app_7);
        starter.setZip(o.getPath());
        starter.start(mContext, DfuService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DfuServiceInitiator.createDfuNotificationChannel(mContext);
        }
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

            B.broadUpdate(mContext, BleKey.ACTION_DFU_STARTING, BleKey.EXTRA_DFU_STARTING, false);
            mTvUpdateTip.setText("升级中断,请重试");
            setCanceledOnTouchOutside(true);
            if (mBLEUpdateListener != null)
                mBLEUpdateListener.fail();
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
            mTvUpdateTip.setText("正在升级，请稍后...");
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
            mTvUpdateTip.setText("升级完成");
            setCanceledOnTouchOutside(true);
            if (mBLEUpdateListener != null)
                mBLEUpdateListener.success();
            mMRxTextRoundProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, 2000);
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
            mTvUpdateTip.setText("升级失败,请重试");
            setCanceledOnTouchOutside(true);
            if (mBLEUpdateListener != null)
                mBLEUpdateListener.fail();
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
            mMRxTextRoundProgressBar.setProgress(percent, false);
            mMRxTextRoundProgressBar.setProgressText("");
            mLoadView.setVisibility(View.GONE);
            RxTextUtils.getBuilder((int) mMRxTextRoundProgressBar.getProgress() + "")
                    .append("\t%").setForegroundColor(Color.parseColor("#574D5F"))
                    .setProportion(0.5f).into(mTvProgress);
        }
    };


    public static int dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}