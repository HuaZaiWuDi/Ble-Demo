package lab.wesmartclothing.wefit.flyso.test

import android.support.v7.app.AppCompatActivity
import com.wesmarclothing.kotlintools.kotlin.utils.d
import com.wesmarclothing.mylibrary.net.RxBus
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SportingActivity
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils

/**
 * @Package lab.wesmartclothing.wefit.flyso.test
 * @FileName Test
 * @Date 2019/7/26 15:49
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
object Test {

    const val testEnable = false


    fun main(activity: AppCompatActivity) {


//        ChatManager.register()
//
//
//        Handler().postDelayed({
//            if (ChatClient.getInstance().isLoggedInBefore)
//                ChatManager.joinService()
//        }, 4000)

//        TodayStepManager.startTodayStepService(activity.application)

        SportingActivity.start(activity, "")

        RxBus.getInstance().register2(String::class.java)
                .compose(RxComposeUtils.bindLifeResume(activity))
                .subscribe {
                    it.d()
                }

        RxBus.getInstance().post("onCreate")


    }


}