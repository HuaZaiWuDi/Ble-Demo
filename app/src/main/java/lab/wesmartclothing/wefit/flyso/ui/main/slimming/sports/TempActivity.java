package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.smartclothing.blelibrary.BleAPI;
import com.smartclothing.blelibrary.BleTools;
import com.smartclothing.blelibrary.listener.BleCallBack;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.smartclothing.blelibrary.listener.SynDataCallBack;
import com.smartclothing.blelibrary.util.ByteUtil;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;

@EActivity(R.layout.activity_temp)
public class TempActivity extends BaseActivity {

    private int temp = 25;//当前温度
    private int bright = 0;//当前温度


    @ViewById
    TextView tv_temp;
    @ViewById
    TextView tv_light;
    @ViewById
    TextView tv_connectDevice;
    @ViewById
    TextView tv_currentTemp;
    @ViewById
    SeekBar seek_temp;
    @ViewById
    SeekBar seek_light;

    //监听瘦身衣连接情况
    @Receiver(actions = Key.ACTION_CLOTHING_CONNECT)
    void clothingConnectStatus(@Receiver.Extra(Key.EXTRA_CLOTHING_CONNECT) boolean state) {
        if (state) {
            tv_connectDevice.setText(R.string.connected);
            BleAPI.readSetting(new BleChartChangeCallBack() {
                @Override
                public void callBack(byte[] data) {
                    RxLogUtils.d("读配置" + HexUtil.encodeHexStr(data));
                    if (data[9] == 0x02) {
                        tv_currentTemp.setText(data[10] + ".00");
                        seek_light.setProgress(data[12]);
                    }
                }
            });

        } else {
            tv_connectDevice.setText(R.string.disConnected);
        }
    }

    @Click
    void back() {
        onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    @AfterViews
    public void initView() {
        //屏幕沉浸
        StatusBarUtils.from(this).setStatusBarColor(getResources().getColor(R.color.colorTheme)).process();
//
        BleAPI.readSetting(new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                RxLogUtils.d("读配置" + HexUtil.encodeHexStr(data));
                if (data[9] == 0x02) {
                    tv_currentTemp.setText(data[10] + ".00");
                    seek_light.setProgress(data[12] / 20);

                }
            }
        });


        BleTools.getInstance().setBleCallBack(new BleCallBack() {
            @Override
            public void onNotify(byte[] data) {
                RxLogUtils.d("蓝牙Notify数据:" + HexUtil.encodeHexStr(data));
                if (data.length < 17) return;

                long time = ByteUtil.bytesToLongD4(data, 3);
//                RxLogUtils.d("时间：" + time);
                RxLogUtils.d("时间：" + RxFormat.setFormatDate(time * 1000, RxFormat.Date_Date_CH) + "--------------心率:" + data[8] + "--------------温度：" + data[10] + "步数：" + ByteUtil.bytesToIntD2(new byte[]{data[12], data[13]}) +
                        "-----" + "电压：" + ByteUtil.bytesToIntD2(new byte[]{data[15], data[16]}));

                //37f4ffff0158021e03000004b612
                tv_currentTemp.setText(data[10] + ".00");

//                RxToast.success("心率:" + data[8]);
//                RxLogUtils.d("步数：" + ByteUtil.bytesToIntD2(new byte[]{data[12], data[13]}));
//                RxLogUtils.d("电压：" + ByteUtil.bytesToIntD2(new byte[]{data[15], data[16]}));
            }
        });

        seek_light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    bright = progress * 20;
                    tv_light.setText(bright + "%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BleAPI.syncSetting(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04}, temp, bright, 0x00, new BleChartChangeCallBack() {
                    @Override
                    public void callBack(byte[] data) {

                    }
                });
            }
        });

        seek_temp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    temp = progress + 25;
                    tv_temp.setText(temp + "℃");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BleAPI.syncSetting(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04}, temp, bright, 0x01, new BleChartChangeCallBack() {
                    @Override
                    public void callBack(byte[] data) {

                    }
                });
            }
        });

        BleAPI.syncDataCount(new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                long packageCount = ByteUtil.bytesToLongD4(data, 3);
                RxLogUtils.d("包总数：" + packageCount);

                if (packageCount > 0) {
                    RxLogUtils.d("开始同步包数据");
                    synData();

                }
            }
        });

    }


    private void synData() {

        BleAPI.queryData();
        BleTools.getInstance().setSynDataCallBack(new SynDataCallBack() {
            @Override
            public void data(byte[] data) {
                packageCounts++;
                RxLogUtils.d("包序号：" + packageCounts);

                //400e0642bd135b 0128022303000004e012

                long time = ByteUtil.bytesToLongD4(data, 3);
                RxLogUtils.d("接收的蓝牙数据包：" + HexUtil.encodeHexStr(data));
                RxLogUtils.d("时间：" + RxFormat.setFormatDate(time * 1000, RxFormat.Date_Date_CH) + "--------------心率:" + data[8] + "---------------温度：" + data[10] + "--------" + "步数：" + ByteUtil.bytesToIntD2(new byte[]{data[12], data[13]}) +
                        "-----" + "电压：" + ByteUtil.bytesToIntD2(new byte[]{data[15], data[16]}));

                byte[] bytes = new byte[4];
                bytes[0] = data[3];
                bytes[1] = data[4];
                bytes[2] = data[5];
                bytes[3] = data[6];

                if (lastTime == time) {
                    RxLogUtils.d("表示重复包");
                } else
                    lastTime = time;

                BleAPI.syncData(bytes);
                if (data[7] != 0x00)
                    synData();

            }
        });
    }

    int packageCounts = 0;
    long lastTime = 0;

    @Override
    protected void onStart() {
        super.onStart();

    }
}
