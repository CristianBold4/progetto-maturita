package com.example.cristian.biblioteca;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.cristian.biblioteca.navbar_activities.SignInActivity;


/**
 * Created by Cristian on 26/01/2018.
 */

public class Home extends NavDrawer  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignInActivity.isAdmin();
        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.richiesta_internet_desc)
                    .setTitle(R.string.richiesta_internet)
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> {});
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.messaggio_uscita)
                .setCancelable(false)
                .setPositiveButton(R.string.si, (dialog, id) -> Home.this.finish())
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
