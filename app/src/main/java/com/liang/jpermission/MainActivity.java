package com.liang.jpermission;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.liang.permission.PermissionHelper;
import com.liang.permission.annotation.Permission;
import com.liang.permission.annotation.PermissionBanned;
import com.liang.permission.annotation.PermissionDenied;


public class MainActivity extends AppCompatActivity {

    private PermissionHelper permissionHelper;

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPermissions();
            }
        });
    }

    @Permission(value = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE})
    public void testPermissions() {
        Toast.makeText(this, "已获得所有权限", Toast.LENGTH_LONG).show();
    }

    @PermissionBanned()
    public void permissionBanned(Object permissions) {
        Log.e("MainActivity", "PermissionBanned: " + ((String[]) permissions).length);
        String[] p = (String[]) permissions;
        String msg = "已拒绝：";
        for (String permission : p) {
            msg += permission + "\n";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @PermissionDenied
    public void permissionDenied(Object permissions) {
        Log.e("MainActivity", "permissionDenied: " + ((String[]) permissions).length);
        String[] p = (String[]) permissions;
        String msg = "取消申请：";
        for (String permission : p) {
            msg += permission + "\n";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
