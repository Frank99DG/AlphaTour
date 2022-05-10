package com.example.alphatour.wizardpercorso;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devzone.checkabletextview.CheckableTextView;
import com.devzone.checkabletextview.CheckedListener;
import com.example.alphatour.DashboardActivity;
import com.example.alphatour.NotificationCounter;
import com.example.alphatour.NotifyFragment;
import com.example.alphatour.R;
import com.example.alphatour.oggetti.MapZoneAndObject;
import com.example.alphatour.oggetti.Path;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.oggetti.ZoneChoosed;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Step4 extends Fragment implements Step, BlockingStep {

    public static List<String> zone_select = new ArrayList<>();
    public static List<String> oggetti_select = new ArrayList<>();
    private int i=0;
    private LinearLayout list_zoneRiepilogo;
    private Dialog dialog;
    private Button dialog_termina, dialog_dismiss;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private Path path;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step4, container, false);
        list_zoneRiepilogo = view.findViewById(R.id.list_zoneRiepilogo);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_step4);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_termina = dialog.findViewById(R.id.btn_termina);
        dialog_dismiss = dialog.findViewById(R.id.btn_dismiss);

        path=new Path();

        progressBar=view.findViewById(R.id.pathLoadingBar);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            for(i=0;i<zone_select.size();i++){
                View zone = getLayoutInflater().inflate(R.layout.row_add_zone_review_creazione_percorso, null, false);
                TextView textZone = (TextView) zone.findViewById(R.id.textZoneReview);
                ImageView deleteZone = zone.findViewById(R.id.deleteZone_review);

                zone.setId(i);

                deleteZone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        zone_select.remove(zone.getId());
                        list_zoneRiepilogo.removeView(zone);
                    }
                });

                textZone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getContext(), ReviewZoneSelected.class);
                        intent.putExtra("zone",textZone.getText().toString());
                        int index = zone.getId();
                        intent.putExtra("index", index);
                        startActivity(intent);

                    }
                });

                textZone.setText(zone_select.get(i));
                list_zoneRiepilogo.addView(zone);

            }
        }
        else{

        }
    }

    public static List<String> getZone_select() {
        return zone_select;
    }

    public static List<String> getOggetti_select() {
        return oggetti_select;
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

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        ReviewZoneSelected.getMap_review_object().clear();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        dialog.show();
        dialog_termina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savePath();
            }
        });
        dialog_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void savePath() {

        progressBar.setVisibility(View.VISIBLE);

        List<MapZoneAndObject> zoneAndObjectList= ReviewZoneSelected.getZoneAndObjectList();
        if(zoneAndObjectList.size()!=0) {

            MapZoneAndObject mapZoneAndOb = zoneAndObjectList.get(0);
            Path path=new Path();
           // path.setName(mapZoneAndOb.getName());
          //  path.setDescription(mapZoneAndOb.getDescription());
            for (int i = 0; i < zoneAndObjectList.size(); i++) {
                MapZoneAndObject mapZoneAndObject = zoneAndObjectList.get(i);
                ZoneChoosed zoneChoosed=new ZoneChoosed();
                zoneChoosed.setName(mapZoneAndObject.getZone());
                List<String> obj=mapZoneAndObject.getListObj();
                for (int j = 0; j< obj.size(); j++) {
                    zoneChoosed.setObjectChoosed(obj.get(j));
                }
                path.setZonePath(zoneChoosed);
            }
                db.collection("Path")
                        .add(path)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(),"Percorso creato con successo",Toast.LENGTH_SHORT).show();
                                moveToDashboard();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"Non è stato possibile creare il percorso",Toast.LENGTH_SHORT).show();
                    }
                });

        }else{
            List<MapZoneAndObject> zoneAndObjectListStep3= Step3.getZoneAndObjectList();
            MapZoneAndObject mapZoneAndObj = zoneAndObjectListStep3.get(0);
            Path path=new Path();
           // path.setName(mapZoneAndObj.getName());
          //  path.setDescription(mapZoneAndObj.getDescription());
            for (int i = 0; i < zoneAndObjectListStep3.size(); i++) {
                MapZoneAndObject mapZoneAndObject = zoneAndObjectListStep3.get(i);
                ZoneChoosed zoneChoosed=new ZoneChoosed();
                zoneChoosed.setName(mapZoneAndObject.getZone());
                //path.setZonePath(mapZoneAndObject.getZone());
                List<String> obj=mapZoneAndObject.getListObj();
                for (int j = 0; j< obj.size(); j++) {
                   // path.setObjectPath(obj.get(j));
                    zoneChoosed.setObjectChoosed(obj.get(j));
                }
                path.setZonePath(zoneChoosed);
            }

            db.collection("Path")
                    .add(path)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getContext(),"Percorso creato con successo",Toast.LENGTH_SHORT).show();
                            moveToDashboard();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Non è stato possibile creare il percorso",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void moveToDashboard() {
        int increment_notifyOnDashboard = NotificationCounter.getCount();
        increment_notifyOnDashboard++;
        NotificationCounter.setCount(increment_notifyOnDashboard);

        Step3.getMap_review().clear();
        ReviewZoneSelected.getMap_review_object().clear();
        list_zoneRiepilogo.removeAllViews();
        zone_select.clear();
        ReviewZoneSelected.getZoneAndObjectList().clear();

        DashboardActivity.setFirstZoneChosen(false);
        NotificationCounter.setSend_notify(true);
        Intent intent= new Intent(getContext(), DashboardActivity.class);
        startActivity(intent);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
    }

}