package org.breeze.core.bean.api;

import com.alibaba.fastjson.JSONObject;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.config.ResponseCode;
import org.breeze.core.utils.string.UtilString;

/**
 * @Description: 接口返回工具类
 * @Auther: 黑面阿呆
 * @Date: 2019-12-17 14:48
 * @Version: 1.0.0
 */
public class R {

    private int code;
    private String msg;
    private String data;

    public R() {
    }

    public R(int code) {
        this.code = code;
    }

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(int code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回成功结果
     *
     * @return
     */
    public static R success() {
        return new R(ResponseCode.SUCCESS, ResponseCode.SUCCESS_MSG);
    }

    /**
     * 返回成功结果
     *
     * @param data
     * @return
     */
    public static R success(boolean data) {
        return new R(ResponseCode.SUCCESS, ResponseCode.SUCCESS_MSG, String.valueOf(data));
    }

    /**
     * 返回成功结果
     *
     * @param data
     * @return
     */
    public static R success(int data) {
        return new R(ResponseCode.SUCCESS, ResponseCode.SUCCESS_MSG, String.valueOf(data));
    }

    /**
     * 返回成功结果
     *
     * @param data
     * @return
     */
    public static R success(String data) {
        return new R(ResponseCode.SUCCESS, ResponseCode.SUCCESS_MSG, "\"" + data + "\"");
    }

    /**
     * 返回成功结果
     *
     * @param data
     * @return
     */
    public static R successStr(String data) {
        return new R(ResponseCode.SUCCESS, ResponseCode.SUCCESS_MSG, data);
    }

    /**
     * 返回成功结果
     *
     * @param data
     * @return
     */
    public static R success(JSONObject data) {
        return new R(ResponseCode.SUCCESS, ResponseCode.SUCCESS_MSG, data.toJSONString());
    }

    /**
     * 返回成功结果
     *
     * @param data
     * @return
     */
    public static R success(Data data) {
        return new R(ResponseCode.SUCCESS, ResponseCode.SUCCESS_MSG, data.toJsonStr());
    }

    /**
     * 返回成功结果
     *
     * @param dataList
     * @return
     */
    public static R success(DataList dataList) {
        return new R(ResponseCode.SUCCESS, ResponseCode.SUCCESS_MSG, dataList.toJsonStr());
    }

    /**
     * 返回失败结果
     *
     * @param code
     * @return
     */
    public static R failure(int code) {
        return new R(code, ResponseCode.getResponseMsg(code));
    }

    /**
     * 返回失败结果
     *
     * @param code
     * @param msg
     * @return
     */
    public static R failure(int code, String msg) {
        return new R(code, msg);
    }

    /**
     * 重写toString方法
     *
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("{\"code\":");
        sb.append(code).append(",\"msg\":\"").append(msg).append("\"");
        if (UtilString.isNotEmpty(data)) {
            sb.append(",\"data\":").append(data);
        }
        sb.append("}");
        return sb.toString();
    }
}
