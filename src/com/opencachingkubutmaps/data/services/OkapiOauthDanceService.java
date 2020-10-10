package com.opencachingkubutmaps.data.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.service.OkapiService;
import com.opencachingkubutmaps.domain.service.PreferencesService;

public class OkapiOauthDanceService {
    public static void MakeDance(final Context context) {
        Toast.makeText(context, context.getString(R.string.oauth_in_progress), Toast.LENGTH_SHORT).show();

        final PreferencesService preferencesService = new PreferencesService(context);
        final String apiUrl = preferencesService.getServerAPI();

        final String consumerKey = OkapiService.getOkapiKey(context, apiUrl);
        final String consumerSecret = OkapiService.getOkapiSecret(context, apiUrl);

        new OkapiOauthAuthorizationUrlTask(context) {
            @Override
            public void onPostExecute(final String result) {
                if (result == null) {
                    Toast.makeText(context, context.getString(R.string.oauth_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                OpenPINDialog(context, consumerKey, consumerSecret);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(result));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
                .execute(consumerKey, consumerSecret);
    }

    private static void OpenPINDialog(final Context context, final String consumerKey, final String consumerSecret) {
        new MaterialDialog.Builder(context)
                .title(context.getString(R.string.oauth_PIN_title))
                .content(context.getString(R.string.oauth_PIN_subtitle))
                .positiveText(context.getString(R.string.ok))
                .negativeText(context.getString(R.string.cancel))
                .negativeColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(context.getString(R.string.oauth_PIN), null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        GetAccessToken(input.toString(), context, consumerKey, consumerSecret);
                    }
                })
                .show();
    }

    private static void GetAccessToken(String verifier, final Context context, String consumerKey, String consumerSecret) {
        new OkapiOauthAccessTokenTask(context) {
            @Override
            public void onPostExecute(final OAuth1AccessToken result) {
                if (result == null) {
                    Toast.makeText(context, context.getString(R.string.oauth_wrong_PIN), Toast.LENGTH_SHORT).show();
                    return;
                }

                PreferencesService preferencesService = new PreferencesService(context);
                preferencesService.setAccessToken(result.getToken(), result.getTokenSecret());
            }
        }
                .execute(verifier, consumerKey, consumerSecret);
    }
}
