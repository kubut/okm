package com.example.OKM.data.dataManagers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import com.example.OKM.data.repositories.AttributesDAO;
import com.example.OKM.data.services.OpenHelper;
import com.example.OKM.domain.valueObject.CacheAttributeValue;

import java.util.ArrayList;

/**
 * Created by Jakub on 20.12.2015.
 */
public class AttributesDM {
    private static final int VERSION = 1;
    private static final String NAME = "Attributes";

    private Context context;
    private SQLiteDatabase db;
    private AttributesDAO attributesDAO;

    public AttributesDM(Context context){
        this.context = context;

        SQLiteOpenHelper openHelper = new OpenHelper(this.context, AttributesDM.NAME, AttributesDM.VERSION);
        this.db = openHelper.getWritableDatabase();

        this.attributesDAO = new AttributesDAO(this.db, this.context);
    }

    @Nullable
    public CacheAttributeValue findByAcodeAndLanguage(String acode, String language){
        return this.attributesDAO.findByAcodeAndLanguage(acode, language);
    }

    public void addAttrs(ArrayList<CacheAttributeValue> attrs){
        for(CacheAttributeValue attr : attrs){
            this.attributesDAO.save(attr);
        }
    }
}
