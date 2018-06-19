package com.example.cristian.biblioteca.navbar_activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cristian.biblioteca.Home;
import com.example.cristian.biblioteca.InformazioniLibro;
import com.example.cristian.biblioteca.NavDrawer;
import com.example.cristian.biblioteca.R;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrReader extends NavDrawer implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;

    public int PERMESSO_VIDEOCAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_reader);
        SignInActivity.isAdmin();
    }

    public void QrScanner(View view){

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mScannerView != null) {
            mScannerView.stopCamera();   // Stop camera on pause
        }
    }

    @Override
    public void onStart(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                //Richiesta permesso
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMESSO_VIDEOCAMERA);

            } else {

                //Richiesta del permesso

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMESSO_VIDEOCAMERA);
            }
        }else{
            Button avvio = (Button) findViewById(R.id.avvio);
            if(avvio != null)
                avvio.performClick();
        }
        super.onStart();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMESSO_VIDEOCAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Button avvio = (Button) findViewById(R.id.avvio);
                if(avvio != null)
                    avvio.performClick();
            }else{
                Toast.makeText(this,"Permesso_fotocamera_richiesto",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),Home.class);
                this.startActivity(i);
            }
        }
    }


    @Override
    public void handleResult(Result rawResult) {

        //risultato della lettura:
        rawResult.getText();
        rawResult.getBarcodeFormat().toString();

        String[] result = rawResult.getText().split(" ");

        if(!result[0].equals("biblioteca2018_progetto_boldrin")){
            Toast.makeText(this, "Il codice non appartiene ad un nostro libro", Toast.LENGTH_SHORT).show();
        } else {
            //Passo il codice letto alla sezione "InformazioniLibro"
            Intent ris = new Intent(getBaseContext(), InformazioniLibro.class);
            ris.putExtra("uid", result[1]);
            startActivity(ris);
            finish();
        }
    }


    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(),Home.class);
        this.startActivity(i);
        finish();
    }


}


