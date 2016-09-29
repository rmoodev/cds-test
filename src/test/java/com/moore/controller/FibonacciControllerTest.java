package com.moore.controller;

import com.moore.controllers.FibonacciController;
import com.moore.models.dtos.BaseResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ryanmoore - 9/28/16.
 */
public class FibonacciControllerTest {

    @Test
    public void testFibonacciLogic() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, InstantiationException {
        Class<?> c = FibonacciController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { Long.class };
        Method fibonacciMethod = c.getDeclaredMethod("fibonacci", argTypes);
        fibonacciMethod.setAccessible(true);
        Long arg = 15L;
        System.out.format("invoking %s.fibonacci()%n", c.getName());
        Long result = (Long) fibonacciMethod.invoke(obj, (Object)arg);
        System.out.println(result);
        Assert.assertTrue(result.equals(610L));
    }

    @Test
    public void testFibonacciLogicZero() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> c = FibonacciController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { Long.class };
        Method fibonacciMethod = c.getDeclaredMethod("fibonacci", argTypes);
        fibonacciMethod.setAccessible(true);
        Long arg = 0L;
        System.out.format("invoking %s.fibonacci()%n", c.getName());
        Long result = (Long) fibonacciMethod.invoke(obj, (Object) arg);
        Assert.assertTrue(result.equals(0L));
    }

    @Test
    public void testFibonacciLogicNegative() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> c = FibonacciController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { Long.class };
        Method fibonacciMethod = c.getDeclaredMethod("fibonacci", argTypes);
        fibonacciMethod.setAccessible(true);
        Long arg = -10L;
        System.out.format("invoking %s.fibonacci()%n", c.getName());
        Long result = (Long) fibonacciMethod.invoke(obj, (Object) arg);
        Assert.assertTrue(result.equals(-10L));
    }

    @Test
    public void testControllerMethodHappyPath() {
        FibonacciController controller = new FibonacciController();
        ResponseEntity response = controller.fibonacciNumber("15");
        BaseResponse baseResponse = (BaseResponse) response.getBody();
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        Assert.assertTrue(((Long)baseResponse.getResult()).equals(610L));
    }

    @Test
    public void testControllerMethodBadRequest() {
        FibonacciController controller = new FibonacciController();
        ResponseEntity response = controller.fibonacciNumber(null);
        BaseResponse baseResponse = (BaseResponse) response.getBody();
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        Assert.assertTrue(baseResponse.getResult() == null);
    }

    @Test
    public void testControllerMethodBadRequest2() {
        FibonacciController controller = new FibonacciController();
        ResponseEntity response = controller.fibonacciNumber("-1");
        BaseResponse baseResponse = (BaseResponse) response.getBody();
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        Assert.assertTrue(baseResponse.getResult() == null);
    }

    @Test
    public void testControllerMethodBadRequest3() {
        FibonacciController controller = new FibonacciController();
        ResponseEntity response = controller.fibonacciNumber("0");
        BaseResponse baseResponse = (BaseResponse) response.getBody();
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        Assert.assertTrue(baseResponse.getResult() == null);
    }

    @Test
    public void testControllerMethodBadRequest4() {
        FibonacciController controller = new FibonacciController();
        ResponseEntity response = controller.fibonacciNumber("1.67");
        BaseResponse baseResponse = (BaseResponse) response.getBody();
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        Assert.assertTrue(baseResponse.getResult() == null);
    }

    @Test
    public void testControllerMethodBadRequest5() {
        FibonacciController controller = new FibonacciController();
        ResponseEntity response = controller.fibonacciNumber("46");
        BaseResponse baseResponse = (BaseResponse) response.getBody();
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        Assert.assertTrue(baseResponse.getResult() == null);
    }
}
