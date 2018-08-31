package lab.wesmartclothing.wefit.flyso.utils;

import android.app.Application;
import android.speech.tts.TextToSpeech;

import com.vondear.rxtools.utils.RxLogUtils;

import java.util.Locale;

/**
 * Created by jk on 2018/8/30.
 */
public class TextSpeakUtils {
    private static TextToSpeech mTextToSpeech;

    public static void init(final Application application) {
        mTextToSpeech = new TextToSpeech(application, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                RxLogUtils.e("语音合成：" + status);
                // 判断是否转化成功
                if (status == TextToSpeech.SUCCESS) {
                    //默认设定语言为中文，原生的android貌似不支持中文。
                    int result = mTextToSpeech.setLanguage(Locale.CHINESE);
                    RxLogUtils.e("支持中文:" + result);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        RxLogUtils.e("支持中文");
                    } else {
                        //不支持中文就将语言设置为英文
                        mTextToSpeech.setLanguage(Locale.US);
                    }
                }
            }
        });
    }

    public static TextToSpeech getInstance() {
        return mTextToSpeech;
    }


    public static void speakFlush(String text) {
        if (mTextToSpeech != null) {
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public static void speakAdd(String text) {
        if (mTextToSpeech != null) {
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public static void stop() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
        }
    }

}
