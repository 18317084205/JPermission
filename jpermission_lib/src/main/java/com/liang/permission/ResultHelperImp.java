package com.liang.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.liang.permission.annotation.Permission;
import com.liang.permission.annotation.PermissionBanned;
import com.liang.permission.annotation.PermissionDenied;
import com.liang.permission.utils.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ResultHelperImp extends ResultHelper {

    @Pointcut("execution(@com.liang.permission.annotation.Permission * *(..))")//方法切入点
    public void requestPermissionMethod() {
    }

    @Around("requestPermissionMethod()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) {
        Context context = null;
        Object object = joinPoint.getThis();

        if (object instanceof Context) {
            context = (Context) object;
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        } else if (object instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Permission permission = signature.getMethod().getAnnotation(Permission.class);

        if (context == null || permission == null) {
            return;
        }

        if (context instanceof Activity) {
            PermissionFragment.injectIfNeededIn((Activity) context, permission.value(), joinPoint);
            return;
        }

        PermissionUtils.go2PermissionRequest(context, permission.value(), joinPoint);
    }

    @Override
    public void onPermissionGranted(ProceedingJoinPoint joinPoint, String[] permissions) {
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onPermissionDenied(ProceedingJoinPoint joinPoint, String[] permissions) {
        PermissionUtils.requestPermissionsResult(joinPoint, permissions, PermissionDenied.class);
    }

    @Override
    public void onPermissionBanned(ProceedingJoinPoint joinPoint, String[] permissions) {
        PermissionUtils.requestPermissionsResult(joinPoint, permissions, PermissionBanned.class);
    }
}
