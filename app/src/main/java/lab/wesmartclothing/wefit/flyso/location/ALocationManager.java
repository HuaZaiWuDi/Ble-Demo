package lab.wesmartclothing.wefit.flyso.location;

import android.app.Application;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.vondear.rxtools.utils.RxLogUtils;

/**
 * Created by jk on 2018/6/4.
 */
public class ALocationManager {

    //声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;

    public static void init(Application application) {

        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                RxLogUtils.d("定位改变");
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(application);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }

}
