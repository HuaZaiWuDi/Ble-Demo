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
        MobclickAgent.onEvent(myApplication, "Clo-$userId-$macAddr", message)
        RxLogUtils.d("Clo-$userId-$macAddr", message)
    }

    /**
     * 体脂称连接状态
     */
    fun scaleState(macAddr: String, state: String) {
        val message = "${RxTimeUtils.getCurrentDateTime("MM-dd HH:mm:ss")}-$state"
        MobclickAgent.onEvent(myApplication, "Scale-$userId-$macAddr", message)
        RxLogUtils.d("Scale-$userId-$macAddr", message)
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
        MobclickAgent.onEvent(myApplication, "Net-$userId", message)
        RxLogUtils.d("Net-$userId-$interfaceName", message)
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