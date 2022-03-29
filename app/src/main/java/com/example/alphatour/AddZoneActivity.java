package com.example.alphatour;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;

public class AddZoneActivity extends AppCompatActivity {



    private int zoneNumber = 1;
    private EditText nameZone,title,description,photo,qrCode,activity,sensorCode;
    Button addElement, generateQrCode;
    LinearLayout layout_list;
    ArrayList<Element> element_list = new ArrayList<>();

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
        layout_list = findViewById(R.id.listaElementiLayout);
        generateQrCode = findViewById(R.id.inputQrCode);

    }


    public void addViewElemento(View v) {

        final View elementView = getLayoutInflater().inflate(R.layout.row_add_element,null,false);
        ImageView removeElement = (ImageView) elementView.findViewById(R.id.buttonDeleteElement);
        Button addPhoto=(Button) elementView.findViewById(R.id.inputPhoto);

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

        Uri uri = data.getData();
        Toast.makeText(AddZoneActivity.this,R.string.upload_photo,Toast.LENGTH_LONG).show();
        //salvataggio foto
    }


    public void /*boolean*/ inputControl(View v){
        element_list.clear();
        Boolean errorFlag = false;

        String NameZone = nameZone.getText().toString();
        if(NameZone.isEmpty()){
            nameZone.setError(getString(R.string.campo_obbligatorio));
            nameZone.requestFocus();
        }

        for(int i = 0; i < layout_list.getChildCount(); i++){

            View elementView = layout_list.getChildAt(i);

             title = elementView.findViewById(R.id.inputTitle);
             description = elementView.findViewById(R.id.inputDescription);
             //foto = elementView.findViewById( );
             qrCode = elementView.findViewById(R.id.inputQrCode);
             activity = elementView.findViewById(R.id.inputActivity);
             sensorCode = elementView.findViewById(R.id.inputSensorCode);

             Element element = new Element();

             String Title = title.getText().toString();
             String Description= description.getText().toString();
             //String Photo = foto.getText().toString();
             String QrCode = qrCode.getText().toString();
             String Activity = activity.getText().toString();
             String SensorCode = sensorCode.getText().toString();


            if(Title.isEmpty()){
                title.setError(getString(R.string.campo_obbligatorio));
                title.requestFocus();
                errorFlag = true;
            }else{
                element.setTitle(Title);
            }

            if(Description.isEmpty()){
                description.setError(getString(R.string.campo_obbligatorio));
                description.requestFocus();
                errorFlag = true;
            }else{
                element.setDescription(Description);
            }

            if(QrCode.isEmpty()){
                qrCode.setError(getString(R.string.campo_obbligatorio));
                qrCode.requestFocus();
                errorFlag = true;
            }else{
                element.setQrCode(QrCode);
            }

            if(Activity.isEmpty()){
                activity.setError(getString(R.string.campo_obbligatorio));
                activity.requestFocus();
                errorFlag = true;
            }else{
                element.setActivity(Activity);
            }

            if(SensorCode.isEmpty()){
                sensorCode.setError(getString(R.string.campo_obbligatorio));
                sensorCode.requestFocus();
                errorFlag = true;
            }else{
                element.setSensorCode(SensorCode);
            }

            if(!errorFlag){
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


    /*public void saveElements(View v) {

        String Nome = nome.getText().toString();
        String Cognome = cognome.getText().toString();
        String DataNascita = dataNascita.getText().toString();
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
    }*/

    public void generateQrCode(View v){

        startActivity(new Intent(this, GenerateQrCodeActivity.class));
    }

    public void createZoneActivity(View v){
        startActivity(new Intent(this, AddZoneActivity.class));

    }


}