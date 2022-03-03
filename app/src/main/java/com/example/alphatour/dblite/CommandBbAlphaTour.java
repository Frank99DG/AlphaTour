package com.example.alphatour.dblite;

import android.provider.BaseColumns;

public final class CommandBbAlphaTour {

    public CommandBbAlphaTour(){
        /*costruttore vuoto*/
    }

    public static abstract class Command {

        public static final String TIPO_TESTO="TEXT";
        public static final String SEPARATORE_STRINGA=",";
        public static final String SPACE=" ";

        /*creazione tabella utente*/
        public static final String CREATE_USER_TABLE="create Table"+SPACE+AlphaTourContract.AlphaTourEntry.NOME_TABELLA_UTENTE+
                "("+AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_ID+" INTEGER primary key,"+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_NOME+SPACE+TIPO_TESTO+SEPARATORE_STRINGA+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_COGNOME+SPACE+TIPO_TESTO+SEPARATORE_STRINGA+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_DATA_NASCITA+SPACE+TIPO_TESTO+SEPARATORE_STRINGA+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_USERNAME+SPACE+TIPO_TESTO+SEPARATORE_STRINGA+
                AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_EMAIL+SPACE+TIPO_TESTO+")";
        //create table Utente(idUtente INTEGER primary key,nomeUtente TEXT,cognomeUtente TEXT,dataNascitaUtente TEXT,usernameUtente TEXT,emailUtente TEXT)

        /*eliminazione tabella utente*/
        public static final String DELETE_USER_TABLE="drop Table if exists"+AlphaTourContract.AlphaTourEntry.NOME_TABELLA_UTENTE;
    }
}
