package com.moore.controller;

import com.moore.controllers.FactoringController;
import com.moore.models.dtos.BaseResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * ryanmoore - 9/28/16.
 */
public class FactoringControllerTest {

    @Test
    public void testFactorLogic() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, InstantiationException {
        Class<?> c = FactoringController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { Long.class };
        Method factorMethod = c.getDeclaredMethod("findFactors", argTypes);
        factorMethod.setAccessible(true);
        Long arg = 15L;
        System.out.format("invoking %s.findFactors()%n", c.getName());
        Set<Long> result = (Set<Long>) factorMethod.invoke(obj, (Object)arg);
        System.out.println(result);
        Assert.assertTrue(result.contains(1L));
        Assert.assertTrue(result.contains(3L));
        Assert.assertTrue(result.contains(5L));
        Assert.assertTrue(result.contains(15L));
        Assert.assertTrue(result.size() == 4);

    }

    @Test
    public void testFactorLogicNegative() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> c = FactoringController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { Long.class };
        Method factorMethod = c.getDeclaredMethod("findFactors", argTypes);
        factorMethod.setAccessible(true);
        Long arg = -15L;
        System.out.format("invoking %s.findFactors()%n", c.getName());
        Set<Long> result = (Set<Long>) factorMethod.invoke(obj, (Object)arg);
        System.out.println(result);
        Assert.assertTrue(result.contains(1L));
        Assert.assertTrue(result.contains(3L));
        Assert.assertTrue(result.contains(5L));
        Assert.assertTrue(result.contains(15L));
        Assert.assertTrue(result.contains(-1L));
        Assert.assertTrue(result.contains(-3L));
        Assert.assertTrue(result.contains(-5L));
        Assert.assertTrue(result.contains(-15L));
        Assert.assertTrue(result.size() == 8);

    }

    @Test
    public void testFactorLogicZero() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> c = FactoringController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { Long.class };
        Method factorMethod = c.getDeclaredMethod("findFactors", argTypes);
        factorMethod.setAccessible(true);
        Long arg = 0L;
        System.out.format("invoking %s.findFactors()%n", c.getName());
        Set<Long> result = (Set<Long>) factorMethod.invoke(obj, (Object)arg);
        System.out.println(result);
        Assert.assertTrue(result.size() == 0);
    }

    @Test
    public void testControllerMethodHappyPath() {
        FactoringController controller = new FactoringController();
        ResponseEntity response = controller.factors("15");
        BaseResponse baseResponse = (BaseResponse) response.getBody();
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        Assert.assertTrue(((Set<Long>)baseResponse.getResult()).contains(15L));
    }

    @Test
    public void testControllerMethodBadRequest() {
        FactoringController controller = new FactoringController();
        ResponseEntity response = controller.factors(null);
        BaseResponse baseResponse = (BaseResponse) response.getBody();
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        Assert.assertTrue(baseResponse.getResult() == null);
    }

    @Test
    public void testControllerMethodBadRequest2() {
        FactoringController controller = new FactoringController();
        ResponseEntity response = controller.factors("cats");
        BaseResponse baseResponse = (BaseResponse) response.getBody();
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        Assert.assertTrue(baseResponse.getResult() == null);
    }
}
