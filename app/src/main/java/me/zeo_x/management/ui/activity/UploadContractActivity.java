package me.zeo_x.management.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import me.zeo_x.management.R;
import me.zeo_x.management.data.Cookie;
import me.zeo_x.management.data.HttpHelper;

public class UploadContractActivity extends AppCompatActivity implements HttpHelper.SimpleHttpListener, AdapterView.OnItemSelectedListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_contract_id)
    AutoCompleteTextView et_contract_id;
    @Bind(R.id.iv_scan_id)
    ImageView iv_scan_id;
    @Bind(R.id.spinner_content)
    Spinner spinner_content;
    @Bind(R.id.btn_take_photo)
    Button btn_take_photo;
    @Bind(R.id.tv_path)
    TextView tv_path;
    @Bind(R.id.btn_upload)
    Button btn_upload;
    static final int REQUEST_QR_CODE_SCAN_FOR_ID = 100;
    private String path = "";
    private String subject = "";
    private HttpHelper httpHelper;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_contract);
        httpHelper = HttpHelper.getInstance(this);
        ButterKnife.bind(this);
        initView();
        et_contract_id.setText(getIntent().getStringExtra("contract_id"));
    }

    private void initView() {
        toolbar.setTitle("上传合同图片");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("正在上传，请稍候");
        progressDialog.setCancelable(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, R.id.tv, getResources().getStringArray(R.array.subjects));
        spinner_content.setAdapter(adapter);
        spinner_content.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Cookie.getContractArr(this));
        et_contract_id.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.iv_scan_id)
    void iv_scan_id() {
        Intent i = new Intent(this, QRCodeActivity.class);
        startActivityForResult(i, REQUEST_QR_CODE_SCAN_FOR_ID);
    }

    @OnClick(R.id.btn_take_photo)
    void take_photo() {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setPhotoCount(1);
        intent.setShowCamera(true);
        intent.setShowGif(true);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.btn_upload)
    void btn_upload() {
        if (TextUtils.isEmpty(et_contract_id.getText())) {
            et_contract_id.setError("请填写合同编号");
        } else if (TextUtils.isEmpty(subject)) {
            Toast.makeText(this, "请选择项目", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "请先拍照", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            httpHelper.upload_contract_picture(et_contract_id.getText().toString().trim(), subject, path, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                path = photos.get(0);
                tv_path.setText(path);
                tv_path.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_QR_CODE_SCAN_FOR_ID) {
            if (data != null) {
                et_contract_id.setText(data.getStringExtra("text"));
            }
        }
    }

    @Override
    public void onSuccess() {
        progressDialog.dismiss();
        Cookie.updateContractArr(this, et_contract_id.getText().toString().trim());
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String error) {
        progressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        subject = position + "";
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
