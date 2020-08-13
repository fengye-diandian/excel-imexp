package com.fengye.excel.imexp.model;

/**
 * @author zhoufeng
 * @Description:
 * @date 2020-08-11 11:44
 */
public class ParesResult<T> {
    private static final String SUCCESS_CODE = "0000";
    private static final String SUCCESS_MESSAGE = "Excel解析成功";

    private String code;
    private String msg;
    private T data;



    public static ParesResult error(String code, String message){
        return new ParesResult(){{
            setCode(code);
            setMsg(message);
        }};
    }

    public static <T> ParesResult<T> success(T data){
        return new ParesResult(){{
            setCode(SUCCESS_CODE);
            setMsg(SUCCESS_MESSAGE);
            setData(data);
        }};
    }


    public static <T> ParesResult<T> success(){
        return new ParesResult(){{
            setCode(SUCCESS_CODE);
            setMsg(SUCCESS_MESSAGE);
        }};
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
