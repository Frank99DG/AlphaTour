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

        /*tabella Zone*/
        public static final String NAME_TABLE_ZONE="Zone";
        public static final String NAME_COLUMN_ZONE_ID="idZone";
        public static final String NAME_COLUMN_ZONE_ID_PLACE="idPlace";
        public static final String NAME_COLUMN_ZONE_NAME="nameZone";
        public static final String NAME_COLUMN_ZONE_LOAD="loadZone";

        /*tabella luoghi*/
        public static final String NAME_TABLE_PLACE="Place";
        public static final String NAME_COLUMN_PLACE_ID="idPlace";
        public static final String NAME_COLUMN_PLACE_NAME="namePlace";
        public static final String NAME_COLUMN_PLACE_CITY="cityPlace";
        public static final String NAME_COLUMN_PLACE_TYPOLOGY="typologyPlace";
        public static final String NAME_COLUMN_PLACE_LOAD="loadPlace";

        /*tabella elementi*/
        public static final String NAME_TABLE_ELEMENT="Element";
        public static final String NAME_COLUMN_ELEMENT_ID="idElement";
        public static final String NAME_COLUMN_ELEMENT_ID_ZONE="idZone";
        public static final String NAME_COLUMN_ELEMENT_NAME="titleElement";
        public static final String NAME_COLUMN_ELEMENT_DESCRIPTION="descriptionElement";
        public static final String NAME_COLUMN_ELEMENT_PHOTO="photoElement";
        public static final String NAME_COLUMN_ELEMENT_QR_CODE="qrCodeElement";
        public static final String NAME_COLUMN_ELEMENT_LOAD="loadElement";

        /*tabella constraints*/
        public static final String NAME_TABLE_CONSTRAINTS="Constraints";
        public static final String NAME_COLUMN_CONSTRAINTS_ID="idConstraints";
        public static final String NAME_COLUMN_CONSTRAINTS_FROM_ZONE="fromZoneConstraints";
        public static final String NAME_COLUMN_CONSTRAINTS_IN_ZONE="inZoneConstraints";
        public static final String NAME_COLUMN_CONSTRAINTS_LOAD="loadConstraints";
        /*tabella percorsi*/

    }
}
