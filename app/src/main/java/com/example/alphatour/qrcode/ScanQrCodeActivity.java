package com.example.alphatour.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.DashboardActivity;
import com.example.alphatour.ModifyObjectActivity;
import com.example.alphatour.R;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class ScanQrCodeActivity extends AppCompatActivity {

    private TextView scanText;
    private ScannerLiveView scannerLiveView;
    private Button modifyObject;
    private Dialog dialog;
    private TextView yesFinal,titleDialog,textDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);

        scannerLiveView=findViewById(R.id.scannerLayout);
        scanText=findViewById(R.id.textScan);
        modifyObject=findViewById(R.id.buttonModifyObject);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        yesFinal = dialog.findViewById(R.id.btn_termina_permission);
        titleDialog = dialog.findViewById(R.id.titleDialog_permission);
        textDialog = dialog.findViewById(R.id.textDialog_permission);


        if (checkPermission()) {
            Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_LONG).show();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ScanQrCodeActivity.this, Manifest.permission.CAMERA)) {
                dialog.show();
                yesFinal.setText("OK");

                titleDialog.setText(R.string.permit_required);
                textDialog.setText(R.string.permission_qr_text);
                textDialog.setTextColor(getResources().getColor(R.color.black));

                yesFinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        requestPermission();
                    }
                });
            } else {
                requestPermission();
            }

        }

        scannerLiveView.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
               // Toast.makeText(ScanQrCodeActivity.this,"Scaner Started...",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
  //              Toast.makeText(ScanQrCodeActivity.this,"Scaner Stopped...",Toast.LENGTH_LONG).show();
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
                         String dashboardFlag = "1"; //per indicare a ModifyObject che fa parte della scannerizzazione
                         intent.putExtra("dashboardFlag", dashboardFlag);
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

            //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
        }else{
                //Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(ScanQrCodeActivity.this, DashboardActivity.class);
                startActivity(intent);

        }
    }

}