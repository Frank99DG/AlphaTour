package com.example.alphatour.wizardcreazione;

import android.app.Dialog;
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
import com.example.alphatour.oggetti.Element;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

public class CreateZoneWizard extends Fragment implements Step {
    private int zoneNumber = 1;
    private EditText nameZone;
    private Button addZone;
    private boolean zoneCreated=false;
    private LinearLayout layout_list;
    private ArrayList<Element> element_list = new ArrayList<>();
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_crea_zone_wizard, container, false);

        addZone = view.findViewById(R.id.buttonAddZone);
        nameZone = view.findViewById(R.id.inputNomeZona);
        layout_list = view.findViewById(R.id.listaElementiLayout);

        dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;

        Button yes= dialog.findViewById(R.id.btn_okay);
        Button cancel= dialog.findViewById(R.id.btn_cancel);


        addZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameZone.getText().toString();
                if (!name.isEmpty()){
                    zoneCreated=true;
                    final View zoneView = getLayoutInflater().inflate(R.layout.row_add_zone, null, false);
                    ImageView removeZone = (ImageView) zoneView.findViewById(R.id.deleteZone);
                    TextView zone = (TextView) zoneView.findViewById(R.id.inputZone);
                    zone.setText(name);
                    layout_list.addView(zoneView);


                removeZone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.show();

                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "Hai eliminato la zona", Toast.LENGTH_LONG).show();
                                layout_list.removeView(zoneView);
                                dialog.dismiss();
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
}