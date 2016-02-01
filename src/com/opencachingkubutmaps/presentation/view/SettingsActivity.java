package com.opencachingkubutmaps.presentation.view;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import com.opencachingkubutmaps.R;

/**
 * Created by kubut on 2015-08-05.
 */
public class SettingsActivity extends com.fnp.materialpreferences.PreferenceActivity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setTitle(R.string.drawer_settings);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.textColorPrimaryInactive), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);

        this.setPreferenceFragment(new SettingsFragment());
    }
}
