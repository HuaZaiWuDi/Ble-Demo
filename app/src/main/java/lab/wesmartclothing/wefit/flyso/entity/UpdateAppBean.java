package lab.wesmartclothing.wefit.flyso.entity;

import com.google.gson.Gson;
import com.vondear.rxtools.utils.RxLogUtils;

import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/5/31.
 */
public class UpdateAppBean {


    /**
     * beforeDate : 0
     * endTime : 2018-05-31T02:05:05.432Z
     * macAddr : string
     * phoneType : string
     * startTime : 2018-05-31T02:05:05.432Z
     * system : string
     * useStatus : 0
     * userId : string
     * version : string
     * versionFlag : string
     */

    public static final String VERSION_FLAG_APP = "app";
    public static final String VERSION_FLAG_firmware = "firmware";
    public static final String VERSION_FLAG_mcu = "mcu";

    private int beforeDate;
    private String endTime;
    private String macAddr;
    private String phoneType;
    private String startTime;
    private String system;
    private int useStatus;
    private String userId;
    private String version;
    private String versionFlag;


    //APP升级监听
    public void addDeviceVersion(UpdateAppBean updateApp) {

        String s = new Gson().toJson(updateApp, UpdateAppBean.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addDeviceVersion(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("统计接口：" + s);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxLogUtils.e("统计接口：" + error);
//                        RxToast.error(error);
                    }
                });
    }


    public int getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(int beforeDate) {
        this.beforeDate = beforeDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionFlag() {
        return versionFlag;
    }

    public void setVersionFlag(String versionFlag) {
        this.versionFlag = versionFlag;
    }

    @Override
    public String toString() {
        return "UpdateAppBean{" +
                "beforeDate=" + beforeDate +
                ", endTime='" + endTime + '\'' +
                ", macAddr='" + macAddr + '\'' +
                ", phoneType='" + phoneType + '\'' +
                ", startTime='" + startTime + '\'' +
                ", system='" + system + '\'' +
                ", useStatus=" + useStatus +
                ", userId='" + userId + '\'' +
                ", version='" + version + '\'' +
                ", versionFlag='" + versionFlag + '\'' +
                '}';
    }
}
