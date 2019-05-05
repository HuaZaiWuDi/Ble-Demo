package lab.wesmartclothing.wefit.flyso.rxbus;

/**
 * @Package lab.wesmartclothing.wefit.flyso.rxbus
 * @FileName ScaleConnectBus
 * @Date 2019/5/5 15:37
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class ScaleConnectBus {

    private boolean isConnect;

    public ScaleConnectBus(boolean isConnect) {
        this.isConnect = isConnect;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
