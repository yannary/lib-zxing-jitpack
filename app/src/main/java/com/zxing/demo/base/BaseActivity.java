package com.zxing.demo.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyanyan on 2017/11/27.
 */

public class BaseActivity extends AppCompatActivity {

    public Activity mActivity;
    private final static int PERMISSION_REQUEST = 12;   // 权限申请请求

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = BaseActivity.this;
    }

    /**
     * 单个权限的检查
     *
     * @param permission 需要检查的权限
     * @param reason     需要这个权限的原因
     */
    public void checkPermission(final String permission, String reason) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {  //权限已经申请
            onPermissionGranted(permission);
            return;
        }
        if (!TextUtils.isEmpty(reason)) {
            //判断用户先前是否拒绝过该权限申请，如果为true，我们可以向用户解释为什么使用该权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("温馨提示")
                        .setMessage(reason)
                        .setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission(new String[]{permission});
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                requestPermission(new String[]{permission});
            }
        } else {
            requestPermission(new String[]{permission});
        }
    }

    /**
     * 多个权限的检查
     *
     * @param permissions 需要检查的权限
     */
    public void checkPermissions(String... permissions) {
        // 用于记录权限申请被拒绝的权限集合
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            requestPermission(deniedPermissions);
        }
    }

    /**
     * 调用系统API完成权限申请
     *
     * @param permissions 需要申请的权限数组
     */
    private void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);
    }

    /**
     * 申请权限被允许的回调
     *
     * @param permission 被允许的权限
     */
    public void onPermissionGranted(String permission) {

    }

    /**
     * 申请权限被拒绝的回调
     *
     * @param permission 被拒绝的权限
     */
    public void onPermissionDenied(String permission) {

    }

    /**
     * 申请权限的失败的回调
     */
    public void onPermissionFailure() {

    }

    /**
     * 弹出系统权限询问对话框，用户交互后的结果回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        } else {
                            onPermissionDenied(permissions[i]);
                        }
                    }
                } else {
                    onPermissionFailure();
                }
                break;
        }
    }
}
