package lab.wesmartclothing.wefit.flyso.base;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.utils.RxLogUtils;

import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

/**
 * Created icon_hide_password jk on 2018/6/4.
 * <p>
 * 高德地图定位
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 * <p>
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 */

/**
 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
 * amapLocation.getLatitude();//获取纬度
 * amapLocation.getLongitude();//获取经度
 * amapLocation.getAccuracy();//获取精度信息
 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
 * amapLocation.getCountry();//国家信息
 * amapLocation.getProvince();//省信息
 * amapLocation.getCity();//城市信息
 * amapLocation.getDistrict();//城区信息
 * amapLocation.getStreet();//街道信息
 * amapLocation.getStreetNum();//街道门牌号信息
 * amapLocation.getCityCode();//城市编码
 * amapLocation.getAdCode();//地区编码
 * amapLocation.getAoiName();//获取当前定位点的AOI信息
 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
 * amapLocation.getFloor();//获取当前室内定位的楼层
 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
 */

//错误码

/**
 * 0

 定位成功。

 可以在定位回调里判断定位返回成功后再进行业务逻辑运算。

 1

 一些重要参数为空，如context；

 请对定位传递的参数进行非空判断。

 2

 定位失败，由于仅扫描到单个wifi，且没有基站信息。

 请重新尝试。

 3

 获取到的请求参数为空，可能获取过程中出现异常。

 请对所连接网络进行全面检查，请求可能被篡改。

 4

 请求服务器过程中的异常，多为网络情况差，链路不通导致

 请检查设备网络是否通畅，检查通过接口设置的网络访问超时时间，建议采用默认的30秒。

 5

 请求被恶意劫持，定位结果解析失败。

 您可以稍后再试，或检查网络链路是否存在异常。

 6

 定位服务返回定位失败。

 请获取errorDetail（通过getLocationDetail()方法获取）信息并参考定位常见问题进行解决。

 7

 KEY鉴权失败。

 请仔细检查key绑定的sha1值与apk签名sha1值是否对应，或通过高频问题查找相关解决办法。

 8

 Android exception常规错误

 请将errordetail（通过getLocationDetail()方法获取）信息通过工单系统反馈给我们。

 9

 定位初始化时出现异常。

 请重新启动定位。

 10

 定位客户端启动失败。

 请检查AndroidManifest.xml文件是否配置了APSService定位服务

 11

 定位时的基站信息错误。

 请检查是否安装SIM卡，设备很有可能连入了伪基站网络。

 12

 缺少定位权限。

 请在设备的设置中开启app的定位权限。

 13

 定位失败，由于未获得WIFI列表和基站信息，且GPS当前不可用。

 建议开启设备的WIFI模块，并将设备中插入一张可以正常工作的SIM卡，或者检查GPS是否开启；如果以上都内容都确认无误，请您检查App是否被授予定位权限。

 14

 GPS 定位失败，由于设备当前 GPS 状态差。

 建议持设备到相对开阔的露天场所再次尝试。

 15

 定位结果被模拟导致定位失败

 如果您希望位置被模拟，请通过setMockEnable(true);方法开启允许位置模拟

 16

 当前POI检索条件、行政区划检索条件下，无可用地理围栏

 建议调整检索条件后重新尝试，例如调整POI关键字，调整POI类型，调整周边搜区域，调整行政区关键字等。

 18

 定位失败，由于手机WIFI功能被关闭同时设置为飞行模式

 建议手机关闭飞行模式，并打开WIFI开关

 19

 定位失败，由于手机没插sim卡且WIFI功能被关闭

 建议手机插上sim卡，打开WIFI开关
 *
 * */


public abstract class BaseALocationActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //定位回调
    public interface MyLocationListener {
        void location(AMapLocation aMapLocation);
    }

    public MyLocationListener mALocationListener;


    public void init() {

        configLocation();
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (tipDialog != null)
                    tipDialog.dismiss();
                if (aMapLocation != null) {
                    RxLogUtils.d("【高德定位】" + aMapLocation.toString());
                    MyAPP.aMapLocation = aMapLocation;
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        if (mALocationListener != null) {
                            mALocationListener.location(aMapLocation);
                        }
                    } else {
//                        RxToast.warning("定位失败");
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
    }


    public void configLocation() {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);


//        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

//设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

//        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);

        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setHttpTimeOut(10 * 1000);
        mLocationOption.setLocationCacheEnable(true);
    }

    public void startLocation(MyLocationListener mALocationListener) {
        this.mALocationListener = mALocationListener;

        new RxPermissions(mActivity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxComposeUtils.<Boolean>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            if (null != mLocationClient) {
                                mLocationClient.setLocationOption(mLocationOption);
                                //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                                mLocationClient.stopLocation();
                                mLocationClient.startLocation();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
        super.onDestroy();
    }
}
