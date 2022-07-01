package com.example.alphatour.wizardcreatepath;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.mainUI.DashboardActivity;
import com.example.alphatour.R;
import com.example.alphatour.connection.Receiver;
import com.example.alphatour.objectclass.MapZoneAndObject;
import com.example.alphatour.objectclass.Place;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;


public class Step1 extends Fragment implements Step, BlockingStep {

    private static EditText namePath, descriptionPath;
    private static AutoCompleteTextView placePath;
    private static List<String> placesList =new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private String item;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String idUser;
    private static String Place;
    private boolean errorFlag = true,connected=false;
    private Receiver receiver;
    private static String NamePlace;
    private Dialog dialog;
    private Button dialog_delete_path, dialog_dismiss;
    private TextView dialog_title,dialog_text;
    private ImageView dialog_delete_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //istanza oggetti firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_delete_path = dialog.findViewById(R.id.btn_okay);
        dialog_dismiss = dialog.findViewById(R.id.btn_cancel);
        dialog_title = dialog.findViewById(R.id.titleDialog);
        dialog_text = dialog.findViewById(R.id.textDialog);
        dialog_delete_image= dialog.findViewById(R.id.imageDialog);
        dialog_delete_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));


        dialog_title.setText("Attenzione");
        dialog_text.setText("Sei sicuro di voler tornare indietro? i dati inseriti durante la creazione del percorso verranno persi");
        dialog_delete_path.setText("Torna alla dashboard");

        dialog_delete_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToDashboard();
            }
        });

        dialog_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        View view = inflater.inflate(R.layout.fragment_step1, container, false);
        namePath = view.findViewById(R.id.inputNamePath);
        placePath = view.findViewById(R.id.inputPlacePath);
        placesList = getPlacesList();
        adapterItems = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, placesList);
        placePath.setAdapter(adapterItems);
        descriptionPath = view.findViewById(R.id.inputDescriptionPath);

        placePath.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                placePath.setError(null);
                PercorsoWizard.setPlace(parent.getItemAtPosition(position).toString());
            }
        });

        placePath.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(PercorsoWizard.getPlace()!=null) {
                    placePath.setText(PercorsoWizard.getPlace());
                    placePath.showDropDown();
                }
            }
        }, 10);
/**
        placePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(receiver.isConnected()) {
                    placesList = getPlacesList();
                }else{
                    placesList = getPlacesList();
                    adapterItems = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, placesList);
                    placePath.setAdapter(adapterItems);
                }
            }
        });**/


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        PercorsoWizard.setCount(0);
        /**controllo connessione**/
        receiver=new Receiver();

        broadcastIntent();

        namePath.setText(PercorsoWizard.getNamePath());
        descriptionPath.setText(PercorsoWizard.getDescriptionPath());
    }

    private void broadcastIntent() {
        requireActivity().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(receiver);
    }


    public List<String> getPlacesList() {

        List<String> list = new ArrayList<String>();

        //if(receiver.isConnected()) {

            db.collection("Places").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista luoghi

                        for (DocumentSnapshot d : listDocument) {
                            Place place = d.toObject(Place.class);

                            if (idUser.equals(place.getIdUser())) {
                                String namePlace = place.getName();
                                list.add(namePlace);
                            }

                        }
                        /*adapterItems = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, list);
                        placePath.setAdapter(adapterItems);*/
                    }
                }
            });
       /* }else{

            AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(getContext());
            SQLiteDatabase db = dbAlpha.getReadableDatabase();

            Cursor cursor=db.query(AlphaTourContract.AlphaTourEntry.NAME_TABLE_PLACE,new String[]{AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_NAME},null,null,
                    null,null,null);

            if(cursor.moveToFirst()){
                list.add(cursor.getString(cursor.getColumnIndexOrThrow(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PATH_ID)));
            }
        }*/

        return list;

    }


    @Nullable
    @Override
    public VerificationError verifyStep() {

        VerificationError error=null;
        String NamePath = namePath.getText().toString();
        String PlacePath = placePath.getText().toString();
        String DescriptionPath = descriptionPath.getText().toString();
        errorFlag = inputControl(NamePath,PlacePath,DescriptionPath);
        if(errorFlag){
            error =new VerificationError("Compila tutti i campi");
        }else{

            if(Step4.getZoneAndObjectList_()!=null){
            if(Step4.getZoneAndObjectList_().size()!=0){
                    Step4.getZoneAndObjectList_().get(0).setName(NamePath);
                    Step4.getZoneAndObjectList_().get(0).setDescription(DescriptionPath);
            }}
            Place = PlacePath;
            Step2.setPlace(PlacePath);
            PercorsoWizard.setNamePath(NamePath);
            PercorsoWizard.setDescriptionPath(DescriptionPath);
        }
        return error;
    }


    private boolean inputControl(String NamePath, String PlacePath, String DescriptionPath) {

        Boolean errorFlag = false;

        if (NamePath.isEmpty()) {
            namePath.setError(getString(R.string.required_field));
            namePath.requestFocus();
            errorFlag = true;
        }

        if (PlacePath.isEmpty()) {
            placePath.setError(getString(R.string.required_field));
            placePath.requestFocus();
            errorFlag = true;
        }

        if (DescriptionPath.isEmpty()) {
            descriptionPath.setError(getString(R.string.required_field));
            descriptionPath.requestFocus();
            errorFlag = true;
        }

        return errorFlag;
    }


    private void moveToDashboard(){

        List<String> a = Step4.getZone_select();
        List<String> b = Step4.getOggetti_select();
        List<String> c = Step2.getArray_database();
        List<MapZoneAndObject> d = Step4.getZoneAndObjectList_();
        List<MapZoneAndObject> e = ReviewZoneSelected.getZoneAndObjectList();
        List<MapZoneAndObject> f = CreateJsonActivity.getZoneAndObjectListReviewPath();

        if(!a.isEmpty()) Step4.getZone_select().clear();
        if(!b.isEmpty()) Step4.getOggetti_select().clear();
        if(!c.isEmpty()) Step2.getArray_database().clear();
        if(!d.isEmpty()) Step4.getZoneAndObjectList_().clear();
        if(!e.isEmpty()) ReviewZoneSelected.getZoneAndObjectList().clear();
        if(!f.isEmpty()) CreateJsonActivity.getZoneAndObjectListReviewPath().clear();

        DashboardActivity.setFirstZoneChosen(false);
        PercorsoWizard.setDescriptionPath("");
        PercorsoWizard.setNamePath("");
        PercorsoWizard.setPlace(null);

        Intent intent= new Intent(getContext(), DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(),error.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

        dialog.show();

    }

    public static String getNamePath() {

        NamePlace=namePath.getText().toString();
        return NamePlace;
    }

    public static String getPlacePath(){
        return placePath.getText().toString();
    }


    public static String getDescriptionPath() {

        return descriptionPath.getText().toString();
    }


    public static String getPlace() {
        return Place;
    }

    public static void setPlace(String place) {
        Place = place;
    }


}