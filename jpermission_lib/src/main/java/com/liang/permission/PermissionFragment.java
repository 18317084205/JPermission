package com.liang.permission;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.liang.permission.utils.PermissionUtils;

import java.util.Arrays;

public class PermissionFragment extends Fragment implements Request {
    private static final String PERMISSION_FRAGMENT_TAG = "j_permission_fragment_tag";

    private PermissionRequest permissionRequest;
    private PermissionResult permissionResult;
    private String[] permissions;

    public static void injectIfNeededIn(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        PermissionFragment fragment = new PermissionFragment();
        manager.beginTransaction().add(fragment, PERMISSION_FRAGMENT_TAG).commit();
        manager.executePendingTransactions();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionOption permissionOption = PermissionUtils.getPermissionOption();
        permissions = permissionOption.getPermissions();
        PermissionUtils.release(permissionOption);

        Log.d(getClass().getSimpleName(), "permissions: " + Arrays.toString(permissions));

        if (permissions == null || permissions.length == 0) {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            return;
        }

        permissionRequest = new PermissionRequestImp();
        permissionResult = new PermissionResultImp();
        permissionRequest.checkPermissions(getActivity(), permissions, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionRequest.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onPermissionUntreated(String[] permissions) {
        permissionRequest.requestPermissions(this, permissions);
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
        permissionResult.onPermissionGranted(permissions);
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onPermissionDenied(String[] permissions) {
        permissionResult.onPermissionDenied(permissions);
    }

    @Override
    public void onPermissionBanned(String[] permissions) {
        permissionResult.onPermissionBanned(permissions);
    }

}
