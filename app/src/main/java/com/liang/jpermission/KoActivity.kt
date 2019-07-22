package com.liang.jpermission

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.liang.permission.annotation.JPermission
import com.liang.permission.annotation.JPermissionBanned
import com.liang.permission.annotation.JPermissionDenied
import kotlinx.android.synthetic.main.activity_ko.*

class KoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ko)
        button.setOnClickListener { testPermissions() }

        button2.setOnClickListener { testPermission() }
    }


    @JPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun testPermission() {
        Toast.makeText(this, "已获得所有权限", Toast.LENGTH_LONG).show()
    }

    @JPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE)
    fun testPermissions() {
        Toast.makeText(this, "已获得所有权限", Toast.LENGTH_LONG).show()
    }

    @JPermissionBanned
    fun permissionBanned(permissions: Any) {
        Log.e("TestActivity", "PermissionBanned: " + (permissions as Array<String>).size)
        var msg = "已拒绝："
        for (permission in permissions) {
            msg += permission + "\n"
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    @JPermissionDenied
    fun permissionDenied(permissions: Any) {
        Log.e("TestActivity", "permissionDenied: " + (permissions as Array<String>).size)
        var msg = "取消申请："
        for (permission in permissions) {
            msg += permission + "\n"
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
