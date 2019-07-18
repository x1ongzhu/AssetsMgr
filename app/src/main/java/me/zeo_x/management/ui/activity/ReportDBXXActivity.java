package me.zeo_x.management.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPickerActivity;
import me.zeo_x.management.R;
import me.zeo_x.management.data.Cookie;
import me.zeo_x.management.data.HttpHelper;
import me.zeo_x.management.utils.ImageCompress;
import me.zeo_x.management.utils.Utils;

public class ReportDBXXActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ImageCompress.ImageCompressListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_take_photo)
    Button btn_take_photo;
    @Bind(R.id.tv_path)
    TextView tv_path;
    @Bind(R.id.et_equ_id)
    AutoCompleteTextView et_equ_id;
    @Bind(R.id.iv_scan)
    ImageView iv_scan;
    @Bind(R.id.et_desc)
    EditText et_desc;
    @Bind(R.id.tv_content)
    TextView tv_content;
    @Bind(R.id.spinner_content)
    Spinner spinner_content;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    private HttpHelper httpHelper;
    private ProgressDialog progressDialog;
    private List<Map<String, String>> data;
    private SimpleAdapter adapter;
    private String type;
    private String id_type;
    private String content_type;
    private String content;
    private String path = "";
    private String tempFileName;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_dbxx);
        type = getIntent().getStringExtra("type");
        httpHelper = HttpHelper.getInstance(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (TextUtils.equals(type, "1")) {
            toolbar.setTitle("定保");
            id_type = "db_id";
            content_type = "db_content";
            tv_content.setText("定保内容");
        } else {
            toolbar.setTitle("小修");
            id_type = "xx_id";
            content_type = "xx_content";
            tv_content.setText("小修内容");
        }
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
        progressDialog.setMessage("正在提交，请稍候");
        progressDialog.setCancelable(false);
        data = new ArrayList<>();
        adapter = new SimpleAdapter(this, data, R.layout.spinner_item, new String[]{content_type}, new int[]{R.id.tv});
        spinner_content.setAdapter(adapter);
        spinner_content.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Cookie.getEquArr(this));
        et_equ_id.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.btn_take_photo)
    void take_photo() {
//        PhotoPickerIntent intent = new PhotoPickerIntent(ReportDBXXActivity.this);
//        intent.setPhotoCount(1);
//        intent.setShowCamera(true);
//        intent.setShowGif(true);
//        startActivityForResult(intent, 1);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tempFileName = Utils.getRandomString(10) + ".jpg";
        Uri imageUri = Uri.fromFile(new File(getExternalCacheDir(), tempFileName));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @OnClick(R.id.btn_view)
    void btn_view() {
        if (TextUtils.isEmpty(et_equ_id.getText())) {
            Toast.makeText(this, "请输入设备编号", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("正在加载，请稍候");
            progressDialog.show();
            httpHelper.get_db_xx_content(et_equ_id.getText().toString(), type, new HttpHelper.HttpListener<List<Map<String, String>>>() {
                @Override
                public void onSuccess(List<Map<String, String>> data) {
                    Cookie.updateEquArr(ReportDBXXActivity.this, et_equ_id.getText().toString().trim());
                    progressDialog.dismiss();
                    ReportDBXXActivity.this.data.clear();
                    ReportDBXXActivity.this.data.addAll(data);
                    Log.d("zzzzz", data + "");
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String error) {
                    progressDialog.dismiss();
                    Toast.makeText(ReportDBXXActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.iv_scan)
    void iv_scan() {
        Intent i = new Intent(this, QRCodeActivity.class);
        startActivityForResult(i, QRCodeActivity.REQUEST_QR_CODE_SCAN);
    }

    @OnClick(R.id.btn_commit)
    void btn_commit() {
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请选择" + tv_content.getText(), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_desc.getText())) {
            et_desc.setError("请填写描述");
        } else if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "请先拍照", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("正在提交，请稍候");
            progressDialog.show();
            if (TextUtils.equals(type, "1")) {
                httpHelper.upload_db(et_equ_id.getText().toString().trim(), path, content, et_desc.getText().toString(), new HttpHelper.SimpleHttpListener() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                        Toast.makeText(ReportDBXXActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(ReportDBXXActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                httpHelper.upload_xx(et_equ_id.getText().toString().trim(), path, content, et_desc.getText().toString(), new HttpHelper.SimpleHttpListener() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                        Toast.makeText(ReportDBXXActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(ReportDBXXActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
        } else if (resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            path = getExternalCacheDir() + File.separator + tempFileName;
            ImageCompress imageCompress = new ImageCompress();
            imageCompress.compress(path, this, this);
        } else if (resultCode == RESULT_OK && requestCode == QRCodeActivity.REQUEST_QR_CODE_SCAN) {
            if (data != null) {
                et_equ_id.setText(data.getStringExtra("text"));
                btn_view();
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        content = data.get(position).get(id_type);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCompressSuccess(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ReportDBXXActivity.this.path = path;
                tv_path.setText(path);
                tv_path.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onCompressFailed(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ReportDBXXActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
