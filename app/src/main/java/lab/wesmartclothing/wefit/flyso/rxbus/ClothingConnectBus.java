package lab.wesmartclothing.wefit.flyso.rxbus;

/**
 * @Package lab.wesmartclothing.wefit.flyso.rxbus
 * @FileName ClothingConnectBus
 * @Date 2019/5/5 15:42
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class ClothingConnectBus {

    private boolean isConnect;

    public ClothingConnectBus(boolean isConnect) {
        this.isConnect = isConnect;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
