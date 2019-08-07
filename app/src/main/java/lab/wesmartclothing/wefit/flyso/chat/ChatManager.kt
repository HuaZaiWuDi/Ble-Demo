package lab.wesmartclothing.wefit.flyso.chat

import android.app.Application
import android.content.Intent
import com.hyphenate.chat.ChatClient
import com.hyphenate.chat.ChatManager
import com.hyphenate.chat.Message
import com.hyphenate.helpdesk.callback.Callback
import com.hyphenate.helpdesk.easeui.UIProvider
import com.hyphenate.helpdesk.easeui.util.IntentBuilder
import com.hyphenate.helpdesk.model.AgentIdentityInfo
import com.hyphenate.helpdesk.model.VisitorInfo
import com.vondear.rxtools.activity.RxActivityUtils
import com.vondear.rxtools.utils.RxLogUtils
import com.wesmarclothing.kotlintools.kotlin.utils.d
import com.wesmarclothing.mylibrary.net.RxBus
import lab.wesmartclothing.wefit.flyso.BuildConfig
import lab.wesmartclothing.wefit.flyso.base.MyAPP
import lab.wesmartclothing.wefit.flyso.rxbus.UnreadStateBus

/**
 * @Package lab.wesmartclothing.wefit.flyso.chat
 * @FileName ChatManager
 * @Date 2019/7/26 15:42
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
object ChatManager {


    fun initChat(app: Application) {
        val options = ChatClient.Options()
        //必填项，appkey获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“AppKey”
        options.setAppkey("1407190726061449#kefuchannelapp22550")
        //必填项，tenantId获取地址：kefu.easemob.com，“管理员模式 > 设置 > 企业信息”页面的“租户ID”
        options.setTenantId("22550")
        options.setConsoleLog(BuildConfig.DEBUG)
        // Kefu SDK 初始化
        if (!ChatClient.getInstance().init(app, options)) {
            RxLogUtils.d("【环信Chat】", "初始化失败")
            return
        }
        // Kefu EaseUI的初始化
        UIProvider.getInstance().init(app)
        //后面可以设置其他属性
//        UIProvider.getInstance().setUserProfileProvider { context, message, userAvatarView, usernickView ->
//            //            GlideImageLoader.getInstance().displayImage(context, R.mipmap.icon_app, userAvatarView)
////            usernickView?.text = "智能客服"
//        }

        unreadMsgsCount()

        ChatClient.getInstance().chatManager().addMessageListener(object : ChatManager.MessageListener {
            override fun onMessage(msgs: MutableList<Message>?) {
                "onMessage${msgs?.size}".d()
                unreadMsgsCount()
            }

            override fun onMessageSent() {
                "onMessageSent".d()
            }

            override fun onCmdMessage(msgs: MutableList<Message>?) {
                "onCmdMessage${msgs?.size}".d()
            }

            override fun onMessageStatusUpdate() {
                "onMessageStatusUpdate".d()
            }
        })
    }


    fun unreadMsgsCount() {
        val unreadMsgsCount = ChatClient.getInstance().chatManager().unreadMsgsCount
        "unreadMsgsCount$unreadMsgsCount".d()
        RxBus.getInstance().postSticky(UnreadStateBus(unreadMsgsCount))
    }


    var userName: String = ""
    var password: String = ""


    fun register() {
        userName = MyAPP.gUserInfo.phone
        password = "111111"
        ChatClient.getInstance().register(userName, password, object : Callback {
            override fun onSuccess() {
                login(userName, password)
            }

            override fun onError(code: Int, error: String) {
                RxLogUtils.e("【环信Chat】", "Error:$error----code$code")

                if (code == 203) {
                    login(userName, password)
                }

                /**
                 * //ErrorCode:
                Error.NETWORK_ERROR 网络不可用
                Error.USER_ALREADY_EXIST  用户已存在
                Error.USER_AUTHENTICATION_FAILED 无开放注册权限（后台管理界面设置[开放|授权]）
                Error.USER_ILLEGAL_ARGUMENT 用户名非法
                 */
            }

            override fun onProgress(progress: Int, status: String) {

            }
        })
    }

    fun login(userName: String, password: String) {
        if (ChatClient.getInstance().isLoggedInBefore) {
            //已经登录，可以直接进入会话界面
            RxLogUtils.d("用户$userName 已经登录了")
            joinService()
            return
        } else {
            //未登录，需要登录后，再进入会话界面
        }

        ChatClient.getInstance().login(userName, password, object : Callback {
            override fun onSuccess() {
                joinService()
            }

            override fun onProgress(progress: Int, status: String?) {

            }

            override fun onError(code: Int, error: String?) {
                RxLogUtils.e("【环信Chat】", "Error:$error----code$code")
            }
        })
    }

    fun logout() {
        //第一个参数为是否解绑推送的devicetoken
        ChatClient.getInstance().logout(false, object : Callback {
            override fun onSuccess() {
            }

            override fun onProgress(progress: Int, status: String?) {
            }

            override fun onError(code: Int, error: String?) {
                RxLogUtils.e("【环信Chat】", "Error:$error----code$code")
            }
        })
    }

    fun joinService() {
        val currentActivity = RxActivityUtils.currentActivity() ?: return
        //获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“IM服务号”
        val visitorInfo = VisitorInfo()
        visitorInfo.companyName("智裳科技")
        visitorInfo.name(MyAPP.getgUserInfo().userName)
        visitorInfo.phone(MyAPP.getgUserInfo().phone)
//        visitorInfo.description("详细信息")
        visitorInfo.nickName(MyAPP.getgUserInfo().userName)

        val agentIdentityInfo = AgentIdentityInfo()
        agentIdentityInfo.agentName("智能客服")

        val intent = IntentBuilder(currentActivity)
                .setServiceIMNumber("kefuchannelimid_332674")
                .setTitleName("在线客服")
                .setScheduleAgent(agentIdentityInfo)
                .setVisitorInfo(visitorInfo)
                .setShowUserNick(true)
                .build()
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        currentActivity.startActivity(intent)

        RxBus.getInstance().postSticky(UnreadStateBus(0))
    }

}