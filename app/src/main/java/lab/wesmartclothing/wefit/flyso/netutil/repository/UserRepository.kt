package lab.wesmartclothing.wefit.flyso.netutil.repository

import com.vondear.rxtools.utils.RxLogUtils
import com.vondear.rxtools.view.RxToast
import com.wesmarclothing.kotlintools.kotlin.utils.d
import lab.wesmartclothing.wefit.flyso.entity.UserInfo
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils
import lab.wesmartclothing.wefit.flyso.view.TipDialog

/**
 * @Package lab.wesmartclothing.wefit.flyso.netutil.repository
 * @FileName UserRepository
 * @Date 2019/7/30 17:36
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
object UserRepository {


    fun saveUserInfo(info: UserInfo) {
        NetManager.getApiService().saveUserInfo(info)
                .compose(RxComposeUtils.handleResult2())
                .compose(RxComposeUtils.showDialog(TipDialog()))
                .compose(RxComposeUtils.rxThreadHelper())
                .subscribe(object : RxNetSubscriber<String>() {
                    override fun _onNext(s: String) {

                    }

                    override fun _onError(error: String, code: Int) {
                        RxToast.error(error, code)
                    }
                })
    }


    fun sendSms() {
        NetManager.getApiService().sendSms("17665248850")
                .compose(RxComposeUtils.handleResult2())
                .compose(RxComposeUtils.rxThreadHelper())
                .compose(RxComposeUtils.showDialog(TipDialog()))
                .subscribe(object : RxNetSubscriber<Any>() {
                    override fun _onNext(s: Any) {
                        s.d("sendSms")
                        RxLogUtils.d("sendSms：" + Thread.currentThread().name)
                    }

                    override fun _onError(error: String, code: Int) {
                        RxToast.error(error, code)
                    }
                })
    }

}