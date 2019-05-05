package lab.wesmartclothing.wefit.flyso.tools;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
public interface Key {


    ///////////////////////////////////////////////////////////////////////////
    // BUNDLE跳转
    ///////////////////////////////////////////////////////////////////////////

    String ADD_FOOD_NAME = "ADD_FOOD_NAME";//食物界面跳转携带
    String ADD_FOOD_TYPE = "ADD_FOOD_TYPE";
    String ADD_FOOD_DATE = "ADD_FOOD_DATE";

    String ADD_FOOD_INFO = "ADD_FOOD_INFO";
    String ADDED_FOOD_INFO = "ADDED_FOOD_INFO";//已经添加的书屋列表

    String BUNDLE_WEIGHT_HISTORY = "BUNDLE_WEIGHT_HISTORY";//跳转体重信息
    String BUNDLE_SPORTS_INFO = "BUNDLE_SPORTS_INFO";//跳转运动信息

    String BUNDLE_FORCE_BIND = "BUNDLE_FORCE_BIND";//是否强制绑定
    String BUNDLE_BIND_TYPE = "BUNDLE_BIND_TYPE";//绑定类型


    String BUNDLE_WEB_URL = "BUNDLE_WEB_URL";//跳转网页URL

    String BUNDLE_RELOGIN = "BUNDLE_RELOGIN";//重新登录

    //标题
    String BUNDLE_TITLE = "BUNDLE_TITLE";

    String BUNDLE_DATA = "BUNDLE_DATA";

    String BUNDLE_OTHER_LOGIN_INFO = "BUNDLE_OTHER_LOGIN_INFO";//第三方登录用户信息

    String BUNDLE_FRAGMENT = "BUNDLE_FRAGMENT";//跳转制定页面

    String BUNDLE_DATE_TIME = "BUNDLE_DATE_TIME";//日期
    String BUNDLE_DATA_GID = "BUNDLE_DATA_GID";//数据的GID

    String BUNDLE_IS_CONNECT = "BUNDLE_IS_CONNECT";//传递蓝牙的链接状态

    //设置体重目标传递参数
    String BUNDLE_HAS_DAYS = "BUNDLE_HAS_DAYS";
    String BUNDLE_INITIAL_WEIGHT = "BUNDLE_INITIAL_WEIGHT";
    String BUNDLE_STILL_NEED = "BUNDLE_STILL_NEED";
    String BUNDLE_TARGET_WEIGHT = "BUNDLE_TARGET_WEIGHT";

    //最近一次上秤的真实体重
    String BUNDLE_LAST_WEIGHT = "BUNDLE_LAST_WEIGHT";

    //传递体型类型
    String BUNDLE_BODY_INDEX = "BUNDLE_BODY_INDEX";

    //是否结束前一个Fragment
    String BUNDLE_FINISH_FRAGMENT = "BUNDLE_FINISH_FRAGMENT";

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


    //瘦身衣连接状态
    String ACTION_CLOTHING_CONNECT = "ACTION_CLOTHING_CONNECT";
    String EXTRA_CLOTHING_CONNECT = "EXTRA_CLOTHING_CONNECT";

    //心率更改
    String ACTION_HEART_RATE_CHANGED = "ACTION_HEART_RATE_CHANGED";
    String EXTRA_HEART_RATE_CHANGED = "EXTRA_HEART_RATE_CHANGED";


    //体脂称连接状态
    String ACTION_SCALE_CONNECT = "ACTION_SCALE_CONNECT";
    String EXTRA_SCALE_CONNECT = "EXTRA_SCALE_CONNECT";
    //蓝牙秤状态改变(开始测量)
    String ACTION_STATE_START_MEASURE = "ACTION_STATE_START_MEASURE";


    String ACTION_CLOTHING_STOP = "ACTION_CLOTHING_STOP";
    String EXTRA_CLOTHING_STOP = "EXTRA_CLOTHING_STOP";


    ///////////////////////////////////////////////////////////////////////////
    // 分享登录SDK
    ///////////////////////////////////////////////////////////////////////////
    String QQ_ID = "1106924585";//QQkey：RGcOhc7q8qZMrhxz
    String WX_ID = "wxaaeb0352e04684de";
    String WX_SECRET = "0d23407fe42a2665dabe3ea2a958daf9";
    String WEIBO_ID = "3322261844";
    String WB_URL = "https://sns.whalecloud.com/sina2/callback";

    String LoginType_WEXIN = "WeChat";
    String LoginType_QQ = "QQ";
    String LoginType_WEIBO = "MicroBlog";

    //BUGLY
    String BUGly_id = "11c87579c7";


    String APP_KEY = "Timetofit";

    String COMPANY_KEY="weSmartClothing";

    ///////////////////////////////////////////////////////////////////////////
    // 缓存KEY
    ///////////////////////////////////////////////////////////////////////////
    String CACHE_ATHL_RECORD_FREE = "CACHE_ATHL_RECORD_FREE";
    String CACHE_ATHL_RECORD_PLAN = "CACHE_ATHL_RECORD_PLAN";

    String CACHE_SEARCH_KEY = "CACHE_SEARCH_KEY";//历史搜索词

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
    byte[] HRART_SECTION = new byte[]{(byte) 80, (byte) 100, (byte) 120, (byte) 140, (byte) 160, (byte) 180, (byte) 200};

}
