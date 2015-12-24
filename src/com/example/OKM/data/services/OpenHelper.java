package com.example.OKM.data.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.OKM.data.sqlTables.AttributesTable;

/**
 * Created by Jakub on 20.12.2015.
 */
public class OpenHelper extends SQLiteOpenHelper {
    public OpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AttributesTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AttributesTable.onUpgrade(db, oldVersion, newVersion);
    }
}
