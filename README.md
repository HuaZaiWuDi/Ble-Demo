
莱特妮丝智瘦


调试SHA1：18:6F:C8:FE:18:F9:9B:38:AE:44:26:E5:F3:E4:11:69:9E:21:43:15
发布SHA1： 18:6F:C8:FE:18:F9:9B:38:AE:44:26:E5:F3:E4:11:69:9E:21:43:15
Debug签名：2a34a83c3bc235b96568a56ef62a65c0
Release签名：2a34a83c3bc235b96568a56ef62a65c0
应用包名：com.lightness.wisenfit

//友盟第三方
应用名：TimeToFit-Android
AppKey：5ca338162036572bc50017d8

//极光
AppKey 312a083f2aa786ea41bc8bfd
Master Secret ? 159b414e200ec23833c19e91


微信开放平台：
AppID：wx270d9fa3441b877e
AppSecret：2500e5de5ef522a51c816a58993b0333

QQ开放平台：
安卓：
APP ID：1108606528
APP KEY：yVyd71DGzFu8iJut

微博开放平台：
App Key:613183269
AppSecret：49b5d1a655eb0be6e0854ed32c9e3b7e


安卓 - 高德：
Key：d08d76ae6bd68061111b1bfd697accf1
AppKey：5c662b4cf1f5567e310009e5



**瘦身衣项目文档**

**一、项目的架构：**

	MVC+RxJava2+Retrofit2+RxBus的组合
  	MVC：项目开发模式
	RxJava+Rettofit2：网络请求的组合
	RxBus：应用内通知监听


**二、相关流程**

	蓝牙操作流程：开启应用时启动扫描，手机息屏结束扫描，扫描到指定类型设备，判断是否绑定，绑定连接，连接失败重连，直至连接成功。
		连接成功开启数据通道，读写数据，所以操作添加失败重试3次的操作。同步蓝牙数据，接收蓝牙通知消息。
	蓝牙升级流程，判断蓝牙连接，已连接请求蓝牙版本并上传后台，后台返回升级信息，有最新版本，然后下载升级文件，进入升级模式，开始升级
		，升级成功，提示用户，重新连接
		
**三、技术点**

	封装了网络请求的操作，不同的接口分开处理，请求头，公共参数统一添加；
	封装了Base，所有界面的重复性操作放到Base里面；
	自定义复杂的UI效果；
	封装了蓝牙扫描、连接、重连、升级、数据交互的操作。增加去重，失败重试的功能。
	使用RxBus做全局事件更新，代替广播实现数据和event的通知，优化广播的注册造成性能损耗
	使用Service 实现所有蓝牙扫描、连接数据交互的操作、防止因为Activity的生命周期的产生的影响
	使用以屏幕宽度作为标准的屏幕适配方案，不同手机屏幕只要屏幕宽度是一致的，展示的内容大小就是一致的。
	使用状态模式，处理蓝牙各种状态
	使用工厂模式
	实现APP运动时退回后台开启前台服务及息屏展示运动界面的操作，防止运动时APP被后台杀死
	运动界面实时保存防止数据丢失
	
**四、使用的第三方库和SDK**

	轻牛体脂称SDK；
	动态权限Rxpermissions；
	UI、工具库RxTool、QMUI_Android ；
	Json数据解析Gson；
	异常捕捉Bugly；
	图片加载缓存库Glide。
	soinc : 网页缓存
	RxCache：数据内存缓存、磁盘缓存框架
	Umeng：友盟第三方登录分享工具
	FastBle：蓝牙扫描连接库
	Noridc DFU：dfu空中升级库
	Leakcanary：facebook的内存泄露检测性能优化框架
	高德地图定位库
	bugly :腾讯崩溃、升级、热更新库