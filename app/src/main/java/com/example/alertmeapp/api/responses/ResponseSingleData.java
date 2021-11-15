package com.example.alertmeapp.api.responses;


public class ResponseSingleData<T> {

    private String status;
    private int statusCode;
    private String error;
    private int errorCode;
    private T data;

    public ResponseSingleData(T data) {
        this.data = data;
    }

    public ResponseSingleData() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ResponseSingleData{" +
                "status='" + status + '\'' +
                ", statusCode=" + statusCode +
                ", error='" + error + '\'' +
                ", errorCode=" + errorCode +
                ", data=" + data +
                '}';
    }
}
