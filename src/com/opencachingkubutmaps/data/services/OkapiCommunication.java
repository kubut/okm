package com.opencachingkubutmaps.data.services;

import android.os.AsyncTask;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kubut on 2015-09-01.
 */
public class OkapiCommunication extends AsyncTask<String, Void, String> {
    @Override
     protected String doInBackground(final String... params) {
        String response = "";

        try{
            final URL url = new URL(params[0]);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            final InputStream in = new BufferedInputStream(connection.getInputStream());

            response = OkapiCommunication.convertStreamToString(in);
        } catch (final Exception e){
            e.printStackTrace();
        }

        return response;
    }

    private static String convertStreamToString(final InputStream is) {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
