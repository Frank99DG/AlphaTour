package com.example.alphatour.qrcode;

import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.content.Context;
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

public class GenerateQrCodeClass {

    private Context context;

    public GenerateQrCodeClass(Context context) {
        this.context=context;
    }

    private TextView qrCodeView;
    private ImageView qrCodeImage;
    private TextInputEditText qrData;
    private Button generateQrCode;
    private QRGEncoder qrgEncoder;
    private static Bitmap bitmap;
    public static boolean qrFlag=false,created=false;
    private static String data;


    public Bitmap generateQrCode(String data) {

            WindowManager manager= (WindowManager) context.getSystemService(WINDOW_SERVICE);
            Display display= manager.getDefaultDisplay();
            Point point= new Point();
            display.getSize(point);
            int width= point.x;
            int height=point.y;
            int dimen=width<height ? width:height;
            dimen= dimen * 3/4;

            qrgEncoder= new QRGEncoder(data,null, QRGContents.Type.TEXT,dimen);

            try{
                bitmap= qrgEncoder.encodeAsBitmap();
            }catch(WriterException e){

                e.printStackTrace();

            }
          return bitmap;
    }
}
