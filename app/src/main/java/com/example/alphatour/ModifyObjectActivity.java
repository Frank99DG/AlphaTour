package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.User;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.qrcode.GenerateQrCodeActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModifyObjectActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText title,description;
    private static Bitmap newQr;
    private static Uri newUri;
    private AutoCompleteTextView typology;
    private ImageView imagePhoto,imageQrCode;
    private FloatingActionButton photo,qrCode;
    private StorageReference storegeProfilePick;
    private StorageTask uploadTask;
    private ProgressBar loadingBar;
    private Map<String,String> zoneMap = new HashMap<String,String>();
    private List<String> zoneList = new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private int i=0,j=0;
    private String newTitle,newDescription,newSensor,idElement,item,Place,idPlace,Zone,idZone,Element,Qrdata;
    private long idPhotoAndQrCode;
    private String myQrData;
    private String dashboardFlag = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_object);

        db = FirebaseFirestore.getInstance();
        storegeProfilePick= FirebaseStorage.getInstance().getReference();

        Intent intent= getIntent();
        myQrData = intent.getStringExtra("data");
        Place = intent.getStringExtra("Place");
        idPlace = intent.getStringExtra("idPlace");
        Zone = intent.getStringExtra("Zone");
        idZone = intent.getStringExtra("idZone");
        Element = intent.getStringExtra("Element");
        dashboardFlag = intent.getStringExtra("dashboardFlag");

        title=findViewById(R.id.titleQr);
        description=findViewById(R.id.descriptionQr);
        photo=findViewById(R.id.changePhotoObjectQr);
        qrCode=findViewById(R.id.changeQr);
        imagePhoto=findViewById(R.id.photoQr);
        imageQrCode=findViewById(R.id.qr);
        typology = findViewById(R.id.inputTypeZoneQr);
        loadingBar=findViewById(R.id.modifyLoadingBar);

        loadingBar.setVisibility(View.VISIBLE);

        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,zoneList);

        db.collection("Elements")
                .whereEqualTo("qrData", myQrData)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult()!=null){

                              for (QueryDocumentSnapshot document : task.getResult()) {

                                ElementString element = document.toObject(ElementString.class);
                                idElement = document.getId();


                                db.collection("Zones")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    i++;
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        Zone zone = document.toObject(Zone.class);

                                                        if ( i == 1 && zone.getIdPlace().equals(idPlace) ) {
                                                            zoneList.add(zone.getName());
                                                            zoneMap.put(document.getId(), zone.getName());

                                                        }

                                                        if (document.getId().matches(element.getIdZone())) {
                                                            zone = document.toObject(Zone.class);

                                                            title.setText(element.getTitle());
                                                            description.setText(element.getDescription());
                                                            idPhotoAndQrCode = element.getIdPhotoAndQrCode();
                                                            Qrdata=element.getQrData();
                                                            showPhoto();
                                                            showQrCode();
                                                            typology.setAdapter(adapterItems);
                                                            Zone = zone.getName();
                                                            typology.setHint(Zone);
                                                            typology.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    typology.setError(null);
                                                                    item = parent.getItemAtPosition(position).toString();
                                                                }
                                                            });
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(ModifyObjectActivity.this, "Non è stato possibile caricare i dati dell'oggetto !!!", Toast.LENGTH_LONG).show();
                                                    loadingBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }
                        }else{
                                Toast.makeText(ModifyObjectActivity.this, "Oggetto non trovato !!!", Toast.LENGTH_LONG).show();
                                loadingBar.setVisibility(View.GONE);
                                Intent intent=new Intent(ModifyObjectActivity.this,DashboardActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            Toast.makeText(ModifyObjectActivity.this, "Non è stato possibile caricare i dati dell'oggetto", Toast.LENGTH_LONG).show();
                            loadingBar.setVisibility(View.GONE);
                            Intent intent=new Intent(ModifyObjectActivity.this,DashboardActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ModifyObjectActivity.this, "Oggetto non trovato !!!", Toast.LENGTH_LONG).show();
                loadingBar.setVisibility(View.GONE);
            }
        });

    }



    private void showQrCode() {
        final StorageReference fileRef = storegeProfilePick.child("QrCodeObjects").child("QrCode_Objects_"+idPhotoAndQrCode);

        try{
            File localFile= File.createTempFile("tempfile",".png");
            fileRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageQrCode.setImageBitmap(bitmap);
                            loadingBar.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ModifyObjectActivity.this, "Non è stato possibile caricare i dati dell'oggetto", Toast.LENGTH_LONG).show();
                    loadingBar.setVisibility(View.GONE);
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void showPhoto() {

        final StorageReference fileRef = storegeProfilePick.child("PhotoObjects").child("Photo_Objects_"+idPhotoAndQrCode);

        try{
            File localFile= File.createTempFile("tempfile",".png");
            fileRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imagePhoto.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ModifyObjectActivity.this, "Non è stato possibile caricare i dati dell'oggetto", Toast.LENGTH_LONG).show();
                    loadingBar.setVisibility(View.GONE);
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void modifyPhotoObject(View view) {
        ImagePicker.with(ModifyObjectActivity.this)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                .start(20);
    }

    public void modifyQrCode(View view) {
        Intent intent=new Intent(this, GenerateQrCodeActivity.class);
        startActivityForResult(intent,70);
        //imageQrCode.setImageBitmap(GenerateQrCodeActivity.getBitmap());
        //setQr(GenerateQrCodeActivity.getBitmap());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 20) {
            newUri = data.getData();
            if (newUri != null) {
                Toast.makeText(ModifyObjectActivity.this, R.string.upload_photo, Toast.LENGTH_LONG).show();
                imagePhoto.setImageURI(newUri);
            } else {
                Toast.makeText(ModifyObjectActivity.this, "Non hai aggiunto la foto!", Toast.LENGTH_LONG).show();
            }
            //salvataggio foto
        }else{

            if(GenerateQrCodeActivity.getQrFlag()==true) {
                imageQrCode.setImageBitmap(GenerateQrCodeActivity.getBitmap());
                newQr = GenerateQrCodeActivity.getBitmap();
                Qrdata=GenerateQrCodeActivity.getData();
                Toast.makeText(ModifyObjectActivity.this, "QrCode generato con successo !", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ModifyObjectActivity.this, "QrCode non generato !", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void modifyObj(View view) {

        newTitle = title.getText().toString();
        newDescription = description.getText().toString();

        Boolean error = inputControl(newTitle,newDescription,newSensor);

        if(error){
            return;
        }else{
            if(item!=null) {
                saveObject(newTitle, newDescription, newUri, newQr, item);
            }else{
                saveObject(newTitle, newDescription, newUri, newQr, Zone);
            }
        }
    }


    private void saveObject(String newTitle, String newDescription, Uri newUri, Bitmap newQr, String newZone) {
        loadingBar.setVisibility(View.VISIBLE);

        Iterator it = zoneMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry val = (Map.Entry) it.next();

            if(newZone.matches(val.getValue().toString())){
                newZone = val.getKey().toString();
            }
        }

        if (newUri != null) {
            savePhoto(newUri);
        }

        if (newUri != null) {
            saveQrCode(newQr);
        }

        db.collection("Elements").document(idElement).
                update("title",newTitle,"description",newDescription,"idZone",newZone,"qrData",Qrdata).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ModifyObjectActivity.this, "Oggetto aggiornato correttamente", Toast.LENGTH_LONG).show();
                        if(dashboardFlag.equals("1")){
                            startActivity(new Intent(ModifyObjectActivity.this, DashboardActivity.class));
                            loadingBar.setVisibility(View.GONE);
                            finish();
                        }else if(dashboardFlag.equals("scan")){
                            loadingBar.setVisibility(View.GONE);
                            finish();
                        }else  {
                            Intent intent = new Intent(ModifyObjectActivity.this, ListElementsActivity.class);
                            intent.putExtra("Place", Place);
                            intent.putExtra("idPlace", idPlace);
                            intent.putExtra("Zone", Zone);
                            intent.putExtra("idZone", idZone);
                            intent.putExtra("dashboardFlag", dashboardFlag);
                            startActivity(intent);
                            loadingBar.setVisibility(View.GONE);
                            finishAffinity();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ModifyObjectActivity.this, "Non è stato possibile aggiornare i dati dell'oggetto", Toast.LENGTH_LONG).show();
                loadingBar.setVisibility(View.GONE);
            }
        });


    }

    private void savePhoto(Uri photo) {


        final StorageReference fileRef = storegeProfilePick.child("PhotoObjects").child("Photo_Objects"+"_"+idPhotoAndQrCode);

        uploadTask = fileRef.putFile(photo);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    String uriUploadPhoto=downloadUrl.toString();

                    db.collection("Elements").document(idElement).
                            update("photo",uriUploadPhoto).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ModifyObjectActivity.this, "Oggetto aggiornato correttamente", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(ModifyObjectActivity.this, DashboardActivity.class));
                                    loadingBar.setVisibility(View.GONE);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ModifyObjectActivity.this, "Non è stato possibile aggiornare i dati dell'oggetto", Toast.LENGTH_LONG).show();
                            loadingBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


    }

    private void saveQrCode(Bitmap qrCode) {

        final StorageReference fileRef = storegeProfilePick.child("QrCodeObjects").child("QrCode_Objects"+"_"+idPhotoAndQrCode);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrCode.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            //scarico il link di Storage dell'immagine
                            Uri downloadUrl = (Uri) task.getResult();
                            String uriUploadQrCode=downloadUrl.toString();

                            db.collection("Elements").document(idElement).
                                    update("qrCode",uriUploadQrCode).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ModifyObjectActivity.this, "Oggetto aggiornato correttamente", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(ModifyObjectActivity.this, DashboardActivity.class));
                                            loadingBar.setVisibility(View.GONE);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ModifyObjectActivity.this, "Non è stato possibile aggiornare i dati dell'oggetto", Toast.LENGTH_LONG).show();
                                    loadingBar.setVisibility(View.GONE);
                                }
                            });

                        }

                    }
                });
            }
        });
    }

    private boolean inputControl(String Title, String Description, String Sensor) {
        Boolean errorFlag = false;

        if (Title.isEmpty()) {
            title.setError(getString(R.string.required_field));
            title.requestFocus();
            errorFlag = true;
        }

        if (Description.isEmpty()) {
            description.setError(getString(R.string.required_field));
            description.requestFocus();
            errorFlag = true;
        }

        return errorFlag;
    }

    public void onBackButtonClick(View view){

        if(dashboardFlag.equals("1")){
            startActivity(new Intent(ModifyObjectActivity.this, DashboardActivity.class));
            finish();
        }else if(dashboardFlag.equals("scan")){
            finish();
        }else {
            Intent intent = new Intent(ModifyObjectActivity.this, ListElementsActivity.class);
            intent.putExtra("Place",Place);
            intent.putExtra("idPlace",idPlace);
            intent.putExtra("Zone",Zone);
            intent.putExtra("idZone",idZone);
            intent.putExtra("dashboardFlag", dashboardFlag);
            startActivity(intent);
            finish();
        }

    }

}