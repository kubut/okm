package com.example.OKM.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import com.example.OKM.data.sqlTables.AttributesTable;
import com.example.OKM.domain.valueObject.CacheAttributeValue;

/**
 * Created by Jakub on 20.12.2015.
 */
public class AttributesDAO {
    private static final String INSERT =
            "insert into " + AttributesTable.TABLE_NAME
                    + "(" + AttributesTable.AttributesColumns.ACODE + ", "
                    + AttributesTable.AttributesColumns.LANGUAGE + ", "
                    + AttributesTable.AttributesColumns.NAME + ") "
                    + "values (?, ?, ?)";

    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;
    private Context context;

    public AttributesDAO(SQLiteDatabase db, Context context){
        this.db = db;
        this.insertStatement = db.compileStatement(AttributesDAO.INSERT);
        this.context = context;
    }

    public long save(CacheAttributeValue entity){
        insertStatement.clearBindings();
        insertStatement.bindString(1,entity.getAcode());
        insertStatement.bindString(2,entity.getLanguage());
        insertStatement.bindString(3,entity.getName());

        return insertStatement.executeInsert();
    }

    public void update(CacheAttributeValue entity){
        final ContentValues values = new ContentValues();

        values.put(AttributesTable.AttributesColumns.ACODE, entity.getAcode());
        values.put(AttributesTable.AttributesColumns.LANGUAGE, entity.getLanguage());
        values.put(AttributesTable.AttributesColumns.NAME, entity.getName());

        db.update(AttributesTable.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[]{
                String.valueOf(entity.getId())
        });
    }

    @Nullable
    public CacheAttributeValue findByAcodeAndLanguage(String acode, String language){
        CacheAttributeValue attr = null;
        Cursor cursor = db.query(
                AttributesTable.TABLE_NAME,
                new String[]{
                    BaseColumns._ID,
                    AttributesTable.AttributesColumns.ACODE,
                    AttributesTable.AttributesColumns.NAME,
                    AttributesTable.AttributesColumns.LANGUAGE
                },
                AttributesTable.AttributesColumns.ACODE + " = ? and " + AttributesTable.AttributesColumns.LANGUAGE + " = ?",
                new String[] { acode, language },
                null,null, null, "1"
        );

        if(cursor.moveToFirst()){
            attr = this.buildCacheAttributeFromCursor(cursor);
        }
        if(!cursor.isClosed()){
            cursor.close();
        }

        return attr;
    }

    @Nullable
    private CacheAttributeValue buildCacheAttributeFromCursor(Cursor cursor){
        CacheAttributeValue cacheAttributeValue = null;

        if(cursor != null){
            cacheAttributeValue = new CacheAttributeValue(this.context, cursor.getLong(0), cursor.getString(1));
            cacheAttributeValue.setName(cursor.getString(2));
            cacheAttributeValue.setLanguage(cursor.getString(3));
        }

        return cacheAttributeValue;
    }

}
