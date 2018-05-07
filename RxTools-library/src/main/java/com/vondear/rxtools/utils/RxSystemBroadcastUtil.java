package com.vondear.rxtools.utils;

import android.bluetooth.BluetoothAdapter;

/**
 * 项目名称：CheckInOut
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/9/7
 */
public class RxSystemBroadcastUtil {

    //是否息屏
    public static final String SCREEN_ON = "android.intent.action.SCREEN_ON";
    public static final String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

    //蓝牙是否关闭
    public static final String ACTION_BLUETOOTH = BluetoothAdapter.ACTION_STATE_CHANGED;
    public static final String EXTRA_BLUETOOTH_STATE = BluetoothAdapter.EXTRA_STATE;
    public static final int[] BLUETOOTH_STATES = {BluetoothAdapter.STATE_OFF, BluetoothAdapter.STATE_TURNING_OFF, BluetoothAdapter.STATE_ON, BluetoothAdapter.STATE_TURNING_ON};

    /**
     *  if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
     //            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
     //                    BluetoothAdapter.ERROR);
     //            switch (state) {
     //                case BluetoothAdapter.STATE_OFF:
     //                    Log.d("aaa", "STATE_OFF 手机蓝牙关闭");
     //                    break;
     //                case BluetoothAdapter.STATE_TURNING_OFF:
     //                    Log.d("aaa", "STATE_TURNING_OFF 手机蓝牙正在关闭");
     //                    break;
     //                case BluetoothAdapter.STATE_ON:
     //                    Log.d("aaa", "STATE_ON 手机蓝牙开启");
     //                    break;
     //                case BluetoothAdapter.STATE_TURNING_ON:
     //                    Log.d("aaa", "STATE_TURNING_ON 手机蓝牙正在开启");
     //                    break;
     //            }
     //        }
     *
     * */

    //蓝牙是否配对
    /***
     *  if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
     //            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
     //            String name = device.getName();
     //            Log.d("aaa", "device name: " + name);
     //            int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
     //            switch (state) {
     //                case BluetoothDevice.BOND_NONE:
     //                    Log.d("aaa", "BOND_NONE 删除配对");
     //                    break;
     //                case BluetoothDevice.BOND_BONDING:
     //                    Log.d("aaa", "BOND_BONDING 正在配对");
     //                    break;
     //                case BluetoothDevice.BOND_BONDED:
     //                    Log.d("aaa", "BOND_BONDED 配对成功");
     //                    break;
     //            }
     //        }
     *
     * */

    //wifi状态改变
    /***
     *action: WifiManager.WIFI_STATE_CHANGED_ACTION
     *
     *  int statusInt = bundle.getInt("wifi_state");
     switch (statusInt) {
     case WifiManager.WIFI_STATE_UNKNOWN:
     Log.i(TAG, "未知状态");
     break;
     case WifiManager.WIFI_STATE_ENABLING:
     Log.i(TAG, "wifi正在连接");
     break;
     case WifiManager.WIFI_STATE_ENABLED:
     Log.i(TAG, "wifi可用");
     break;
     case WifiManager.WIFI_STATE_DISABLING:
     Log.i(TAG, "wifi正在断开连接");
     break;
     case WifiManager.WIFI_STATE_DISABLED:
     Log.i(TAG, "wifi不可用");
     break;
     *
     * */


    //监听Home健
    /***
     *
     * action:  Intent.ACTION_CLOSE_SYSTEM_DIALOGS
     *
     * static final String SYSTEM_REASON = "reason";
     static final String SYSTEM_HOME_KEY = "homekey";
     static final String SYSTEM_RECENT_APPS = "recentapps";
     private boolean isRegisterReceiver = false;

     @Override public void onReceive(Context context, Intent intent) {
     String action = intent.getAction();
     if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
     String reason = intent.getStringExtra(SYSTEM_REASON);
     if (reason != null) {
     if (reason.equals(SYSTEM_HOME_KEY)) {
     // home key处理点
     Log.i(TAG, "点击了Home键");
     } else if (reason.equals(SYSTEM_RECENT_APPS)) {
     // long home key处理点
     Log.i(TAG, "长按了Home键");
      *
      *
      * */

    //短信监听拦截
    /***
     *
     * action:  android.provider.Telephony.SMS_RECEIVED

     Object[] pdus = (Object[]) intent.getExtras().get("pdus");      // 获取短信数据(可能有多段)
     for (Object pdu : pdus) {
     SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);    // 把短信数据封装成SmsMessage对象
     Date date = new Date(sms.getTimestampMillis());             // 短信时间
     String address = sms.getOriginatingAddress();               // 获取发信人号码
     String body = sms.getMessageBody();                         // 短信内容

     System.out.println(date + ", " + address + ", " + body);
     if ("18600012345".equals(address)) {
     abortBroadcast();
     return;
     }
     }
     *
     * **/

