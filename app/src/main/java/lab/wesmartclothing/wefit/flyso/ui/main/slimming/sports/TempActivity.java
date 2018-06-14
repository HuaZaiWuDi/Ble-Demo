package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.smartclothing.blelibrary.BleAPI;
import com.smartclothing.blelibrary.BleTools;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.utils.RxLogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;

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

    @Click
    void tv_currentTemp() {

    }


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
                        tv_currentTemp.setText((data[10] & 0xff) + ".00");
                        seek_light.setProgress((data[12] & 0xff));
                        tv_light.setText((data[12] & 0xff) + "%");
                    }
                }
            });

        } else {
            tv_connectDevice.setText(R.string.disConnected);
        }
    }

    //心率
    @Receiver(actions = Key.ACTION_HEART_RATE_CHANGED)
    void myHeartRate(@Receiver.Extra(Key.EXTRA_HEART_RATE_CHANGED) byte[] data) {
        tv_currentTemp.setText((data[10] & 0xff) + ".00");
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
        tv_connectDevice.setText(BleTools.getInstance().isConnect() ? "已连接" : "未连接");
        BleAPI.readSetting(new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                RxLogUtils.d("读配置" + HexUtil.encodeHexStr(data));
                if (data[9] == 0x02) {
                    tv_currentTemp.setText((data[10] & 0xff) + ".00");
                    seek_light.setProgress((data[12] & 0xff));
                    tv_light.setText((data[12] & 0xff) + "%");
                }
            }
        });


        seek_light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    bright = progress;
                    tv_light.setText(bright + "%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BleAPI.syncSetting(temp, bright, 0x00, new BleChartChangeCallBack() {
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
                BleAPI.syncSetting(temp, bright, 0x01, new BleChartChangeCallBack() {
                    @Override
                    public void callBack(byte[] data) {

                    }
                });
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
