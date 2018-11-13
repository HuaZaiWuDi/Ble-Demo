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
     * appDownloadUrl : string
     * orderAddress : string
     * shareInformUrl : string
     * shareRootUrl : string
     * shppingAddress : string
     */

    private String appDownloadUrl;
    private String orderAddress;
    private String shareInformUrl;
    private String shareRootUrl;
    private String shppingAddress;

    public String getAppDownloadUrl() {
        return appDownloadUrl;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getShareInformUrl() {
        return shareInformUrl;
    }

    public void setShareInformUrl(String shareInformUrl) {
        this.shareInformUrl = shareInformUrl;
    }

    public String getShareRootUrl() {
        return shareRootUrl;
    }

    public void setShareRootUrl(String shareRootUrl) {
        this.shareRootUrl = shareRootUrl;
    }

    public String getShppingAddress() {
        return shppingAddress;
    }

    public void setShppingAddress(String shppingAddress) {
        this.shppingAddress = shppingAddress;
    }
}
