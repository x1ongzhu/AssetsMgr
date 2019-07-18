package me.zeo_x.management.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zeo_x.management.R;
import me.zeo_x.management.data.Cookie;
import me.zeo_x.management.data.HttpHelper;
import me.zeo_x.management.ui.adapter.SimpleRecyclerViewAdapter;

public class EquDetailActivity extends AppCompatActivity implements HttpHelper.HttpListener<List<String>> {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private HttpHelper httpHelper;
    private String equ_id = "";
    private List<String> data;
    private SimpleRecyclerViewAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equ_detail);
        ButterKnife.bind(this);
        equ_id = getIntent().getStringExtra("equ_id");
        if (TextUtils.isEmpty(equ_id))
            finish();
        httpHelper = HttpHelper.getInstance(this);
        data = new ArrayList<>();
        initView();
    }

    private void initView() {
        toolbar.setTitle(equ_id + "设备详情");
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
        progressDialog.setMessage("正在加载,请稍候");
        progressDialog.setCancelable(false);
        adapter = new SimpleRecyclerViewAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.RED)
                        .colorResId(R.color.dividers_color_dark)
                        .sizeResId(R.dimen.dividerHeight)
                        .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                        .build());
        getEquDetail();
    }

    void getEquDetail() {
        progressDialog.show();
        httpHelper.get_equ_detail(equ_id, this);
    }

    @Override
    public void onSuccess(List<String> data) {
        Cookie.updateEquArr(this, equ_id);
        this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

}
