package lab.wesmartclothing.wefit.flyso.utils

import com.alibaba.fastjson.JSON
import com.vondear.rxtools.utils.SPUtils
import com.zchu.rxcache.RxCache
import lab.wesmartclothing.wefit.flyso.base.MyAPP
import lab.wesmartclothing.wefit.flyso.ble.EMSManager
import lab.wesmartclothing.wefit.flyso.ble.MyBleManager
import lab.wesmartclothing.wefit.flyso.ble.QNBleManager
import lab.wesmartclothing.wefit.flyso.chat.ChatManager
import lab.wesmartclothing.wefit.flyso.entity.UserInfo
import lab.wesmartclothing.wefit.flyso.tools.SPKey

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName UserLifecycleManager
 * @Date 2019/8/20 10:47
 * @Author JACK
 * @Describe 用户生命周期管理
 * @Project Android_WeFit_2.0
 */
object UserLifecycleManager {


    val userInfo: UserInfo by lazy {
        JSON.parseObject(SPUtils.getString(SPKey.SP_UserInfo), UserInfo::class.java)
    }


    fun logout() {
        ChatManager.logout()
        val baseUrl = SPUtils.getString(SPKey.SP_BSER_URL)
        val SP_GUIDE = SPUtils.getBoolean(SPKey.SP_GUIDE)
        MyAPP.aMapLocation = null
        SPUtils.clear()
        SPUtils.put(SPKey.SP_BSER_URL, baseUrl)
        SPUtils.put(SPKey.SP_GUIDE, SP_GUIDE)

        MyBleManager.instance.disConnect()
        QNBleManager.getInstance().disConnect()
        EMSManager.instance.disConnect()
        try {
            RxCache.getDefault().clear2()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun login() {

    }

}