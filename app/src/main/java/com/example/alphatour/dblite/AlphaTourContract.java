package com.example.alphatour.dblite;

import android.provider.BaseColumns;

public final class AlphaTourContract {

    public AlphaTourContract() {
        /*costruttore vuoto*/
    }

    public static abstract class AlphaTourEntry implements BaseColumns{
        /*tabella Utente*/
        public static final String COLUMN_NAME_NULLABLE="null";
        public static final String NOME_TABELLA_UTENTE="Utente";
        public static final String NOME_COLONNA_UTENTE_ID="idUtente";
        public static final String NOME_COLONNA_UTENTE_NOME="nomeUtente";
        public static final String NOME_COLONNA_UTENTE_COGNOME="cognomeUtente";
        public static final String NOME_COLONNA_UTENTE_DATA_NASCITA="dataNascitaUtente";
        public static final String NOME_COLONNA_UTENTE_USERNAME="usernameUtente";
        public static final String NOME_COLONNA_UTENTE_EMAIL="emailUtente";

    }
}
