package lab.wesmartclothing.wefit.flyso.test

import android.app.Activity
import lab.wesmartclothing.wefit.flyso.netutil.repository.UserRepository

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


    fun main(activity: Activity) {


//        ChatManager.register()
//
//
//        Handler().postDelayed({
//            if (ChatClient.getInstance().isLoggedInBefore)
//                ChatManager.joinService()
//        }, 4000)


        UserRepository.sendSms()

    }


}