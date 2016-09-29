package com.moore.controller;

import com.moore.controllers.PalindromeController;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ryanmoore - 9/28/16.
 */
public class PalindromeControllerTest {

    @Test
    public void testPalindromeLogicFalse() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, InstantiationException {

        Class<?> c = PalindromeController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { String.class };
        Method palindromeMethod = c.getDeclaredMethod("testPalindrome", argTypes);
        palindromeMethod.setAccessible(true);
        String arg = "hello";
        System.out.format("invoking %s.testPalindrome()%n", c.getName());
        Boolean result = (Boolean) palindromeMethod.invoke(obj, (Object)arg);
        System.out.println(result);
        Assert.assertTrue(!result);
    }

    @Test
    public void testPalindromeLogicTrue() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> c = PalindromeController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { String.class };
        Method palindromeMethod = c.getDeclaredMethod("testPalindrome", argTypes);
        palindromeMethod.setAccessible(true);
        String arg = "racecar racecar";
        System.out.format("invoking %s.testPalindrome()%n", c.getName());
        Boolean result = (Boolean) palindromeMethod.invoke(obj, (Object)arg);
        System.out.println(result);
        Assert.assertTrue(result);
    }

    @Test
    public void testPalindromeLogicEmpty() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> c = PalindromeController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { String.class };
        Method palindromeMethod = c.getDeclaredMethod("testPalindrome", argTypes);
        palindromeMethod.setAccessible(true);
        String arg = "";
        System.out.format("invoking %s.testPalindrome()%n", c.getName());
        Boolean result = (Boolean) palindromeMethod.invoke(obj, (Object)arg);
        System.out.println(result);
        Assert.assertTrue(!result);
    }

    @Test
    public void testPalindromeLogicNull() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> c = PalindromeController.class;
        Object obj = c.newInstance();
        Class[] argTypes = new Class[] { String.class };
        Method palindromeMethod = c.getDeclaredMethod("testPalindrome", argTypes);
        palindromeMethod.setAccessible(true);
        String arg = null;
        System.out.format("invoking %s.testPalindrome()%n", c.getName());
        Boolean result = (Boolean) palindromeMethod.invoke(obj, (Object)arg);
        System.out.println(result);
        Assert.assertTrue(!result);
    }

}
