package lab.wesmartclothing.wefit.flyso.entity;

/**
 * Created icon_hide_password jk on 2018/6/13.
 */
public class FirmwareVersionUpdate {


    /**
     * fileUrl : string
     * hasNewVersion : true
     * mustUpgrade : 0
     */

    private String fileUrl;
    private boolean hasNewVersion;
    private int mustUpgrade;//是否强制升级  0非强制，1强制

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean isHasNewVersion() {
        return hasNewVersion;
    }

    public void setHasNewVersion(boolean hasNewVersion) {
        this.hasNewVersion = hasNewVersion;
    }

    public int getMustUpgrade() {
        return mustUpgrade;
    }

    public void setMustUpgrade(int mustUpgrade) {
        this.mustUpgrade = mustUpgrade;
    }
}
