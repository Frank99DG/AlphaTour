package com.example.alphatour.wizardcreateplace;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alphatour.R;
import com.example.alphatour.objectclass.Zone;
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

public class CreateZoneWizard extends Fragment implements Step, BlockingStep {

    private EditText nameZone;
    private Button addZone,yes,cancel;
    private static boolean zoneCreated=false;
    private LinearLayout layout_list;
    private Dialog dialog;
    private TextView titleDialog,textDialog;
    private static  ArrayList<String> zone_list = new ArrayList<>();
    private ImageView imgDialog;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private List<DocumentSnapshot> listaDocumenti=new ArrayList<DocumentSnapshot>();



    public static ArrayList<String> getZone_list() {
        return zone_list;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_crea_zone_wizard, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();


        addZone = view.findViewById(R.id.buttonAddElement);
        nameZone = view.findViewById(R.id.inputNomeZona);
        layout_list = view.findViewById(R.id.listZoneLayout);


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
         imgDialog=dialog.findViewById(R.id.imageDialog);

         LoadPreferences();

        db.collection("Zones")
                .whereEqualTo("idUser",user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            listaDocumenti = queryDocumentSnapshots.getDocuments();
                        }
                    }
                });


        addZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameZone.getText().toString();
                if (!name.isEmpty()) {
                    boolean duplicate = duplicateControl(name);
                    if (!duplicate){
                        zoneCreated = true;
                    final View zoneView = getLayoutInflater().inflate(R.layout.row_add_zone, null, false);
                    ImageView removeZone = (ImageView) zoneView.findViewById(R.id.deleteZone);
                    TextView zone = (TextView) zoneView.findViewById(R.id.displayZone);
                    zone.setText(name);
                    layout_list.addView(zoneView);
                    zone_list.add(name);

                    removeZone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.show();
                            titleDialog.setText(R.string.delete_zone);
                            textDialog.setText(R.string.delete_zone_text);
                            imgDialog.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));

                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getContext(), R.string.deleted_zone, Toast.LENGTH_LONG).show();
                                    layout_list.removeView(zoneView);
                                    dialog.dismiss();
                                    zone_list.remove(name);
                                }
                            });
                        }
                    });
                    nameZone.setText(null);
                }else{
                        Toast.makeText(getContext(), R.string.zone_already_exists, Toast.LENGTH_LONG).show();
                    }
                }else{
                    nameZone.setError(getString(R.string.required_field));
                    nameZone.requestFocus();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        return view;
    }

    private boolean duplicateControl(String name) {
        boolean flag=false;

        if(listaDocumenti.size()>0){
            for(DocumentSnapshot d:listaDocumenti){
                Zone zone=d.toObject(Zone.class);
                if(zone.getName().matches(name)){
                    flag=true;
                }
            }
        }
        return flag;
    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        VerificationError error=null;

        if(!zoneCreated){
            error =new VerificationError("Devi creare almeno una Zona");
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

    private void SavePreferences(){
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NameZone", nameZone.getText().toString());
        for(int i=0;i<zone_list.size();i++){
            editor.putString("Zona"+i, zone_list.get(i));
        }
        editor.putInt("SizeList",zone_list.size());
        editor.commit();
    }

    private void LoadPreferences(){
        Boolean created=false;
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        nameZone.setText(sharedPreferences.getString("NameZone",""));
        int size=sharedPreferences.getInt("SizeList",0);
        for(int i=0;i<size;i++){
            String name=sharedPreferences.getString("Zona"+i,"");
            zoneCreated=true;
            final View zoneView = getLayoutInflater().inflate(R.layout.row_add_zone, null, false);
            ImageView removeZone = (ImageView) zoneView.findViewById(R.id.deleteZone);
            TextView zone = (TextView) zoneView.findViewById(R.id.displayZone);
            zone.setText(name);
            layout_list.addView(zoneView);
            for(int j=0;j<zone_list.size();j++){

                if(name.matches(zone_list.get(j))){
                    created=true;
                }
            }

            if(!created){
                zone_list.add(name);
            }



            removeZone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    titleDialog.setText(R.string.delete_zone);
                    textDialog.setText(R.string.delete_zone_text);
                    imgDialog.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), R.string.deleted_zone, Toast.LENGTH_LONG).show();
                            layout_list.removeView(zoneView);
                            dialog.dismiss();
                            zone_list.remove(name);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SavePreferences();
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

        callback.goToPrevStep();

    }

}