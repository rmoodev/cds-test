package com.moore.models.dtos;

public class ErrorResponse extends BaseResponse {

    public static final String INTERNAL_SERVER_ERROR = "An unknown error occurred.";

    public ErrorResponse() {
        super.success = false;
    }
}
