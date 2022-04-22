package com.example.alphatour.wizardcreazione;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alphatour.AddPlaceActivity;
import com.example.alphatour.AddZoneActivity;
import com.example.alphatour.DashboardActivity;
import com.example.alphatour.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;


public class CreatePlaceWizard extends Fragment implements Step, BlockingStep {

    private EditText namePlace,city;
    private AutoCompleteTextView typology;
    private List<String> typology_list = new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private String idUser;
    private ProgressBar loadingBar;
    private boolean errorFlag=true;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String item;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_crea_luogo, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        namePlace = view.findViewById(R.id.inputNamePlace);
        city = view.findViewById(R.id.inputCityPlace);
        typology = view.findViewById(R.id.inputTypologyPlace);
        typology_list.add(getString(R.string.museo));
        typology_list.add(getString(R.string.fiera));
        typology_list.add(getString(R.string.sito_archeologico));
        typology_list.add(getString(R.string.mostra));
        adapterItems = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,typology_list);
        typology.setAdapter(adapterItems);
        typology.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                typology.setError(null);
                item = parent.getItemAtPosition(position).toString();
            }
        });

        loadingBar = view.findViewById(R.id.placeLoadingBar);
        LoadPreferences();
        // Inflate the layout for this fragment
        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        VerificationError error=null;
        String NamePlace = namePlace.getText().toString();
        String City = city.getText().toString();
        String Typology = typology.getText().toString();
        errorFlag = inputControl(NamePlace, City, Typology);
        if(errorFlag){
             error =new VerificationError("Compila tutti i campi");
        }
        return error;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

        Toast.makeText(getContext(),error.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
    }

    public boolean inputControl(String NamePlace, String City, String Typology){

        Boolean errorFlag = false;

        if (NamePlace.isEmpty()) {
            namePlace.setError(getString(R.string.campo_obbligatorio));
            namePlace.requestFocus();
            errorFlag = true;
        }

        if (City.isEmpty()) {
            city.setError(getString(R.string.campo_obbligatorio));
            city.requestFocus();
            errorFlag = true;
        }


        if (Typology.isEmpty()) {
            typology.setError(getString(R.string.campo_obbligatorio));
            typology.requestFocus();
            errorFlag = true;
        }

        return errorFlag;

    }




    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

            StepperLayout step;
            loadingBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.goToNextStep();
                    loadingBar.setVisibility(View.GONE);
                }
            }, 2000L);

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

        Intent intent= new Intent(getContext(), DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("NamePlace",namePlace.getText().toString());
        outState.putString("City",city.getText().toString());
        outState.putString("Typology",typology.getText().toString());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState!=null) {

            namePlace.setText(savedInstanceState.getCharSequence("NamePlace"));
            city.setText(savedInstanceState.getCharSequence("City"));
           // typology.setHint(savedInstanceState.getCharSequence("Typology"));
        }

    }

    private void SavePreferences(){
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NamePlace", namePlace.getText().toString());
        editor.putString("City",city.getText().toString());
        editor.putString("Typology",typology.getText().toString());
        editor.commit();   // I missed to save the data to preference here,.
    }

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        namePlace.setText(sharedPreferences.getString("NamePlace",""));
        city.setText(sharedPreferences.getString("City",""));
        item=sharedPreferences.getString("Typology","");
        if(item==""){
            typology.setHint("Tipologia");
        }else{
            typology.setHint(sharedPreferences.getString("Typology",""));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SavePreferences();
    }


}