package com.moore.models.dtos;

/**
 * ryanmoore - 9/27/16.
 */
public class BaseResponse {

    protected Object result;
    protected String message;
    protected Boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
