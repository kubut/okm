package com.opencachingkubutmaps.data.dataManagers;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import com.opencachingkubutmaps.data.repositories.AttributesDAO;
import com.opencachingkubutmaps.data.services.OpenHelper;
import com.opencachingkubutmaps.domain.valueObject.CacheAttributeValue;

import java.util.ArrayList;

/**
 * Created by Jakub on 20.12.2015
 */
public class AttributesDM {
    public static final int VERSION = 1;
    public static final String NAME = "Attributes";

    private final AttributesDAO attributesDAO;

    public AttributesDM(final Context context){
        final SQLiteOpenHelper openHelper = new OpenHelper(context);

        this.attributesDAO = new AttributesDAO(openHelper.getWritableDatabase(), context);
    }

    @Nullable
    public CacheAttributeValue findByAcodeAndLanguage(final String acode, final String language){
        return this.attributesDAO.findByAcodeAndLanguage(acode, language);
    }

    public void addAttrs(final ArrayList<CacheAttributeValue> attrs){
        for(final CacheAttributeValue attr : attrs){
            this.attributesDAO.save(attr);
        }
    }
}
