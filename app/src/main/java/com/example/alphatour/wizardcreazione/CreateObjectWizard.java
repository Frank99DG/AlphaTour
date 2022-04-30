package com.example.alphatour.wizardcreazione;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alphatour.ActivityElementDetails;
import com.example.alphatour.AddElementActivity;
import com.example.alphatour.DashboardActivity;
import com.example.alphatour.R;
import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Zone;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateObjectWizard extends Fragment implements Step, BlockingStep {

    private Button addElement,yes,cancel,yesFinal,cancelFinal;
    private static LinearLayout layout_list;
    private Dialog dialog;
    private String zone;
    private List<String> uriUploadPhoto=new ArrayList<String>();
    private List<String> uriUploadQrCode=new ArrayList<String>();
    private static Bitmap qr;
    private boolean success=false, created=false;
    private static Uri ph;
    private static List<View> typology_list = new ArrayList<View>();
    private static List<Element> elementList = new ArrayList<Element>();
    private ArrayList<String> zone_list=new ArrayList<>();
    private TextView titleDialog,textDialog;
    private ProgressBar loadingBar;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storegeProfilePick;
    private StorageTask uploadTask;
    private  Map<String, Object> elm = new HashMap<>();
    private ImageView imgDialog;

    public static LinearLayout getLayout_list() {
        return layout_list;
    }

    public static List<View> getTypology_list() {
        return typology_list;
    };

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

    public static List<Element> getElementList() {
        return elementList;
    }

    public static void setElementList(List<Element> elementList) {
        CreateObjectWizard.elementList = elementList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_crea_oggetti_wizard, container, false);
        addElement=view.findViewById(R.id.buttonAddElement);
        layout_list=view.findViewById(R.id.listElementLayout);
       // loadingBar=view.findViewById(R.id.objectLoadingBar);

        zone_list=CreateZoneWizard.getZone_list();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        storegeProfilePick= FirebaseStorage.getInstance().getReference();


        dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;

        yes= dialog.findViewById(R.id.btn_okay);
        cancel= dialog.findViewById(R.id.btn_cancel);
        yesFinal= dialog.findViewById(R.id.btn_okay);
        cancelFinal= dialog.findViewById(R.id.btn_cancel);
        yesFinal.setText("Crea Vincoli");
        titleDialog=dialog.findViewById(R.id.titleDialog);
        textDialog=dialog.findViewById(R.id.textDialog);
        imgDialog=dialog.findViewById(R.id.imageDialog);


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

        if (requestCode == 10){
            if (data != null) {
                created=true;
                Element element = new Element();

                element.setTitle(data.getStringExtra("title"));
                element.setDescription(data.getStringExtra("description"));
                element.setSensorCode(data.getStringExtra("sensor"));
                zone = data.getStringExtra("zone");
                element.setZoneRif(zone);
                setPh(AddElementActivity.getPhoto());
                setQr(AddElementActivity.getQr());
                createdObject(element);
            }
        }else{
            if (data != null) {
                created=true;
                Element element = new Element();

                element.setTitle(data.getStringExtra("title"));
                element.setDescription(data.getStringExtra("description"));
                element.setSensorCode(data.getStringExtra("sensor"));
                zone = data.getStringExtra("zone");
                element.setZoneRif(zone);
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
        element.setPhoto(getPh());
        element.setQrCode(getQr());
        elementList.add(element);

        removeElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                imgDialog.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));
                titleDialog.setText("Elimina Oggetto");
                textDialog.setText("Sei sicuro di voler eliminare l'oggetto creato ?");

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Hai eliminato l'oggetto", Toast.LENGTH_LONG).show();

                        for(int i=0;i<layout_list.getChildCount();i++){

                            if(layout_list.getChildAt(i).equals(elementView)){
                                elementList.remove(i);
                            }
                        }
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
                intent.putExtra("ZoneList",zone_list);
                intent.putExtra("zone",zone);
                startActivityForResult(intent,20);
            }
        });

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        VerificationError error=null;

        if(!created){
            error =new VerificationError("Devi creare almeno un oggetto !");
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

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        dialog.show();
        imgDialog.setImageDrawable(getResources().getDrawable(R.drawable.ic_cant_back));
        titleDialog.setText("Creazione Vincoli");
        textDialog.setText("Proseguendo con la creazione dei vincoli non sarà più possibile modificare zone e oggetti creati" +
                            " nella fase precedente. Vuoi proseguire ? ");

        yesFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.goToNextStep();

            }
        });

        cancelFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.dismiss();
            }
        });

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
    }


    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
            callback.goToPrevStep();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}