package com.liang.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import com.liang.permission.annotation.JPermission;
import com.liang.permission.annotation.JPermissionBanned;
import com.liang.permission.annotation.JPermissionDenied;
import com.liang.permission.utils.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

@Aspect
public class PermissionResultImp extends PermissionResult {

    @Pointcut("execution(@com.liang.permission.annotation.JPermission * *(..))")//方法切入点
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
        JPermission permission = signature.getMethod().getAnnotation(JPermission.class);

        if (context == null || permission == null) {
            return;
        }

        PermissionUtils.release(PermissionUtils.getPermissionOption()
                .setProceedingJoinPoint(joinPoint)
                .setPermissions(permission.value())
                .setPermissionListener(null));

        if (context instanceof Activity) {
            PermissionFragment.injectIfNeededIn((Activity) context);
            return;
        }
        PermissionActivity.start(context);
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
        PermissionUtils.requestPermissionsResult();
    }

    @Override
    public void onPermissionDenied(String[] permissions) {
        PermissionUtils.requestPermissionsResult(permissions, JPermissionDenied.class);
    }

    @Override
    public void onPermissionBanned(String[] permissions) {
        PermissionUtils.requestPermissionsResult(permissions, JPermissionBanned.class);
    }
}
