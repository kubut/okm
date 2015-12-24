package com.example.OKM.data.sqlTables;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Jakub on 20.12.2015.
 */
public final class AttributesTable{
    public static final String TABLE_NAME = "attributes";

    public static class AttributesColumns implements BaseColumns {
        public static final String ACODE = "acode";
        public static final String LANGUAGE = "language";
        public static final String NAME = "name";
    }

    public static void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + AttributesTable.TABLE_NAME + " (");
        sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
        sb.append(AttributesColumns.ACODE + " TEXT, ");
        sb.append(AttributesColumns.LANGUAGE + " TEXT, ");
        sb.append(AttributesColumns.NAME + " TEXT ");
        sb.append(");");
        db.execSQL(sb.toString());
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion >= newVersion) {
            return;
        }

        db.execSQL("DROP TABLE IF EXIST " + AttributesTable.TABLE_NAME);
        AttributesTable.onCreate(db);
    }
}
