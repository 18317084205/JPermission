package com.liang.permission;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;

public class PermissionOption {

    private ProceedingJoinPoint proceedingJoinPoint;
    private OnPermissionListener permissionListener;
    private String[] permissions;

    public ProceedingJoinPoint getProceedingJoinPoint() {
        return proceedingJoinPoint;
    }

    public PermissionOption setProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) {
        this.proceedingJoinPoint = proceedingJoinPoint;
        return this;
    }

    public OnPermissionListener getPermissionListener() {
        return permissionListener;
    }

    public PermissionOption setPermissionListener(OnPermissionListener permissionListener) {
        this.permissionListener = permissionListener;
        return this;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public PermissionOption setPermissions(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public void reset() {
        proceedingJoinPoint = null;
        permissionListener = null;
        permissions = null;
    }

    @Override
    public String toString() {
        return "PermissionOption{" + ", permissions=" + Arrays.toString(permissions) +
                '}';
    }
}
