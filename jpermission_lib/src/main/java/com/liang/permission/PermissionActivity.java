package com.liang.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.liang.permission.utils.PermissionUtils;


import java.util.Arrays;

public class PermissionActivity extends AppCompatActivity implements Request {

    private PermissionRequest permissionRequest;
    private PermissionResult resultHelper;

    public static void start(Context context) {
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionOption permissionOption = PermissionUtils.getPermissionOption();
        String[] permissions = permissionOption.getPermissions();
        PermissionUtils.release(permissionOption);
        Log.d(getClass().getSimpleName(), "permissions: " + Arrays.toString(permissions));

        if (permissions == null || permissions.length == 0) {
            finish();
            return;
        }

        permissionRequest = new PermissionRequestImp();
        resultHelper = new PermissionResultImp();
        permissionRequest.checkPermissions(this, permissions, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionRequest.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPermissionUntreated(String[] permissions) {
        permissionRequest.requestPermissions(this, permissions);
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
        resultHelper.onPermissionGranted(permissions);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPermissionDenied(String[] permissions) {
        resultHelper.onPermissionDenied(permissions);
    }

    @Override
    public void onPermissionBanned(String[] permissions) {
        resultHelper.onPermissionBanned(permissions);
    }

}
