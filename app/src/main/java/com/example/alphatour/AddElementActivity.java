package com.example.alphatour;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.qrcode.GenerateQrCodeActivity;
import com.example.alphatour.qrcode.ScanQrCodeActivity;
import com.example.alphatour.wizardcreazione.CreateZoneWizard;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    private boolean errorFlag=false;
    private boolean flagPhoto=false;
    private boolean flagQrCode=false;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private List<DocumentSnapshot> listaDocumenti= new ArrayList<DocumentSnapshot>();


    public static Element getElement() {
        return element;
    }

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
        setContentView(R.layout.activity_add_element);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

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

        db.collection("Elements")
                .whereEqualTo("idUser",user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            listaDocumenti = queryDocumentSnapshots.getDocuments();
                        }
                    }
                });
    }


    //per rimuovere il focus e la tastiera quando si clicca fuori dalla EditText e/o AutoCompleteTextView
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
         }
        return super.dispatchTouchEvent( event );
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

            if(!duplicateControl(Title)) {
                Intent intent = new Intent();
                intent.putExtra("title", element.getTitle());
                intent.putExtra("description", element.getDescription());
                intent.putExtra("data", GenerateQrCodeActivity.getData());
                Bundle bundle;
                intent.putExtra("zone", item);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(AddElementActivity.this, "L'oggetto che vuoi creare esiste già", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean duplicateControl(String name) {
        boolean flag=false;

        if(listaDocumenti.size()>0){
            for(DocumentSnapshot d:listaDocumenti){
                ElementString elm=d.toObject(ElementString.class);
                if(elm.getTitle().matches(name)){
                    flag=true;
                }
            }
        }
        return flag;
    }

}