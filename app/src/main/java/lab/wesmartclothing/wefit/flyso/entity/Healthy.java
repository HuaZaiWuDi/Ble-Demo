package lab.wesmartclothing.wefit.flyso.entity;

import java.util.Arrays;

public class Healthy {
    private String[] labels;
    private double[] sections;
    private String[] sectionLabels;
    private int[] colors;
    private String unit;
    private String bodyDataTitle;
    private int bodyDataImg;

//    public Healthy(String[] labels, String[] sectionLabels, double[] sections, int[] colors) {
//        this.labels = labels;
//        this.sections = sections;
//        this.sectionLabels = sectionLabels;
//        this.colors = colors;
//    }


    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public double[] getSections() {
        return sections;
    }

    public void setSections(double[] sections) {
        this.sections = sections;
    }

    public String[] getSectionLabels() {
        return sectionLabels;
    }

    public void setSectionLabels(String[] sectionLabels) {
        this.sectionLabels = sectionLabels;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBodyDataTitle() {
        return bodyDataTitle;
    }

    public void setBodyDataTitle(String bodyDataTitle) {
        this.bodyDataTitle = bodyDataTitle;
    }

    public int getBodyDataImg() {
        return bodyDataImg;
    }

    public void setBodyDataImg(int bodyDataImg) {
        this.bodyDataImg = bodyDataImg;
    }


    @Override
    public String toString() {
        return "Healthy{" +
                "labels=" + Arrays.toString(labels) +
                ", sections=" + Arrays.toString(sections) +
                ", sectionLabels=" + Arrays.toString(sectionLabels) +
                ", colors=" + Arrays.toString(colors) +
                ", unit='" + unit + '\'' +
                ", bodyDataTitle='" + bodyDataTitle + '\'' +
                ", bodyDataImg=" + bodyDataImg +
                '}';
    }
}