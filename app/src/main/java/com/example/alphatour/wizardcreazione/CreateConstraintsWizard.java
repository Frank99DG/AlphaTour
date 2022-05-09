package com.example.alphatour.wizardcreazione;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
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

import com.example.alphatour.DashboardActivity;
import com.example.alphatour.R;
import com.example.alphatour.oggetti.Constraint;
import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.Zone;
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

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CreateConstraintsWizard<zone_list> extends Fragment implements Step, BlockingStep {

    //private boolean constraintFlag = false;
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
    private boolean success=false;
    private List<String> uriUploadPhoto=new ArrayList<String>();
    private List<String> uriUploadQrCode=new ArrayList<String>();
    private long idPhotoAndQrCode,id;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private List<Constraint> listConstranints=new ArrayList<Constraint>();
    private String data;


    public CreateConstraintsWizard() {
        // Required empty public constructor
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
                        Toast.makeText(getContext(), "La zona è gia stata collegata", Toast.LENGTH_LONG).show();
                        menu_zones.setText(null);
                    }else if(item.equals(nameZone)){
                        Toast.makeText(getContext(), "Una zona non è collegabile con se stessa", Toast.LENGTH_LONG).show();
                        menu_zones.setText(null);
                    }else{

                        final View destinationView = getLayoutInflater().inflate(R.layout.row_in_zone, null, false);
                        TextView in_zone = (TextView) destinationView.findViewById(R.id.displayZone);
                        ImageView removeZone = (ImageView) destinationView.findViewById(R.id.deleteZone);

                        in_zone.setText(item);
                        Constraint constraint=new Constraint(nameZone,item);
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

                                Toast.makeText(getContext(), "Vincolo eliminato", Toast.LENGTH_LONG).show();
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


    public void saveZonesAndConstraintInGraph() {

        Graph<Zone,DefaultEdge> zones = new SimpleDirectedGraph<>(DefaultEdge.class);

        for (String nameZone : zone_list){
            Zone zone = new Zone(nameZone);
            zones.addVertex(zone);
        }

        /*for (List list : all_link_lists){
            for (String  : list){
                zones.addEdge( , );

            }
        }*/

        Place place = new Place("NewTopMuseum","Bari","Museo","1",zones);
        Log.i("Luogo creato:", place.toString());





    }




    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        dialog.show();
        titleDialog.setText("Completa inserimenti");
        textDialog.setText("Sei sicuro di voler completare la creazione dei luoghi e degli oggetti ? ");
        yesFinal.setText("Si");

        yesFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                savePlace();
                saveZones(CreateZoneWizard.getZone_list());
                saveConstraints();
                saveObjects(CreateObjectWizard.getElementList());
                dialog.dismiss();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(success) {
                            Intent intent = new Intent(getContext(), DashboardActivity.class);
                            startActivity(intent);
                            loadingBar.setVisibility(View.GONE);
                            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                        }else{
                            Toast.makeText(getContext(), "Salvataggio Luogo non riuscito" , Toast.LENGTH_LONG).show();
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
    }

    private void saveConstraints() {

        for (int i=0;i<listConstranints.size();i++){
            db.collection("Constraints")
                    .add(listConstranints.get(i))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
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
                        success=true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Salvataggio Luogo non riuscito" , Toast.LENGTH_LONG).show();
                loadingBar.setVisibility(View.GONE);
            }
        });

    }


    private void saveZones(ArrayList<String> zone_list) {

        for(int i=0;i<zone_list.size();i++){
            Zone zone=new Zone(user.getUid(),zone_list.get(i));

            db.collection("Zones")
                    .add(zone)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            success=true;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Salvataggio Zone non riuscito" , Toast.LENGTH_LONG).show();
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

        //if(constraintFlag){
          //  error = new VerificationError("Non puoi tornare indietro");
        //}
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
                Toast.makeText(getContext(), "Non è stato possibile salvare le zone e gli oggetti creati!!!", Toast.LENGTH_LONG).show();
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
                                                Toast.makeText(getContext(), "Non è stato possibile salvare le zone e gli oggetti creati!!!", Toast.LENGTH_LONG).show();
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
                                                                Toast.makeText(getContext(), "Zone e Oggetti creati con successo", Toast.LENGTH_LONG).show();
                                                                loadingBar.setVisibility(View.GONE);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        success=false;
                                                        Toast.makeText(getContext(), "Non è stato possibile salvare le zone e gli oggetti creati!!!", Toast.LENGTH_LONG).show();
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

}