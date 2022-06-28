package com.example.alphatour.wizardcreateplace;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alphatour.R;
import com.example.alphatour.objectclass.Element;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateObjectWizard extends Fragment implements Step, BlockingStep, StepperLayout.StepperListener {


    private Button addElement,yes,cancel,yesFinal,cancelFinal;
    private static LinearLayout layout_list;
    private Dialog dialog;
    private String zone;
    private static Bitmap qr;
    private boolean success=false, created=false, error1 =false;
    private static Uri ph;
    private static List<View> typology_list = new ArrayList<View>();
    private static List<Element> elementList = new ArrayList<Element>();
    private ArrayList<String> zone_list=new ArrayList<>();
    private TextView titleDialog,textDialog;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storegeProfilePick;
    private  Map<String, Object> elm = new HashMap<>();
    private ImageView imgDialog;

    public static LinearLayout getLayout_list() {
        return layout_list;
    }

    public static void setLayout_list(LinearLayout layout_list) {
        CreateObjectWizard.layout_list = layout_list;
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
        View view= inflater.inflate(R.layout.fragment_create_element_wizard, container, false);
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

        LoadPreferences();

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
                element.setQrData(data.getStringExtra("data"));
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
                element.setQrData(data.getStringExtra("data"));
                zone = data.getStringExtra("zone");
                element.setZoneRif(zone);
                setPh(ElementDetailsActivity.getPh());
                setQr(ElementDetailsActivity.getQr());
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
        if(!element.getSaved()){
            element.setPhoto(getPh());
            element.setQrCode(getQr());
        }
        elementList.add(element);

        removeElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                imgDialog.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));
                titleDialog.setText(R.string.delete_object);
                textDialog.setText(R.string.delete_object_text);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), R.string.deleted_object, Toast.LENGTH_LONG).show();

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
                 Boolean flag=false;
                for(int j=0;j<typology_list.size();j++){
                    if(typology_list.get(j).equals(elementView)){
                        flag=true;
                    }

                }

                if(!flag){
                    typology_list.add(elementView);
                }

                Intent intent =new Intent(getContext(), ElementDetailsActivity.class);
                intent.putExtra("ZoneList",zone_list);
                intent.putExtra("zone",element.getZoneRif());
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
    public void onCompleted(View completeButton) {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(),error.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        dialog.show();
        imgDialog.setImageDrawable(getResources().getDrawable(R.drawable.ic_cant_back));
        titleDialog.setText(R.string.constraint_creation);
        textDialog.setText(R.string.constraint_creation_text);

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
        SavePreferences();
    }

    private void SavePreferences(){
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(int i=0;i<elementList.size();i++) {
            if (!elementList.get(i).getSaved()){
                elementList.get(i).setSaved(true);

                editor.putBoolean("Saved" + i, elementList.get(i).getSaved());
                editor.putString("Zone" + i, elementList.get(i).getZoneRif());
                editor.putString("Title" + i, elementList.get(i).getTitle());
                editor.putString("Description" + i, elementList.get(i).getDescription());
                if(elementList.get(i).getPhoto().toString()!=null) {
                    editor.putString("Photo" + i,elementList.get(i).getPhoto().toString());
                }
                editor.putString("QrData" + i, elementList.get(i).getQrData());
                String qr = convertBitmapToString(elementList.get(i).getQrCode());
                editor.putString("QrCode" + i, qr);
            }
        }
        editor.putInt("elementListSize",elementList.size());
        //editor.putInt("layout",getLayout_list());
        editor.commit();
    }

    private String convertBitmapToString(Bitmap qrCode) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrCode.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);
        return encoded;
    }


    private Bitmap convertStringToBitmap(String encoded) {

        byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        Bitmap bitmap=BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        return bitmap;
    }

    private void LoadPreferences(){

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int size=sharedPreferences.getInt("elementListSize",0);
        elementList.clear();

        for(int i=0;i<size;i++){
            created=true;
            Element element=new Element();
            element.setSaved(sharedPreferences.getBoolean("Saved"+i,true));
            element.setZoneRif(sharedPreferences.getString("Zone"+i,""));
            element.setTitle(sharedPreferences.getString("Title"+i,""));
            element.setDescription(sharedPreferences.getString("Description"+i,""));
            element.setPhoto(Uri.parse(sharedPreferences.getString("Photo"+i,"")));
            element.setQrData(sharedPreferences.getString("QrData"+i,""));
            Bitmap bitmap=convertStringToBitmap(sharedPreferences.getString("QrCode"+i,""));
            element.setQrCode(bitmap);
            createdObject(element);

        }
    }

}