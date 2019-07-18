package me.zeo_x.management.ui.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.zeo_x.management.R;
import me.zeo_x.management.data.HttpHelper;

public class RepairCheckActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.cb_all)
    CheckBox cb_all;
    @Bind(R.id.ll_equ_list)
    LinearLayout ll_equ_list;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    private HttpHelper httpHelper;
    private ProgressDialog progressDialog;
    private List<CheckBox> checkBoxList;
    private String equ_id = "";
    private List<String> equList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_check);
        httpHelper = HttpHelper.getInstance(this);
        checkBoxList = new ArrayList<>();
        equList = new ArrayList<>();
        ButterKnife.bind(this);
        initView();
        get_repair_done_list();
    }

    void initView() {
        toolbar.setTitle("维修审核");
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
        progressDialog.setCancelable(false);

    }

    @OnCheckedChanged(R.id.cb_all)
    void cb_all(boolean isChecked) {
        for (CheckBox cb : checkBoxList) {
            cb.setChecked(isChecked);
        }
    }

    private void get_repair_done_list() {
        progressDialog.setMessage("正在加载,请稍候");
        progressDialog.show();
        httpHelper.get_repair_done_list(new HttpHelper.HttpListener<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                progressDialog.dismiss();
                equList.addAll(data);
                for (String str : data) {
                    View v = LayoutInflater.from(RepairCheckActivity.this).inflate(R.layout.repair_done_item, ll_equ_list, false);
                    TextView tv = (TextView) v.findViewById(R.id.tv);
                    tv.setText(str);
                    CheckBox cb = (CheckBox) v.findViewById(R.id.cb);
                    checkBoxList.add(cb);
                    ll_equ_list.addView(v);
                }
            }

            @Override
            public void onFailure(String error) {
                progressDialog.dismiss();
                Toast.makeText(RepairCheckActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_commit)
    void btn_commit() {
        equ_id = "";
        for (int i = 0; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isChecked()) {
                if (i == 0) {
                    equ_id += equList.get(i);
                } else {
                    equ_id += "," + equList.get(i);
                }
            }
        }
        if (TextUtils.isEmpty(equ_id)) {
            Toast.makeText(this, "请选择审核通过的设备", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("正在提交,请稍候");
            progressDialog.show();
            httpHelper.repair_check(equ_id, new HttpHelper.SimpleHttpListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    Toast.makeText(RepairCheckActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String error) {
                    progressDialog.dismiss();
                    Toast.makeText(RepairCheckActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
