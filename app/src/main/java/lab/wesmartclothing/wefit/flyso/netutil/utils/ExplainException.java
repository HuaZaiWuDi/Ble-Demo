package lab.wesmartclothing.wefit.flyso.netutil.utils;

/**
 * Created by jk on 2018/8/15.
 * 解释性异常 用于返回后台异常信息
 */
public class ExplainException extends Exception {

    private String msg;
    private int Code;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public ExplainException(String message, int code) {
        super(message);
        this.msg = message;
        Code = code;
    }

    @Override
    public String toString() {
        return "ExplainException{" +
                "msg='" + msg + '\'' +
                ", Code=" + Code +
                '}';
    }
}
