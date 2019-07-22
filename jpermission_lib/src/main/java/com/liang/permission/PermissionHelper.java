package com.liang.permission;

import android.content.Context;

import com.liang.permission.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {

    /**
     * 判断权限是否存在
     *
     * @param permission permission
     * @return return true if permission exists in SDK version
     */
    public static boolean hasPermission(Context context, String permission) {
        return PermissionUtils.hasSelfPermission(context, permission);
    }

    public static boolean hasPermission(Context context, String... permissions) {
        List<String> noPermission = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!hasPermission(context, permissions[i])) {
                noPermission.add(permissions[i]);
            }
        }
        return noPermission.isEmpty();
    }

    public static void requestPermission(Context context, String[] permissions, OnPermissionListener permissionListener) {
        PermissionUtils.go2PermissionRequest(context, permissions, permissionListener);
    }

}
