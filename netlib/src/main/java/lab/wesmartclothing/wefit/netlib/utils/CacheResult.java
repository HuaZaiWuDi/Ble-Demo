package lab.wesmartclothing.wefit.netlib.utils;

import com.zchu.rxcache.data.ResultFrom;

public class CacheResult<T> {
    private ResultFrom from;//数据来源，原始observable、内存或硬盘
    private String key;
    private T data; // 数据
    private long timestamp; //数据写入到缓存时的时间戳，如果来自原始observable则为0


    public ResultFrom getFrom() {
        return from;
    }

    public void setFrom(ResultFrom from) {
        this.from = from;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}