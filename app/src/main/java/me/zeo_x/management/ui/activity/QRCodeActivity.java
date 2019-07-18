package me.zeo_x.management.ui.activity;

import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zeo_x.management.R;

public class QRCodeActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.qrdecoderview)
    QRCodeReaderView qrdecoderview;
    public static final int REQUEST_QR_CODE_SCAN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        qrdecoderview.setOnQRCodeReadListener(this);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Intent i = new Intent();
        i.putExtra("text", text);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
}
