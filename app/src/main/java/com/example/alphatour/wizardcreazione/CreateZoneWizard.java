package com.example.alphatour.wizardcreazione;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.viewpager.widget.ViewPager;

import com.example.alphatour.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Set;

public class CreateZoneWizard extends Fragment implements Step, BlockingStep {
    private int zoneNumber = 1;
    private EditText nameZone;
    private Button addZone,yes,cancel;
    private boolean zoneCreated=false;
    private LinearLayout layout_list;
    private Dialog dialog;
    private TextView titleDialog,textDialog;
    private static  ArrayList<String> zone_list = new ArrayList<>();
    private ViewPager vpPager;

    public static ArrayList<String> getZone_list() {
        return zone_list;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_crea_zone_wizard, container, false);

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

        LoadPreferences();


        addZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameZone.getText().toString();
                if (!name.isEmpty()){
                    zoneCreated=true;
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
                        titleDialog.setText("Elimina Zona");
                        textDialog.setText("Sei sicuro di voler eliminare la Zona creata ?");

                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "Hai eliminato la zona", Toast.LENGTH_LONG).show();
                                layout_list.removeView(zoneView);
                                dialog.dismiss();
                                zone_list.remove(name);
                            }
                        });
                    }
                });
                nameZone.setText(null);

                }else{
                    nameZone.setError(getString(R.string.campo_obbligatorio));
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

            removeZone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    titleDialog.setText("Elimina Zona");
                    textDialog.setText("Sei sicuro di voler eliminare la Zona creata ?");

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "Hai eliminato la zona", Toast.LENGTH_LONG).show();
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