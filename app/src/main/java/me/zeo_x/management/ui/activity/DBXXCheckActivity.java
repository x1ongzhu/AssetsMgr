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
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.zeo_x.management.R;
import me.zeo_x.management.data.HttpHelper;

public class DBXXCheckActivity extends AppCompatActivity {
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
    private List<Map<String, String>> equList;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbxxcheck);
        type = getIntent().getStringExtra("type");
        httpHelper = HttpHelper.getInstance(this);
        checkBoxList = new ArrayList<>();
        equList = new ArrayList<>();
        ButterKnife.bind(this);
        initView();
        get_db_xx_list();
    }

    void initView() {
        if (TextUtils.equals(type, "1")) {
            toolbar.setTitle("定保审核");
        } else {
            toolbar.setTitle("小修审核");
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
        progressDialog.setCancelable(false);

    }

    @OnCheckedChanged(R.id.cb_all)
    void cb_all(boolean isChecked) {
        for (CheckBox cb : checkBoxList) {
            cb.setChecked(isChecked);
        }
    }

    private void get_db_xx_list() {
        progressDialog.setMessage("正在加载,请稍候");
        progressDialog.show();
        httpHelper.db_xx_check_list(type, new HttpHelper.HttpListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                progressDialog.dismiss();
                equList.addAll(data);
                for (Map<String, String> m : data) {
                    View v = LayoutInflater.from(DBXXCheckActivity.this).inflate(R.layout.db_xx_check_item, ll_equ_list, false);
                    TextView tv1 = (TextView) v.findViewById(R.id.tv1);
                    TextView tv2 = (TextView) v.findViewById(R.id.tv2);
                    tv1.setText(m.get("equ_id"));
                    tv2.setText(m.get("equ_name"));
                    CheckBox cb = (CheckBox) v.findViewById(R.id.cb);
                    checkBoxList.add(cb);
                    ll_equ_list.addView(v);
                }
            }

            @Override
            public void onFailure(String error) {
                progressDialog.dismiss();
                Toast.makeText(DBXXCheckActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_commit)
    void btn_commit() {
        equ_id = "";
        for (int i = 0; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isChecked()) {
                if (i == 0) {
                    equ_id += equList.get(i).get("equ_id");
                } else {
                    equ_id += "," + equList.get(i).get("equ_id");
                }
            }
        }
        if (TextUtils.isEmpty(equ_id)) {
            Toast.makeText(this, "请选择审核通过的设备", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("正在提交,请稍候");
            progressDialog.show();
            httpHelper.db_xx_check(equ_id, type, new HttpHelper.SimpleHttpListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    Toast.makeText(DBXXCheckActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String error) {
                    progressDialog.dismiss();
                    Toast.makeText(DBXXCheckActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
