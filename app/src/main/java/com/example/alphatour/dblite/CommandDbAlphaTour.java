package com.example.alphatour.dblite;

import android.provider.BaseColumns;

public final class CommandDbAlphaTour {

    public CommandDbAlphaTour(){
        /*costruttore vuoto*/
    }

    public static abstract class Command {

        //PAROLE CHIAVI
        public static final String TYPE_TEXT="TEXT";
        public static final String TYPE_INTEGER="INTEGER";
        public static final String PRIMARY_KEY="primary key";
        public static final String CREATE_TABLE="create Table";
        public static final String SELECT="select";
        public static final String WHERE="where";
        public static final String FROM="from";
        public static final String SELECT_ALL="select * from";
        public static final String VALUE="?";
        public static final String EQUAL="=";
        public static final String UPDATE_TABLE="update Table";
        public static final String DROP_TABLE="drop Table if exists";
        public static final String COMMA=",";
        public static final String SPACE=" ";

        /*creazione tabella utente*/
        public static final String CREATE_USER_TABLE=CREATE_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER+
                "("+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID+SPACE+TYPE_INTEGER+SPACE+PRIMARY_KEY+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_NAME+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_SURNAME+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_DATE_BIRTH+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_USERNAME+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_EMAIL+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_IMAGE+SPACE+TYPE_TEXT+")";
        //create table Utente(idUtente INTEGER primary key,nomeUtente TEXT,cognomeUtente TEXT,dataNascitaUtente TEXT,usernameUtente TEXT,emailUtente TEXT)

        /*eliminazione tabella utente*/
        public static final String DELETE_USER_TABLE=DROP_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER;

        //aggiornamento dati utente
        public static final String SELECT_USER_PROFILE=SELECT+SPACE+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID+SPACE+
                FROM+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER+SPACE+WHERE+SPACE+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_EMAIL+SPACE+EQUAL+SPACE+"?";
    }
}
