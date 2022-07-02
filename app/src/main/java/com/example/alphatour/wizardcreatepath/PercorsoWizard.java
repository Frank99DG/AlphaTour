package com.example.alphatour.wizardcreatepath;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alphatour.mainUI.DashboardActivity;
import com.example.alphatour.R;
import com.example.alphatour.objectclass.MapZoneAndObject;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.StepAdapter;

import java.util.List;

public class PercorsoWizard extends AppCompatActivity {

    StepperLayout stepperLayout;
    Button button;
    private StepAdapter stepAdapter;
    private int i;
    private static String zone;
    private static boolean zonePassFromReview=false;
    private static String namePath,descriptionPath,place;
    private static int count=0;
    private Dialog dialog;
    private Button dialog_delete_path, dialog_dismiss;
    private TextView dialog_title,dialog_text;
    private ImageView dialog_delete_image;

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        PercorsoWizard.count = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorso_wizard);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_delete_path = dialog.findViewById(R.id.btn_okay);
        dialog_dismiss = dialog.findViewById(R.id.btn_cancel);
        dialog_title = dialog.findViewById(R.id.titleDialog);
        dialog_text = dialog.findViewById(R.id.textDialog);
        dialog_delete_image= dialog.findViewById(R.id.imageDialog);
        dialog_delete_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));


        dialog_title.setText("Attenzione");
        dialog_text.setText("Sei sicuro di voler tornare indietro? i dati inseriti durante la creazione del percorso verranno persi");
        dialog_delete_path.setText("Torna alla dashboard");

        dialog_delete_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToDashboard();
            }
        });

        dialog_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        stepperLayout=findViewById(R.id.stepperLayout);
       // button=findViewById(R.id.buttonShare);

        Intent intent=getIntent();

            i = intent.getIntExtra("val", count);

            if (count!=-1) {
                stepAdapter = new StepperAdapterWizard(getSupportFragmentManager(), getApplicationContext());
                stepperLayout.setAdapter(stepAdapter);
                stepperLayout.setCurrentStepPosition(count);
            } else {
                stepAdapter = new StepperAdapterWizard(getSupportFragmentManager(), getApplicationContext());
                stepperLayout.setAdapter(stepAdapter);
                stepperLayout.setCurrentStepPosition(i);
            }

            stepAdapter = new StepperAdapterWizard(getSupportFragmentManager(), getApplicationContext());
            stepperLayout.setAdapter(stepAdapter);
        }



    //per rimuovere il focus e la tastiera quando si clicca fuori dalla EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    public static boolean isZonePassFromReview() {
        return zonePassFromReview;
    }

    public static void setZonePassFromReview(boolean zonePassFromReview) {
        PercorsoWizard.zonePassFromReview = zonePassFromReview;
    }

    public static String getDescriptionPath() {
        return descriptionPath;
    }

    public static void setDescriptionPath(String descriptionPath) {
        PercorsoWizard.descriptionPath = descriptionPath;
    }

    public static String getNamePath() {
        return namePath;
    }

    public static void setNamePath(String namePath) {
        PercorsoWizard.namePath = namePath;
    }

    public static String getPlace() {
        return place;
    }

    public static void setPlace(String place) {
        PercorsoWizard.place = place;
    }

    public static String getZone() {
        return zone;
    }

    public static void setZone(String zone) {
        PercorsoWizard.zone = zone;
    }

    @Override
    public void onBackPressed() {

        dialog.show();

    }

    private void moveToDashboard(){

        PercorsoWizard.setCount(0);
        List<String> a = Step4.getZone_select();
        List<String> b = Step4.getOggetti_select();
        List<String> c = Step2.getArray_database();
        List<MapZoneAndObject> d = Step4.getZoneAndObjectList_();
        List<MapZoneAndObject> e = ReviewZoneSelected.getZoneAndObjectList();
        List<MapZoneAndObject> f = CreateJsonActivity.getZoneAndObjectListReviewPath();

        if(!a.isEmpty()) Step4.getZone_select().clear();
        if(!b.isEmpty()) Step4.getOggetti_select().clear();
        if(!c.isEmpty()) Step2.getArray_database().clear();
        if(!d.isEmpty()) Step4.getZoneAndObjectList_().clear();
        if(!e.isEmpty()) ReviewZoneSelected.getZoneAndObjectList().clear();
        if(!f.isEmpty()) CreateJsonActivity.getZoneAndObjectListReviewPath().clear();

        DashboardActivity.setFirstZoneChosen(false);
        PercorsoWizard.setDescriptionPath("");
        PercorsoWizard.setNamePath("");
        PercorsoWizard.setPlace(null);

        Intent intent= new Intent(PercorsoWizard.this, DashboardActivity.class);
        startActivity(intent);
    }
    /* public void ShareFile(View view) {

        String textMessage="send intent";
        Intent sendIntent=new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }*/
}