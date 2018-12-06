package com.liang.permission.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.SimpleArrayMap;

import com.liang.permission.PermissionActivity;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PermissionUtils {
    private static final SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }


    /**
     * 判断单个权限是否同意
     *
     * @param context    context
     * @param permission permission
     * @return return true if permission granted
     */
    public static boolean hasSelfPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断权限是否存在
     *
     * @param permission permission
     * @return return true if permission exists in SDK version
     */
    public static boolean permissionExists(String permission) {
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }

    public static void go2PermissionRequest(Context context, String[] permissions, ProceedingJoinPoint joinPoint) {
        PermissionActivity.start(context, permissions,joinPoint);
    }

    public static <T extends Annotation> void requestPermissionsResult(ProceedingJoinPoint joinPoint, String[] permissions, Class<T> clazz) {
        if (joinPoint == null) {
            return;
        }
        Object object = joinPoint.getThis();
        Class<?> cls = object.getClass();
        Method[] methods = cls.getDeclaredMethods();
        if (methods == null || methods.length == 0) return;
        for (Method method : methods) {
            //过滤不含自定义注解的方法
            boolean isHasAnnotation = method.isAnnotationPresent(clazz);
            if (isHasAnnotation) {
                method.setAccessible(true);
                //获取方法类型
                Class<?>[] types = method.getParameterTypes();
                if (types == null || types.length != 1) return;
                //获取方法上的注解
                T aInfo = method.getAnnotation(clazz);
                if (aInfo == null) return;
                //解析注解上对应的信息
                Object strings = permissions;
                try {
                    method.invoke(object, strings);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
