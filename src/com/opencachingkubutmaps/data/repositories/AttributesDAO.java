package com.opencachingkubutmaps.data.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import com.opencachingkubutmaps.data.sqlTables.AttributesTable;
import com.opencachingkubutmaps.domain.valueObject.CacheAttributeValue;

/**
 * Created by Jakub on 20.12.2015
 */
public class AttributesDAO {
    private static final String INSERT =
            "insert into " + AttributesTable.TABLE_NAME
                    + "(" + AttributesTable.AttributesColumns.ACODE + ", "
                    + AttributesTable.AttributesColumns.LANGUAGE + ", "
                    + AttributesTable.AttributesColumns.NAME + ") "
                    + "values (?, ?, ?)";

    private final SQLiteDatabase db;
    private final SQLiteStatement insertStatement;
    private final Context context;

    public AttributesDAO(final SQLiteDatabase db, final Context context){
        this.db = db;
        this.insertStatement = db.compileStatement(AttributesDAO.INSERT);
        this.context = context;
    }

    public void save(final CacheAttributeValue entity){
        this.insertStatement.clearBindings();
        this.insertStatement.bindString(1,entity.getAcode());
        this.insertStatement.bindString(2,entity.getLanguage());
        this.insertStatement.bindString(3,entity.getName());
    }

    @Nullable
    public CacheAttributeValue findByAcodeAndLanguage(final String acode, final String language){
        CacheAttributeValue attr = null;
        final Cursor cursor = this.db.query(
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
    private CacheAttributeValue buildCacheAttributeFromCursor(final Cursor cursor){
        CacheAttributeValue cacheAttributeValue = null;

        if(cursor != null){
            cacheAttributeValue = new CacheAttributeValue(this.context, cursor.getString(1));
            cacheAttributeValue.setName(cursor.getString(2));
            cacheAttributeValue.setLanguage(cursor.getString(3));
        }

        return cacheAttributeValue;
    }

}
