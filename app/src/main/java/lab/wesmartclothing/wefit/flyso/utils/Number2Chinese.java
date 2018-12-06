package lab.wesmartclothing.wefit.flyso.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jk on 2018/8/31.
 * 数字转大写中文汉字
 */
public class Number2Chinese {


    public static void main(String[] a) {
        System.out.println(number2Chinese(0.007 + ""));

//        System.out.println(MathUtils.div(50, 100, 2));

        int value = -5;
        int index = 0;


        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(-0, -1, -2, -3, -4, -6, -7));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) <= value) {
                index = i;
                break;
            } else {
                index = i + 1;
            }
        }

        System.out.println(index);
        list.add(index, value);


        System.out.println(Arrays.toString(list.toArray()));

    }


    public static String number2Chinese(String numStr) {
        //12345
        String mark[] = new String[]{"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万"};
        String numCn[] = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "点"};

        String numberStr = "";//整数
        String pointStr = "";//小数

        int pointIndex = numStr.indexOf(".");

        if (pointIndex < 0) {
            numberStr = numStr;
        } else {
            numberStr = numStr.substring(0, pointIndex);
            pointStr = numStr.substring(pointIndex, numStr.length());
        }

        char[] numArrRev = String.valueOf(numberStr).toCharArray();

        StringBuffer container = new StringBuffer();
        for (int i = 0; i < numArrRev.length; i++) {
            Integer val = str2int(String.valueOf(numArrRev[i]));
            String number = numCn[val];
            int x = numArrRev.length - i - 1;
            String sign = mark[x];

            if (val == 0) {
                if (x % 4 != 0) {// 删除单位
                    sign = "";
                }
                if (i < numArrRev.length - 1) {
                    Integer val1 = str2int(String.valueOf(numArrRev[i + 1]));
                    if (val == 0 && val == val1) {
                        number = "";
                    } else if (val == 0 && ("万".equals(sign) || "亿".equals(sign))) {
                        number = "";
                    }
                } else if (i == numArrRev.length - 1) {
                    number = "";
                }
            }
            if (val == 1 && sign.equals(mark[1])) {
                container.append("" + sign);
            } else {
                container.append(number + sign);
            }
        }

        if (numberStr.equals("0")) {
            container.append(numCn[0]);
        }

        //计算小数点
        if (!"".equals(pointStr)) {
            char[] pointArrRev = String.valueOf(pointStr).toCharArray();
            for (int i = 0; i < pointArrRev.length; i++) {
                Integer val2 = str2int(String.valueOf(pointArrRev[i]));
                String number2 = numCn[val2];
                container.append(number2);
            }
        }
        //如果是整数，去掉末尾的点零
        if (container.toString().trim().endsWith("点零")) {
            return container.delete(container.length() - 2, container.length()).toString().trim();
        }

        return container.toString().trim();
    }


    private static Integer str2int(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 10;
        }
    }

}
