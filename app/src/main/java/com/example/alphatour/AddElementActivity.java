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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alphatour.oggetti.Element;
import com.example.alphatour.qrcode.GenerateQrCodeActivity;
import com.example.alphatour.qrcode.ScanQrCodeActivity;
import com.example.alphatour.wizardcreazione.CreateZoneWizard;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.List;

public class AddElementActivity extends AppCompatActivity{

    private EditText nameElement,description;
    private Button save, generateQrCode,photo;
    private List<String> typology_list = new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private AutoCompleteTextView typology;
    private static Uri uri,ph;
    private static Bitmap qr;
    private String item;
    private static  Element element;

    public static Element getElement() {
        return element;
    }

    private boolean errorFlag=false;
    private boolean flagPhoto=false;
    private boolean flagQrCode=false;

    public static Uri getPhoto() {
        return ph;
    }

    public static Bitmap getQr() {
        return qr;
    }

    public void setQr(Bitmap qr) {
        this.qr = qr;
    }

    public void setPhoto(Uri ph) {
        this.ph = ph;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_element);

        save = findViewById(R.id.saveObject);
        nameElement = findViewById(R.id.inputElement);
        typology = findViewById(R.id.inputTypologyZone);
        generateQrCode = findViewById(R.id.inputQrCode);
        description = findViewById(R.id.inputDescription);
        photo= findViewById(R.id.inputPhoto);

        ArrayList<String> zone_list=CreateZoneWizard.getZone_list();

        for(int i=0;i<zone_list.size();i++){
            typology_list.add(zone_list.get(i));
        }

        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,typology_list);
        typology.setAdapter(adapterItems);
        typology.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                typology.setError(null);
                item = parent.getItemAtPosition(position).toString();

            }
        });
    }

    public void uploadImageElement(View v){
        ImagePicker.with(AddElementActivity.this)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                .start(20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 20) {
            uri = data.getData();
            if (uri != null) {
                flagPhoto = true;
                photo.setError(null);
                Toast.makeText(AddElementActivity.this, R.string.upload_photo, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddElementActivity.this, "Non hai aggiunto la foto!", Toast.LENGTH_LONG).show();
            }
            //salvataggio foto
        }else{

            setQr(GenerateQrCodeActivity.getBitmap());
            Toast.makeText(AddElementActivity.this,"QrCode generato con successo !", Toast.LENGTH_LONG).show();
        }
    }

    public void generateQrCodeActivity(View v){

        Intent intent=new Intent(this, GenerateQrCodeActivity.class);
        startActivityForResult(intent,70);
    }

    public void scanQrCode(View v){

        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }

    public void saveObject(View v){

        element = new Element();
        String Title = nameElement.getText().toString();
        String Description= description.getText().toString();

        if(Title.isEmpty()){
            nameElement.setError(getString(R.string.required_field));
            nameElement.requestFocus();
            errorFlag = true;
        }else{
            element.setTitle(Title);
            errorFlag = false;
        }

        if(Description.isEmpty()){
            description.setError(getString(R.string.required_field));
            description.requestFocus();
            errorFlag = true;
        }else{
            element.setDescription(Description);
            errorFlag = false;
        }

        if(flagPhoto==false){

            photo.setError(getString(R.string.required_field));
            photo.requestFocus();
            errorFlag=true;
        }else{
            element.setPhoto(uri);
            setPhoto(uri);
            errorFlag = false;
        }

        if(GenerateQrCodeActivity.getQrFlag()==false){
            generateQrCode.setError(getString(R.string.required_field));
            generateQrCode.requestFocus();
            errorFlag = true;
        }else{
            element.setQrCode(GenerateQrCodeActivity.getBitmap());
            setQr(GenerateQrCodeActivity.getBitmap());
            errorFlag = false;
        }

        if(errorFlag){
            return;
        }else{
            Intent intent=new Intent();
            intent.putExtra("title",element.getTitle());
            intent.putExtra("description",element.getDescription());
            intent.putExtra("data",GenerateQrCodeActivity.getData());
            Bundle bundle;
            intent.putExtra("zone",item);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }


}