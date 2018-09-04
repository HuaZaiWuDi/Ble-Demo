package lab.wesmartclothing.wefit.netlib.net;

/**
 * 项目名称：Bracelet
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/5/24
 */
public class ServiceAPI {


    //一期
//    public static final String BASE_URL = "https://dev.wesmartclothing.com/mix/";//外网
    public static final String BASE_URL_192 = "http://10.10.11.192:15390";//德人服务器

    //二期
    public static final String BASE_URL_208 = "http://10.10.11.208:15390";//牛耕测试环境
    public static final String BASE_URL_125 = "http://119.23.225.125:15390";
    public static final String BASE_URL_mix = "http://10.10.11.208:15390/mix/";//网关

    public static final String BASE_URL_TEST = "http://10.10.11.208:15112";//网关


    //上线
    public static final String BASE_RELEASE = "https://api.wesmartclothing.com/mix/";//上线版本
    public static final String BASE_DEBUG = "https://dev.wesmartclothing.com/mix/";//测试版本


    public static String BASE_URL = BASE_RELEASE;


    public static void switchURL(String baseUrl) {
        BASE_URL = baseUrl;
    }


    //商城地址
    public static String Store_Addr = "https://weidian.com/?userid=1063198383";

    //商城URL
    public static String STORE_URL = "http://10.10.11.208:15111";

    public static String Order_Url = "http://10.10.11.208:15111";

    public static String Shopping_Address = "http://10.10.11.208:15111";


    //发现URL   判断网址是否隐藏底部导航栏，需要网址后面加/
    public static String FIND_Addr = "http://39.108.152.50:8088/find/";
//    public static String FIND_Addr = "http://10.10.11.208:8208/";


    //服务协议
    public static String Term_Service = "file:///android_asset/TermService.html";

    //收藏地址
    public static String Detail = "http://39.108.152.50:8088/find/detail.html?gid=";


}
