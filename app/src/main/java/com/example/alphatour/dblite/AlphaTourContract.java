package com.example.alphatour.dblite;

import android.provider.BaseColumns;

public final class AlphaTourContract {

    public AlphaTourContract() {
        /*costruttore vuoto*/
    }

    public static abstract class AlphaTourEntry implements BaseColumns{
        /*tabella Utente*/
        public static final String COLUMN_NAME_NULLABLE="null";
        public static final String NAME_TABLE_USER="User";
        public static final String NAME_COLUMN_USER_ID="idUser";
        public static final String NAME_COLUMN_USER_NAME="nameUser";
        public static final String NAME_COLUMN_USER_SURNAME="surnameUser";
        public static final String NAME_COLUMN_USER_DATE_BIRTH="dateBirthUser";
        public static final String NAME_COLUMN_USER_USERNAME="usernameUser";
        public static final String NAME_COLUMN_USER_EMAIL="emailUser";
        public static final String NAME_COLUMN_USER_IMAGE="imageUser";

    }
}
