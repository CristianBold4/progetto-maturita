package com.example.cristian.biblioteca.navbar_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.biblioteca.Home;
import com.example.cristian.biblioteca.NavDrawer;
import com.example.cristian.biblioteca.R;
import com.example.cristian.biblioteca.entities.Prenotazione;
import com.example.cristian.biblioteca.Adapter.PrenotazioneAdapter;
import com.example.cristian.biblioteca.connection.rf.RFClient;
import com.example.cristian.biblioteca.connection.struct.prenotazioni.EmbeddedPrenotazioni;
import com.example.cristian.biblioteca.connection.struct.prenotazioni.ServicePrenotazioni;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cristian on 22/04/2018.
 */

public class VisualizzaPrenotazioniActivity extends NavDrawer {

    private ArrayList<Prenotazione> listPrenotazioni = new ArrayList<>();
    private PrenotazioneAdapter adapter;
    private RecyclerView recyclerView;

    private ServicePrenotazioni servicePrenotazioni;


    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_prenotazioni);
        SignInActivity.isAdmin();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new PrenotazioneAdapter(this, listPrenotazioni);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        status = findViewById(R.id.status);

        getPrenotazioni();

    }

    public void getPrenotazioni() {
        servicePrenotazioni = RFClient.getClient().create(ServicePrenotazioni.class);
        servicePrenotazioni.getEmbedded().enqueue(new Callback<EmbeddedPrenotazioni>() {
            @Override
            public void onResponse(Call<EmbeddedPrenotazioni> call, Response<EmbeddedPrenotazioni> response) {
                if (response.isSuccessful()){
                    ArrayList<Prenotazione> app = new ArrayList<>(response.body().getEmbedded());
                    if (app.isEmpty()){
                        status.setText(R.string.nessuna_prenotazione);
                    } else {
                        showPrenotazioni(app);
                        status.setText(R.string.lista_prenotazione);
                    }
                } else {
                    Log.d("Contacting server", "Error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EmbeddedPrenotazioni> call, Throwable t) {
                Toast.makeText(VisualizzaPrenotazioniActivity.this, R.string.error_server_non_raggiungibile, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showPrenotazioni(ArrayList<Prenotazione> prenotazioni){
        this.listPrenotazioni.clear();
        this.listPrenotazioni.addAll(prenotazioni);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
    }
}
