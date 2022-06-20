package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.oggetti.Element;
import com.example.alphatour.qrcode.GenerateQrCodeActivity;
import com.example.alphatour.wizardcreazione.CreateObjectWizard;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ElementDetailsActivity extends AppCompatActivity {

    private ImageView close,imagePhoto,imageQrCode;
    private EditText title,description;
    private FloatingActionButton photo,qrCode;
    private AutoCompleteTextView typology;
    private String zone,item;
    private static Bitmap qr;
    private static Uri ph,uri;
    LinearLayout layout;
    private Element element;
    private ArrayAdapter<String> adapterItems;
    List<Element> elementList=new ArrayList<Element>();
    private List<String> typology_list = new ArrayList<String>();
    private List<View> list = new ArrayList<View>();
    private boolean errorFlag=false,selected=false,save=false,permission=false,changed=false;
    private Dialog dialog;
    private TextView yesFinal,titleDialog,textDialog;
    private String[] Permission=new String[]{Manifest.permission.CAMERA};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_details);
        close=findViewById(R.id.closeDetailsElement);
        title=findViewById(R.id.inputTitleElement);
        description=findViewById(R.id.inputDescriptionElement);
        photo=findViewById(R.id.changePhotoElement);
        qrCode=findViewById(R.id.changeQrCodeElement);
        imagePhoto=findViewById(R.id.photoElement);
        imageQrCode=findViewById(R.id.qrCodeElement);
        typology = findViewById(R.id.inputOwnerZone);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        yesFinal = dialog.findViewById(R.id.btn_termina_permission);
        titleDialog = dialog.findViewById(R.id.titleDialog_permission);
        textDialog = dialog.findViewById(R.id.textDialog_permission);

        Intent intent=getIntent();
        zone=intent.getStringExtra("zone");
        typology_list=intent.getStringArrayListExtra("ZoneList");


        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,typology_list);
        typology.setAdapter(adapterItems);
        typology.setHint(zone);
        typology.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                typology.setError(null);
                selected=true;
                item = parent.getItemAtPosition(position).toString();
            }
        });

        elementList= CreateObjectWizard.getElementList();
        list= CreateObjectWizard.getTypology_list();
        layout=CreateObjectWizard.getLayout_list();


        for(int i=0;i<layout.getChildCount();i++){
            for(int j=0;j<list.size() ;j++){
            if(layout.getChildAt(i).equals(list.get(j))) {
                element = elementList.get(i);
                title.setText(element.getTitle());
                description.setText(element.getDescription());
                imagePhoto.setImageURI(element.getPhoto());
                setPh(element.getPhoto());
                imageQrCode.setImageBitmap(element.getQrCode());
                setQr(element.getQrCode());
            }
            }
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!save) {
                    list= CreateObjectWizard.getTypology_list();
                    list.remove(0);
                    finish();
                }
            }
        });
    }


    //per rimuovere il focus e la tastiera quando si clicca fuori dalla EditText e/o AutoCompleteTextView
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof AutoCompleteTextView) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
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



    public static Bitmap getQr() {
        return qr;
    }

    public void setQr(Bitmap qr) {
        this.qr = qr;
    }

    public static Uri getPh() {
        return ph;
    }

    public  void setPh(Uri ph) {
        this.ph = ph;
    }

    public void saveElement(View view) {

        save=true;
        String Title = title.getText().toString();
        String Description= description.getText().toString();
        // String Activity = activity.getText().toString();

        Element elementModified= new Element();
        if(Title.isEmpty()){
            title.setError(getString(R.string.required_field));
            title.requestFocus();
            errorFlag = true;
        }else{
            elementModified.setTitle(Title);
        }

        if(Description.isEmpty()){
            description.setError(getString(R.string.required_field));
            description.requestFocus();
            errorFlag = true;
        }else{
            elementModified.setDescription(Description);
        }


            /*if(Activity.isEmpty()){
                activity.setError(getString(R.string.campo_obbligatorio));
                activity.requestFocus();
                errorFlag = true;
            }else{
                element.setActivity(Activity);
            }*/



        if(errorFlag){
            return;
        }else{
            Intent intent=new Intent();
            intent.putExtra("title",elementModified.getTitle());
            intent.putExtra("description",elementModified.getDescription());
            if(selected) {
                intent.putExtra("zone", item);
            }else{
                intent.putExtra("zone", zone);
            }
            setResult(Activity.RESULT_OK,intent);
            View v=list.get(0);
            LinearLayout layout=CreateObjectWizard.getLayout_list();


            for(int i=0;i<elementList.size();i++) {

                if (elementList.get(i).equals(element)) {
                    elementList.remove(i);
                    CreateObjectWizard.setElementList(elementList);
                }
            }
            
            layout.removeView(v);
            list.remove(0);
            finish();
        }
    }

    public void changeQrCodeElement(View view) {

        Intent intent=new Intent(this, GenerateQrCodeActivity.class);
        startActivityForResult(intent,70);
        imageQrCode.setImageBitmap(GenerateQrCodeActivity.getBitmap());
        setQr(GenerateQrCodeActivity.getBitmap());
    }

    public void changePhotoElement(View view) {

        if (checkPermission()){
            if (ActivityCompat.shouldShowRequestPermissionRationale(ElementDetailsActivity.this, Manifest.permission.CAMERA)) {
                dialog.show();
                yesFinal.setText("OK");

                titleDialog.setText(R.string.permit_required);
                textDialog.setText(R.string.permission_text);
                textDialog.setTextColor(getResources().getColor(R.color.black));

                yesFinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        requestPermission();
                    }
                });
            } else {
                requestPermission();
            }
        }else{
            permission=true;
            ImagePicker.with(ElementDetailsActivity.this)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                    .start(20);
        }

        if(!permission){
            uri=Uri.parse("");
            setPh(uri);
        }

    }

    private boolean checkPermission(){

        for(String permission:Permission){
            if(ContextCompat.checkSelfPermission(getApplicationContext(), permission)== PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermission(){
        int permissionCode=200; //codice definito da me, servirà nel caso in cui serva controllare più permessi
        ActivityCompat.requestPermissions(this,Permission,permissionCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 20) {
            permission=true;
            uri = data.getData();
            if (uri != null) {
                changed=true;
                Toast.makeText(ElementDetailsActivity.this, R.string.upload_photo, Toast.LENGTH_LONG).show();
                imagePhoto.setImageURI(uri);
                setPh(uri);
            } else {
                Toast.makeText(ElementDetailsActivity.this, R.string.not_add_photo, Toast.LENGTH_LONG).show();
            }
            //salvataggio foto
        }else{
            imageQrCode.setImageBitmap(GenerateQrCodeActivity.getBitmap());
            setQr(GenerateQrCodeActivity.getBitmap());
            Toast.makeText(ElementDetailsActivity.this, R.string.qr_generation, Toast.LENGTH_LONG).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED &&
                grantResults[1]== PackageManager.PERMISSION_GRANTED) {

            ImagePicker.with(ElementDetailsActivity.this)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                    .start(20);

        }else{
            Toast.makeText(this,R.string.permission_denied,Toast.LENGTH_LONG).show();
            uri=Uri.parse("");
            setPh(uri);

        }
    }
}