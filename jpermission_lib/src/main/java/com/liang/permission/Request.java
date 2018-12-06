package com.liang.permission;

public interface Request {
    void onPermissionUntreated(String[] permissions);

    void onPermissionGranted(String[] permissions);

    void onPermissionDenied(String[] permissions);

    void onPermissionBanned(String[] permissions);
}
