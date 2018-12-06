package com.liang.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.liang.permission.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelperImp extends PermissionHelper {

    private static final int REQUEST_CODE = 0x200;
    private static final int PERMISSION_UNTREATED = 1;
    private static final int PERMISSION_GRANTED = 2;
    private static final int PERMISSION_DENIED = 3;
    private static final int PERMISSION_BANNED = 4;

    private Request request;

    @Override
    public void checkPermissions(Context context, String[] permissions, Request listener) {
        request = listener;
        List<String> denied = new ArrayList<>();
        for (String permission : permissions) {
            if (PermissionUtils.permissionExists(permission) && !PermissionUtils.hasSelfPermission(context, permission)) {
                denied.add(permission);
            }
        }

        if (!denied.isEmpty()) {
            callbackRequestResult(PERMISSION_UNTREATED, denied.toArray(new String[denied.size()]));
            return;
        }

        callbackRequestResult(PERMISSION_GRANTED, permissions);
    }

    @Override
    public void requestPermissions(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != REQUEST_CODE) {
            return;
        }
        if (grantResults.length > 0) {
            List<String> denied = new ArrayList<>();
            List<String> banned = new ArrayList<>();
            for (int j = 0; j < grantResults.length; j++) {
                if (grantResults[j] != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[j])) {
                        banned.add(permissions[j]);
                    } else {
                        denied.add(permissions[j]);
                    }
                }
            }

            if (!banned.isEmpty()) {
                callbackRequestResult(PERMISSION_BANNED, banned.toArray(new String[banned.size()]));
                return;
            }

            if (!denied.isEmpty()) {
                callbackRequestResult(PERMISSION_DENIED, denied.toArray(new String[denied.size()]));
                return;
            }
            callbackRequestResult(PERMISSION_GRANTED, permissions);
        } else {
            callbackRequestResult(PERMISSION_BANNED, permissions);
        }
    }

    private void callbackRequestResult(int permissionType, String... permissions) {
        if (request == null) {
            return;
        }
        switch (permissionType) {
            case PERMISSION_UNTREATED:
                request.onPermissionUntreated(permissions);
                break;
            case PERMISSION_GRANTED:
                request.onPermissionGranted(permissions);
                break;
            case PERMISSION_DENIED:
                request.onPermissionDenied(permissions);
                break;
            case PERMISSION_BANNED:
                request.onPermissionBanned(permissions);
                break;
        }
    }
}
