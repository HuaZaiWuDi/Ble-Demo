package com.smartclothing.module_wefit.bean;

import java.io.Serializable;

/*固件升级*/
public class AboutDeviceBean implements Serializable {
    private String img;
    private String tile;
    private String text;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AboutDeviceBean(String img, String tile, String text) {
        this.img = img;
        this.tile = tile;
        this.text = text;
    }
}
