package com.example.alphatour.wizardpercorso;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
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

import com.example.alphatour.CreateJsonActivity;
import com.example.alphatour.DashboardActivity;
import com.example.alphatour.NotificationCounter;
import com.example.alphatour.R;
import com.example.alphatour.connection.Receiver;
import com.example.alphatour.dblite.AlphaTourContract;
import com.example.alphatour.dblite.AlphaTourDbHelper;
import com.example.alphatour.dblite.CommandDbAlphaTour;
import com.example.alphatour.oggetti.MapZoneAndObject;
import com.example.alphatour.oggetti.Path;
import com.example.alphatour.oggetti.ZoneChoosed;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Step4 extends Fragment implements Step, BlockingStep {

    private int i=0;
    private int c =0;
    private LinearLayout list_zoneRiepilogo;
    private Dialog dialog;
    private Button dialog_termina, dialog_dismiss,dialog_delete_btn,dialog_dismiss_btn;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private Path path;
    private ProgressBar progressBar;
    private static List<MapZoneAndObject> zoneAndObjectList_ = new ArrayList<MapZoneAndObject>();
    private static List<String> zone_select = new ArrayList<>();
    private static List<String> oggetti_select = new ArrayList<>();
    private static List<ZoneChoosed> zoneChooseds = new ArrayList<ZoneChoosed>();
    private static Graph<ZoneChoosed,DefaultEdge> graph=new SimpleGraph<>(DefaultEdge .class);
    private static int counter;
    private Dialog dialog_delete;
    private TextView dialog_title;
    private ImageView dialog_delete_image;
    private Receiver receiver;

    public static void setCounter(int counter) {
        Step4.counter = counter;
    }

    public static int getCounter() {
        return counter;
    }

    public static Graph<ZoneChoosed, DefaultEdge> getGraph() {
        return graph;
    }

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

        dialog_delete = new Dialog(getContext());
        dialog_delete.setContentView(R.layout.dialog_delete);
        dialog_delete.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog_delete.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_delete.setCancelable(false);
        dialog_delete.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_delete_btn = dialog_delete.findViewById(R.id.btn_okay);
        dialog_dismiss_btn = dialog_delete.findViewById(R.id.btn_cancel);
        dialog_title = dialog_delete.findViewById(R.id.textDialog);
        dialog_delete_image= dialog_delete.findViewById(R.id.imageDialog);


        path=new Path();

        progressBar=view.findViewById(R.id.pathLoadingBar);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /**controllo connessione**/
        receiver=new Receiver();

        broadcastIntent();
    }

    private void broadcastIntent() {
        requireActivity().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(receiver);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (isVisibleToUser) {

            counter=zone_select.size();
            for(i=0;i<zone_select.size();i++){
                View zone = getLayoutInflater().inflate(R.layout.row_zone_review_path, null, false);
                TextView textZone = (TextView) zone.findViewById(R.id.textZoneReview);
                ImageView deleteZone = zone.findViewById(R.id.deleteZone_review);

                zone.setId(i);

                deleteZone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_delete.show();
                        dialog_title.setText("Sei sicuro di voler eliminare la zona? Anche le zone successive a quella selezionata verranno eliminate");
                        dialog_delete_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));
                        dialog_delete_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int index = zone.getId();
                                int size = counter-index;
                                //zoneAndObjectList_.remove(zoneAndObjectList_.get(index));


                                //zoneAndObjectList_.get(zone.getId()).setZone("delete");
                                zoneAndObjectList_.subList(index,zone_select.size()).clear();
                                zone_select.subList(index,zone_select.size()).clear();
                                list_zoneRiepilogo.removeViews(index,size);
                                counter=counter-size;
                                dialog_delete.dismiss();


                                //list_zoneRiepilogo.removeView(zone);
                            }
                        });

                        dialog_dismiss_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_delete.dismiss();
                            }
                        });
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

        List<MapZoneAndObject> zoneAndObjectList= zoneAndObjectList_;

            MapZoneAndObject mapZoneAndOb = zoneAndObjectList.get(0);
            Path path=new Path();
            path.setName(mapZoneAndOb.getName());
            path.setDescription(mapZoneAndOb.getDescription());
            for (int i = 0; i < zoneAndObjectList_.size(); i++) {
                MapZoneAndObject mapZoneAndObject = zoneAndObjectList.get(i);
                //String delete = "delete";
               // if(!mapZoneAndObject.getZone().matches(delete)) {
                    ZoneChoosed zoneChoosed = new ZoneChoosed();
                    zoneChoosed.setName(mapZoneAndObject.getZone());

                    List<String> obj = mapZoneAndObject.getListObj();
                    for (int j = 0; j < obj.size(); j++) {
                        zoneChoosed.setObjectChoosed(obj.get(j));
                    }
                    graph.addVertex(zoneChoosed);
                    zoneChooseds.add(zoneChoosed);
                    if(i>=1){
                        graph.addEdge(zoneChooseds.get(i-1),zoneChooseds.get(i));
                    }
                    path.setZonePath(zoneChoosed);
               // }
            }
            if(receiver.isConnected()) {

                savePathRemote(path);
                savePathLocal(path);

            }else{

                savePathRemote(path);
                savePathLocal(path);
                moveToDashboard();
                progressBar.setVisibility(View.GONE);
            }
    }

    private void savePathRemote(Path path) {

        db.collection("Path")
                .add(path)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Percorso creato con successo", Toast.LENGTH_SHORT).show();
                        //clear_Zones();
                        moveToDashboard();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Non è stato possibile creare il percorso", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveToDashboard() {
        int increment_notifyOnDashboard = NotificationCounter.getCount();
        increment_notifyOnDashboard++;
        NotificationCounter.setCount(increment_notifyOnDashboard);

        list_zoneRiepilogo.removeAllViews();
        zone_select.clear();
        ReviewZoneSelected.getZoneAndObjectList().clear();


        DashboardActivity.setFirstZoneChosen(false);
        NotificationCounter.setSend_notify(true);
        Intent intent= new Intent(getContext(), CreateJsonActivity.class);
        //intent.putExtra("Arraylist",zoneAndObjectList_);
        startActivity(intent);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

        if (zone_select.size() != 0){
            for (int i = 0; i < zone_select.size(); i++) {
                if (i == zone_select.size() - 1) {
                    PercorsoWizard.setZone(zone_select.get(i));
                }
            }

            Intent intent = new Intent(getContext(), PercorsoWizard.class);
            intent.putExtra("val", 1);
            startActivity(intent);
            PercorsoWizard.setZonePassFromReview(true);
            DashboardActivity.setFirstZoneChosen(true);
       }else{
            PercorsoWizard.setZonePassFromReview(false);
            DashboardActivity.setFirstZoneChosen(false);
            Intent intent = new Intent(getContext(), PercorsoWizard.class);
            intent.putExtra("val", 1);
            startActivity(intent);
        }

    }

    public static List<MapZoneAndObject> getZoneAndObjectList_() {
        return zoneAndObjectList_;
    }

    public static void setZoneAndObjectList_(List<MapZoneAndObject> zoneAndObjectList_) {
        Step4.zoneAndObjectList_ = zoneAndObjectList_;
    }

    private void clear_Zones(){
        Iterator<MapZoneAndObject> it = zoneAndObjectList_.iterator();
        while(it.hasNext()){
            MapZoneAndObject zone = it.next();
            String delete="delete";
            if(delete.matches(zone.getZone())){
                it.remove();
            }
        }
    }

    public static List<String> getZone_select() {
        return zone_select;
    }

    public static List<String> getOggetti_select() {
        return oggetti_select;
    }

    public long savePathLocal(Path path){

        long newRowId=-1;
        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(getContext());
        SQLiteDatabase db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PATH_NAME,path.getName());
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PATH_DESCRIPTION,path.getDescription());
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PATH_LOAD,"false");
        newRowId=db.insert(AlphaTourContract.AlphaTourEntry.NAME_TABLE_PATH,AlphaTourContract.AlphaTourEntry.COLUMN_NAME_NULLABLE,values);

        SQLiteDatabase db1 = dbAlpha.getWritableDatabase();
        ContentValues values1 = new ContentValues();

        for(i=0;i<path.getZonePath().size();i++) {

            ZoneChoosed zoneChoosed=path.getZonePath().get(i);

            for(int j=0;j<zoneChoosed.getObjectChoosed().size();j++) {

                values1.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PATH_CONTAINS_ID_PATH, getIdPath(path.getName()));
                values1.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PATH_CONTAINS_ZONE, zoneChoosed.getName());
                values1.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PATH_CONTAINS_OBJECT, zoneChoosed.getObjectChoosed().get(j));
                values1.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PATH_CONTAINS_LOAD, "false");
                newRowId = db1.insert(AlphaTourContract.AlphaTourEntry.NAME_TABLE_PATH_CONTAINS, AlphaTourContract.AlphaTourEntry.COLUMN_NAME_NULLABLE, values1);
            }
        }

        return newRowId;
    }

    private String getIdPath(String name) {
        String idP="";

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(getContext());
        SQLiteDatabase db = dbAlpha.getReadableDatabase();

        Cursor cursor=db.rawQuery(CommandDbAlphaTour.Command.SELECT_ID_PATH,new String[]{name});
        if(cursor.moveToFirst()){
            idP= cursor.getString(cursor.getColumnIndexOrThrow(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PATH_ID));
        }
        return  idP;
    }
}