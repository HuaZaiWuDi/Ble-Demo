package lab.wesmartclothing.wefit.flyso.entity;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName DeviceVersionBean
 * @Date 2019/5/5 15:46
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class DeviceVersionBean {

    private int category;
    private int modelNo;
    private int manufacture;
    private int hwVersion;
    private String firmwareVersion;


    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getModelNo() {
        return modelNo;
    }

    public void setModelNo(int modelNo) {
        this.modelNo = modelNo;
    }

    public int getManufacture() {
        return manufacture;
    }

    public void setManufacture(int manufacture) {
        this.manufacture = manufacture;
    }

    public int getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(int hwVersion) {
        this.hwVersion = hwVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }
}
