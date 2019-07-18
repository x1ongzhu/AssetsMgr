package me.zeo_x.management.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.zeo_x.management.data.LoginHelper;
import me.zeo_x.management.model.bean.UserBean;
import me.zeo_x.management.R;

public class MainActivity extends AppCompatActivity implements LoginHelper.LoginListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_ip)
    AutoCompleteTextView et_ip;
    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.cb_rem_pswd)
    CheckBox cb_rem_pswd;
    @Bind(R.id.bt_login)
    Button bt_login;
    private ProgressDialog progressDialog;
    private SharedPreferences spf;
    private ArrayAdapter<String> arrayAdapter;
    private boolean rememberPswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spf = getPreferences(MODE_APPEND);
        String str = spf.getString("ips", "");
        String[] arr = str.split("#");
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("正在登录，请稍候");
        progressDialog.setCancelable(false);
        et_ip.setAdapter(arrayAdapter);
        rememberPswd = spf.getBoolean("rememberPswd", false);
        et_username.setText(spf.getString("username", ""));
        et_ip.setText(spf.getString("ip", ""));
        if (rememberPswd) {
            cb_rem_pswd.setChecked(true);
            et_password.setText(spf.getString("password", ""));
            //login();
        }
    }

    @OnClick(R.id.bt_login)
    void login() {
        if (TextUtils.isEmpty(et_username.getText())) {
            et_username.setError("请输入用户名");
        } else if (TextUtils.isEmpty(et_password.getText())) {
            et_password.setError("请输入密码");
        } else {
            bt_login.setEnabled(false);
            progressDialog.show();
            LoginHelper.getInstance(MainActivity.this).Login(et_username.getText().toString().trim(), et_password.getText().toString().trim(), this);
        }
    }


    @OnCheckedChanged(R.id.cb_rem_pswd)
    void cb_rem_pswd(boolean isChecked) {
        rememberPswd = isChecked;
    }

    @Override
    public void onSuccess(UserBean user) {
        bt_login.setEnabled(true);
        progressDialog.dismiss();
        SharedPreferences.Editor editor = spf.edit();
        String str = spf.getString("ips", "");
        if (!str.contains(et_ip.getText().toString())) {
            List<String> list = new ArrayList<>();
            for (String s : str.split("#")) {
                list.add(s);
            }
            if (list.size() == 10) {
                list.remove(0);
            }
            list.add(et_ip.getText().toString());
            String tmp = "";
            for (String s : list) {
                tmp += s + "#";
            }
            editor.putString("ips", tmp);
        }
        editor.putString("username", et_username.getText().toString());
        if (rememberPswd) {
            editor.putString("password", et_password.getText().toString());
            editor.putString("ip", et_ip.getText().toString());
        }
        editor.putBoolean("rememberPswd", rememberPswd);
        editor.apply();
        Intent i = new Intent(MainActivity.this, MenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void onFailure(String error) {
        bt_login.setEnabled(true);
        progressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }

        /**
         * 判断IP格式和范围
         */
        String regex1 = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat1 = Pattern.compile(regex1);
        Matcher mat1 = pat1.matcher(addr);
        boolean ipAddress = mat1.find();
        String regex2 = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\:\\d{1,5}$";
        Pattern pat2 = Pattern.compile(regex2);
        Matcher mat2 = pat2.matcher(addr);
        boolean ipport = mat2.find();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

}