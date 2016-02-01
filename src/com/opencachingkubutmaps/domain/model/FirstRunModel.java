package com.opencachingkubutmaps.domain.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.service.PreferencesService;

/**
 * Created by Jakub on 30.01.2016
 */
public class FirstRunModel {
    private static final long TIMESTAMP = 1454163169;

    public static void showIfFirstTime(final Context context){
        final PreferencesService preferencesService = new PreferencesService(context);
        final long lastShowed = preferencesService.getLastRunTime();

        if(lastShowed < TIMESTAMP){
            showWelcome(context, preferencesService);
        }
    }

    private static void showWelcome(final Context context, final PreferencesService preferencesService){
        new MaterialDialog.Builder(context)
                .title(R.string.first_run_welcome_title)
                .content(R.string.first_run_welcome)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction action) {
                        dialog.dismiss();
                        showServerList(context, preferencesService);
                    }
                })
                .show();
    }

    private static void showServerList(final Context context, final PreferencesService preferencesService){
        new MaterialDialog.Builder(context)
                .title(R.string.first_run_select_server_title)
                .items(R.array.settings_servers_list_entries)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(final MaterialDialog dialog, final View view, final int i, final CharSequence text) {
                        preferencesService.setLastRunTime();
                        preferencesService.setServerName(text.toString());
                        return true;
                    }
                })
                .show();
    }
}
