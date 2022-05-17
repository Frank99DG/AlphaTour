package com.example.alphatour.dblite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlphaTourDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "AlphaTour.db";




    public AlphaTourDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override //quando si modifica lo schema
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CommandDbAlphaTour.Command.CREATE_USER_TABLE); // create Table Utente(idUtente INTEGER  primary key,nomeUtente TEXT,cognomeUtente TEXT,dataNascitaUtente TEXT,usernameUtente TEXT,emailUtente TEXT)
        db.execSQL(CommandDbAlphaTour.Command.CREATE_PLACE_TABLE);
        db.execSQL(CommandDbAlphaTour.Command.CREATE_ZONE_TABLE);
        db.execSQL(CommandDbAlphaTour.Command.CREATE_CONSTRAINTS_TABLE);
        db.execSQL(CommandDbAlphaTour.Command.CREATE_ELEMENT_TABLE);



    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override //quando si modifica lo schema
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(CommandDbAlphaTour.Command.DELETE_USER_TABLE); // drop Table if exists Utente
        db.execSQL(CommandDbAlphaTour.Command.DELETE_PLACE_TABLE);
        db.execSQL(CommandDbAlphaTour.Command.DELETE_ZONE_TABLE);
        db.execSQL(CommandDbAlphaTour.Command.DELETE_CONSTRAINTS_TABLE);
        db.execSQL(CommandDbAlphaTour.Command.DELETE_ELEMENT_TABLE);

    }
}
