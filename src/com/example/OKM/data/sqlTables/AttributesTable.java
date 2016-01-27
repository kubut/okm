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

    public static void onCreate(final SQLiteDatabase db){
        final String sb = "CREATE TABLE " + AttributesTable.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                AttributesColumns.ACODE + " TEXT, " +
                AttributesColumns.LANGUAGE + " TEXT, " +
                AttributesColumns.NAME + " TEXT " +
                ");";
        db.execSQL(sb);
    }

    public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion){
        if(oldVersion >= newVersion) {
            return;
        }

        db.execSQL("DROP TABLE IF EXIST " + AttributesTable.TABLE_NAME);
        AttributesTable.onCreate(db);
    }
}
