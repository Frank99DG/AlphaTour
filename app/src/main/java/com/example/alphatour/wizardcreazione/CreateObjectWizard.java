package com.example.alphatour.wizardcreazione;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alphatour.ActivityElementDetails;
import com.example.alphatour.AddElementActivity;
import com.example.alphatour.R;
import com.example.alphatour.oggetti.Element;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

public class CreateObjectWizard extends Fragment implements Step {

    private Button addElement,yes,cancel;
    private static LinearLayout layout_list;
    private Dialog dialog;
    private String zone;
    private static Bitmap qr;
    private static Uri ph;
    private static List<View> typology_list = new ArrayList<View>();
    private ArrayList<String> zone_list=new ArrayList<>();
    private TextView titleDialog,textDialog;

    public static LinearLayout getLayout_list() {
        return layout_list;
    }

    public static List<View> getTypology_list() {
        return typology_list;
    }

    private static ArrayList<String> element_list = new ArrayList<>();

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





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_crea_oggetti_wizard, container, false);
        addElement=view.findViewById(R.id.buttonAddElement);
        layout_list=view.findViewById(R.id.listElementLayout);

        zone_list=CreateZoneWizard.getZone_list();


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
                element = (Element) data.getSerializableExtra("element");
                zone = data.getStringExtra("zone");
                setPh(AddElementActivity.getPhoto());
                setQr(AddElementActivity.getQr());
                createdObject(element);
            }
        }else{
            if (data != null) {
                Element element = new Element();
                element = (Element) data.getSerializableExtra("element");
                zone = data.getStringExtra("zone");
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
                intent.putExtra("element",element);
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
}