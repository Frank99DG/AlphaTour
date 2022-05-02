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
    private EditText title,description, sensor;
    private static Bitmap newQr;
    private static Uri newUri;
    private AutoCompleteTextView typology;
    private ImageView close,imagePhoto,imageQrCode;
    private FloatingActionButton photo,qrCode;
    private ElementString element;
    private Zone zone;
    private StorageReference storegeProfilePick;
    private StorageTask uploadTask;
    private ProgressBar loadingBar;
    private Map<String,String> zoneMap = new HashMap<String,String>();
    private List<String> zoneList = new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private int i=0,j=0;
    private String newTitle,newDescription,newSensor,idElement,item,Zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_object);

        db = FirebaseFirestore.getInstance();
        storegeProfilePick= FirebaseStorage.getInstance().getReference();

        Intent intent= getIntent();
        String data=intent.getStringExtra("data");

        close=findViewById(R.id.closeDetailsQr);
        title=findViewById(R.id.titleQr);
        description=findViewById(R.id.descriptionQr);
        sensor=findViewById(R.id.sensorQr);
        photo=findViewById(R.id.changePhotoObjectQr);
        qrCode=findViewById(R.id.changeQr);
        imagePhoto=findViewById(R.id.photoQr);
        imageQrCode=findViewById(R.id.qr);
        typology = findViewById(R.id.inputTypeZoneQr);
        loadingBar=findViewById(R.id.modifyLoadingBar);

        loadingBar.setVisibility(View.VISIBLE);

        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,zoneList);

        db.collection("Elements")
                .whereEqualTo("title", data)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

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

                                                        zone = document.toObject(Zone.class);
                                                        if(i==1) {
                                                            zoneList.add(zone.getName());
                                                            zoneMap.put(document.getId(),zone.getName());

                                                        }
                                                        if(document.getId().matches(element.getIdZone())) {
                                                            zone = document.toObject(Zone.class);

                                                            title.setText(element.getTitle());
                                                            description.setText(element.getDescription());
                                                            sensor.setText(element.getSensorCode());
                                                            showPhoto();
                                                            showQrCode();
                                                            typology.setAdapter(adapterItems);
                                                            Zone=zone.getName();
                                                            typology.setHint(Zone);
                                                            typology.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                                                                    typology.setError(null);
                                                                    //selected=true;
                                                                    item = parent.getItemAtPosition(position).toString();
                                                                }
                                                            });
                                                        }
                                                    }
                                                }else {
                                                    Toast.makeText(ModifyObjectActivity.this, "Non è stato possibile caricare i dati dell'oggetto !!!", Toast.LENGTH_LONG).show();
                                                    loadingBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(ModifyObjectActivity.this, "Non è stato possibile caricare i dati dell'oggetto", Toast.LENGTH_LONG).show();
                            loadingBar.setVisibility(View.GONE);
                        }
                    }
                });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }



    private void showQrCode() {
        final StorageReference fileRef = storegeProfilePick.child("QrCodeObjects").child("QrCode_Objects_"+title.getText().toString());

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
        String tit=title.getText().toString();
        final StorageReference fileRef = storegeProfilePick.child("PhotoObjects").child("Photo_Objects_"+tit);

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
            imageQrCode.setImageBitmap(GenerateQrCodeActivity.getBitmap());
            newQr=GenerateQrCodeActivity.getBitmap();
            Toast.makeText(ModifyObjectActivity.this,"QrCode generato con successo !", Toast.LENGTH_LONG).show();
        }
    }

    public void modifyObj(View view) {

        newTitle=title.getText().toString();
        newDescription=description.getText().toString();
        newSensor=sensor.getText().toString();

        Boolean error=inputControl(newTitle,newDescription,newSensor);

        if(error){
            return;
        }else{
            if(item!=null) {
                saveObject(newTitle, newDescription, newUri, newQr, newSensor, item);
            }else{
                saveObject(newTitle, newDescription, newUri, newQr, newSensor, Zone);
            }
        }
    }


    private void saveObject(String newTitle, String newDescription, Uri newUri, Bitmap newQr, String newSensor, String newZone) {
        loadingBar.setVisibility(View.VISIBLE);

        Iterator it=zoneMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry val=(Map.Entry) it.next();

            if(newZone.matches(val.getValue().toString())){
                newZone=val.getKey().toString();
            }
        }

        if (newUri != null) {
            savePhoto(newUri);
        }

        if (newUri != null) {
            saveQrCode(newQr);
        }

        db.collection("Elements").document(idElement).
                update("title",newTitle,"description",newDescription,"sensorCode",newSensor,"idZone",newZone).
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

    private void savePhoto(Uri photo) {


        final StorageReference fileRef = storegeProfilePick.child("PhotoObjects").child("Photo_Objects"+"_"+newTitle);

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

        final StorageReference fileRef = storegeProfilePick.child("QrCodeObjects").child("QrCode_Objects"+"_"+newTitle);
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


        if (Sensor.isEmpty()) {
            sensor.setError(getString(R.string.required_field));
            sensor.requestFocus();
            errorFlag = true;
        }

        return errorFlag;
    }
}