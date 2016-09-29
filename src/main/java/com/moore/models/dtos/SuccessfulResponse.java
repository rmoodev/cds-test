package com.moore.models.dtos;

/**
 * ryanmoore - 9/27/16.
 */
public class SuccessfulResponse extends BaseResponse {

    public SuccessfulResponse() {
        super.message = "Success.";
        super.success = true;
    }
}
