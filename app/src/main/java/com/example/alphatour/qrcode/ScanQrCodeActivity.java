package com.example.alphatour.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.ModifyObjectActivity;
import com.example.alphatour.R;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class ScanQrCodeActivity extends AppCompatActivity {

    private TextView scanText;
    private ScannerLiveView scannerLiveView;
    private Button modifyObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);

        scannerLiveView=findViewById(R.id.scannerLayout);
        scanText=findViewById(R.id.textScan);
        modifyObject=findViewById(R.id.buttonModifyObject);

        if(checkPermission()){
            Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show();
        }else{
            requestPermission();
        }

        scannerLiveView.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                Toast.makeText(ScanQrCodeActivity.this,"Scaner Started...",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                Toast.makeText(ScanQrCodeActivity.this,"Scaner Stopped...",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScannerError(Throwable err) {

            }

            @Override
            public void onCodeScanned(String data) {
                 scanText.setText(data);
                 modifyObject.setVisibility(View.VISIBLE);
                 modifyObject.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Intent intent= new Intent(ScanQrCodeActivity.this, ModifyObjectActivity.class);
                         intent.putExtra("data",scanText.getText().toString());
                         startActivity(intent);
                     }
                 });
            }
        });

    }

    private boolean checkPermission(){

        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        return cameraPermission== PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        int permissionCode=200; //codice definito da me, servirà nel caso in cui serva controllare più permessi
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},permissionCode);

    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerLiveView.stopScanner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder= new ZXDecoder();
        decoder.setScanAreaPercent(0.8);
        scannerLiveView.setDecoder(decoder);
        scannerLiveView.startScanner();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ) {

            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
        }else{
                Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show();

        }
    }

}