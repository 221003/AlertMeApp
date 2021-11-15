package com.example.alertmeapp.api.responses;


import java.util.List;

public class ResponseMultipleData<T> {

    private String status;
    private Integer statusCode;
    private String error;
    private Integer errorCode;

    private List<T> data;

    public ResponseMultipleData(List<T> data) {
        this.data = data;
    }

    public ResponseMultipleData() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseMultipleData{" +
                "status='" + status + '\'' +
                ", statusCode=" + statusCode +
                ", data=" + data +
                '}';
    }
}
