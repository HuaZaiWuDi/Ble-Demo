//package lab.dxythch.com.netlib.utils;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.util.Log;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.Locale;
//
//import lab.dxythch.com.netlib.R;
//import lab.dxythch.com.netlib.net.RetrofitService;
//import lab.dxythch.com.netlib.net.ServiceAPI;
//import lab.dxythch.com.netlib.rx.NetManager;
//import lab.dxythch.com.netlib.rx.RxManager;
//import lab.dxythch.com.netlib.rx.RxSubscriber;
//
///**
// * 项目名称：Bracelet
// * 类描述：
// * 创建人：Jack
// * 创建时间：2017/5/24
// */
//public class UpdateManager {
//    public static final int TYPE_APP = 1;
//    public static final int TYPE_BIN = 2;
//    Context mContext;
//
//
//    String TAG = "【UpdateManager】";
//
//
//    public onAppUpgradeListener mOnAppUpgradeListener;
//    public onOtaUpgradeListener mOnOtaUpgradeListener;
//
//    public interface onAppUpgradeListener {
//        void onFindNewVersion(boolean isNewVersion, String downloadUrl, String newVersionInfo);
//
//    }
//
//    public interface onOtaUpgradeListener {
//        void onFindNewVersion(boolean isNewVersion, String downloadUrl, String newVersionInfo);
//
//    }
//
//    public void setOnAppUpgradeListener(onAppUpgradeListener mOnAppUpgradeListener) {
//        this.mOnAppUpgradeListener = mOnAppUpgradeListener;
//    }
//
//    public void setOnOtaUpgradeListener(onOtaUpgradeListener mOnOtaUpgradeListener) {
//        this.mOnOtaUpgradeListener = mOnOtaUpgradeListener;
//    }
//
//
//    public UpdateManager(Context mContext) {
//        this.mContext = mContext;
//    }
//
//
//    public void getOtaUpgradeInfo(String updateParems, final int currentCode) {
//
//        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
//        RxManager.getInstance().doSubscribe(dxyService.getUpgradeVersion(updateParems), new RxSubscriber<String>() {
//
//            @Override
//            protected void _onNext(String s) {
//                Log.d(TAG, "otaInfo:" + s);
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    String i = jsonObject.getString("i");
//                    JSONObject jsonObject1 = new JSONObject(i);
//                    String version = jsonObject1.getString("bin_build");
//                    String fileName = jsonObject1.getString("bin_filename");
//                    String newOTtaInfo = jsonObject1.getString("description");
//                    String newOTtaENInfo = jsonObject1.getString("description_en");
//
//
//                    boolean isNewVersion = Integer.parseInt(version) > currentCode;
//                    if (isZh(mContext))
//                        mOnOtaUpgradeListener.onFindNewVersion(isNewVersion, ServiceAPI.UPDATE_LODERA_OTA_URL + fileName, newOTtaInfo);
//                    else
//                        mOnOtaUpgradeListener.onFindNewVersion(isNewVersion, ServiceAPI.UPDATE_LODERA_OTA_URL + fileName, newOTtaENInfo);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//
//    public void getAppUpgradeInfo(String updateParems, final int currentCode) {
//        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
//        RxManager.getInstance().doSubscribe(dxyService.getUpgradeAppVersion(updateParems), new RxSubscriber<String>() {
//
//            @Override
//            protected void _onNext(String s) {
//                Log.d(TAG, "appInfo:" + s);
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    String i = jsonObject.getString("i");
//                    JSONObject jsonObject1 = new JSONObject(i);
//                    String versionCode = jsonObject1.getString("versionCode");
//                    String version = jsonObject1.getString("android_build");
//                    String fileName = jsonObject1.getString("android_filename");
//                    String newAPPInfo = jsonObject1.getString("description");
//                    String newAPPENInfo = jsonObject1.getString("description_en");
//                    String android_file_size = jsonObject1.getString("android_file_size");
//                    Log.d(TAG, "appVersion: " + version);
//                    Log.d(TAG, "fileName: " + fileName);
//                    Log.d(TAG, "description: " + newAPPInfo);
//                    Log.d(TAG, "description_en: " + newAPPENInfo);
//                    Log.d(TAG, "appVersion:" + "----Integer.parseInt(versionCode):" + Integer.parseInt(version));
//
//                    String AppInFo = "版本信息" + newAPPInfo + "\n版本号/：" + versionCode + "\n新版本大小：" + android_file_size + "M";
////                    String AppInFo = mContext.getString(R.string.applicationInfo, newAPPInfo, versionCode, android_file_size);
//
//
//                    boolean isNewVersion = Integer.parseInt(version) > currentCode;
//
//                    if (isZh(mContext))
//                        mOnAppUpgradeListener.onFindNewVersion(isNewVersion, ServiceAPI.UPDATE_LODER_APP_URL + fileName, AppInFo);
//                    else
//                        mOnAppUpgradeListener.onFindNewVersion(isNewVersion, ServiceAPI.UPDATE_LODER_APP_URL + fileName, AppInFo);
//                    Log.d(TAG, "发现新版本!");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    public void downLoadFile(final OnOTAUpdateListener onOTAUpdateListener, String updateParems) {
//        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
//        RxManager.getInstance().doSubscribe(dxyService.getUpgradeAppVersion(updateParems), new RxSubscriber<String>() {
//            @Override
//            protected void _onNext(String s) {
////                downLoadAppOrBin(s, onOTAUpdateListener);
//            }
//        });
//    }
//
//
//    public void downLoadAppOrBin(String appUrl, final OnOTAUpdateListener onOTAUpdateListener) {
//        final String dirName = Environment.getExternalStorageDirectory() + "/" + mContext.getString(R.string.app_name) + "/";
//
//        @SuppressLint("StaticFieldLeak")
//        AsyncTask<String, Void, Void> execute = new AsyncTask<String, Void, Void>() {
//            @Override
//            protected Void doInBackground(String... params) {
//                try {
//                    File dirFile = new File(dirName);
//                    if (!dirFile.exists()) {
//                        dirFile.mkdir();
//                    }
//                    URL url = new URL(params[0]);
//                    URLConnection connection = url.openConnection();
//
//                    int contentLength = connection.getContentLength();
//                    Log.d(TAG, "长度：" + contentLength);
//
//                    String newFilename = params[0].substring(params[0].lastIndexOf("=") + 1);
//                    newFilename = dirName + newFilename;
//                    Log.d(TAG, "newFilename：" + newFilename);
//                    File file = new File(newFilename);
//
//                    if (file.exists()) {
//                        if (file.delete()) {
//                            Log.d(TAG, "newFilename：" + "已存在，删除!");
//                        }
//                    }
//                    byte[] bs = new byte[128];
//                    int len;
//                    int hasRead = 0;
//                    int progress;
//                    int progressBefore = 0;
//                    onOTAUpdateListener.start(contentLength);
//                    InputStream is = connection.getInputStream();
//                    OutputStream os = new FileOutputStream(newFilename);
//                    while ((len = is.read(bs)) != -1) {
//                        os.write(bs, 0, len);
//                        hasRead += len;
//                        progress = (int) ((double) hasRead / (double) contentLength * 100);
//                        if (progressBefore != progress) {
//                            progressBefore = progress;
//                            onOTAUpdateListener.progressChanged(contentLength, hasRead, progress);
//                            Log.d(TAG, "progress：" + progress + "%");
//                            if (progress == 100) {
//                                File apkfile = new File(dirName, params[0].substring(params[0].lastIndexOf("=") + 1));
//                                onOTAUpdateListener.finish(apkfile.getPath());
////                                if (type == TYPE_APP) {
//////                                    RxAppUpdateUtils.installApp(mContext, apkfile, Contents.appPackageName);
////
////                                } else if (type == TYPE_BIN) {
//////                                    updateOTA(newFilename);
////                                }
//                            }
//                        }
//                    }
//                    is.close();
//                    os.close();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    onOTAUpdateListener.error(e);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    onOTAUpdateListener.error(e);
//                }
//                return null;
//            }
//        }.execute(appUrl);
//
//    }
//
//
//    public interface OnOTAUpdateListener {
//        void progressChanged(long len, long current, int progress);
//
//        void finish(String path);
//
//        void start(long len);
//
//        void error(Exception throwable);
//    }
//
//    private OnOTAUpdateListener onOTAUpdateListener;
//
//    public void setOnOTAUpdateListener(OnOTAUpdateListener onOTAUpdateListener) {
//        this.onOTAUpdateListener = onOTAUpdateListener;
//    }
//
//
//    //zh：汉语  en：英语
//    public boolean isZh(Context mContext) {
//        Locale locale = mContext.getResources().getConfiguration().locale;
//        String language = locale.getLanguage();
//        if (language.endsWith("zh"))
//            return true;
//        else
//            return false;
//    }
//}
