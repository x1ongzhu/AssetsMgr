package me.zeo_x.management.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zeo_x.management.R;
import me.zeo_x.management.data.HttpHelper;
import me.zeo_x.management.ui.adapter.EquAdapter;

public class EquListActivity extends AppCompatActivity implements EquAdapter.OnItemClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private HttpHelper httpHelper;
    private EquAdapter adapter;
    private List<Map<String, String>> data;
    private Map<String, List<String>> map;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equ_list);
        ButterKnife.bind(this);
        httpHelper = HttpHelper.getInstance(this);
        data = new ArrayList<>();
        map = new HashMap<>();
        initView();
    }

    private void initView() {
        toolbar.setTitle("全部设备");
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
        adapter = new EquAdapter(this, map, data);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.RED)
                        .colorResId(R.color.dividers_color_dark)
                        .sizeResId(R.dimen.dividerHeight)
                        .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                        .build());
        getEquList();
    }

    void getEquList() {
        progressDialog.show();
        httpHelper.get_equ_list(new HttpHelper.HttpListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                EquListActivity.this.data.clear();
                EquListActivity.this.data.addAll(data);
                Log.d("zzzzz", data.size() + "");
                httpHelper.get_equ_state(new HttpHelper.HttpListener<Map<String, List<String>>>() {
                    @Override
                    public void onSuccess(Map<String, List<String>> data) {
                        EquListActivity.this.map.clear();
                        EquListActivity.this.map.putAll(data);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(EquListActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                progressDialog.dismiss();
                Toast.makeText(EquListActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v, final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//        if (map.containsKey(data.get(position).get("equ_id"))) {
//            builder.setMessage("状态:" + map.get(data.get(position).get("equ_id")) + "");
//        } else {
//            builder.setMessage("状态:正常");
//        }
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton("详情", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Intent intent = new Intent(EquListActivity.this, EquDetailActivity.class);
//                intent.putExtra("equ_id", data.get(position).get("equ_id"));
//                if (map.containsKey(data.get(position).get("equ_id"))) {
//                    intent.putExtra("state", map.get(data.get(position).get("equ_id")) + "");
//                }
//                startActivity(intent);
//            }
//        });
//        builder.create().show();
        Intent intent = new Intent(EquListActivity.this, EquDetailActivity.class);
        intent.putExtra("equ_id", data.get(position).get("equ_id"));
        if (map.containsKey(data.get(position).get("equ_id"))) {
            intent.putExtra("state", map.get(data.get(position).get("equ_id")) + "");
        }
        startActivity(intent);
    }

}
