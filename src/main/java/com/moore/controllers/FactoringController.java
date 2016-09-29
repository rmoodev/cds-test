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

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/1")
public class FactoringController {

    /**
     * Factors controller method. GET request to find factors of a given input request param.
     * @param numberToFactor Request parameter that will be validated by the controller method as a Long. Bad request if non-numeric.
     * @return Response Entity containing BaseResponse and a response status.
     */
    @ApiOperation(value = "GET the factors.",
            httpMethod = "GET", response = BaseResponse.class, nickname = "factors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Factors response. Result member variable will be List<Longs> - ie. {[123, 457]}"
                    , response = BaseResponse.class),
            @ApiResponse(code = 400, message = "'input' request param was missing, empty, or non-integer."),
            @ApiResponse(code = 500, message = ErrorResponse.INTERNAL_SERVER_ERROR)
    })
    @RequestMapping(value = "/factors", method = RequestMethod.GET)
    public ResponseEntity<?> factors(@RequestParam(value=Application.INPUT_PARAMETER, required = false) String numberToFactor) {

        //setup response
        BaseResponse response;
        ResponseEntity<BaseResponse> responseEntity;

        if (StringUtils.isBlank(numberToFactor)) {
            //do bad request error stuff
            response = new ErrorResponse();
            response.setMessage("Missing request parameter.");
            responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } else {
            try {
                Long numberToFactorLong = Long.valueOf(numberToFactor);
                //find all the factors
                Set<Long> factors = findFactors(numberToFactorLong);
                response = new SuccessfulResponse();
                response.setResult(factors);
                responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
            } catch (NumberFormatException nfe) {
                //nfe means wasnt an integer - reject
                response = new ErrorResponse();
                response.setMessage("Input was not an integer.");
                responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        return responseEntity;
    }

    /**
     * Private method to find factors of a given Long.
     * @param input - Long input to find factors for.
     * @return Set of Longs that are factors of the input.
     */
    private Set<Long> findFactors(Long input) {

        //returnable list
        Set<Long> set = new HashSet<>();

        //short circuit on 0
        if (input == 0) return set;

        //check for negativity
        Boolean negative = input < 0;

        //make it positive, add negative one here
        if (negative) {
            set.add(input);
            input = input * -1;
        }

        int incrementer = 1;

        //we only have to check for odd factors for odd numbers
        if (input % 2 != 0) {
            incrementer = 2;
        }

        //loop over until we make it to half our number, incrementing by 1 or 2 depending on even or odd
        for (Long i = 1L; i <= input / 2; i=i+incrementer) {

            //if we mod to 0 - we know its a factorial
            if (input % i == 0){
                set.add(i);

                //do negative stuff
                if (negative) {
                    set.add(i * -1);
                }
            }
        }

        //always add yourself too!
        set.add(input);

        return set;
    }

}
