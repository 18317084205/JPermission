package com.liang.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.aspectj.lang.ProceedingJoinPoint;

public class PermissionActivity extends Activity implements Request {

    public static final String PERMISSION_KEY = "permission_key";
    private PermissionHelper permissionHelper;
    private ResultHelper resultHelper;
    private String[] permissions;
    private static ProceedingJoinPoint proceedingJoinPoint;

    public static void start(Context context, String[] permissions, ProceedingJoinPoint joinPoint) {
        proceedingJoinPoint = joinPoint;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(PermissionActivity.PERMISSION_KEY, permissions);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissions = getIntent().getStringArrayExtra(PERMISSION_KEY);
        if (permissions == null || permissions.length == 0) {
            finish();
            return;
        }
        permissionHelper = new PermissionHelperImp();
        resultHelper = new ResultHelperImp();
        permissionHelper.checkPermissions(this, permissions, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPermissionUntreated(String[] permissions) {
        permissionHelper.requestPermissions(this, permissions);
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
        resultHelper.onPermissionGranted(proceedingJoinPoint, permissions);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPermissionDenied(String[] permissions) {
        resultHelper.onPermissionDenied(proceedingJoinPoint, permissions);
    }

    @Override
    public void onPermissionBanned(String[] permissions) {
        resultHelper.onPermissionBanned(proceedingJoinPoint, permissions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proceedingJoinPoint = null;
    }
}
