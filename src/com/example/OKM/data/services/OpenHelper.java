package com.example.OKM.data.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.OKM.data.sqlTables.AttributesTable;

/**
 * Created by Jakub on 20.12.2015.
 */
public class OpenHelper extends SQLiteOpenHelper {
    public OpenHelper(final Context context) {
        super(context, com.example.OKM.data.dataManagers.AttributesDM.NAME, null, com.example.OKM.data.dataManagers.AttributesDM.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        AttributesTable.onCreate(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        AttributesTable.onUpgrade(db, oldVersion, newVersion);
    }
}
