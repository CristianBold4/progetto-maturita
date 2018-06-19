package com.example.cristian.biblioteca.connection.struct.libri;


import com.example.cristian.biblioteca.entities.Libro;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Cristian on 13/04/2018.
 */

public interface ServiceLibri {


    @GET("boldrin/libri")
    Call<EmbeddedLibri> getEmbedded();

    @GET("boldrin/libri/{id}")
    Call<Libro> getLibroById(@Path(value = "id") String $oid);

    @POST("boldrin/libri")
    Call<Libro> updateScorta(@Body Libro libro);

    @POST("boldrin/libri")
    Call<Libro> postLibro(@Body Libro libro);

    @PUT("boldrin/libri/{id}")
    Call<Libro> editLibro(@Path("id") String id, @Body Libro libro);

    @DELETE("boldrin/libri/{id}")
    Call<Libro> deleteLibro(@Path(value = "id") String $oid);
}
