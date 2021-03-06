package com.example.alphatour.wizardcreateplace;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import com.example.alphatour.connection.Receiver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.mainUI.DashboardActivity;
import com.example.alphatour.R;
import com.example.alphatour.dblite.AlphaTourContract;
import com.example.alphatour.dblite.AlphaTourDbHelper;
import com.example.alphatour.dblite.CommandDbAlphaTour;
import com.example.alphatour.objectclass.Constraint;
import com.example.alphatour.objectclass.Element;
import com.example.alphatour.objectclass.ElementString;
import com.example.alphatour.objectclass.Place;
import com.example.alphatour.objectclass.Zone;
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
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CreateConstraintsWizard<zone_list> extends Fragment implements Step, BlockingStep {



    private boolean constraintFlag = true;
    //private boolean listFlag = false;
    private ArrayList<String> zone_list=new ArrayList<>();
    private LinearLayout layout_list;
    private ArrayAdapter<String> adapterItems;
    private ArrayList<List> all_link_lists = new ArrayList<>();
    private String item;
    private CharSequence name;
    private FirebaseFirestore db;
    private StorageReference storegeProfilePick;
    private StorageTask uploadTask;
    private Dialog dialog;
    private Button yesFinal,cancelFinal;
    private TextView titleDialog,textDialog;
    private ProgressBar loadingBar;
    private Map<String, Object> elm = new HashMap<>();
    private static boolean success=false, load=true,visible=false;
    private List<String> uriUploadPhoto=new ArrayList<String>();
    private List<String> uriUploadQrCode=new ArrayList<String>();
    private long idPhotoAndQrCode,id;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private List<Constraint> listConstranints = new ArrayList<Constraint>();
    private String data;
    private String idPlace;
    private int i,j;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private Receiver receiver;
    private long res=-1;
    private ImageView imgDialog;


    public CreateConstraintsWizard() {
        // Required empty public constructor
    }

    public static boolean getVisible() {
        return visible;
    }

    public static void setVisible(boolean vis) {
        CreateConstraintsWizard.visible=vis;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_create_constraints_wizard, container, false);

        layout_list = view.findViewById(R.id.listConstraintLayout);

        zone_list = CreateZoneWizard.getZone_list();


        db = FirebaseFirestore.getInstance();
        storegeProfilePick= FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;


        yesFinal= dialog.findViewById(R.id.btn_okay);
        cancelFinal= dialog.findViewById(R.id.btn_cancel);
        titleDialog=dialog.findViewById(R.id.titleDialog);
        textDialog=dialog.findViewById(R.id.textDialog);
        loadingBar=view.findViewById(R.id.objectLoadingBar);
        imgDialog=dialog.findViewById(R.id.imageDialog);

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



        for (String nameZone : zone_list){

            final View zoneView = getLayoutInflater().inflate(R.layout.row_from_zone, null, false);
            TextView from_zone = (TextView) zoneView.findViewById(R.id.displayZone);
            LinearLayout sub_layout_list = (LinearLayout) zoneView.findViewById(R.id.listConstraintLayout);
            AutoCompleteTextView menu_zones = (AutoCompleteTextView) zoneView.findViewById(R.id.inputLinkZone);
            List<String> link_list = new ArrayList<String>();

            from_zone.setText(nameZone);

            adapterItems = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, zone_list);
            menu_zones.setAdapter(adapterItems);
            menu_zones.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                    item = parent.getItemAtPosition(position).toString();

                    if(link_list.contains(item)){
                        Toast.makeText(getContext(), R.string.zone_already_connected, Toast.LENGTH_LONG).show();
                        menu_zones.setText(null);
                    }else if(item.equals(nameZone)){
                        Toast.makeText(getContext(), R.string.zone_connected_itself, Toast.LENGTH_LONG).show();
                        menu_zones.setText(null);
                    }else{

                        final View destinationView = getLayoutInflater().inflate(R.layout.row_in_zone, null, false);
                        TextView in_zone = (TextView) destinationView.findViewById(R.id.displayZone);
                        ImageView removeZone = (ImageView) destinationView.findViewById(R.id.deleteZone);

                        in_zone.setText(item);
                        Constraint constraint = new Constraint( CreatePlaceWizard.getNamePlace(),nameZone,item );
                        listConstranints.add(constraint);
                        link_list.add(item);
                        sub_layout_list.addView(destinationView);
                        menu_zones.setText(null);

                        if( !(all_link_lists.contains(link_list)) ){
                            all_link_lists.add(link_list);
                        }


                        removeZone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Toast.makeText(getContext(), R.string.constraint_eliminated, Toast.LENGTH_LONG).show();
                                sub_layout_list.removeView(destinationView);

                                name = ((TextView) destinationView.findViewById(R.id.displayZone)).getText();
                                link_list.remove(name);
                            }
                        });
                    }

                }
            });

            layout_list.addView(zoneView);
        }


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        /**controllo connessione**/
        receiver=new Receiver();

        broadcastIntent();
    }

    private void broadcastIntent() {
        requireActivity().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(receiver);
    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        dialog.show();
        titleDialog.setText(R.string.button_complete);
        textDialog.setText(R.string.complete_creation_place);
        yesFinal.setText(R.string.yes);
        imgDialog.setImageDrawable(getResources().getDrawable(R.drawable.dialog_constraints_complete));

        yesFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                if(receiver.isConnected()){
                    savePlace();
                    load=true;
                    res=saveOnDbLocal(zone_list,CreateObjectWizard.getElementList(),load);
                }else{
                    savePlace();
                    load=false;
                    res=saveOnDbLocal(zone_list,CreateObjectWizard.getElementList(), load);
                }
                dialog.dismiss();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(success || res!=-1) {
                            Intent intent = new Intent(getContext(), DashboardActivity.class);
                            startActivity(intent);
                            loadingBar.setVisibility(View.GONE);
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ZoneAndObj",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            CreateZoneWizard.clearZone();
                            CreateObjectWizard.clearObject();
                        }else{
                            Toast.makeText(getContext(), R.string.place_save_failed , Toast.LENGTH_LONG).show();
                            loadingBar.setVisibility(View.GONE);
                        }
                    }
                }, 2000L);

            }
        });

        cancelFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        CreationWizard.setvalore(0);
        CreateZoneWizard.setZCreated(false);
        CreateObjectWizard.setObjCreated(false);

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

    private void savePlace() {


        Place place = new Place(CreatePlaceWizard.getNamePlace(), CreatePlaceWizard.getCity(),
                CreatePlaceWizard.getTypology(), user.getUid());

        db.collection("Places")
                .add(place)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        idPlace = documentReference.getId();
                        saveZones(CreateZoneWizard.getZone_list());
                        success=true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), R.string.place_save_failed , Toast.LENGTH_LONG).show();
                loadingBar.setVisibility(View.GONE);
            }
        });

    }


    private void saveZones(ArrayList<String> zone_list) {

        for(i=0;i<zone_list.size();i++){
            Zone zone=new Zone(zone_list.get(i),idPlace,null,user.getUid());

            db.collection("Zones")
                    .add(zone)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            if(i == zone_list.size() && flag1 == false ){
                                saveConstraints();
                                flag1 = true;
                            }
                            success=true;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), R.string.zone_saving_failed , Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        VerificationError error = null;
        //String Prova = prova.getText().toString();
        //stepFlag = inputControl(Prova);


        return error;



    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(),error.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
    }




    /*public boolean inputControl(String Prova){

        Boolean errorFlag = false;

        if (Prova.isEmpty()) {
            prova.setError(getString(R.string.campo_obbligatorio));
            prova.requestFocus();
            errorFlag = true;
        }

        return errorFlag;

    }*/


    private void saveObjects(List<Element> elementlist) {

        db.collection("Zones")
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (int i=0;i<elementlist.size();i++) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Element newElement = elementlist.get(i);
                                    Zone zon = document.toObject(Zone.class);
                                    if (zon.getName().matches(newElement.getZoneRif())) {
                                        newElement.setIdZone(document.getId());
                                        id=generateidPhotoAndQrCode();
                                        elm.put("idZone", newElement.getIdZone());
                                        elm.put("title", newElement.getTitle());
                                        elm.put("description", newElement.getDescription());
                                        elm.put("photo", null);
                                        elm.put("qrCode", null);
                                        elm.put("idPhotoAndQrCode",id);
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
                                        savePhoto(newElement.getPhoto(),newElement,i,id);
                                        saveQrCode(newElement.getQrCode(),newElement,i,id);
                                    }
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                success=false;
                Toast.makeText(getContext(), R.string.not_possible_save_zone, Toast.LENGTH_LONG).show();
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


    private void savePhoto(Uri photo, Element element, int i,long id) {

        final StorageReference fileRef = storegeProfilePick.child("PhotoObjects").child("Photo_Objects"+"_"+id);

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
                    uriUploadPhoto.add(downloadUrl.toString());


                    //elm.put("Photo", uriUploadPhoto);

                    db.collection("Elements").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista zone

                                for (DocumentSnapshot d : listDocument) {
                                    ElementString elme = d.toObject(ElementString.class);
                                    if (elme.getTitle().matches(element.getTitle())) {
                                        HashMap<String,Object> userMap=new HashMap<>();
                                        userMap.put("photo",uriUploadPhoto.get(i));

                                        db.collection("Elements").document(d.getId()).
                                                update("photo", uriUploadPhoto.get(i)).
                                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        success=true;
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                success=false;
                                                Toast.makeText(getContext(), R.string.not_possible_save_zone, Toast.LENGTH_LONG).show();
                                                loadingBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }

                            }
                        }
                    });
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


    }



    private void saveQrCode(Bitmap qrCode, Element element, int i, long id) {

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
                                                                Toast.makeText(getContext(), R.string.zones_objects_created, Toast.LENGTH_LONG).show();
                                                                loadingBar.setVisibility(View.GONE);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        success=false;
                                                        Toast.makeText(getContext(), R.string.not_possible_save_zone, Toast.LENGTH_LONG).show();
                                                        loadingBar.setVisibility(View.GONE);
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
    private long saveOnDbLocal(ArrayList<String> zone_list, List<Element> elementList, boolean load) {

        long result=-1;
        result=savePlaceLocal();
        result=saveZonesLocal(zone_list);
        result=saveConstraintsLocal();
        result=saveObjectsLocal(elementList);

        return result;
    }

    private long saveObjectsLocal(List<Element> elementList) {
        long newRowId=-1;
        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(getContext());
        SQLiteDatabase db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i=0;i<elementList.size();i++) {

            Element elm= elementList.get(i);
            String idZone=getIdZone(elm);
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_ID_ZONE,idZone);
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_NAME,elm.getTitle());
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_DESCRIPTION,elm.getDescription());
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_PHOTO,elm.getPhoto().toString());
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_QR_CODE,elm.getQrData());
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_LOAD,load);
            newRowId=db.insert(AlphaTourContract.AlphaTourEntry.NAME_TABLE_ELEMENT,AlphaTourContract.AlphaTourEntry.COLUMN_NAME_NULLABLE,values);
        }

        return newRowId;
    }

    private String getIdZone(Element elm) {
        String idZone="";
        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(getContext());
        SQLiteDatabase db = dbAlpha.getReadableDatabase();

        Cursor cursor=db.rawQuery(CommandDbAlphaTour.Command.SELECT_ID_ZONE,new String[]{elm.getZoneRif()});
        if(cursor.moveToFirst()){
            idZone= cursor.getString(cursor.getColumnIndexOrThrow(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_ID));
        }
        return idZone;
    }

    private long saveConstraintsLocal() {

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(getContext());
        SQLiteDatabase db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();

        long newRowId=-1;

        for (j=0;j<listConstranints.size();j++){
            Constraint constraint=listConstranints.get(j);
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_CONSTRAINTS_FROM_ZONE,constraint.getFromZone());
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_CONSTRAINTS_IN_ZONE,constraint.getInZone());
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_CONSTRAINTS_LOAD,load);
            newRowId=db.insert(AlphaTourContract.AlphaTourEntry.NAME_TABLE_CONSTRAINTS,AlphaTourContract.AlphaTourEntry.COLUMN_NAME_NULLABLE,values);
        }
        return newRowId;
    }

    private long saveZonesLocal(ArrayList<String> zone_list) {
        long newRowId=-1;

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(getContext());
        SQLiteDatabase db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();
        idPlace = getIdPlace(CreatePlaceWizard.getNamePlace());

        for(i=0;i<zone_list.size();i++) {


            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_NAME,zone_list.get(i));
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_ID_PLACE,idPlace);
            values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_LOAD,load);
            newRowId=db.insert(AlphaTourContract.AlphaTourEntry.NAME_TABLE_ZONE,AlphaTourContract.AlphaTourEntry.COLUMN_NAME_NULLABLE,values);
        }

        return newRowId;
    }

    private String getIdPlace(String s) {
        String idPl="";

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(getContext());
        SQLiteDatabase db = dbAlpha.getReadableDatabase();

        Cursor cursor=db.rawQuery(CommandDbAlphaTour.Command.SELECT_ID_PLACE,new String[]{s});
        if(cursor.moveToFirst()){
            idPl= cursor.getString(cursor.getColumnIndexOrThrow(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_ID));
        }


        return idPl;
    }

    private long savePlaceLocal() {
        long newRowId;

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(getContext());
        SQLiteDatabase db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_NAME,CreatePlaceWizard.getNamePlace());
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_CITY,CreatePlaceWizard.getCity());
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_TYPOLOGY,CreatePlaceWizard.getCity());
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_LOAD,load);

        newRowId=db.insert(AlphaTourContract.AlphaTourEntry.NAME_TABLE_PLACE,AlphaTourContract.AlphaTourEntry.COLUMN_NAME_NULLABLE,values);

        return newRowId;
    }

}