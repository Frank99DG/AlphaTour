package com.example.alphatour.wizardpercorso;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.alphatour.DashboardActivity;
import com.example.alphatour.R;
import com.example.alphatour.oggetti.Place;
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
    private boolean errorFlag = true;

    public static String getPlace() {
        return Place;
    }

    public static void setPlace(String place) {
        Place = place;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //istanza oggetti firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();


        View view = inflater.inflate(R.layout.fragment_step1, container, false);
        namePath = view.findViewById(R.id.inputNamePath);
        placePath = view.findViewById(R.id.inputPlacePath);
        placesList = getPlacesList();
        adapterItems = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, placesList);
        placePath.setAdapter(adapterItems);
        descriptionPath = view.findViewById(R.id.inputDescriptionPath);


        // Inflate the layout for this fragment
        return view;
    }



    public List<String> getPlacesList() {

        List<String> list = new ArrayList<String>();

        db.collection("Places").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista luoghi

                    for (DocumentSnapshot d : listDocument) {
                        Place place = d.toObject(Place.class);

                        if( idUser.equals(place.getIdUser()) ){
                            String namePlace = place.getName();
                            list.add(namePlace);
                        }

                    }
                }
            }
        });

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
            Place = PlacePath;
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
        Intent intent= new Intent(getContext(), DashboardActivity.class);
        startActivity(intent);
    }

    public static String getNamePath() {

        return namePath.getText().toString();
    }

    public static String getPlacePath(){
        return placePath.getText().toString();
    }


    public static String getDescriptionPath() {

        return descriptionPath.getText().toString();
    }



}