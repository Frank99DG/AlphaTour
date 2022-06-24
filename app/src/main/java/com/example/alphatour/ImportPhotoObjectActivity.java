package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.oggetti.Constraint;
import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.qrcode.GenerateQrCodeClass;
import com.example.alphatour.wizardcreazione.CreateObjectWizard;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ImportPhotoObjectActivity extends AppCompatActivity {

    private CardView addPhoto,downloadEmpty,loadFull;
    private FirebaseFirestore db;
    private StorageReference storegeProfilePick;
    private StorageTask uploadTask;
    private Uri imageUri;
    private long idPhotoAndQrCode,id;
    private int count=0,i,j,idP=1,idZ=1;
    private final int FILEWRITE =500, FILEREAD =501;
    private LinearLayout layout_list;
    private List<String> linkList= new ArrayList<String>();
    private String uriUploadPhoto,idPlace;
    private ProgressBar progressBar;
    private List<ReadCsv> listLineCsv=new ArrayList<ReadCsv>();
    private List<Zone> listZone=new ArrayList<Zone>();
    private List<Element> listElement=new ArrayList<Element>();
    private List<Place> listPlace=new ArrayList<Place>();
    private boolean place=false,flag1=false,flag2=false,loadConstraints=false, perm =false;
    private boolean success=false, load=true;
    private List<String> uriUploadQrCode=new ArrayList<String>();
    private FirebaseUser user;
    private FirebaseAuth auth;
    private List<Constraint> listConstranints = new ArrayList<Constraint>();
    private Map<String, Object> elm = new HashMap<>();
    private Dialog dialog;
    private TextView yesFinal,titleDialog,textDialog;
    private String[] Permission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ProgressBar loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_photo_object);

        db = FirebaseFirestore.getInstance();
        storegeProfilePick= FirebaseStorage.getInstance().getReference();
        addPhoto=findViewById(R.id.cardPhoto);
        downloadEmpty=findViewById(R.id.cardDocEmpty);
        loadFull=findViewById(R.id.cardDocFull);
        progressBar=findViewById(R.id.phLoadingBar);
        layout_list=findViewById(R.id.listphotoLayout);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        yesFinal = dialog.findViewById(R.id.btn_termina_permission);
        titleDialog = dialog.findViewById(R.id.titleDialog_permission);
        textDialog = dialog.findViewById(R.id.textDialog_permission);



        DocumentReference docRef = db.collection("LastIdPhotoAndQrCode").document("1");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = new HashMap<>();
                        data=document.getData();
                        Iterator it=data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry val = (Map.Entry) it.next();
                            Object g=val.getValue();
                            idPhotoAndQrCode = (long) g;
                        }
                    } else {
                        idPhotoAndQrCode=0;
                    }
                } else {
                    // Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermission();
                if(perm) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "Seleziona immagini"), 300);
                }
            }
        });

        loadFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermission();
                if(perm) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("text/comma-separated-values");
                    startActivityForResult(intent, 100);
                }
            }
        });

        downloadEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermission();
                if (perm) {

                    String state = Environment.getExternalStorageState();
                    //external storage availability check
                    if (!Environment.MEDIA_MOUNTED.equals(state)) {
                        return;
                    }

                    File fil = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "CsvEmpty.csv");
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(fil);
                        String line = "namePlace,cityPlace,typologyPlace,nameZone,titleObject,descriptionObject,qrData,linkImageObject,fromZone,inZone";
                        fos.write(line.getBytes(StandardCharsets.UTF_8), 0, line.length());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(ImportPhotoObjectActivity.this, "Saved to " + getFilesDir() + "/" + "CsvEmpty", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private void verifyPermission(){
        if (checkPermission()){
            if (ActivityCompat.shouldShowRequestPermissionRationale(ImportPhotoObjectActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                dialog.show();
                yesFinal.setText("OK");

                titleDialog.setText(R.string.permit_required);
                textDialog.setText(R.string.permission_text);
                textDialog.setTextColor(getResources().getColor(R.color.black));

                yesFinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        requestPermission();
                    }
                });
            } else {
                requestPermission();
            }
        }else{
            perm=true;
        }
    }

    private boolean checkPermission(){

        for(String permission:Permission){
            if(ContextCompat.checkSelfPermission(getApplicationContext(), permission)== PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermission(){
        int permissionCode=200; //codice definito da me, servirà nel caso in cui serva controllare più permessi
        ActivityCompat.requestPermissions(this,Permission,permissionCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==300) {
                progressBar.setVisibility(View.VISIBLE);
                if (data.getClipData() != null) {

                    saveMultiplePhoto(data);

                } else {
                    Toast.makeText(ImportPhotoObjectActivity.this, "seleziona le immagini", Toast.LENGTH_LONG).show();
                    saveSinglePhoto(data);
                }
            }else{
                if(requestCode==100) {
                    readCsv(data);
                }
            }
        }
    }


    private void saveSinglePhoto(Intent data) {

            imageUri=data.getData();
            long id=generateidPhotoAndQrCode();
            final StorageReference fileRef = storegeProfilePick.child("PhotoObjects").child("Photo_Objects"+"_"+id);

            uploadTask = fileRef.putFile(imageUri);

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
                        uriUploadPhoto=downloadUrl.toString();
                        createNewView(data);

                    } else {
                        Toast.makeText(ImportPhotoObjectActivity.this, R.string.link_not_generated, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });


    }

    private void saveMultiplePhoto(Intent data) {

        int contData=data.getClipData().getItemCount();

        for(int i=0;i<contData;i++){

            imageUri=data.getClipData().getItemAt(i).getUri();
            long id=generateidPhotoAndQrCode();
            final StorageReference fileRef = storegeProfilePick.child("PhotoObjects").child("Photo_Objects"+"_"+id);

            uploadTask = fileRef.putFile(imageUri);

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
                        count++;
                        Uri downloadUrl = task.getResult();
                        String uriUploadPhoto=downloadUrl.toString();
                        linkList.add(uriUploadPhoto);

                        if(count==contData){
                            createNewView(data);
                        }

                    } else {
                        Toast.makeText(ImportPhotoObjectActivity.this, R.string.link_not_generated, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    private void createNewView(Intent data) {

        if(data.getClipData()==null){
            imageUri=data.getData();
            final View photoView = getLayoutInflater().inflate(R.layout.row_add_photo, null, false);
            ImageView photo = (ImageView) photoView.findViewById(R.id.photoObj);
            ImageView deletePhoto = (ImageView) photoView.findViewById(R.id.removePhoto);
            ImageView copyLink = (ImageView) photoView.findViewById(R.id.copyLink);
            TextView linkImage = (TextView) photoView.findViewById(R.id.linkImage);
            photo.setImageURI(imageUri);
            linkImage.setText(uriUploadPhoto);
            layout_list.addView(photoView);
            progressBar.setVisibility(View.GONE);

            deletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layout_list.removeView(photoView);
                }
            });

            copyLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String copied = linkImage.getText().toString();
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("link", copied);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(ImportPhotoObjectActivity.this, R.string.link_copied, Toast.LENGTH_LONG).show();
                }
            });

        }else {
            int contData = data.getClipData().getItemCount();

            for (int i = 0; i < contData; i++) {

                imageUri = data.getClipData().getItemAt(i).getUri();
                final View photoView = getLayoutInflater().inflate(R.layout.row_add_photo, null, false);
                ImageView photo = (ImageView) photoView.findViewById(R.id.photoObj);
                ImageView deletePhoto = (ImageView) photoView.findViewById(R.id.removePhoto);
                ImageView copyLink = (ImageView) photoView.findViewById(R.id.copyLink);
                TextView linkImage = (TextView) photoView.findViewById(R.id.linkImage);
                photo.setImageURI(imageUri);
                linkImage.setText(linkList.get(i));
                layout_list.addView(photoView);
                progressBar.setVisibility(View.GONE);

                deletePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        layout_list.removeView(photoView);
                    }
                });

                copyLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String copied = linkImage.getText().toString();
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("link", copied);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(ImportPhotoObjectActivity.this, R.string.link_copied, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }

    private long generateidPhotoAndQrCode() {

        idPhotoAndQrCode++;
        Map<String, Object> data = new HashMap<>();
        data.put("lastId", idPhotoAndQrCode);

        db.collection("LastIdPhotoAndQrCode").document("1")
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                });
        return idPhotoAndQrCode;
    }


    private void readCsv(Intent data) {

        progressBar.setVisibility(View.VISIBLE);

        if(data==null){
            return;
        }else{
            Uri uri = data.getData();
            Toast.makeText(ImportPhotoObjectActivity.this,uri.getPath(),Toast.LENGTH_SHORT).show();
            String path=uri.getPath();
            path=path.substring(path.indexOf(":")+1);
            File file =new File(path);
            try {
                BufferedReader read=new BufferedReader(new FileReader(file));
                String line="";
                Boolean isEmpty=true;
                while((line=read.readLine())!=null){
                    isEmpty=false;
                    String[] token=line.split(",");
                    ReadCsv readCsv= new ReadCsv();
                    boolean flag=false;
                    for(int i=0;i< token.length;i++){

                        if(token[i]==null){
                            flag=true;
                        }
                    }
                    if(!flag) {
                        if(!token[0].matches("namePlace")) {

                            Element elm=new Element();
                            Place place=new Place();
                            Zone zone = new Zone();
                            Constraint constraint=new Constraint();


                            if(listLineCsv.size()>0) {
                                setDataToken(token, readCsv);
                                if(!token[0].isEmpty()){
                                for (int i = 0; i < listPlace.size(); i++) {

                                    if (!listPlace.get(i).getName().matches(token[0]) &&
                                            !listPlace.get(i).getCity().matches(token[1]) &&
                                            !listPlace.get(i).getTypology().matches(token[2])) {

                                        idP++;
                                        place.setIdPlace(idP);
                                        place.setName(token[0]);
                                        place.setCity(token[1]);
                                        place.setTypology(token[2]);

                                        listPlace.add(place);
                                    }
                                }

                                    for (int j = 0; j < listPlace.size(); j++) {

                                        if (!listZone.get(i).getName().matches(token[3])) {
                                            zone.setIdPl(idP);
                                            zone.setName(token[3]);
                                            idZ++;
                                            listZone.add(zone);
                                        }
                                    }

                                    elm.setIdZon(idZ);
                                    elm.setTitle(readCsv.getTitleObject());
                                    elm.setDescription(readCsv.getDescriptionObject());
                                    elm.setQrData(readCsv.getQrDataObject());
                                    elm.setPhoto(Uri.parse(readCsv.getLinkImageObject()));
                                    String[] str = readCsv.getLinkImageObject().split("Objects_");
                                    String[] st = str[1].split("\\?");
                                    String idPh = st[0];
                                    elm.setIdPhotoAndQrCodeString(idPh);
                                    elm.setZoneRif(token[3]);
                                    if (!token[8].isEmpty() && !!token[9].isEmpty()) {
                                        constraint.setFromZone(token[8]);
                                        constraint.setInZone(token[9]);
                                    }

                                    listElement.add(elm);

                                    if (!token[8].isEmpty() && !token[9].isEmpty()) {
                                        constraint.setFromZone(token[8]);
                                        constraint.setInZone(token[9]);
                                        listConstranints.add(constraint);
                                    }

                            }
                            }else{

                                setDataToken(token,readCsv);

                                if(!token[0].isEmpty()) {
                                    elm.setTitle(readCsv.getTitleObject());
                                    elm.setDescription(readCsv.getDescriptionObject());
                                    elm.setQrData(readCsv.getQrDataObject());
                                    elm.setPhoto(Uri.parse(readCsv.getLinkImageObject()));
                                    String[] str = readCsv.getLinkImageObject().split("Objects_");
                                    String[] st = str[1].split("\\?");
                                    String idPh = st[0];
                                    elm.setIdPhotoAndQrCodeString(idPh);
                                    elm.setZoneRif(token[3]);
                                    place.setName(token[0]);
                                    place.setCity(token[1]);
                                    place.setTypology(token[2]);
                                    place.setIdPlace(idP);
                                    zone.setIdPl(idP);
                                    zone.setName(token[3]);
                                    elm.setIdZon(idZ);

                                    listPlace.add(place);
                                    listZone.add(zone);
                                    listElement.add(elm);
                                }

                                if(!token[8].isEmpty()&&!token[9].isEmpty()) {
                                    constraint.setFromZone(token[8]);
                                    constraint.setInZone(token[9]);
                                    listConstranints.add(constraint);
                                }
                            }

                        }
                    }else{

                        Toast.makeText(ImportPhotoObjectActivity.this,R.string.fields_may_be_empty,Toast.LENGTH_LONG).show();
                        line=null;
                        progressBar.setVisibility(View.GONE);
                    }
                }

                if(isEmpty){
                    Toast.makeText(ImportPhotoObjectActivity.this,R.string.fields_may_be_empty,Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else{

                    savePlace();
                    saveConstraints();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if(success) {
                                listConstranints.clear();
                                listPlace.clear();
                                listLineCsv.clear();
                                listZone.clear();
                                Intent intent = new Intent(ImportPhotoObjectActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(ImportPhotoObjectActivity.this, R.string.place_save_failed , Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }, 2000L);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDataToken(String[] token, ReadCsv readCsv) {

        readCsv.setNamePlace(token[0]);
        readCsv.setCityPlace(token[1]);
        readCsv.setTypologyPlace(token[2]);
        readCsv.setNameZone(token[3]);
        readCsv.setTitleObject(token[4]);
        readCsv.setDescriptionObject(token[5]);
        readCsv.setQrDataObject(token[6]);
        readCsv.setLinkImageObject(token[7]);
        readCsv.setFromZone(token[8]);
        readCsv.setInZone(token[9]);
        listLineCsv.add(readCsv);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ) {
                perm=true;

        }else{
            Toast.makeText(this,R.string.permission_denied,Toast.LENGTH_LONG).show();

        }

    }


    private void savePlace() {

        for (i = 0; i < listPlace.size(); i++){

            Place place = new Place(listPlace.get(i).getName(), listPlace.get(i).getCity(),
                    listPlace.get(i).getTypology(), user.getUid());

        db.collection("Places")
                .add(place)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        idPlace = documentReference.getId();
                        saveZones(listZone,listPlace.get(i-1).getIdPlace());
                        success = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ImportPhotoObjectActivity.this, R.string.place_save_failed, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    }




    private void saveZones(List<Zone> zone_list, int idPlac) {

        for(i=0;i<zone_list.size();i++){
            if(listZone.get(i).getIdPl()==idPlac) {
                Zone zone = new Zone(zone_list.get(i).getName(), this.idPlace, null, user.getUid());

                db.collection("Zones")
                        .add(zone)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                if(i==zone_list.size() && flag1==false) {
                                    flag1 = true;
                                    saveObjects(listElement);
                                }
                                success = true;

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ImportPhotoObjectActivity.this, R.string.zone_saving_failed, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }


    private void saveConstraints() {

        for (j=0;j<listConstranints.size();j++){
            db.collection("Constraints")
                    .add(listConstranints.get(j))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            if(j == listConstranints.size() && flag2 == false) {
                                //saveObjects(CreateObjectWizard.getElementList());
                                flag2 = true;
                            }
                            success=true;
                        }
                    });
        }
    }


    private void saveObjects(List<Element> elmlist) {

        db.collection("Zones")
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (int i=0;i<elmlist.size();i++) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Element newElement = elmlist.get(i);
                                    Zone zon = document.toObject(Zone.class);
                                    if (zon.getName().matches(newElement.getZoneRif())) {
                                        newElement.setIdZone(document.getId());
                                        id=generateidPhotoAndQrCode();
                                        elm.put("idZone", newElement.getIdZone());
                                        elm.put("title", newElement.getTitle());
                                        elm.put("description", newElement.getDescription());
                                        elm.put("photo", newElement.getPhoto().toString());
                                        elm.put("qrCode", null);
                                        elm.put("idPhotoAndQrCode",Long.parseLong(newElement.getIdPhotoAndQrCodeString()));
                                        elm.put("idUser",user.getUid());
                                        elm.put("qrData",newElement.getQrData());
                                        db.collection("Elements")
                                                .add(elm)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        success=true;
                                                        // String id=documentReference.getId();
                                                    }
                                                });
                                        saveQrCode(newElement.getQrData(),newElement,i,id);
                                    }
                                }
                            }
                            if(i==elmlist.size()-1){
                                listElement.clear();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                success=false;
                Toast.makeText(ImportPhotoObjectActivity.this, R.string.not_possible_save_zone, Toast.LENGTH_LONG).show();

            }
        });
    }


    private void saveQrCode(String dataQr, Element element, int i, long id) {

        GenerateQrCodeClass generateQrCodeClass=new GenerateQrCodeClass(ImportPhotoObjectActivity.this);
        Bitmap qrCode=generateQrCodeClass.generateQrCode(dataQr);

        final StorageReference fileRef = storegeProfilePick.child("QrCodeObjects").child("QrCode_Objects"+"_"+id);
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
                            uriUploadQrCode.add(downloadUrl.toString());

                            db.collection("Elements").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista zone

                                        for (DocumentSnapshot d : listDocument) {
                                            ElementString elme = d.toObject(ElementString.class);
                                            if (elme.getTitle().matches(element.getTitle())) {
                                                HashMap<String,Object> userMap=new HashMap<>();
                                                userMap.put("qrCode",uriUploadQrCode.get(i));

                                                db.collection("Elements").document(d.getId()).
                                                        update("qrCode", uriUploadQrCode.get(i)).
                                                        addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                success=true;
                                                                Toast.makeText(ImportPhotoObjectActivity.this, R.string.zones_objects_created, Toast.LENGTH_LONG).show();
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        success=false;
                                                        Toast.makeText(ImportPhotoObjectActivity.this, R.string.not_possible_save_zone, Toast.LENGTH_LONG).show();
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                });
                                            }
                                        }

                                    }
                                }
                            });
                        }

                    }
                });
            }
        });
    }
}