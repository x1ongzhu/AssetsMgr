package me.zeo_x.management.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zeo_x.management.R;

public class ManageContractActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_query_contract)
    Button btn_query_contract;
    @Bind(R.id.btn_upload_contract)
    Button btn_upload_contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contract);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setTitle("合同管理");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_query_contract)
    void btn_query_contract() {
        Intent i = new Intent(this, QueryContractActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_upload_contract)
    void btn_upload_contract() {
        Intent i = new Intent(this, UploadContractActivity.class);
        startActivity(i);
    }
}
