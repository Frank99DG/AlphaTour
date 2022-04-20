package com.example.alphatour.wizardcreazione;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.R;
import com.example.alphatour.oggetti.Element;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateConstraintsWizard<zone_list> extends Fragment implements Step, BlockingStep {

    private boolean contraintFlag = false;
    private ArrayList<String> zone_list=new ArrayList<>();
    private LinearLayout layout_list;
    private ArrayAdapter<String> adapterItems;
    private String item;
    private CharSequence name;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


    public CreateConstraintsWizard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_create_constraints_wizard, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        layout_list = view.findViewById(R.id.listConstraintLayout);

        zone_list = CreateZoneWizard.getZone_list();

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
                    contraintFlag = false;
                    item = parent.getItemAtPosition(position).toString();

                    for(String zone : link_list){
                        if(zone.equals(item)){
                            contraintFlag = true;
                        }
                    }

                    if(item.equals(nameZone)){
                        Toast.makeText(getContext(), "Una zona non è collegabile con se stessa", Toast.LENGTH_LONG).show();
                        menu_zones.setText(null);
                    }else if(contraintFlag){
                        Toast.makeText(getContext(), "La zona è gia stata collegata", Toast.LENGTH_LONG).show();
                        menu_zones.setText(null);
                    }else{

                        final View destinationView = getLayoutInflater().inflate(R.layout.row_in_zone, null, false);
                        TextView in_zone = (TextView) destinationView.findViewById(R.id.displayZone);
                        ImageView removeZone = (ImageView) destinationView.findViewById(R.id.deleteZone);

                        in_zone.setText(item);
                        link_list.add(item);
                        sub_layout_list.addView(destinationView);
                        menu_zones.setText(null);

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






    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

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

        if(contraintFlag){
            error = new VerificationError("Non puoi tornare indietro");
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



    /*public boolean inputControl(String Prova){

        Boolean errorFlag = false;

        if (Prova.isEmpty()) {
            prova.setError(getString(R.string.campo_obbligatorio));
            prova.requestFocus();
            errorFlag = true;
        }

        return errorFlag;

    }*/

}