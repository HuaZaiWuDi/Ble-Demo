package lab.wesmartclothing.wefit.flyso.rxbus;

/**
 * @Package lab.wesmartclothing.wefit.flyso.rxbus
 * @FileName BleStateChangedBus
 * @Date 2019/5/5 16:03
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class BleStateChangedBus {

    private boolean isOn;

    public BleStateChangedBus(boolean isOn) {
        this.isOn = isOn;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
