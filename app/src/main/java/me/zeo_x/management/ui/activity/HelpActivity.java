package me.zeo_x.management.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zeo_x.management.R;

public class HelpActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_version_name)
    TextView tv_version_name;
    @Bind(R.id.tv_check_update)
    TextView tv_check_update;
    @Bind(R.id.tv_report)
    TextView tv_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setTitle("帮助");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PackageManager pm = this.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(this.getPackageName(), 0);
            tv_version_name.setText(pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_check_update)
    void tv_check_update() {
    }

    @OnClick(R.id.tv_report)
    void tv_report() {
    }
}
