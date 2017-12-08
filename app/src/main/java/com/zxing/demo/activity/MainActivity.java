package com.zxing.demo.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxing.demo.R;
import com.zxing.demo.base.BaseActivity;
import com.zxing.demo.util.ImageUtil;

import bdk.qr.lib.activity.CaptureActivity;
import bdk.qr.lib.util.CodeUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button scanCommon;
    private Button scanDefineUI;
    private Button scanPic;
    private Button createCommon;
    private Button createLogo;
    private EditText inputContent;
    private TextView resultScan;
    private ImageView resultCreateCommon;
    private ImageView resultCreateLogo;
    private String content;
    public Bitmap mBitmap = null;
    private int type = 0;

    // 扫描跳转Activity RequestCode
    public static final int REQUEST_CODE = 111;
    // 选择系统图片Request Code
    public static final int REQUEST_IMAGE = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        scanCommon = (Button) findViewById(R.id.btn_scan_common);
        scanCommon.setOnClickListener(this);
        scanDefineUI = (Button) findViewById(R.id.btn_scan_define);
        scanDefineUI.setOnClickListener(this);
        scanPic = (Button) findViewById(R.id.btn_scan_pic);
        scanPic.setOnClickListener(this);
        createCommon = (Button) findViewById(R.id.btn_create_common);
        createCommon.setOnClickListener(this);
        createLogo = (Button) findViewById(R.id.btn_create_logo);
        createLogo.setOnClickListener(this);
        inputContent = (EditText) findViewById(R.id.et_input);
        resultScan = (TextView) findViewById(R.id.tv_scan_result);
        resultCreateCommon = (ImageView) findViewById(R.id.iv_create_common);
        resultCreateLogo = (ImageView) findViewById(R.id.iv_create_logo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan_common:  // 普通扫描
                type = 0;
                String[] permissions = {Manifest.permission.CAMERA};
                requestPermission(permissions);
                break;
            case R.id.btn_scan_define:    // 定制扫描界面
                type = 1;
                String[] permissions1 = {Manifest.permission.CAMERA};
                requestPermission(permissions1);
                break;
            case R.id.btn_scan_pic:   // 选择图片解析
                String[] permissions2 = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermission(permissions2);
                break;
            case R.id.btn_create_common:  // 普通二维码图片
                content = inputContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(mActivity, "请输入内容后再生成二维码", Toast.LENGTH_SHORT).show();
                    return;
                }
                mBitmap = CodeUtils.createImage(content, 300, 300, null);
                resultCreateCommon.setImageBitmap(mBitmap);
                break;
            case R.id.btn_create_logo:  // 带LOGO二维码图片
                content = inputContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(mActivity, "请输入内容后再生成二维码", Toast.LENGTH_SHORT).show();
                    return;
                }
                mBitmap = CodeUtils.createImage(content, 400, 400,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
                resultCreateLogo.setImageBitmap(mBitmap);
                break;
        }
    }

    private void requestPermission(String[] permissions) {
        checkPermissions(permissions);
    }

    @Override
    public void onPermissionGranted(String permission) {
        super.onPermissionGranted(permission);
        switch (permission) {
            case Manifest.permission.CAMERA:
                if (type == 0) {
                    startActivityForResult(new Intent(mActivity, CaptureActivity.class), REQUEST_CODE);
                } else if (type == 1) {
                    startActivityForResult(new Intent(mActivity, ScanDefineUiActivity.class), REQUEST_CODE);
                }
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {  // 处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (TextUtils.isEmpty(result))
                        result = "";
                    resultScan.setText("二维码扫描结果:\n" + result);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    resultScan.setText("二维码扫描结果:\n解析二维码失败");
                }
            }
        } else if (requestCode == REQUEST_IMAGE) {  // 选择系统图片并解析
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            resultScan.setText("选择图片解析结果:\n" + result);
//                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            resultScan.setText("选择图片解析结果:\n解析二维码失败");
//                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
