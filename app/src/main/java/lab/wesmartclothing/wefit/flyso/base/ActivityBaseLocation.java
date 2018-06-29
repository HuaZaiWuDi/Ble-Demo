package lab.wesmartclothing.wefit.flyso.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.tbruyelle.rxpermissions2.Permission;
import com.vondear.rxtools.utils.RxLocationUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogGPSCheck;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.netlib.utils.RxThreadUtils;

public abstract class ActivityBaseLocation extends BaseActivity {

    public double mLongitude = 0;//经度
    public double mLatitude = 0;//纬度
    public static String City = "";//城市
    public static String Country = "";//国家
    public static String Province = "";//省份
    public LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Disposable subscribe;

    public abstract void setGpsInfo(Location location);

    public void getAddress(Address address) {
        if (address == null) return;
        RxLogUtils.d("地理位置" + address.toString());
        Country = address.getCountryName();
        Province = address.getAdminArea();
        City = address.getLocality();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initGPS();//初始化GPS
//        gpsCheck();//GPS开启状态检测
    }

    private void initGPS() {
        if (mLocationManager == null)
            mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    }

    //----------------------------------------------------------------------------------------------检测GPS是否已打开 start
    public void gpsCheck() {
        tipDialog.show();
        if (!RxLocationUtils.isGpsEnabled(this)) {
            RxDialogGPSCheck rxDialogGPSCheck = new RxDialogGPSCheck(mContext);
            rxDialogGPSCheck.show();
        } else {
            checkLocation(new Consumer<Permission>() {
                @Override
                public void accept(Permission permission) throws Exception {
                    if (permission.granted) {
                        getLocation();
                    }
                }
            });
        }
    }

    //==============================================================================================检测GPS是否已打开 end

    private void getLocation() {
        mLocationListener = new LocationListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onLocationChanged(final Location location) {
                mLongitude = location.getLongitude();
                mLatitude = location.getLatitude();
                RxLogUtils.d("经纬度" + location.toString());
                setGpsInfo(location);

                Observable.create(new ObservableOnSubscribe<Address>() {
                    @Override
                    public void subscribe(ObservableEmitter<Address> emitter) throws Exception {
                        Address address = null;
                        if (mContext != null) {
                            address = RxLocationUtils.getAddress(mContext, location.getLatitude(), location.getLongitude());
                        }
                        if (address != null)
                            emitter.onNext(address);
                    }
                }).compose(RxThreadUtils.<Address>rxThreadHelper())
                        .subscribe(new Observer<Address>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Address address) {
                                getAddress(address);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    //GPS状态为可见时
                    case LocationProvider.AVAILABLE:
                        RxLogUtils.d("GPS状态为可见时");
                        break;
                    //GPS状态为服务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        RxToast.normal("当前GPS信号弱");
//                        RxVibrateUtils.vibrateOnce(mContext, 3000);
                        break;
                    //GPS状态为暂停服务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        RxLogUtils.d("GPS状态为暂停服务时");
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                RxToast.normal("当前GPS设备已打开");
//                RxVibrateUtils.vibrateOnce(mContext, 800);
            }

            @Override
            public void onProviderDisabled(String provider) {
                RxToast.normal("当前GPS设备已关闭");
//                RxVibrateUtils.vibrateOnce(mContext, 800);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            RxLogUtils.d("验证权限");
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 100, mLocationListener);
    }


    public void closeLocation() {
        if (mLocationListener != null) {
            RxLogUtils.d("关闭定位监听");
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    protected void onDestroy() {
        if (mLocationListener != null) {
            RxLogUtils.d("关闭定位监听");
            mLocationManager.removeUpdates(mLocationListener);
            mLocationManager = null;
            mLocationListener = null;
        }
        if (subscribe != null) subscribe.dispose();
        super.onDestroy();
    }
}
