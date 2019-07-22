package com.liang.permission;


public abstract class PermissionResult {

    public abstract void onPermissionGranted(String[] permissions);

    public abstract void onPermissionDenied(String[] permissions);

    public abstract void onPermissionBanned(String[] permissions);
}
