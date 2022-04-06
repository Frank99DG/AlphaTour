package com.example.alphatour.wizardcreazione;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alphatour.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;


public class CreatePlaceWizard extends Fragment implements Step {

    private EditText namePlace,city;
    private AutoCompleteTextView typology;
    private List<String> typology_list = new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private String idUser;
    private ProgressBar loadingBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


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
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        loadingBar = view.findViewById(R.id.placeLoadingBar);
        // Inflate the layout for this fragment
        return view;
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
}