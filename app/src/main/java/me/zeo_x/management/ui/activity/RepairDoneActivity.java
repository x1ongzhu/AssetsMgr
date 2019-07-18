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

public class RepairDoneActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ImageCompress.ImageCompressListener {
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
    @Bind(R.id.spinner_content)
    Spinner spinner_content;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    @Bind(R.id.btn_view)
    Button btn_view;
    private String path = "";
    private HttpHelper httpHelper;
    private ProgressDialog progressDialog;
    private String baoxiu_id = "";
    private List<Map<String, String>> data;
    private SimpleAdapter adapter;
    private String tempFileName;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_done);
        httpHelper = HttpHelper.getInstance(this);
        ButterKnife.bind(this);
        initView();
    }

    void initView() {
        toolbar.setTitle("维修完成");
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
        adapter = new SimpleAdapter(this, data, R.layout.spinner_item, new String[]{"baoxiu_desc"}, new int[]{R.id.tv});
        spinner_content.setAdapter(adapter);
        spinner_content.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Cookie.getEquArr(this));
        et_equ_id.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.btn_take_photo)
    void btn_take_photo() {
//        PhotoPickerIntent intent = new PhotoPickerIntent(RepairDoneActivity.this);
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

    @OnClick(R.id.iv_scan)
    void iv_scan() {
        Intent i = new Intent(this, QRCodeActivity.class);
        startActivityForResult(i, QRCodeActivity.REQUEST_QR_CODE_SCAN);
    }

    @OnClick(R.id.btn_view)
    void btn_view() {
        if (TextUtils.isEmpty(et_equ_id.getText())) {
            Toast.makeText(this, "请输入设备编号", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("正在加载，请稍候");
            progressDialog.show();
            httpHelper.get_repair_content(et_equ_id.getText().toString(), new HttpHelper.HttpListener<List<Map<String, String>>>() {
                @Override
                public void onSuccess(List<Map<String, String>> data) {
                    Cookie.updateEquArr(RepairDoneActivity.this, et_equ_id.getText().toString().trim());
                    progressDialog.dismiss();
                    RepairDoneActivity.this.data.clear();
                    RepairDoneActivity.this.data.addAll(data);
                    Log.d("zzzzz", data + "");
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String error) {
                    progressDialog.dismiss();
                    Toast.makeText(RepairDoneActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.btn_commit)
    void btn_commit() {
        if (TextUtils.isEmpty(baoxiu_id)) {
            Toast.makeText(this, "请选择维修完成的项目", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_desc.getText())) {
            Toast.makeText(this, "请填写描述", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("正在提交，请稍候");
            progressDialog.show();
            httpHelper.reort_repair_done(et_equ_id.getText().toString().trim(), baoxiu_id, path, et_desc.getText().toString(), new HttpHelper.HttpUploadListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    Toast.makeText(RepairDoneActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(int progress) {

                }

                @Override
                public void onFailure(String error) {
                    progressDialog.dismiss();
                    Toast.makeText(RepairDoneActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
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
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        baoxiu_id = data.get(position).get("baoxiu_id");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCompressSuccess(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RepairDoneActivity.this.path = path;
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
                Toast.makeText(RepairDoneActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
