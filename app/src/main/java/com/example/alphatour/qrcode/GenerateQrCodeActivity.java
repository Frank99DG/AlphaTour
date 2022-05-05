package com.example.alphatour.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateQrCodeActivity extends AppCompatActivity {

    private TextView qrCodeView;
    private ImageView qrCodeImage;
    private TextInputEditText qrData;
    private Button generateQrCode;
    private QRGEncoder qrgEncoder;
    private static Bitmap bitmap;
    public static boolean qrFlag=false,created=false;
    private static String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        qrCodeView=findViewById(R.id.qrTextView);
        qrCodeImage=findViewById(R.id.qrImage);
        qrData=findViewById(R.id.idQrText);
        generateQrCode=findViewById(R.id.buttonGenerateQrCode);
    }

    public static boolean getQrFlag() {
        return qrFlag;
    }

    public void setQrFlag(boolean qrFlag) {
        this.qrFlag = qrFlag;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static String getData() {
        return data;
    }

    public void generateQrCode(View view) {

        data= qrData.getText().toString();
        if(data.isEmpty()){
            Toast.makeText(GenerateQrCodeActivity.this,R.string.qr_data_missing,Toast.LENGTH_LONG).show();
        }else{

            WindowManager manager= (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display= manager.getDefaultDisplay();
            Point point= new Point();
            display.getSize(point);
            int width= point.x;
            int height=point.y;
            int dimen=width<height ? width:height;
            dimen= dimen * 3/4;

            qrgEncoder= new QRGEncoder(qrData.getText().toString(),null, QRGContents.Type.TEXT,dimen);

            try{
                bitmap= qrgEncoder.encodeAsBitmap();
                qrCodeView.setVisibility(View.GONE);
                qrCodeImage.setImageBitmap(bitmap);
                setQrFlag(true);
                created=true;
                setBitmap(bitmap);
                Intent intent= new Intent();
                setResult(Activity.RESULT_CANCELED,intent);
                finish();

            }catch(WriterException e){

                e.printStackTrace();

            }



        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!created){
            setQrFlag(false);
        }
    }
}