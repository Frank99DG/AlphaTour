package com.example.alphatour;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.alphatour.oggetti.Element;
import com.example.alphatour.qrcode.GenerateQrCodeActivity;
import com.example.alphatour.qrcode.ScanQrCodeActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;

public class AddZoneActivity extends AppCompatActivity {



    private int zoneNumber = 1;
    private EditText nameZone,title,description,activity,sensorCode;
    private Button addElement, generateQrCode,photo;
    private LinearLayout layout_list;
    private ArrayList<Element> element_list = new ArrayList<>();
    private Uri uri;
    private boolean flagPhoto=false;
    private boolean flagQrCode=false;

    /*FloatingActionButton bottone;
    EditText nomeLuogo;
    private   Animation rotateOpen;
    private   Animation rotateClose;
    private   Animation fromBottom;
    private   Animation toBottom;
    private  boolean clicked=false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zone_curator);

        addElement = findViewById(R.id.buttonAggiungiElemento);
        nameZone = findViewById(R.id.inputNomeZona);
        layout_list = findViewById(R.id.listZoneLayout);
        generateQrCode = findViewById(R.id.inputQrCode1);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(GenerateQrCodeActivity.getQrFlag()==true){
            photo.setTextColor(getResources().getColor(R.color.white));
        }

    }

    public void addViewElemento(View v) {

        final View elementView = getLayoutInflater().inflate(R.layout.row_add_element_hold,null,false);
        ImageView removeElement = (ImageView) elementView.findViewById(R.id.deleteZone1);
        Button addPhoto=(Button) elementView.findViewById(R.id.inputPhoto1);

        removeElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layout_list.removeView(elementView);
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddZoneActivity.this)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                        .start(20);
            }
        });

        layout_list.addView(elementView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();
        if(uri!=null) {
            flagPhoto = true;
            photo.setTextColor(getResources().getColor(R.color.white));
            Toast.makeText(AddZoneActivity.this, R.string.upload_photo, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(AddZoneActivity.this, "Non hai aggiunto la foto!", Toast.LENGTH_LONG).show();
        }
        //salvataggio foto
    }


    public void /*boolean*/ inputControl(View v){
        element_list.clear();


        String NameZone = nameZone.getText().toString();
        if(NameZone.isEmpty()){
            nameZone.setError(getString(R.string.required_field));
            nameZone.requestFocus();
        }

        for(int i = 0; i < layout_list.getChildCount(); i++){

            View elementView = layout_list.getChildAt(i);

             title = elementView.findViewById(R.id.inputZone1);
             description = elementView.findViewById(R.id.inputDescription1);
             photo= elementView.findViewById(R.id.inputPhoto1);
             generateQrCode = elementView.findViewById(R.id.inputQrCode1);
             //activity = elementView.findViewById(R.id.inputActivity);
             sensorCode = elementView.findViewById(R.id.inputSensorCode1);

             Element element = new Element();

             String Title = title.getText().toString();
             String Description= description.getText().toString();
            // String Activity = activity.getText().toString();
             String SensorCode = sensorCode.getText().toString();

            boolean control=inputElementControl(Title,Description,flagPhoto,SensorCode,element);


            if(!control){
                element_list.add(element);
            }

        }

        if(layout_list.getChildCount() == 0){
            Toast.makeText(this,"Aggiungi almeno un elemento", Toast.LENGTH_SHORT).show();
        }else if( element_list.size() < layout_list.getChildCount() || NameZone.isEmpty()){
            Toast.makeText(this,"Completa correttamente i campi", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("elementi", element_list.toString());
            Toast.makeText(this,"Zona ed elementi salvati", Toast.LENGTH_SHORT).show();
        }

        //return errorFlag;
    }

    private boolean inputElementControl(String Title, String Description, boolean flagPhoto, String SensorCode,Element element) {

            Boolean errorFlag = false;

            if(Title.isEmpty()){
                title.setError(getString(R.string.required_field));
                title.requestFocus();
                errorFlag = true;
            }else{
                element.setTitle(Title);
            }

            if(Description.isEmpty()){
                description.setError(getString(R.string.required_field));
                description.requestFocus();
                errorFlag = true;
            }else{
                element.setDescription(Description);
            }

            if(flagPhoto==false){

                photo.setTextColor(getResources().getColor(R.color.red));
                errorFlag=true;
            }else{
                element.setPhoto(uri);
            }

            if(GenerateQrCodeActivity.getQrFlag()==false){
                generateQrCode.setTextColor(getResources().getColor(R.color.red));
                errorFlag = true;
            }else{
                element.setQrCode(GenerateQrCodeActivity.getBitmap());
            }

            /*if(Activity.isEmpty()){
                activity.setError(getString(R.string.campo_obbligatorio));
                activity.requestFocus();
                errorFlag = true;
            }else{
                element.setActivity(Activity);
            }*/

            if(SensorCode.isEmpty()){
                sensorCode.setError(getString(R.string.required_field));
                sensorCode.requestFocus();
                errorFlag = true;
            }else{
                element.setSensorCode(SensorCode);
            }
            return errorFlag;

    }




    /*public void saveElements(View v) {

        String Nome = nome.getText().toString();
        String Cognome = cognome.getText().toString();
        String DataNascita = dataNascita.getText().toString();
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
    }*/

    public void generateQrCodeActivity(View v){

        Intent intent=new Intent(this, GenerateQrCodeActivity.class);
        startActivity(intent);
    }

    public void scanQrCode(View v){

        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }

    public void createZoneActivity(View v){
        startActivity(new Intent(this, AddZoneActivity.class));

    }


}