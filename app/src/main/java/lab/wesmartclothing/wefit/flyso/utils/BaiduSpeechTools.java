package lab.wesmartclothing.wefit.flyso.utils;

import android.app.Application;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;

/**
 * Created by jk on 2018/7/20.
 */
public class BaiduSpeechTools {


    public static void init(Application application) {
        SpeechSynthesizer instance = SpeechSynthesizer.getInstance();
        instance.setContext(application);
        instance.setAppId("");
        instance.setApiKey("", "");
        instance.setParam("", "");
        //选择TtsMode.ONLINE  不需要设置以下参数; 选择TtsMode.MIX 需要设置下面2个离线资源文件的路径
        instance.initTts(TtsMode.MIX);
    }

}
