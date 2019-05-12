package com.liang.permission;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.aspectj.lang.ProceedingJoinPoint;

public class PermissionFragment extends Fragment implements Request {
    private static final String PERMISSION_FRAGMENT_TAG = "j_permission_fragment_tag";

    private PermissionHelper permissionHelper;
    private ResultHelper resultHelper;
    private String[] permissions;
    private static ProceedingJoinPoint proceedingJoinPoint;

    public static void injectIfNeededIn(Activity activity, String[] permissions, ProceedingJoinPoint joinPoint) {
        proceedingJoinPoint = joinPoint;
        FragmentManager manager = activity.getFragmentManager();
        PermissionFragment fragment = new PermissionFragment();
        Bundle args = new Bundle();
        args.putStringArray(PermissionActivity.PERMISSION_KEY, permissions);
        fragment.setArguments(args);
        manager.beginTransaction().add(fragment, PERMISSION_FRAGMENT_TAG).commit();
        manager.executePendingTransactions();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            return;
        }

        permissions = getArguments().getStringArray(PermissionActivity.PERMISSION_KEY);

        if (permissions == null || permissions.length == 0) {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            return;
        }

        permissionHelper = new PermissionHelperImp();
        resultHelper = new ResultHelperImp();
        permissionHelper.checkPermissions(getActivity(), permissions, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionUntreated(String[] permissions) {
        permissionHelper.requestPermissions(this, permissions);
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
        resultHelper.onPermissionGranted(proceedingJoinPoint, permissions);
    }

    @Override
    public void onPermissionDenied(String[] permissions) {
        resultHelper.onPermissionDenied(proceedingJoinPoint, permissions);
    }

    @Override
    public void onPermissionBanned(String[] permissions) {
        resultHelper.onPermissionBanned(proceedingJoinPoint, permissions);
    }

}
