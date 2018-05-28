package com.followme.common;

import java.io.Serializable;
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public ServerResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
