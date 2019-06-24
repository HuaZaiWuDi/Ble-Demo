package lab.wesmartclothing.wefit.flyso.tools;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
public interface Key {


    long HOURS_12 = 12 * 60 * 60 * 1000;
    long HOURS_6 = 6 * 60 * 60 * 1000;
    long HOURS_1 = 60 * 60 * 1000;
    long DAY_1 = 24 * 60 * 60 * 1000;


    ///////////////////////////////////////////////////////////////////////////
    // BUNDLE跳转
    ///////////////////////////////////////////////////////////////////////////

    String ADD_FOOD_NAME = "ADD_FOOD_NAME";//食物界面跳转携带
    String ADD_FOOD_TYPE = "ADD_FOOD_TYPE";
    String ADD_FOOD_DATE = "ADD_FOOD_DATE";

    String ADD_FOOD_INFO = "ADD_FOOD_INFO";


    String BUNDLE_FORCE_BIND = "BUNDLE_FORCE_BIND";//是否强制绑定


    String BUNDLE_WEB_URL = "BUNDLE_WEB_URL";//跳转网页URL


    //标题
    String BUNDLE_TITLE = "BUNDLE_TITLE";

    String BUNDLE_DATA = "BUNDLE_DATA";

    String BUNDLE_OTHER_LOGIN_INFO = "BUNDLE_OTHER_LOGIN_INFO";//第三方登录用户信息


    String BUNDLE_DATE_TIME = "BUNDLE_DATE_TIME";//日期
    String BUNDLE_DATA_GID = "BUNDLE_DATA_GID";//数据的GID


    //设置体重目标传递参数
    String BUNDLE_HAS_DAYS = "BUNDLE_HAS_DAYS";
    String BUNDLE_INITIAL_WEIGHT = "BUNDLE_INITIAL_WEIGHT";
    String BUNDLE_STILL_NEED = "BUNDLE_STILL_NEED";
    String BUNDLE_TARGET_WEIGHT = "BUNDLE_TARGET_WEIGHT";


    //传递体型类型
    String BUNDLE_BODY_INDEX = "BUNDLE_BODY_INDEX";


    //体重秤实时数据
    String BUNDLE_WEIGHT_UNSTEADY = "BUNDLE_WEIGHT_UNSTEADY";

    String BUNDLE_WEIGHT_QNDATA = "BUNDLE_WEIGHT_QNDATA";

    //计划的状态
    String BUNDLE_PLAN_STATUS = "BUNDLE_PLAN_STATUS";

    String BUNDLE_SPORTING_PLAN = "BUNDLE_SPORTING_PLAN";
    String BUNDLE_GO_BCAK = "BUNDLE_GO_BCAK";

    ///////////////////////////////////////////////////////////////////////////
    // 广播
    ///////////////////////////////////////////////////////////////////////////

    String ACTION_CLOTHING_STOP = "ACTION_CLOTHING_STOP";


    ///////////////////////////////////////////////////////////////////////////
    // 分享登录SDK
    ///////////////////////////////////////////////////////////////////////////

    String LoginType_WEXIN = "WeChat";
    String LoginType_QQ = "QQ";
    String LoginType_WEIBO = "MicroBlog";

    //BUGLY
    String BUGly_id = "dd2f9fff3f";


    String APP_KEY = "wisenfit";

    String COMPANY_KEY = "Lightness";

    ///////////////////////////////////////////////////////////////////////////
    // 缓存KEY
    ///////////////////////////////////////////////////////////////////////////
    String CACHE_ATHL_RECORD_FREE = "CACHE_ATHL_RECORD_FREE";
    String CACHE_ATHL_RECORD_PLAN = "CACHE_ATHL_RECORD_PLAN";

    String CACHE_SEARCH_KEY = "CACHE_SEARCH_KEY";//历史搜索词


    String CACHE_SPORT_KET = "CACHE_SPORT_KET";


    ///////////////////////////////////////////////////////////////////////////
    // 常量
    ///////////////////////////////////////////////////////////////////////////
    //饮食类型
    int TYPE_BREAKFAST = 1;
    int TYPE_LUNCH = 2;
    int TYPE_DINNER = 3;
    int TYPED_MEAL = 5;

    //心率阈值
    //100-120-140-160-180
    byte[] heartRates = new byte[]{(byte) 100, (byte) 120, (byte) 140, (byte) 160, (byte) 180};


    /**
     * 1.静息（最小心率）
     * 2.热身
     * 3.燃脂
     * 4.有氧
     * 5.无氧
     * 6.极限
     * 7.最大心率
     */
    int[] HRART_SECTION = new int[]{(byte) 80, (byte) 100, (byte) 120, (byte) 140, (byte) 160, (byte) 180, (byte) 200};

}
