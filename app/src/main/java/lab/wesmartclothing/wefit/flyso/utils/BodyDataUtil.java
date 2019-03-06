package lab.wesmartclothing.wefit.flyso.utils;

import lab.wesmartclothing.wefit.flyso.entity.Healthy;

/**
 * Created by jk on 2018/7/30.
 */
public class BodyDataUtil {


    public BodyDataUtil() {
    }


    /**
     * @param healthy
     * @param realValue
     * @return
     */
    public int transformation(Healthy healthy, double realValue) {
        int temp = 0;
        if (healthy.getSections().length == 1) {
            temp = (int) (realValue / (healthy.getSections()[0] * 2) * 100);
        } else {
            temp = bmi(realValue, healthy.getSections());
        }

        return temp;
    }

    /**
     * @param realValue
     * @param section
     * @return
     */
    private int bmi(double realValue, double[] section) {
        int temp = 0;
        if (section.length == 1)
            if (realValue < section[0]) {
                temp = (int) (realValue / section[0] * 50);
            } else {
                temp = (int) ((realValue - section[0]) / 50 + 50);
            }
        else if (section.length == 2) {
            if (realValue < section[0]) {

                temp = (int) (realValue / section[0] * 33);
            } else if (realValue >= section[0] && realValue < section[1]) {
                temp = (int) ((realValue - section[0]) / (section[1] - section[0]) * 33) + 33;
            } else {
                temp = (int) ((realValue - section[1]) / (section[1] + (section[1] - section[0])) * 33) + 66;
            }
        } else if (section.length == 3) {
            if (realValue < section[0]) {
                temp = (int) (realValue / section[0] * 25);
            } else if (realValue >= section[0] && realValue < section[1]) {
                temp = (int) ((realValue - section[0]) / (section[1] - section[0]) * 25) + 25;
            } else if (realValue >= section[1] && realValue < section[2]) {
                temp = (int) ((realValue - section[1]) / (section[2] - section[1]) * 25) + 50;
            } else {
                temp = (int) ((realValue - section[2]) / (section[2] + (section[1] - section[0])) * 25) + 75;
            }
        }

        return temp;
    }

    /**
     * @param realValue
     * @param healthy
     * @return
     */
    public Object[] checkStatus(double realValue, Healthy healthy) {

        double[] section = healthy.getSections();
        String[] labels = healthy.getLabels();
        int[] colors = healthy.getColors();

        Object[] temps = new Object[2];
        String temp = "";
        int color = 0;
        if (section.length == 1)
            if (realValue < section[0]) {
                temp = labels[0];
                color = colors[0];
            } else {
                temp = labels[1];
                color = colors[1];
            }
        else if (section.length == 2) {
            if (realValue < section[0]) {
                temp = labels[0];
                color = colors[0];
            } else if (realValue >= section[0] && realValue < section[1]) {
                temp = labels[1];
                color = colors[1];
            } else {
                temp = labels[2];
                color = colors[2];
            }
        } else if (section.length == 3) {
            if (realValue < section[0]) {
                temp = labels[0];
                color = colors[0];
            } else if (realValue >= section[0] && realValue < section[1]) {
                temp = labels[1];
                color = colors[1];
            } else if (realValue >= section[1] && realValue < section[2]) {
                temp = labels[2];
                color = colors[2];
            } else {
                color = colors[3];
                temp = labels[3];
            }
        }
        temps[0] = temp;
        temps[1] = color;
        return temps;
    }


}
