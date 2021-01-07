package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObject(Object target, String fieldName, Object toInject){
        boolean isPrivate = false;

        try {
            final Field declaredField = target.getClass().getDeclaredField(fieldName);
            if(!declaredField.isAccessible()) {
                isPrivate = true;
                declaredField.setAccessible(true);
            }
            declaredField.set(target, toInject);
            if(isPrivate){
                declaredField.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
