package me.zeo_x.management.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zeo_x.management.model.bean.UserBean;
import me.zeo_x.management.R;

public class MenuActivity extends AppCompatActivity {
    @Bind({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11,})
    List<Button> buttons;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private UserBean user;
    private static final int REQUEST_QR_CODE_SCAN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        user = (UserBean) getIntent().getExtras().getSerializable("user");
        initView();
    }

    private void initView() {
        toolbar.setTitle("设备管理");
        setSupportActionBar(toolbar);
        if (user == null) {
            finish();
        } else {
            if (user.getMobile_check_cz() == 1 || user.getMobile_check_sb() == 1) {
                buttons.get(0).setEnabled(true);
            } else {
                buttons.get(0).setEnabled(false);
            }
            if (user.getMobile_baoxiu_upload() == 1) {
                buttons.get(1).setEnabled(true);
            } else {
                buttons.get(1).setEnabled(false);
            }
            if (user.getMobile_db_upload() == 1) {
                buttons.get(2).setEnabled(true);
            } else {
                buttons.get(2).setEnabled(false);
            }
            if (user.getMobile_xx_upload() == 1) {
                buttons.get(3).setEnabled(true);
            } else {
                buttons.get(3).setEnabled(false);
            }
            if (user.getMobile_weixiu_upload() == 1) {
                buttons.get(4).setEnabled(true);
            } else {
                buttons.get(4).setEnabled(false);
            }
            if (user.getMobile_weixiu_shenhe() == 1) {
                buttons.get(5).setEnabled(true);
            } else {
                buttons.get(5).setEnabled(false);
            }
            if (user.getMobile_record_shenhe() == 1) {
                buttons.get(6).setEnabled(true);
                buttons.get(7).setEnabled(true);
            } else {
                buttons.get(6).setEnabled(false);
                buttons.get(7).setEnabled(true);
            }
            if (user.getMobile_hetong_show() == 1) {
                buttons.get(8).setEnabled(true);
            } else {
                buttons.get(8).setEnabled(false);
            }
            if (user.getMobile_equ_show() == 1) {
                buttons.get(9).setEnabled(true);
            } else {
                buttons.get(9).setEnabled(false);
            }
        }
    }

    @OnClick({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11,})
    void click(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btn0:
//                if (user.getMobile_check_cz() == 1 && user.getMobile_check_sb() == 1) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                    builder.setPositiveButton("拍照点检", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent i = new Intent(MenuActivity.this, CheckSBActivity.class);
//                            startActivity(i);
//                        }
//                    });
//                    builder.setNegativeButton("扫码点检", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent i = new Intent(MenuActivity.this, CheckCZActivity.class);
//                            startActivity(i);
//                        }
//                    });
//                    builder.create().show();
//                } else if (user.getMobile_check_cz() == 1) {
//                    i = new Intent(MenuActivity.this, CheckCZActivity.class);
//                    startActivity(i);
//                } else {
//                    i = new Intent(MenuActivity.this, CheckSBActivity.class);
//                    startActivity(i);
//                }
                i = new Intent(MenuActivity.this, CheckSBActivity.class);
                startActivity(i);
                break;
            case R.id.btn1:
                i = new Intent(MenuActivity.this, ReportRepairActivity.class);
                startActivity(i);
                break;
            case R.id.btn2:
                i = new Intent(MenuActivity.this, ReportDBXXActivity.class);
                i.putExtra("type", "1");
                startActivity(i);
                break;
            case R.id.btn3:
                i = new Intent(MenuActivity.this, ReportDBXXActivity.class);
                i.putExtra("type", "2");
                startActivity(i);
                break;
            case R.id.btn4:
                i = new Intent(MenuActivity.this, RepairDoneActivity.class);
                startActivity(i);
                break;
            case R.id.btn5:
                i = new Intent(MenuActivity.this, RepairCheckActivity.class);
                startActivity(i);
                break;
            case R.id.btn6:
                i = new Intent(MenuActivity.this, DBXXCheckActivity.class);
                i.putExtra("type", "1");
                startActivity(i);
                break;
            case R.id.btn7:
                i = new Intent(MenuActivity.this, DBXXCheckActivity.class);
                i.putExtra("type", "2");
                startActivity(i);
                break;
            case R.id.btn8:
                i = new Intent(MenuActivity.this, ManageContractActivity.class);
                startActivity(i);
                break;
            case R.id.btn9:
                i = new Intent(this, QueryEquActivity.class);
                startActivity(i);
                break;
            case R.id.btn10:
//                i = new Intent(MenuActivity.this, QRCodeActivity.class);
//                startActivityForResult(i, REQUEST_QR_CODE_SCAN);
                i = new Intent(MenuActivity.this, CheckCZActivity.class);
                startActivity(i);
                break;
            case R.id.btn11:
                i = new Intent(MenuActivity.this, HelpActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (data != null) {
                    Toast.makeText(this, data.getStringExtra("text"), Toast.LENGTH_SHORT).show();
                }
                break;
            case RESULT_CANCELED:
                break;
        }
    }
}
