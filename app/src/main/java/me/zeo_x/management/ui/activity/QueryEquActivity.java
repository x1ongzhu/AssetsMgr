package me.zeo_x.management.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zeo_x.management.R;
import me.zeo_x.management.data.Cookie;

public class QueryEquActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_scan)
    ImageView iv_scan;
    @Bind(R.id.et_equ_id)
    AutoCompleteTextView et_equ_id;
    @Bind(R.id.btn_query)
    Button btn_query;
    @Bind(R.id.btn_view_all)
    Button btn_view_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_equ);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setTitle("查询设备");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Cookie.getEquArr(this));
        et_equ_id.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.btn_query)
    void btn_query() {
        if (!TextUtils.isEmpty(et_equ_id.getText())) {
            Intent intent = new Intent(this, EquDetailActivity.class);
            intent.putExtra("equ_id", et_equ_id.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "请输入设备编号", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_view_all)
    void btn_view_all() {
        Intent intent = new Intent(this, EquListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_scan)
    void iv_scan() {
        Intent i = new Intent(this, QRCodeActivity.class);
        startActivityForResult(i, QRCodeActivity.REQUEST_QR_CODE_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == QRCodeActivity.REQUEST_QR_CODE_SCAN) {
            if (data != null) {
                et_equ_id.setText(data.getStringExtra("text"));
            }
        }
    }
}
