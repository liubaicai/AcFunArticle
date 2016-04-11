package net.liubaicai.android.acfun.models;

/**
 * Created by liush on 2016/4/10.
 */
public class CheckInResult {

    /**
     * code : 200
     * data : {"count":3,"msg":"签到成功，已领取3蕉"}
     * message : OK
     */

    private int code;
    /**
     * count : 3
     * msg : 签到成功，已领取3蕉
     */

    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        private int count;
        private String msg;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
