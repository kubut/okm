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
import com.opencachingkubutmaps.domain.service.PreferencesService;

public class OkapiOauthDanceService {
    public static void MakeDance(final Context context) {
        Toast.makeText(context, "Trwa autoryzacja...", Toast.LENGTH_SHORT).show();

        new OkapiOauthAuthorizationUrlTask() {
            @Override
            public void onPostExecute(final String result) {
                if (result == null) {
                    Toast.makeText(context, "Wystąpił błąd, sprawdź swoje połączenie z internetem", Toast.LENGTH_SHORT).show();
                    return;
                }

                OpenPINDialog(context);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(result));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
                .execute();
    }

    private static void OpenPINDialog(final Context context) {
        new MaterialDialog.Builder(context)
                .title("Podaj kod autoryzacyjny")
                .content("Podaj kod PIN który zobaczyłeś po nadaniu dostępu do aplikacji na stronie opencaching.pl")
                .positiveText("OK")
                .negativeText("Anuluj")
                .negativeColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Kod PIN", null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        GetAccessToken(input.toString(), context);
                    }
                })
                .show();
    }

    private static void GetAccessToken(String verifier, final Context context) {
        new OkapiOauthAccessTokenTask() {
            @Override
            public void onPostExecute(final OAuth1AccessToken result) {
                if (result == null) {
                    Toast.makeText(context, "Wystąpił błąd, być może nie masz internetu lub podałeś błędny kod", Toast.LENGTH_SHORT).show();
                    return;
                }

                PreferencesService preferencesService = new PreferencesService(context);
                preferencesService.setAccessToken(result.getToken(), result.getTokenSecret());
            }
        }
                .execute(verifier);
    }
}
