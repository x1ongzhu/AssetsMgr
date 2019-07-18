package me.zeo_x.management.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zeo_x.management.R;
import me.zeo_x.management.data.HttpHelper;

/*
* 扫码点检
* */
public class CheckCZActivity extends AppCompatActivity implements HttpHelper.SimpleHttpListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_equ_id)
    EditText et_equ_id;
    @Bind(R.id.et_project)
    EditText et_project;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    @Bind(R.id.iv_scan_id)
    ImageView iv_scan_id;
    @Bind(R.id.iv_scan_project)
    ImageView iv_scan_project;
    private HttpHelper httpHelper;
    private ProgressDialog progressDialog;
    static final int REQUEST_QR_CODE_SCAN_FOR_ID = 100;
    static final int REQUEST_QR_CODE_SCAN_FOR_PROJECT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_cz);
        ButterKnife.bind(this);
        httpHelper = HttpHelper.getInstance(this);
        initView();
    }

    private void initView() {
        toolbar.setTitle("设备点检");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("正在提交,请稍候");
        progressDialog.setCancelable(false);
    }

    @OnClick(R.id.btn_commit)
    void click() {
        if (TextUtils.isEmpty(et_equ_id.getText())) {
            et_equ_id.setError("请输入设备编号");
        } else if (TextUtils.isEmpty(et_project.getText())) {
            et_project.setError("请输入点检项目");
        } else {
            progressDialog.show();
            btn_commit.setEnabled(false);
            httpHelper.check_cz(et_equ_id.getText().toString().trim(), et_project.getText().toString().trim(), this);
        }
    }

    @Override
    public void onSuccess() {
        progressDialog.dismiss();
        btn_commit.setEnabled(true);
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String error) {
        progressDialog.dismiss();
        btn_commit.setEnabled(true);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_scan_id)
    void iv_scan_id() {
        Intent i = new Intent(this, QRCodeActivity.class);
        startActivityForResult(i, REQUEST_QR_CODE_SCAN_FOR_ID);
    }

    @OnClick(R.id.iv_scan_project)
    void iv_scan_project() {
        Intent i = new Intent(this, QRCodeActivity.class);
        startActivityForResult(i, REQUEST_QR_CODE_SCAN_FOR_PROJECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case REQUEST_QR_CODE_SCAN_FOR_ID:
                    case REQUEST_QR_CODE_SCAN_FOR_PROJECT:
                        String str = data.getStringExtra("text");
                        if (!TextUtils.isEmpty(str)) {
                            if (str.contains(",")) {
                                String[] arr = str.split(",");
                                if (arr.length == 2) {
                                    et_equ_id.setText(arr[0]);
                                    et_project.setText(arr[1]);
                                }
                            } else if (str.contains("，")) {
                                String[] arr = str.split("，");
                                if (arr.length == 2) {
                                    et_equ_id.setText(arr[0]);
                                    et_project.setText(arr[1]);
                                }
                            }
                        }
                        break;
                }
            }
        }
    }
}
