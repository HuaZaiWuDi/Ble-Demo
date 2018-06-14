package lab.wesmartclothing.wefit.flyso.tools;

/**
 * Created by jk on 2018/5/7.
 */
public interface Key {

    public static final String ADD_FOOD_NAME = "ADD_FOOD_NAME";//食物界面跳转携带
    public static final String ADD_FOOD_TYPE = "ADD_FOOD_TYPE";
    public static final String ADD_FOOD_DATE = "ADD_FOOD_DATE";

    public static final String ADD_FOOD_INFO = "ADD_FOOD_INFO";

    public static final String BUNDLE_WEIGHT_HISTORY = "BUNDLE_WEIGHT_HISTORY";//跳转体重信息
    public static final String BUNDLE_SPORTS_INFO = "BUNDLE_SPORTS_INFO";//跳转运动信息

    public static final String BUNDLE_FORCE_BIND = "BUNDLE_FORCE_BIND";//是否强制绑定
    public static final String BUNDLE_BIND_TYPE = "BUNDLE_BIND_TYPE";//绑定类型


    public static final String BUNDLE_WEB_URL = "BUNDLE_WEB_URL";//跳转网页URL

    public static final String BUNDLE_RELOGIN = "BUNDLE_RELOGIN";//重新登录
    public static final String BUNDLE_TITLE = "BUNDLE_TITLE";//重新登录
    String BUNDLE_VOLTAGE = "BUNDLE_VOLTAGE";//电压容量


    ///////////////////////////////////////////////////////////////////////////
    // 广播
    ///////////////////////////////////////////////////////////////////////////

    //切换主题
    public static final String ACTION_SWITCH_THEME = "ACTION_SWITCH_THEME";
    public static final String EXTRA_SWITCH_THEME = "EXTRA_SWITCH_THEME";

    //切换底部Tab
    public static final String ACTION_SWITCH_BOTTOM_TAB = "ACTION_SWITCH_BOTTOM_TAB";
    public static final String EXTRA_SWITCH_BOTTOM_TAB = "EXTRA_SWITCH_BOTTOM_TAB";

    //瘦身衣连接状态
    String ACTION_CLOTHING_CONNECT = "ACTION_CLOTHING_CONNECT";
    String EXTRA_CLOTHING_CONNECT = "EXTRA_CLOTHING_CONNECT";

    //心率更改
    String ACTION_HEART_RATE_CHANGED = "ACTION_HEART_RATE_CHANGED";
    String EXTRA_HEART_RATE_CHANGED = "EXTRA_HEART_RATE_CHANGED";


    //体脂称连接状态
    String ACTION_SCALE_CONNECT = "ACTION_SCALE_CONNECT";
    String EXTRA_SCALE_CONNECT = "EXTRA_SCALE_CONNECT";


    ///////////////////////////////////////////////////////////////////////////
    // 分享登录SDK
    ///////////////////////////////////////////////////////////////////////////
    public static final String QQ_ID = "";
    public static final String WX_ID = "wxaaeb0352e04684de";
    public static final String WEIBO_ID = "";
    public static final String WB_URL = "";
    public static final String WX_SECRET = "";


    ///////////////////////////////////////////////////////////////////////////
    // 缓存KEY
    ///////////////////////////////////////////////////////////////////////////
    public static final String CACHE_USER_INFO = "CACHE_USER_INFO";
    public static final String CACHE_WEIGHT_INFO = "CACHE_WEIGHT_INFO";
    public static final String CACHE_BIND_INFO = "CACHE_BIND_INFO";


    ///////////////////////////////////////////////////////////////////////////
    // 蓝牙设备名字
    ///////////////////////////////////////////////////////////////////////////


}
