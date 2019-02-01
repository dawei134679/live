package com.qiniu.pili;

import okhttp3.Response;
import com.google.gson.Gson;

public class PiliException extends Exception {
    private String message;
    private  int code;


    public PiliException(Response response) {
        try {
            ErrorResp err = new Gson().fromJson(response.body().string(), ErrorResp.class);
            this.message = err.error;
            this.code = response.code();
        } catch (Exception e) {
            e.printStackTrace();
            this.message = null;
            this.code = 0;
        }
    }

    public PiliException(String msg) {
        super(msg);
        this.message = msg;
    }

    public PiliException(Exception e) {
        super(e);
        this.message= null;
    }

    public int code() {
        return code == 0 ? -1 : code;
    }

    public boolean isDuplicate() {
        return code() == 614;
    }

    public boolean isNotFound() {
        return code() == 612;
    }

    public boolean isNotInLive() {
        return code() == 619;
    }

    public String getMessage() {
        return message;
    }

    class ErrorResp {
        String error;
    }

}
