package com.example.alphatour.wizardcreazione;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alphatour.ActivityElementDetails;
import com.example.alphatour.AddElementActivity;
import com.example.alphatour.AddPlaceActivity;
import com.example.alphatour.AddZoneActivity;
import com.example.alphatour.DashboardActivity;
import com.example.alphatour.ProfileActivity;
import com.example.alphatour.R;
import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.User;
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
import com.google.firebase.firestore.Query;
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
import java.util.List;
import java.util.Map;

public class CreateObjectWizard extends Fragment implements Step, BlockingStep {

    private Button addElement,yes,cancel;
    private static LinearLayout layout_list;
    private Dialog dialog;
    private String zone,idZone;
    private List<String> uriUploadPhoto=new ArrayList<String>();
    private List<String> uriUploadQrCode=new ArrayList<String>();
    private static Bitmap qr;
    private boolean success=false;
    private static Uri ph;
    private static List<View> typology_list = new ArrayList<View>();
    private static List<Element> elementList = new ArrayList<Element>();
    private ArrayList<String> zone_list=new ArrayList<>();
    private TextView titleDialog,textDialog;
    private ProgressBar loadingBar;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storegeProfilePick;
    private StorageTask uploadTask;
    private  Map<String, Object> elm = new HashMap<>();

    public static LinearLayout getLayout_list() {
        return layout_list;
    }

    public static List<View> getTypology_list() {
        return typology_list;
    };

    public static Bitmap getQr() {
        return qr;
    }

    public void setQr(Bitmap qr) {
        this.qr = qr;
    }

    public static Uri getPh() {
        return ph;
    }

    public void setPh(Uri ph) {
        this.ph = ph;
    }

    public static List<Element> getElementList() {
        return elementList;
    }

    public static void setElementList(List<Element> elementList) {
        CreateObjectWizard.elementList = elementList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_crea_oggetti_wizard, container, false);
        addElement=view.findViewById(R.id.buttonAddElement);
        layout_list=view.findViewById(R.id.listElementLayout);

        zone_list=CreateZoneWizard.getZone_list();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        storegeProfilePick= FirebaseStorage.getInstance().getReference();


        dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;

        yes= dialog.findViewById(R.id.btn_okay);
        cancel= dialog.findViewById(R.id.btn_cancel);
        titleDialog=dialog.findViewById(R.id.titleDialog);
        textDialog=dialog.findViewById(R.id.textDialog);

        addElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), AddElementActivity.class);

                startActivityForResult(intent,10);
            }
        });

         return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        /*element.setTitle(data.getStringExtra("title"));
        element.setDescription(data.getStringExtra("description"));
        element.setPhoto((Uri) data.getSerializableExtra("image"));
        element.setQrCode(data.getParcelableExtra("qrCode"));
        element.setSensorCode(data.getStringExtra("sensor"));*/

        if (requestCode == 10){
            if (data != null) {
                Element element = new Element();

                element.setTitle(data.getStringExtra("title"));
                element.setDescription(data.getStringExtra("description"));
                element.setSensorCode(data.getStringExtra("sensor"));
                zone = data.getStringExtra("zone");
                element.setZoneRif(zone);
                setPh(AddElementActivity.getPhoto());
                setQr(AddElementActivity.getQr());
                createdObject(element);
            }
        }else{
            if (data != null) {
                Element element = new Element();
                element.setTitle(data.getStringExtra("title"));
                element.setDescription(data.getStringExtra("description"));
                element.setSensorCode(data.getStringExtra("sensor"));
                zone = data.getStringExtra("zone");
                element.setZoneRif(zone);
                setPh(ActivityElementDetails.getPh());
                setQr(ActivityElementDetails.getQr());
                createdObject(element);
            }
        }
    }

    private void createdObject(Element element) {
        final View elementView = getLayoutInflater().inflate(R.layout.row_add_element, null, false);
        ImageView removeElement = (ImageView) elementView.findViewById(R.id.deleteElement);
        TextView elementTitle = (TextView) elementView.findViewById(R.id.inputElement);
        elementTitle.setText(element.getTitle());
        layout_list.addView(elementView);
        element.setPhoto(getPh());
        element.setQrCode(getQr());
        elementList.add(element);

        removeElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                titleDialog.setText("Elimina Oggetto");
                textDialog.setText("Sei sicuro di voler eliminare l'oggetto creato ?");

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Hai eliminato l'oggetto", Toast.LENGTH_LONG).show();

                        for(int i=0;i<layout_list.getChildCount();i++){

                            if(layout_list.getChildAt(i).equals(elementView)){
                                elementList.remove(i);
                            }
                        }
                        layout_list.removeView(elementView);
                        dialog.dismiss();
                    }
                });

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        elementTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                typology_list.add(elementView);
                Intent intent =new Intent(getContext(), ActivityElementDetails.class);
                intent.putExtra("ZoneList",zone_list);
                intent.putExtra("zone",zone);
                startActivityForResult(intent,20);
            }
        });

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        /*boolean save;


        save=saveObject(elementList);



        if(save){
            Toast.makeText(getContext(), "Zone e Oggetti salvati con successo", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), DashboardActivity.class));
            //loadingBar.setVisibility(View.GONE);
        }else{

            //loadingBar.setVisibility(View.GONE);
        }*/

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
    }

    private boolean saveObject(List<Element> elementlist) {

        String idUser = user.getUid();
        /*for (int i=0;i<elementlist.size();i++) {
            Element newElement = elementlist.get(i);
            Intent intent = new Intent(getContext(), Save.class);
            intent.putExtra("title",newElement.getTitle());
            intent.putExtra("zone",newElement.getZoneRif());
            intent.putExtra("description",newElement.getDescription());
            setPh(newElement.getPhoto());
            setQr(newElement.getQrCode());
            intent.putExtra("sensor",newElement.getSensorCode());
            startActivity(intent);
        }*/
       // SaveData dat= new SaveData();
       // dat.Save(elementlist.get(0),db);

        /*for (int i=0;i<elementlist.size();i++) {
            Element newElementSupport = elementlist.get(i);
            uriUploadPhoto=newElementSupport.getZoneRif();
            db.collection("Zones")
                    .whereEqualTo("name",newElementSupport.getZoneRif())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    newElementSupport.setIdZone(document.getId());
                                    uriUploadPhoto="pezos";
                                    elementlist.set(0,newElementSupport);
                                }
                            } else {
                            }
                        }
                    });
        }*/



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
                                elm.put("idZone", newElement.getIdZone());
                                elm.put("title", newElement.getTitle());
                                elm.put("description", newElement.getDescription());
                                elm.put("photo", null);
                                elm.put("qrCode", null);
                                elm.put("activity", null);
                                elm.put("sensorCode", newElement.getSensorCode());
                                db.collection("Elements")
                                        .add(elm)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                success=true;
                                            }
                                        });
                                savePhoto(newElement.getPhoto(),newElement,i);
                                saveQrCode(newElement.getQrCode(),newElement,i);
                                Log.i("map: ",elm.toString());
                                /*Element newElement=new Element(newElementSupport.getIdZone(),newElementSupport.getTitle(),
                                        newElementSupport.getDescription(),"activity",newElementSupport.getSensorCode());*/
                            }
                        }
                    }
                }
            }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Salvataggio Zone e Oggetti non riuscito", Toast.LENGTH_LONG).show();
            }
        });

        return success;
    }

    private String getIdZone(List<Element> elementlist,int i) {
       final int c=i;
        db.collection("Zones").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista zone

                        for (DocumentSnapshot d : listDocument) {

                            Element newElementSupport = elementlist.get(c);
                            Zone zon = d.toObject(Zone.class);
                            if (zon.getName().matches(newElementSupport.getZoneRif())) {
                                idZone= d.getId();
                            }
                        }

                }
            }
        });
        return idZone;
    }

    private void saveQrCode(Bitmap qrCode,Element element,int i) {

        final StorageReference fileRef = storegeProfilePick.child("QrCode_Objects"+"_"+element.getTitle());
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
                                                                Toast.makeText(getContext(), "Hai aggiornato l'immagine di profilo", Toast.LENGTH_LONG).show();
                                                                //loadingBar.setVisibility(View.GONE);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Non è stato possibile aggiornare l'immagine di profilo!", Toast.LENGTH_LONG).show();
                                                        // loadingBar.setVisibility(View.GONE);
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

    private void savePhoto(Uri photo,Element element,int i) {


        final StorageReference fileRef = storegeProfilePick.child("PhotoObjects").child("Photo_Objects"+"_"+element.getTitle());

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
                                                            Toast.makeText(getContext(), "Hai aggiornato l'immagine di profilo", Toast.LENGTH_LONG).show();
                                                            //loadingBar.setVisibility(View.GONE);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Non è stato possibile aggiornare l'immagine di profilo!", Toast.LENGTH_LONG).show();
                                                   // loadingBar.setVisibility(View.GONE);
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

    private boolean saveZone(ArrayList<String> zone_list) {

        for (int i = 0; i < zone_list.size(); i++){

            Zone newZone= new Zone();
            newZone.setName(zone_list.get(i));
            db.collection("Zones")
                    .add(newZone)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            success=true;
                        }
                    });
        }
        return success;
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }
}