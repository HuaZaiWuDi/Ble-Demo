package lab.wesmartclothing.wefit.flyso.utils.soinc;

import android.content.Context;
import android.content.Intent;

import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConnection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfflinePkgSessionConnection extends SonicSessionConnection {

    private final WeakReference<Context> context;

    public OfflinePkgSessionConnection(Context context, SonicSession session, Intent intent) {
        super(session, intent);
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected int internalConnect() {
        Context ctx = context.get();
        if (null != ctx) {
            try {
                InputStream offlineHtmlInputStream = ctx.getAssets().open("sonic-demo-index.html");
                responseStream = new BufferedInputStream(offlineHtmlInputStream);
                return SonicConstants.ERROR_CODE_SUCCESS;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return SonicConstants.ERROR_CODE_UNKNOWN;
    }

    @Override
    protected BufferedInputStream internalGetResponseStream() {
        return responseStream;
    }

    @Override
    protected String internalGetCustomHeadFieldEtag() {
        return SonicSessionConnection.CUSTOM_HEAD_FILED_ETAG;
    }

    @Override
    public void disconnect() {
        if (null != responseStream) {
            try {
                responseStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public Map<String, List<String>> getResponseHeaderFields() {
        return new HashMap<>(0);
    }

    @Override
    public String getResponseHeaderField(String key) {
        return "";
    }

}