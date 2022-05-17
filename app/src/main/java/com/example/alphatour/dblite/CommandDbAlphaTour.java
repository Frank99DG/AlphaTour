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

        /**creazione tabella utente**/
        public static final String CREATE_USER_TABLE=CREATE_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER+
                "("+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID+SPACE+TYPE_INTEGER+SPACE+PRIMARY_KEY+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_NAME+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_SURNAME+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_DATE_BIRTH+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_USERNAME+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_EMAIL+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_IMAGE+SPACE+TYPE_TEXT+")";
        //create table Utente(idUtente INTEGER primary key,nomeUtente TEXT,cognomeUtente TEXT,dataNascitaUtente TEXT,usernameUtente TEXT,emailUtente TEXT)

        /**creazione tabella luogo**/
        public static final String CREATE_PLACE_TABLE=CREATE_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_PLACE+
                "("+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_ID+SPACE+TYPE_INTEGER+SPACE+PRIMARY_KEY+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_NAME+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_CITY+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_TYPOLOGY+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_LOAD+SPACE+TYPE_TEXT+")";

        /**creazione tabella zone**/
        public static final String CREATE_ZONE_TABLE=CREATE_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_ZONE+
                "("+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_ID+SPACE+TYPE_INTEGER+SPACE+PRIMARY_KEY+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_NAME+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_ID_PLACE+SPACE+TYPE_INTEGER+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_LOAD+SPACE+TYPE_TEXT+")";


        /**creazione tabella vincoli**/
        public static final String CREATE_CONSTRAINTS_TABLE=CREATE_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_CONSTRAINTS+
                "("+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_CONSTRAINTS_ID+SPACE+TYPE_INTEGER+SPACE+PRIMARY_KEY+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_CONSTRAINTS_FROM_ZONE+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_CONSTRAINTS_IN_ZONE+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_CONSTRAINTS_LOAD+SPACE+TYPE_TEXT+")";


        /**creazione tabella elementi**/
        public static final String CREATE_ELEMENT_TABLE=CREATE_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_ELEMENT+
                "("+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_ID+SPACE+TYPE_INTEGER+SPACE+PRIMARY_KEY+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_ID_ZONE+SPACE+TYPE_INTEGER+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_NAME+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_DESCRIPTION+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_PHOTO+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_QR_CODE+SPACE+TYPE_TEXT+COMMA+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ELEMENT_LOAD+SPACE+TYPE_TEXT+")";


        /**eliminazione tabella utente**/
        public static final String DELETE_USER_TABLE=DROP_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER;

        /**eliminazione tabella luogo**/
        public static final String DELETE_PLACE_TABLE=DROP_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_PLACE;

        /**eliminazione tabella **/
        public static final String DELETE_ZONE_TABLE=DROP_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_ZONE;

        /**eliminazione tabella **/
        public static final String DELETE_CONSTRAINTS_TABLE=DROP_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_CONSTRAINTS;

        /**eliminazione tabella utente**/
        public static final String DELETE_ELEMENT_TABLE=DROP_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_ELEMENT;

        //aggiornamento dati utente
        public static final String SELECT_USER_PROFILE=SELECT+SPACE+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID+SPACE+
                FROM+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER+SPACE+WHERE+SPACE+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_EMAIL+SPACE+EQUAL+SPACE+"?";

        /**recupero id luogo**/
        public static final String SELECT_ID_PLACE=SELECT+SPACE+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_ID+SPACE+
                FROM+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_PLACE+SPACE+WHERE+SPACE+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_PLACE_NAME+SPACE+EQUAL+SPACE+"?";

        /**recupero id zona **/
        public static final String SELECT_ID_ZONE=SELECT+SPACE+AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_ID+SPACE+
                FROM+SPACE+AlphaTourContract.AlphaTourEntry.NAME_TABLE_ZONE+SPACE+WHERE+SPACE+
                AlphaTourContract.AlphaTourEntry.NAME_COLUMN_ZONE_NAME+SPACE+EQUAL+SPACE+"?";
    }
}
