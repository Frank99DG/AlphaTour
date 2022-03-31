package com.example.alphatour.qrcode;

import androidx.appcompat.app.AppCompatActivity;

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
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        qrCodeView=findViewById(R.id.qrTextView);
        qrCodeImage=findViewById(R.id.qrImage);
        qrData=findViewById(R.id.idQrText);
        generateQrCode=findViewById(R.id.buttonGenerateQrCode);
    }

    public void generateQrCode(View view) {

        String data= qrData.getText().toString();
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

            }catch(WriterException e){

                e.printStackTrace();

            }



        }
    }
}