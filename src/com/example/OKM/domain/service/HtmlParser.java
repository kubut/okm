package com.example.OKM.domain.service;

import android.content.Context;

/**
 * Created by Jakub on 13.12.2015.
 */
public class HtmlParser {
    public static String parseHtml(String str, Context ctx){
        PreferencesService preferencesService = new PreferencesService(ctx);
        String url = preferencesService.getServerAPI();

        String o = str.replace("\n", "");
        o = o.replace("src=\"lib","src=\"http://www."+url+"/lib");
        o = o.replace("src=\"/images","src=\"http://www."+url+"/images");
        o = o.replace("src=\"/lib","src=\"http://www."+url+"/lib");
        o = o.replace("src=\"images","src=\"http://www."+url+"/images");

        o = o.replaceAll("<(.*?)(style=\"\\.*?\")(.*?)>", "<$1 $3>");
        o = o.replaceAll("<(.*?)(width=\"\\d*?\")(.*?)>", "<$1 $3>");
        o = o.replaceAll("<(.*?)(height=\"\\d*?\")(.*?)>", "<$1 style=\"max-width: 100%\" $3>");
        return o;
    }
}
