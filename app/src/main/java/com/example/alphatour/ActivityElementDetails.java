package com.example.alphatour;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.alphatour.oggetti.Element;
import com.example.alphatour.qrcode.GenerateQrCodeActivity;
import com.example.alphatour.wizardcreazione.CreateObjectWizard;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityElementDetails extends AppCompatActivity {

    private ImageView close,imagePhoto,imageQrCode;
    private EditText title,description,sensor;
    private FloatingActionButton photo,qrCode;
    private AutoCompleteTextView typology;
    private String zone,item;
    private static Bitmap qr;
    private static Uri ph,uri;
    LinearLayout layout;
    private Element element;
    private ArrayAdapter<String> adapterItems;
    List<Element> elementList=new ArrayList<Element>();
    private List<String> typology_list = new ArrayList<String>();
    private List<View> list = new ArrayList<View>();
    private boolean errorFlag=false,selected=false,save=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_details);
        close=findViewById(R.id.closeDetails);
        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        sensor=findViewById(R.id.sensor);
        photo=findViewById(R.id.changePhotoObject);
        qrCode=findViewById(R.id.changeQrCode);
        imagePhoto=findViewById(R.id.photo);
        imageQrCode=findViewById(R.id.qrCode);
        typology = findViewById(R.id.inputTypeZone);

        Intent intent=getIntent();
        zone=intent.getStringExtra("zone");
        typology_list=intent.getStringArrayListExtra("ZoneList");


        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,typology_list);
        typology.setAdapter(adapterItems);
        typology.setHint(zone);
        typology.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                typology.setError(null);
                selected=true;
                item = parent.getItemAtPosition(position).toString();
            }
        });

        elementList= CreateObjectWizard.getElementList();
        list= CreateObjectWizard.getTypology_list();
        layout=CreateObjectWizard.getLayout_list();


        for(int i=0;i<layout.getChildCount()&&i<list.size() ;i++){

            if(layout.getChildAt(i).equals(list.get(i))){
                element=elementList.get(i);
                title.setText(element.getTitle());
                description.setText(element.getDescription());
                imagePhoto.setImageURI(element.getPhoto());
                imageQrCode.setImageBitmap(element.getQrCode());
                sensor.setText(element.getSensorCode());
            }
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!save) {
                    list= CreateObjectWizard.getTypology_list();
                    list.remove(0);
                    finish();
                }
            }
        });
    }

    public static Bitmap getQr() {
        return qr;
    }

    public void setQr(Bitmap qr) {
        this.qr = qr;
    }

    public static Uri getPh() {
        return ph;
    }

    public  void setPh(Uri ph) {
        this.ph = ph;
    }

    public void modifyObject(View view) {

        save=true;
        String Title = title.getText().toString();
        String Description= description.getText().toString();
        // String Activity = activity.getText().toString();
        String SensorCode = sensor.getText().toString();
        Element elementModified= new Element();
        if(Title.isEmpty()){
            title.setError(getString(R.string.required_field));
            title.requestFocus();
            errorFlag = true;
        }else{
            elementModified.setTitle(Title);
        }

        if(Description.isEmpty()){
            description.setError(getString(R.string.required_field));
            description.requestFocus();
            errorFlag = true;
        }else{
            elementModified.setDescription(Description);
        }


            /*if(Activity.isEmpty()){
                activity.setError(getString(R.string.campo_obbligatorio));
                activity.requestFocus();
                errorFlag = true;
            }else{
                element.setActivity(Activity);
            }*/

        if(SensorCode.isEmpty()){
            sensor.setError(getString(R.string.required_field));
            sensor.requestFocus();
            errorFlag = true;
        }else{
            elementModified.setSensorCode(SensorCode);
        }

        if(errorFlag){
            return;
        }else{
            Intent intent=new Intent();
            intent.putExtra("title",elementModified.getTitle());
            intent.putExtra("description",elementModified.getDescription());
            intent.putExtra("sensor",elementModified.getSensorCode());
            if(selected) {
                intent.putExtra("zone", item);
            }else{
                intent.putExtra("zone", zone);
            }
            setResult(Activity.RESULT_OK,intent);
            View v=list.get(0);
            LinearLayout layout=CreateObjectWizard.getLayout_list();


            for(int i=0;i<elementList.size();i++) {

                if (elementList.get(i).equals(element)) {
                    elementList.remove(i);
                    CreateObjectWizard.setElementList(elementList);
                }
            }
            
            layout.removeView(v);
            list.remove(0);
            finish();
        }
    }

    public void changeQrCode(View view) {

        Intent intent=new Intent(this, GenerateQrCodeActivity.class);
        startActivityForResult(intent,70);
        imageQrCode.setImageBitmap(GenerateQrCodeActivity.getBitmap());
        setQr(GenerateQrCodeActivity.getBitmap());
    }

    public void changePhotoObject(View view) {

        ImagePicker.with(ActivityElementDetails.this)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                .start(20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 20) {
            uri = data.getData();
            if (uri != null) {
                Toast.makeText(ActivityElementDetails.this, R.string.upload_photo, Toast.LENGTH_LONG).show();
                imagePhoto.setImageURI(uri);
                setPh(uri);
            } else {
                Toast.makeText(ActivityElementDetails.this, "Non hai aggiunto la foto!", Toast.LENGTH_LONG).show();
            }
            //salvataggio foto
        }else{
            imageQrCode.setImageBitmap(GenerateQrCodeActivity.getBitmap());
            setQr(GenerateQrCodeActivity.getBitmap());
            Toast.makeText(ActivityElementDetails.this,"QrCode generato con successo !", Toast.LENGTH_LONG).show();
        }
    }
}