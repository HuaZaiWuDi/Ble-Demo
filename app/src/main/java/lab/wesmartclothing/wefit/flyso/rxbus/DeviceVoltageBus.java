package lab.wesmartclothing.wefit.flyso.rxbus;

/**
 * @Package lab.wesmartclothing.wefit.flyso.rxbus
 * @FileName DeviceVoltageBus
 * @Date 2019/2/14 15:39
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class DeviceVoltageBus {

    private int voltage;
    private int capacity;
    private double time;

    public DeviceVoltageBus(int voltage, int capacity, double time) {
        this.voltage = voltage;
        this.capacity = capacity;
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }
}
