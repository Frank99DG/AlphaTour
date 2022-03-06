package com.example.alphatour.dblite;

import android.provider.BaseColumns;

public final class CommandDbAlphaTour {

    public CommandDbAlphaTour(){
        /*costruttore vuoto*/
    }

    public static abstract class Command {

        //PAROLE CHIAVI
        public static final String TIPO_TESTO="TEXT";
        public static final String TPO_INTEGER="INTEGER";
        public static final String PRIMARY_KEY="primary key";
        public static final String CREATE_TABLE="create Table";
        public static final String DROP_TABLE="drop Table if exists";
        public static final String SEPARATORE_STRINGA=",";
        public static final String SPACE=" ";

        /*creazione tabella utente*/
        public static final String CREATE_USER_TABLE=CREATE_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NOME_TABELLA_UTENTE+
                "("+AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_ID+SPACE+TPO_INTEGER+SPACE+PRIMARY_KEY+SEPARATORE_STRINGA+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_NOME+SPACE+TIPO_TESTO+SEPARATORE_STRINGA+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_COGNOME+SPACE+TIPO_TESTO+SEPARATORE_STRINGA+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_DATA_NASCITA+SPACE+TIPO_TESTO+SEPARATORE_STRINGA+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_USERNAME+SPACE+TIPO_TESTO+SEPARATORE_STRINGA+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_EMAIL+SPACE+TIPO_TESTO+")";
        //create table Utente(idUtente INTEGER primary key,nomeUtente TEXT,cognomeUtente TEXT,dataNascitaUtente TEXT,usernameUtente TEXT,emailUtente TEXT)

        /*eliminazione tabella utente*/
        public static final String DELETE_USER_TABLE=DROP_TABLE+SPACE+AlphaTourContract.AlphaTourEntry.NOME_TABELLA_UTENTE;
    }
}
