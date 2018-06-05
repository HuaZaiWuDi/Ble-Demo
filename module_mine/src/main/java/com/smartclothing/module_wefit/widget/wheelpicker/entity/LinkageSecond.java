package com.smartclothing.module_wefit.widget.wheelpicker.entity;

import java.util.List;

/**
 * 用于联动选择器展示的第二级条目
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/04/17 00:34
 * Builder:Android Studio
 *
 */
public interface LinkageSecond<Trd> extends LinkageItem {

    List<Trd> getThirds();

}