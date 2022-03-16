package com.example.alphatour;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageProfileActivity extends AppCompatActivity {

    ImageView cover;
    CircleImageView profile;
    FloatingActionButton fab,changeProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_profile);

        cover=findViewById(R.id.coverImg);
        fab=findViewById(R.id.floatingButton);
        changeProfile=findViewById(R.id.changeProfile);
        profile=findViewById(R.id.profile_image);
    }

    public void pickerImage(View v){

        ImagePicker.with(this)
                //.galleryOnly() apre solo la galleria
                .crop()//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                .start(10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10) {
            Uri uri = data.getData();
            cover.setImageURI(uri);
        }else{
            Uri uri = data.getData();
            profile.setImageURI(uri);
        }
    }

    public void changeProfile(View v){

        ImagePicker.with(this)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                .start(20);
    }
}