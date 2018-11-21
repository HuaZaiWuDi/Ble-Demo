package lab.wesmartclothing.wefit.flyso.rxbus;

/**
 * @Package lab.wesmartclothing.wefit.flyso.rxbus
 * @FileName NetWorkType
 * @Date 2018/11/21 18:05
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class NetWorkType {

    int netType;
    boolean mBoolean;//是否有网


    public NetWorkType(int netType, boolean aBoolean) {
        this.netType = netType;
        mBoolean = aBoolean;
    }

    public boolean isBoolean() {
        return mBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        mBoolean = aBoolean;
    }

    public int getNetType() {
        return netType;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }


    @Override
    public String toString() {
        return "NetWorkType{" +
                "netType=" + netType +
                ", mBoolean=" + mBoolean +
                '}';
    }
}
