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
public class FibonacciController {

    /**
     * GET request method for finding fibonacci # of given request parameter.
     * @param fibonacciNumber - Request Parameter that is validated as a Long. Must be >=1 or returns bad request.
     * @return Response entity containing the BaseResponse body and an http status code.
     */
    @ApiOperation(value = "GET the total fibonacci number for this input.",
            httpMethod = "GET", response = BaseResponse.class, nickname = "fibonacciNumber")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fibonacci response. Result member variable will be populated with a Long.",
                    response = BaseResponse.class),
            @ApiResponse(code = 400, message = "'input' request param is missing, empty, non-integer, or <1."),
            @ApiResponse(code = 500, message = ErrorResponse.INTERNAL_SERVER_ERROR)
    })
    @RequestMapping(value = "/fibonacci", method = RequestMethod.GET)
    public ResponseEntity<?> fibonacciNumber(@RequestParam(value= Application.INPUT_PARAMETER, required = false) String fibonacciNumber) {

        //setup response
        BaseResponse response;
        ResponseEntity<BaseResponse> responseEntity;

        if (StringUtils.isBlank(fibonacciNumber)) {
            //do bad request error stuff
            response = new ErrorResponse();
            response.setMessage("Missing request parameter.");
            responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            try {
                //find that fibonacci #!
                Long fibonacciLong = Long.valueOf(fibonacciNumber);

                //cant be less than 1
                if (fibonacciLong < 1 ) {
                    response = new ErrorResponse();
                    response.setMessage("Input was less than 1.");
                    responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                } else if (fibonacciLong > 45 ) {
                    //our fibonacci algorithm isn't the best - so prevent users from trying greater than 45 to prevent hangs.
                    response = new ErrorResponse();
                    response.setMessage("Input was greater than 45. Our algorithm isn't good enough for that!");
                    responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                } else {
                    //finally happy!
                    response = new SuccessfulResponse();
                    response.setResult(fibonacci(fibonacciLong));
                    responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
                }
            } catch (NumberFormatException nfe) {
                //wasnt number, error out
                response = new ErrorResponse();
                response.setMessage("Input was not an integer.");
                responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        return responseEntity;
    }

    /**
     *  This method recursively calculates the Fibonacci # of input until it reaches 1, then returns itself.
     * @param n - number to recursively calculate fibonacci #.
     * @return Calculated fibonacci # of input.
     */
    private Long fibonacci(Long n) {
        if (n <= 1) return n;
        else return fibonacci( n - 1 ) + fibonacci( n - 2 );
    }
}




