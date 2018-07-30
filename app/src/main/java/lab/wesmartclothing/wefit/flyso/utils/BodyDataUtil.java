package lab.wesmartclothing.wefit.flyso.utils;

/**
 * Created by jk on 2018/7/30.
 */
public class BodyDataUtil {


    public BodyDataUtil() {
    }


    public static int transformation(int index, double realValue) {
        int temp = 0;
        switch (index) {
            case 0:
                temp = bmi(realValue, new double[]{21, 31, 36});
                break;
            case 1:
                temp = bmi(realValue, new double[]{18.5, 25});
                break;
            case 2:
                temp = bmi(realValue, new double[]{9, 11});
                break;
            case 3:
                temp = bmi(realValue, new double[]{36.5, 42.5});
                break;
            case 4:
                temp = (int) (realValue / (903 * 2) * 100);
                break;
            case 5:
                temp = bmi(realValue, new double[]{45, 60});
                break;
            case 6:
                temp = bmi(realValue, new double[]{2.0, 2.4});
                break;
            case 7:
                temp = bmi(realValue, new double[]{20, 30, 40});
                break;
        }
        return temp;
    }

    private static int bmi(double realValue, double[] section) {

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
                temp = (int) ((realValue - section[1]) / (section[1] + 25) * 33) + 66;
            }
        } else if (section.length == 3) {
            if (realValue < section[0]) {
                temp = (int) (realValue / section[0] * 25);
            } else if (realValue >= section[0] && realValue < section[1]) {
                temp = (int) ((realValue - section[0]) / (section[1] - section[0]) * 25) + 25;
            } else if (realValue >= section[1] && realValue < section[2]) {
                temp = (int) ((realValue - section[1]) / (section[2] - section[1]) * 25) + 50;
            } else {
                temp = (int) ((realValue - section[2]) / (section[2] + 25) * 25) + 75;
            }
        }

        if (realValue <= 0) {
            temp = 5;
        }
        return temp;
    }

}
