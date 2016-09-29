package com.moore.controllers;

import com.moore.Application;
import com.moore.models.dtos.BaseResponse;
import com.moore.models.dtos.ErrorResponse;
import com.moore.models.dtos.SuccessfulResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1")
public class PalindromeController {

    /**
     *  GET request method for checking whether the request method is a palindrome, or not.
     * @param testString - String request variable that is validated as a populated String. Bad request if null/empty.
     * @return Response Entity containing BaseResponse and Http status code.
     */
    @ApiOperation(value = "GET whether or not this input contains a palindrome.",
        httpMethod = "GET", response = BaseResponse.class, nickname = "isPalindrome")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Palindrome response. Result member variable will be Boolean response.",
                response = BaseResponse.class),
        @ApiResponse(code = 400, message = "'input' request param was missing or empty"),
        @ApiResponse(code = 500, message = ErrorResponse.INTERNAL_SERVER_ERROR)
    })
    @RequestMapping(value = "/palindrome", method = RequestMethod.GET)
    public ResponseEntity<?> isPalindrome(@RequestParam(value=Application.INPUT_PARAMETER, required = false) String testString) {

        //setup response
        BaseResponse response;
        ResponseEntity<BaseResponse> responseEntity;

        if (StringUtils.isBlank(testString)) {
            //do error stuff
            response = new ErrorResponse();
            response.setMessage("Missing request parameter.");
            responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            //check if its a palindrome
            response = new SuccessfulResponse();
            response.setResult(testPalindrome(testString));
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        }

        return responseEntity;
    }

    /**
     * This helper method tests a String to see whether it's a palindrome or not.
     * @param testString - String to test as a palindrome. If null/empty - this method returns false.
     * @return Boolean based on whether it was a palindrome, or not.
     */
    private Boolean testPalindrome(String testString) {
        //if blank - its obviously not a palindrome.
        if (StringUtils.isBlank(testString)) return false;

        //test the reversed String against it self to test for palidromiumism.
        String reversedString = new StringBuilder(testString).reverse().toString();
        return testString.equals(reversedString);
    }
}
