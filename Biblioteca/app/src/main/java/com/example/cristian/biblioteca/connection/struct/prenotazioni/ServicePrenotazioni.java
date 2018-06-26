package com.example.cristian.biblioteca.connection.struct.prenotazioni;


import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.entities.Prenotazione;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Cristian on 13/04/2018.
 */

public interface ServicePrenotazioni {

    @GET("boldrin/prenotazioni")
    Call<EmbeddedPrenotazioni> getEmbedded();

    @GET("boldrin/{MailUtente}/prenotazioni/")
    Call<EmbeddedPrenotazioni> getPrenotazioneByUser(@Path(value = "MailUtente") String mail);


    @POST("boldrin/prenotazioni")
    Call<Prenotazione> insertPrenotazione(@Body Prenotazione p);

    @DELETE("boldrin/prenotazioni/{id}")
    Call<Prenotazione> deletePrenotazione(@Path(value = "id") String $oid);

}
