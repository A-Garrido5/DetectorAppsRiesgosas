package com.example.adrin.detectorappsinseguras.controlador;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.adrin.detectorappsinseguras.Bienvenida;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionDetected {

    public static boolean hasActiveInternetConnection(Context c)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static int isNetworkAvailable(Context context)
    {
        if (hasActiveInternetConnection(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();

                if(urlc.getResponseCode() == 200)
                    return 1;
            } catch (IOException e) {
                System.out.println("No internet disponible");
                return -1;

            }
        } else {

            System.out.println("No internet disponible");
            return -1;



        }
        return 0;
    }
}