    //常用系统广播
    /***
     * String ADD_SHORTCUT_ACTION 动作：在系统中添加一个快捷方式。
     String ALL_APPS_ACTION 动作：列举所有可用的应用。输入：无。
     String ALTERNATIVE_CATEGORY 类别：说明 activity 是用户正在浏览的数据的一个可选操作。
     String ANSWER_ACTION 动作：处理拨入的电话。
     String BATTERY_CHANGED_ACTION 广播：充电状态，或者电池的电量发生变化。
     String BOOT_COMPLETED_ACTION 广播：在系统启动后，这个动作被广播一次（只有一次）。
     String BROWSABLE_CATEGORY 类别：能够被浏览器安全使用的 activities 必须支持这个类别。
     String BUG_REPORT_ACTION 动作：显示 activity 报告错误。
     String CALL_ACTION 动作：拨打电话，被呼叫的联系人在数据中指定。
     String CALL_FORWARDING_STATE_CHANGED_ACTION 广播：语音电话的呼叫转移状态已经改变。
     String CLEAR_CREDENTIALS_ACTION 动作：清除登陆凭证 (credential)。
     String CONFIGURATION_CHANGED_ACTION 广播：设备的配置信息已经改变，参见 Resources.Configuration.
     Creator CREATOR 无 无
     String DATA_ACTIVITY_STATE_CHANGED_ACTION 广播：电话的数据活动(data activity)状态（即收发数据的状态）已经改变。
     String DATA_CONNECTION_STATE_CHANGED_ACTION 广播：电话的数据连接状态已经改变。
     String DATE_CHANGED_ACTION 广播：日期被改变。
     String DEFAULT_ACTION 动作：和 VIEW_ACTION 相同，是在数据上执行的标准动作。
     String DEFAULT_CATEGORY 类别：如果 activity 是对数据执行确省动作（点击, center press）的一个选项，需要设置这个类别。
     String DELETE_ACTION 动作：从容器中删除给定的数据。
     String DEVELOPMENT_PREFERENCE_CATEGORY 类别：说明 activity 是一个设置面板 (development preference panel).
     String DIAL_ACTION 动作：拨打数据中指定的电话号码。
     String EDIT_ACTION 动作：为制定的数据显示可编辑界面。
     String EMBED_CATEGORY 类别：能够在上级（父）activity 中运行。
     String EMERGENCY_DIAL_ACTION 动作：拨打紧急电话号码。
     int FORWARD_RESULT_LAUNCH 启动标记：如果这个标记被设置，而且被一个已经存在的 activity 用来启动新的 activity，已有 activity 的回复目标 (reply target) 会被转移给新的 activity。
     String FOTA_CANCEL_ACTION 广播：取消所有被挂起的 (pending) 更新下载。
     String FOTA_INSTALL_ACTION 广播：更新已经被确认，马上就要开始安装。
     String FOTA_READY_ACTION 广播：更新已经被下载，可以开始安装。
     String FOTA_RESTART_ACTION 广播：恢复已经停止的更新下载。
     String FOTA_UPDATE_ACTION 广播：通过 OTA 下载并安装操作系统更新。
     String FRAMEWORK_INSTRUMENTATION_TEST_CATEGORY 类别：To be used as code under test for framework instrumentation tests.
     String GADGET_CATEGORY 类别：这个 activity 可以被嵌入宿主 activity (activity that is hosting gadgets)。
     String GET_CONTENT_ACTION 动作：让用户选择数据并返回。
     String HOME_CATEGORY 类别：主屏幕 (activity)，设备启动后显示的第一个 activity。
     String INSERT_ACTION 动作：在容器中插入一个空项 (item)。
     String INTENT_EXTRA 附加数据：和 PICK_ACTIVITY_ACTION 一起使用时，说明用户选择的用来显示的 activity；和 ADD_SHORTCUT_ACTION 一起使用的时候，描述要添加的快捷方式。
     String LABEL_EXTRA 附加数据：大写字母开头的字符标签，和 ADD_SHORTCUT_ACTION 一起使用。
     String LAUNCHER_CATEGORY 类别：Activity 应该被显示在顶级的 launcher 中。
     String LOGIN_ACTION 动作：获取登录凭证。
     String MAIN_ACTION 动作：作为主入口点启动，不需要数据。
     String MEDIABUTTON_ACTION 广播：用户按下了“Media Button”。
     String MEDIA_BAD_REMOVAL_ACTION 广播：扩展介质（扩展卡）已经从 SD 卡插槽拔出，但是挂载点 (mount point) 还没解除 (unmount)。
     String MEDIA_EJECT_ACTION 广播：用户想要移除扩展介质（拔掉扩展卡）。
     String MEDIA_MOUNTED_ACTION 广播：扩展介质被插入，而且已经被挂载。
     String MEDIA_REMOVED_ACTION 广播：扩展介质被移除。
     String MEDIA_SCANNER_FINISHED_ACTION 广播：已经扫描完介质的一个目录。
     String MEDIA_SCANNER_STARTED_ACTION 广播：开始扫描介质的一个目录。
     String MEDIA_SHARED_ACTION 广播：扩展介质的挂载被解除 (unmount)，因为它已经作为 USB 大容量存储被共享。
     String MEDIA_UNMOUNTED_ACTION 广播：扩展介质存在，但是还没有被挂载 (mount)。
     String MESSAGE_WAITING_STATE_CHANGED_ACTION 广播：电话的消息等待（语音邮件）状态已经改变。
     int MULTIPLE_TASK_LAUNCH 启动标记：和 NEW_TASK_LAUNCH 联合使用，禁止将已有的任务改变为前景任务 (foreground)。
     String NETWORK_TICKLE_RECEIVED_ACTION 广播：设备收到了新的网络 "tickle" 通知。
     int NEW_TASK_LAUNCH 启动标记：设置以后，activity 将成为历史堆栈中的第一个新任务（栈顶）。
     int NO_HISTORY_LAUNCH 启动标记：设置以后，新的 activity 不会被保存在历史堆栈中。
     String PACKAGE_ADDED_ACTION 广播：设备上新安装了一个应用程序包。
     String PACKAGE_REMOVED_ACTION 广播：设备上删除了一个应用程序包。
     String PHONE_STATE_CHANGED_ACTION 广播：电话状态已经改变。
     String PICK_ACTION 动作：从数据中选择一个项目 (item)，将被选中的项目返回。
     String PICK_ACTIVITY_ACTION 动作：选择一个 activity，返回被选择的 activity 的类（名）。
     String PREFERENCE_CATEGORY 类别：activity是一个设置面板 (preference panel)。
     String PROVIDER_CHANGED_ACTION 广播：更新将要（真正）被安装。
     String PROVISIONING_CHECK_ACTION 广播：要求 polling of provisioning service 下载最新的设置。
     String RUN_ACTION 动作：运行数据（指定的应用），无论它（应用）是什么。
     String SAMPLE_CODE_CATEGORY 类别：To be used as an sample code example (not part of the normal user experience).
     String SCREEN_OFF_ACTION 广播：屏幕被关闭。
     String SCREEN_ON_ACTION 广播：屏幕已经被打开。
     String SELECTED_ALTERNATIVE_CATEGORY 类别：对于被用户选中的数据，activity 是它的一个可选操作。
     String SENDTO_ACTION 动作：向 data 指定的接收者发送一个消息。
     String SERVICE_STATE_CHANGED_ACTION 广播：电话服务的状态已经改变。
     String SETTINGS_ACTION 动作：显示系统设置。输入：无。
     String SIGNAL_STRENGTH_CHANGED_ACTION 广播：电话的信号强度已经改变。
     int SINGLE_TOP_LAUNCH 启动标记：设置以后，如果 activity 已经启动，而且位于历史堆栈的顶端，将不再启动（不重新启动） activity。
     String STATISTICS_REPORT_ACTION 广播：要求 receivers 报告自己的统计信息。
     String STATISTICS_STATE_CHANGED_ACTION 广播：统计信息服务的状态已经改变。
     String SYNC_ACTION 动作：执行数据同步。
     String TAB_CATEGORY 类别：这个 activity 应该在 TabActivity 中作为一个 tab 使用。
     String TEMPLATE_EXTRA 附加数据：新记录的初始化模板。
     String TEST_CATEGORY 类别：作为测试目的使用，不是正常的用户体验的一部分。
     String TIMEZONE_CHANGED_ACTION 广播：时区已经改变。
     String TIME_CHANGED_ACTION 广播：时间已经改变（重新设置）。
     String TIME_TICK_ACTION 广播：当前时间已经变化（正常的时间流逝）。
     String UMS_CONNECTED_ACTION 广播：设备进入 USB 大容量存储模式。
     String UMS_DISCONNECTED_ACTION 广播：设备从 USB 大容量存储模式退出。
     String UNIT_TEST_CATEGORY 类别：应该被用作单元测试（通过 test harness 运行）。
     String VIEW_ACTION 动作：向用户显示数据。
     String WALLPAPER_CATEGORY 类别：这个 activity 能过为设备设置墙纸。
     String WALLPAPER_CHANGED_ACTION 广播：系统的墙纸已经改变。
     String WALLPAPER_SETTINGS_ACTION 动作：显示选择墙纸的设置界面。输入：无。
     String WEB_SEARCH_ACTION 动作：执行 web 搜索。
     String XMPP_CONNECTED_ACTION 广播：XMPP 连接已经被建立。
     String XMPP_DISCONNECTED_ACTION 广播：XMPP 连接已经被断开。

     */


}
