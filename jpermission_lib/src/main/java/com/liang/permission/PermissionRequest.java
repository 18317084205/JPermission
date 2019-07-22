package com.liang.permission;

import android.app.Activity;
import android.content.Context;

public abstract class PermissionRequest {
    public static final String TAG = PermissionRequest.class.getSimpleName();

    public abstract void checkPermissions(Context activity, String[] permissions, Request listener);

    public abstract void requestPermissions(Object object, String[] permissions);

    public abstract void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults);
}
