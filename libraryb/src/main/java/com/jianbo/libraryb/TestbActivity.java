package com.jianbo.libraryb;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.liang.permission.annotation.JPermission;
import com.liang.permission.annotation.JPermissionBanned;
import com.liang.permission.annotation.JPermissionDenied;


public class TestbActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textb);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPermissions();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPermission();
            }
        });

    }


    @JPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void testPermission() {
        Toast.makeText(this, "已获得所有权限", Toast.LENGTH_LONG).show();
    }

    @JPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE})
    public void testPermissions() {
        Toast.makeText(this, "已获得所有权限", Toast.LENGTH_LONG).show();
    }

    @JPermissionBanned
    public void permissionBanned(Object permissions) {
        Log.e("TestActivity", "PermissionBanned: " + ((String[]) permissions).length);
        String[] p = (String[]) permissions;
        String msg = "已拒绝：";
        for (String permission : p) {
            msg += permission + "\n";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @JPermissionDenied
    public void permissionDenied(Object permissions) {
        Log.e("TestActivity", "permissionDenied: " + ((String[]) permissions).length);
        String[] p = (String[]) permissions;
        String msg = "取消申请：";
        for (String permission : p) {
            msg += permission + "\n";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
