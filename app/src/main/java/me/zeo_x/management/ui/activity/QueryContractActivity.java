package me.zeo_x.management.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zeo_x.management.R;
import me.zeo_x.management.data.Cookie;

public class QueryContractActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_contract_id)
    AutoCompleteTextView et_contract_id;
    @Bind(R.id.et_property_name)
    EditText et_property_name;
    @Bind(R.id.et_start)
    EditText et_start;
    @Bind(R.id.et_end)
    EditText et_end;
    @Bind(R.id.iv_scan)
    ImageView iv_scan;
    @Bind(R.id.btn_query)
    Button btn_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_contract);
        ButterKnife.bind(this);
        initView();
    }

    void initView() {
        toolbar.setTitle("合同查询");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Cookie.getContractArr(this));
        et_contract_id.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.iv_scan)
    void iv_scan() {
        Intent i = new Intent(this, QRCodeActivity.class);
        startActivityForResult(i, QRCodeActivity.REQUEST_QR_CODE_SCAN);
    }

    @OnClick(R.id.btn_query)
    void btn_query() {
        if (!TextUtils.isEmpty(et_contract_id.getText())) {
            Cookie.updateContractArr(this, et_contract_id.getText().toString().trim());
            Intent i = new Intent(this, ContractListActivity.class);
            i.putExtra("hetong_id", et_contract_id.getText().toString().trim());
            i.putExtra("zichan_name", "");
            i.putExtra("hetong_start", "");
            i.putExtra("hetong_end", "");
            startActivity(i);
        } else if (!TextUtils.isEmpty(et_property_name.getText())) {
            Intent i = new Intent(this, ContractListActivity.class);
            i.putExtra("hetong_id", "");
            i.putExtra("zichan_name", et_property_name.getText().toString().trim());
            i.putExtra("hetong_start", "");
            i.putExtra("hetong_end", "");
            startActivity(i);
        } else if ((!TextUtils.isEmpty(et_start.getText())) || (!TextUtils.isEmpty(et_end.getText()))) {
            Intent i = new Intent(this, ContractListActivity.class);
            i.putExtra("hetong_id", "");
            i.putExtra("zichan_name", "");
            i.putExtra("hetong_start", et_start.getText().toString().trim());
            i.putExtra("hetong_end", et_end.getText().toString().trim());
            startActivity(i);
        } else {
            Toast.makeText(this, "请填写查询条件", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == QRCodeActivity.REQUEST_QR_CODE_SCAN) {
            if (data != null) {
                et_contract_id.setText(data.getStringExtra("text"));
            }
        }
    }

    @OnClick(R.id.et_start)
    void et_start() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                et_start.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        dialog.show();
    }

    @OnClick(R.id.et_end)
    void et_end() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                et_end.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        dialog.show();
    }
}
