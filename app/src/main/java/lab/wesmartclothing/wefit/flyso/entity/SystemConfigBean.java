package lab.wesmartclothing.wefit.flyso.entity;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName SystemConfigBean
 * @Date 2018/11/13 16:00
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class SystemConfigBean {


    /**
     * confDesc : string
     * confName : string
     * confValue : string
     * createTime : 1511248354000
     * createUser : 1
     * gid : 1
     * status : 101
     * updateTime : 1511248354000
     * updateUser : 1
     */

    private String confDesc;
    private String confName;
    private String confValue;


    public String getConfDesc() {
        return confDesc;
    }

    public void setConfDesc(String confDesc) {
        this.confDesc = confDesc;
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public String getConfValue() {
        return confValue;
    }

    public void setConfValue(String confValue) {
        this.confValue = confValue;
    }


    @Override
    public String toString() {
        return "SystemConfigBean{" +
                "confDesc='" + confDesc + '\'' +
                ", confName='" + confName + '\'' +
                ", confValue='" + confValue + '\'' +
                '}';
    }
}
