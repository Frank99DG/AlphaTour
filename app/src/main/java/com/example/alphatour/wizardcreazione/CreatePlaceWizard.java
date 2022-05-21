package com.example.alphatour.wizardcreazione;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.alphatour.DashboardActivity;
//import com.example.alphatour.ImportPhotoObjectActivity;
import com.example.alphatour.ModifyObjectActivity;
import com.example.alphatour.R;
//import com.example.alphatour.ReadCsv;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CreatePlaceWizard extends Fragment implements Step, BlockingStep {

    private EditText namePlace,city;
    private AutoCompleteTextView typology;
    private List<String> typology_list = new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private static String NamePlace,City,Typology;
    private ProgressBar loadingBar;
    private boolean errorFlag=true;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String idUser;
    private String item;
    private Button qrScan;
    //private Button csv,ph;
    //private int requestCod=1;
    //private List<ReadCsv> listLineCsv=new ArrayList<ReadCsv>();

    public static String getNamePlace() {
        return NamePlace;
    }

    public static String getCity() {
        return City;
    }

    public static String getTypology() {
        return Typology;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_crea_luogo, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        namePlace = view.findViewById(R.id.inputNamePlace);
        city = view.findViewById(R.id.inputCityPlace);
        typology = view.findViewById(R.id.inputTypologyPlace);
        qrScan=view.findViewById(R.id.inputQr);
       // csv=view.findViewById(R.id.csvBtn);
       // ph=view.findViewById(R.id.uploadphoto);
        typology_list.add(getString(R.string.museum));
        typology_list.add(getString(R.string.fair));
        typology_list.add(getString(R.string.archaeological_site));
        typology_list.add(getString(R.string.museum_exhibition));
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

        qrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ModifyObjectActivity.class);
                intent.putExtra("data","Qhhhhjo");
                startActivity(intent);
            }
        });

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},120);
            }
        }


        /*db.collection("Places")
                .whereEqualTo("idUser",idUser)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listDocuments = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listDocuments) {
                                Place place= d.toObject(Place.class);
                                namePlace.setText(place.getName());
                                city.setText(place.getCity());
                                typology.setText(place.getTypology());
                            }
                        }
                    }
                });

       /* csv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/comma-separated-values");
                startActivityForResult(intent,requestCod);
            }
        });*/

       /* ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ImportPhotoObjectActivity.class);
                startActivity(intent);
            }
        });*/


        return view;
    }


    @Nullable
    @Override
    public VerificationError verifyStep() {

        VerificationError error=null;
         NamePlace = namePlace.getText().toString();
         City = city.getText().toString();
         Typology = typology.getText().toString();
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
            namePlace.setError(getString(R.string.required_field));
            namePlace.requestFocus();
            errorFlag = true;
        }

        if (City.isEmpty()) {
            city.setError(getString(R.string.required_field));
            city.requestFocus();
            errorFlag = true;
        }


        if (Typology.isEmpty()) {
            typology.setError(getString(R.string.required_field));
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

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCod==requestCode && resultCode== Activity.RESULT_OK){

            if(data==null){
                return;
            }else{
                Uri uri = data.getData();
                Toast.makeText(getContext(),uri.getPath(),Toast.LENGTH_SHORT).show();
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
                            if(!token[0].matches("nameZone")) {
                                readCsv.setNameZone(token[0]);
                                readCsv.setTitleObject(token[1]);
                                readCsv.setDescriptionObject(token[2]);
                                readCsv.setQrDataObject(token[3]);
                                readCsv.setLinkImageObject(token[4]);
                                listLineCsv.add(readCsv);
                            }
                        }else{

                            Toast.makeText(getContext(),"Errore duratnte l'importazione del file, alcuni"+
                                    "campi potrebbero essere vuoti",Toast.LENGTH_LONG).show();
                            line=null;
                        }
                    }

                    if(isEmpty){
                        Toast.makeText(getContext(),"Errore duratnte l'importazione del file, alcuni"+
                                "campi potrebbero essere vuoti",Toast.LENGTH_LONG).show();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){

            Toast.makeText(getContext(),"Permission granted",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),"Permission not granted",Toast.LENGTH_LONG).show();
        }
        return;
    }*/

}