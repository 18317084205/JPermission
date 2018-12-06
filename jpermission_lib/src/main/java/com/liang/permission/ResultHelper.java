package com.liang.permission;

import org.aspectj.lang.ProceedingJoinPoint;

public abstract class ResultHelper {

    public abstract void onPermissionGranted(ProceedingJoinPoint joinPoint,String[] permissions);

    public abstract void onPermissionDenied(ProceedingJoinPoint joinPoint,String[] permissions);

    public abstract void onPermissionBanned(ProceedingJoinPoint joinPoint,String[] permissions);
}
