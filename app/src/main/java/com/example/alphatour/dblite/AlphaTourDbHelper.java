package com.example.alphatour.dblite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlphaTourDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AlphaTour.db";




    public AlphaTourDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CommandBbAlphaTour.Command.CREATE_USER_TABLE); // create Table Utente(idUtente INTEGER  primary key,nomeUtente TEXT,cognomeUtente TEXT,dataNascitaUtente TEXT,usernameUtente TEXT,emailUtente TEXT)
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL(CommandBbAlphaTour.Command.DELETE_USER_TABLE); // drop Table if exists Utente
    }
}
