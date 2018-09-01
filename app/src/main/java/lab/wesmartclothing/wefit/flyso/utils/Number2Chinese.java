package lab.wesmartclothing.wefit.flyso.utils;

/**
 * Created by jk on 2018/8/31.
 */
public class Number2Chinese {


    public static void main(String[] a) {
        System.out.print(number2Invoice(10009));
    }

    public static String number2Invoice(int num) {
        //12345
        String mark[] = new String[]{"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万"};
        String numCn[] = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

        char[] numArrRev = String.valueOf(num).toCharArray();

        StringBuffer container = new StringBuffer();
        for (int i = 0; i < numArrRev.length; i++) {
            Integer val = Integer.valueOf(String.valueOf(numArrRev[i]));
            String number = numCn[val];
            int x = numArrRev.length - i - 1;
            String sign = mark[x];

            if (val == 0) {
                if (x % 4 != 0) {// 删除单位
                    sign = "";
                }
                if (i < numArrRev.length - 1) {
                    Integer val1 = Integer.parseInt(String.valueOf(numArrRev[i + 1]));
                    if (val == 0 && val == val1) {
                        number = "";
                    } else if (val == 0 && ("万".equals(sign) || "亿".equals(sign))) {
                        number = "";
                    }
                } else if (i == numArrRev.length - 1) {
                    number = "";
                }
            }

            container.append(number + sign);
        }
        return container.toString();
    }

}
