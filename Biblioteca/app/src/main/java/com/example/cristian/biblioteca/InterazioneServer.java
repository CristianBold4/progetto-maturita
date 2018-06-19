package com.example.cristian.biblioteca;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.cristian.biblioteca.connection.struct.libri.EmbeddedLibri;
import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.connection.rf.RFClient;
import com.example.cristian.biblioteca.connection.struct.libri.ServiceLibri;
import com.example.cristian.biblioteca.connection.struct.prenotazioni.ServicePrenotazioni;
import com.example.cristian.biblioteca.entities.Prenotazione;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InterazioneServer {

    private ServicePrenotazioni servicePrenotazioni;
    private static ServiceLibri serviceLibri;

    private Context context;


    public InterazioneServer(Context context) {
        Log.d("Interazione Server", "create Retrofit Service");
        serviceLibri = RFClient.getClient().create(ServiceLibri.class);
        servicePrenotazioni = RFClient.getClient().create(ServicePrenotazioni.class);
        this.context = context;
    }

    public void deleteLibro(Libro libro){

        serviceLibri.deleteLibro(libro.getId().get$oid()).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                Toast.makeText(context, R.string.libro_eliminato, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) { }
        });
    }

    public void modifyLibro(Libro libro){
        serviceLibri.editLibro(libro.getId().get$oid(), libro).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) { }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) { }
        });
    }


    public void postLibro(Libro libro) {
        serviceLibri.postLibro(libro).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {

            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) { }
        });
    }


    public void postPrenotazione(Prenotazione p){
        servicePrenotazioni.insertPrenotazione(p).enqueue(new Callback<Prenotazione>() {
            @Override
            public void onResponse(Call<Prenotazione> call, Response<Prenotazione> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, R.string.prenotazione_effettuata_correttamente, Toast.LENGTH_SHORT).show();
                } else {
                    int statusCode = response.code();
                    Log.d("Contacting server", "Error " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<Prenotazione> call, Throwable t) { }
        });
    }

    public void updateScorta(Libro nuovo){
        serviceLibri.updateScorta(nuovo).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (!response.isSuccessful()){
                    int statusCode = response.code();
                    Log.d("Contacting server", "Error " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {}
        });
    }

}
