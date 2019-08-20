package lab.wesmartclothing.wefit.flyso.buryingpoint

import android.app.Application
import com.umeng.analytics.MobclickAgent
import com.vondear.rxtools.utils.RxLogUtils
import com.vondear.rxtools.utils.RxNetUtils
import com.vondear.rxtools.utils.dateUtils.RxTimeUtils
import lab.wesmartclothing.wefit.flyso.base.MyAPP

/**
 * @Package lab.wesmartclothing.wefit.flyso.buryingpoint
 * @FileName BuryingPoint
 * @Date 2019/7/17 9:40
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
object BuryingPoint {

    var userId: String = ""
    var myApplication: Application? = null

    init {
        userId = MyAPP.getgUserInfo()?.phone ?: ""
        RxLogUtils.d("用户ID：$userId")

        myApplication = MyAPP.sMyAPP
    }

    /**
     * 瘦身衣连接状态
     */
    fun clothingState(macAddr: String, state: String) {
        val message = "${RxTimeUtils.getCurrentDateTime("MM-dd HH:mm:ss")}-$state"
        MobclickAgent.onEvent(myApplication, userId, "Clo-$macAddr-$message")
        RxLogUtils.d(userId, "Clo-$macAddr-$message")
    }

    /**
     * 体脂称连接状态
     */
    fun scaleState(macAddr: String, state: String) {
        val message = "${RxTimeUtils.getCurrentDateTime("MM-dd HH:mm:ss")}-$state"
        MobclickAgent.onEvent(myApplication, userId, "Scale-$macAddr-$message")
        RxLogUtils.d(userId, "Scale-$macAddr-$message")
    }

    /**
     * 体脂称连接状态
     */
    fun emsState(macAddr: String, state: String) {
        val message = "${RxTimeUtils.getCurrentDateTime("MM-dd HH:mm:ss")}-$state"
        MobclickAgent.onEvent(myApplication, userId, "ems-$macAddr-$message")
        RxLogUtils.d(userId, "ems-$macAddr-$message")
    }

    /**
     * 设备扫描状态
     */
    fun deviceScan() {

    }


    /**
     * 网络异常信息上报
     */
    fun netErrorMessage(interfaceName: String, errorMessage: String) {
        val message = "${RxTimeUtils.getCurrentDateTime("MM-dd HH:mm:ss")}-" +
                "net:${RxNetUtils.isConnected(MyAPP.sMyAPP)}-$errorMessage"
        MobclickAgent.onEvent(myApplication, userId, "net-$message-$interfaceName")
        RxLogUtils.d(userId, "net-$message-$interfaceName")
    }


    /**
     * 手机信息(帧率、内存、电量等)
     */
    fun phoneInfo() {

    }

    /**
     * 应用生命周期
     */
    fun AppLifecycle(appState: String) {
        val message = "${RxTimeUtils.getCurrentDateTime("MM-dd HH:mm:ss")}-$appState"
        MobclickAgent.onEvent(myApplication, "app", message)
        RxLogUtils.d("app", message)
    }

}