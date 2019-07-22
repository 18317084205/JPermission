package com.liang.permission;

public interface OnPermissionListener {
    void onPermissionGranted();

    void onPermissionDenied(String[] permissions);

    void onPermissionBanned(String[] permissions);
}
