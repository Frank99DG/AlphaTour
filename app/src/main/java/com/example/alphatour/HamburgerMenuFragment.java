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
    private TextView name,surname,hamburger_home, hamburger_logout;
    private ImageView close_hamburger;
    private ConstraintLayout ignoreTabBar;
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

        hamburger_home = view.findViewById(R.id.shortcutHome);
        hamburger_logout = view.findViewById(R.id.shortcutLogout);
        closeLayout = view.findViewById(R.id.closeLayout);
        close_hamburger= view.findViewById(R.id.close_hamburger);
        ignoreTabBar= view.findViewById(R.id.ignoreTabBar);




        buttonClick();


        return view;
    }

    private void buttonClick(){

        hamburger_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getActivity(), DashboardActivity.class) );
            }
        });

        hamburger_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity( new Intent(getActivity(), LoginActivity.class) );
                Toast.makeText(getActivity(), "Ti sei disconnesso", Toast.LENGTH_LONG).show();
            }
        });


        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(HamburgerMenuFragment.this).commit();
            }
        });

        close_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(HamburgerMenuFragment.this).commit();
            }
        });

        ignoreTabBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

}