package com.wangzehao.flashsale.common;

import com.wangzehao.flashsale.common.enums.CustomResponseStatus;

public class CustomAbstractResponse {
    private CustomResponseStatus status;
    private int code;
    private String message;

    protected CustomAbstractResponse(CustomResponseStatus status, String message) {
        this.code = status.getCode();
        this.status = status;
        this.message = message;
    }

    protected CustomAbstractResponse(CustomResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.status = status;
    }

    public static boolean isSuccess(CustomAbstractResponse result) {
        return result != null && result.status == CustomResponseStatus.SUCCESS && result.getCode() == CustomResponseStatus.SUCCESS.getCode();
    }

    public CustomAbstractResponse withError(CustomResponseStatus status) {
        this.status = status;
        return this;
    }

    public CustomAbstractResponse withError(String message) {
        this.status = CustomResponseStatus.ERROR;
        this.message = message;
        return this;
    }

    public CustomAbstractResponse withError(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public CustomAbstractResponse success() {
        this.status = CustomResponseStatus.SUCCESS;
        return this;
    }
    public CustomResponseStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message == null ? this.status.getMessage() : this.message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
