package com.example.alphatour;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HamburgerMenuFragment extends Fragment {


    private LinearLayout closeLayout;
    private TextView name,surname,shortcutHome, shortcutLogout;
    private ImageView close_hamburger;
    private ConstraintLayout headerLayout,shortcutLayout;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        idUser = user.getUid();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hamburger_menu, container, false);

        name = view.findViewById(R.id.nameProfile);
        surname = view.findViewById(R.id.surnameProfile);
        takeNameSurnameUser();

        shortcutHome = view.findViewById(R.id.shortcutHome);
        shortcutLogout = view.findViewById(R.id.shortcutLogout);
        closeLayout = view.findViewById(R.id.closeLayout);
        headerLayout= view.findViewById(R.id.headerLayout);
        shortcutLayout= view.findViewById(R.id.shortcutLayout);
        close_hamburger= view.findViewById(R.id.close_hamburger);




        buttonClick();


        return view;
    }


    private void takeNameSurnameUser(){

        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista di utenti (id)

                    for (DocumentSnapshot d : listDocument) {    //d è un id ciclato dalla lista
                        User user = d.toObject(User.class);

                        if(idUser.equals( d.getId() )){     //se l'id dell'utente loggato corrisponde all'id della lista
                            name.setText(user.getName());
                            surname.setText(user.getSurname());
                            break;
                        }
                    }
                }
            }
        });

    }



    private void buttonClick(){

        shortcutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getActivity(), DashboardActivity.class) );
            }
        });

        shortcutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadingBar.setVisibility(View.VISIBLE);
                auth.signOut();
                startActivity( new Intent(getActivity(), LoginActivity.class) );
                Toast.makeText(getActivity(), "Ti sei disconnesso", Toast.LENGTH_LONG).show();
                //loadingBar.setVisibility(View.GONE);
            }
        });


        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(HamburgerMenuFragment.this).commit();
            }
        });

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        shortcutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        close_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(HamburgerMenuFragment.this).commit();
            }
        });


    }

}