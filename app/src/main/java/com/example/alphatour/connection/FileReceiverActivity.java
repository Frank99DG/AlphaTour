package com.example.alphatour.connection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alphatour.BuildConfig;
import com.example.alphatour.DashboardActivity;
import com.example.alphatour.ImportPhotoObjectActivity;
import com.example.alphatour.R;
import com.example.alphatour.ReadCsv;
import com.example.alphatour.oggetti.Constraint;
import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.qrcode.GenerateQrCodeClass;
import com.example.alphatour.wizardcreazione.CreateObjectWizard;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileReceiverActivity extends AppCompatActivity {

    private List<ReadCsv> listLineCsv=new ArrayList<ReadCsv>();
    private List<Zone> listZone=new ArrayList<Zone>();
    private List<Element> listElement=new ArrayList<Element>();
    private List<Place> listPlace=new ArrayList<Place>();
    private List<Constraint> listConstranints = new ArrayList<Constraint>();
    private int i,j,idP=1,idZ=1;
    private FirebaseFirestore db;
    private StorageReference storegeProfilePick;
    private StorageTask uploadTask;
    private Uri imageUri;
    private long idPhotoAndQrCode,id;
    private LinearLayout layout_list;
    private List<String> linkList= new ArrayList<String>();
    private String uriUploadPhoto,idPlace;
    private ProgressBar progressBar;
    private boolean place=false,flag1=false,flag2=false,loadConstraints=false;
    private boolean success=false, load=true;
    private List<String> uriUploadQrCode=new ArrayList<String>();
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Map<String, Object> elm = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_receiver);

        db = FirebaseFirestore.getInstance();
        storegeProfilePick= FirebaseStorage.getInstance().getReference();
        progressBar=findViewById(R.id.phLoadingBar);
        layout_list=findViewById(R.id.listphotoLayout);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Intent intent = getIntent();
        if(intent != null){
            String action = intent.getAction();
            String type = intent.getType();
            if(Intent.ACTION_SEND.equals(action) && type != null){
                if(type.equalsIgnoreCase("text/comma-separated-values")){
                    readCsv(intent);
                }
            }
        }
    }


    private void readCsv(Intent data) {

        if(data==null){
            return;
        }else{


            Uri uri = data.getParcelableExtra(Intent.EXTRA_STREAM);
            Toast.makeText(FileReceiverActivity.this,uri.getPath(),Toast.LENGTH_SHORT).show();
            String path = uri.getPath();
            String filename= URLUtil.guessFileName(path,null,null);
            String downloadPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            path = path.substring(path.lastIndexOf('/')+1);
            File file = new File(downloadPath,filename);
            try {
                BufferedReader read = new BufferedReader(new FileReader(file));
                String line = "";
                Boolean isEmpty=true;
                while((line=read.readLine())!=null){
                    isEmpty=false;
                    String[] token = line.split(",");
                    ReadCsv readCsv = new ReadCsv();
                    boolean flag = false;
                    for(int i=0;i< token.length;i++){

                        if(token[i] == null){
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

                        Toast.makeText(FileReceiverActivity.this,R.string.fields_may_be_empty,Toast.LENGTH_LONG).show();
                        line = null;
                    }
                }

                if(isEmpty){
                    Toast.makeText(FileReceiverActivity.this,R.string.fields_may_be_empty,Toast.LENGTH_LONG).show();
                }else{

                    savePlace();
                    saveConstraints();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if(success) {
                                Intent intent = new Intent(FileReceiverActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(FileReceiverActivity.this, R.string.place_save_failed , Toast.LENGTH_LONG).show();
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
                    Toast.makeText(FileReceiverActivity.this, R.string.place_save_failed, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(FileReceiverActivity.this, R.string.zone_saving_failed, Toast.LENGTH_LONG).show();
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
                                saveObjects(CreateObjectWizard.getElementList());
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
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                success=false;
                Toast.makeText(FileReceiverActivity.this, R.string.not_possible_save_zone, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void saveQrCode(String dataQr, Element element, int i, long id) {

        GenerateQrCodeClass generateQrCodeClass=new GenerateQrCodeClass(FileReceiverActivity.this);
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
                                                                Toast.makeText(FileReceiverActivity.this, R.string.zones_objects_created, Toast.LENGTH_LONG).show();
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        success=false;
                                                        Toast.makeText(FileReceiverActivity.this, R.string.not_possible_save_zone, Toast.LENGTH_LONG).show();
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


}