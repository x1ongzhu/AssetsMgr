package me.zeo_x.management.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zeo_x.management.R;
import me.zeo_x.management.data.HttpHelper;
import me.zeo_x.management.ui.adapter.ContractAdapter;

public class ContractListActivity extends AppCompatActivity implements HttpHelper.HttpListener<List<Map<String, String>>> {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private HttpHelper httpHelper;
    private ContractAdapter adapter;
    private List<Map<String, String>> data;
    private String hetong_id, zichan_name, hetong_start, hetong_end;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_list);
        httpHelper = HttpHelper.getInstance(this);
        Intent i = getIntent();
        hetong_id = i.getStringExtra("hetong_id");
        zichan_name = i.getStringExtra("zichan_name");
        hetong_start = i.getStringExtra("hetong_start");
        hetong_end = i.getStringExtra("hetong_end");
        data = new ArrayList<>();
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setTitle("查看合同");
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
        adapter = new ContractAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.RED)
                        .colorResId(R.color.dividers_color_dark)
                        .sizeResId(R.dimen.dividerHeight)
                        .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                        .build());
        getContractList();
    }

    private void getContractList() {
        progressDialog.show();
        httpHelper.query_contract(hetong_id, zichan_name, hetong_start, hetong_end, this);
    }

    @Override
    public void onSuccess(List<Map<String, String>> data) {
        this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void onFailure(String error) {
        progressDialog.dismiss();
    }
}
