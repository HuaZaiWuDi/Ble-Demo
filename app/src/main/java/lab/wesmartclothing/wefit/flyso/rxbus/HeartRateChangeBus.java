package lab.wesmartclothing.wefit.flyso.rxbus;

/**
 * @Package lab.wesmartclothing.wefit.flyso.rxbus
 * @FileName HeartRateChangeBus
 * @Date 2019/3/25 10:20
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class HeartRateChangeBus {

    public byte[] heartRateData;

    public HeartRateChangeBus(byte[] heartRateData) {
        this.heartRateData = heartRateData;
    }
}
