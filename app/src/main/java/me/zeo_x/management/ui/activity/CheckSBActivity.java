package me.zeo_x.management.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
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

/*
* 拍照点检
* */
public class CheckSBActivity extends AppCompatActivity implements HttpHelper.HttpUploadListener, AdapterView.OnItemSelectedListener, ImageCompress.ImageCompressListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_take_photo)
    Button btn_take_photo;
    @Bind(R.id.et_equ_id)
    AutoCompleteTextView et_equ_id;
    @Bind(R.id.btn_confirm)
    Button btn_confirm;
    @Bind(R.id.spinner_content)
    Spinner spinner_content;
    @Bind(R.id.tv_path)
    TextView tv_path;
    @Bind(R.id.et_desc)
    EditText et_desc;
    @Bind(R.id.rg_qualified)
    RadioGroup rg_qualified;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    @Bind(R.id.iv_scan)
    ImageView iv_scan;
    private String path = "";
    private HttpHelper httpHelper;
    private ProgressDialog progressDialog;
    private String selected_content_id;
    private List<Map<String, String>> data;
    private SimpleAdapter adapter;
    private ArrayAdapter<String> arrayAdapter;
    private String tempFileName;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_sb);
        httpHelper = HttpHelper.getInstance(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setTitle("设备点检");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setCancelable(false);
        data = new ArrayList<>();
        adapter = new SimpleAdapter(this, data, R.layout.spinner_item, new String[]{"content_id", "content_name"}, new int[]{R.id.tv0, R.id.tv});
        spinner_content.setAdapter(adapter);
        spinner_content.setOnItemSelectedListener(this);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Cookie.getEquArr(this));
        et_equ_id.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.btn_confirm)
    void btn_confirm() {
        if (TextUtils.isEmpty(et_equ_id.getText())) {
            et_equ_id.setError("请输入设备编号");
        } else {
            getContent(et_equ_id.getText().toString());
        }
    }

    @OnClick(R.id.btn_take_photo)
    void take_photo() {
//        PhotoPickerIntent intent = new PhotoPickerIntent(CheckSBActivity.this);
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


    @OnClick(R.id.btn_commit)
    void commit() {
        if (TextUtils.isEmpty(et_desc.getText())) {
            et_desc.setError("请填写点检描述");
        } else if (TextUtils.isEmpty(et_equ_id.getText())) {
            Toast.makeText(this, "请填写设备编号", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(selected_content_id)) {
            Toast.makeText(this, "请选择检查内容", Toast.LENGTH_SHORT).show();
        } else {
            String sure = "";
            switch (rg_qualified.getCheckedRadioButtonId()) {
                case R.id.rb_qualified:
                    sure = "0";
                    break;
                case R.id.rb_unqualified:
                    sure = "1";
                    break;
            }
            progressDialog.setMessage("正在提交，请稍候");
            progressDialog.show();
            httpHelper.check_sb(et_equ_id.getText().toString(), path, et_desc.getText().toString().trim(), sure, selected_content_id, this);
            btn_commit.setEnabled(false);
        }
    }

    @OnClick(R.id.iv_scan)
    void iv_scan() {
        Intent i = new Intent(this, QRCodeActivity.class);
        startActivityForResult(i, QRCodeActivity.REQUEST_QR_CODE_SCAN);
    }

    @Override
    public void onSuccess() {
        btn_commit.setEnabled(true);
        progressDialog.dismiss();
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onFailure(String error) {
        btn_commit.setEnabled(true);
        progressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
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
            et_equ_id.setText(data.getStringExtra("text"));
        }

    }

    private void getContent(String equ_id) {
        progressDialog.setMessage("正在加载,请稍候");
        progressDialog.show();
        httpHelper.get_check_content(equ_id, new HttpHelper.HttpListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                Cookie.updateEquArr(CheckSBActivity.this, et_equ_id.getText().toString().trim());
                arrayAdapter.clear();
                arrayAdapter.addAll(Cookie.getEquArr(CheckSBActivity.this));
                arrayAdapter.notifyDataSetChanged();
                CheckSBActivity.this.data.clear();
                CheckSBActivity.this.data.addAll(data);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(CheckSBActivity.this, error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_content_id = data.get(position).get("content_id");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCompressSuccess(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CheckSBActivity.this.path = path;
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
                Toast.makeText(CheckSBActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
