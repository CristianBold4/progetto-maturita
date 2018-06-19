package com.example.cristian.biblioteca.connection.struct.admins;


import com.example.cristian.biblioteca.entities.Admin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Cristian on 13/04/2018.
 */

public interface ServiceAdmin {

    @GET("boldrin/admins")
    Call<EmbeddedAdmins> getEmbedded();

    @GET("boldin/admins/{mail}")
    Call<Admin> getAdminByMail(@Path(value = "mail") String mail);

    @GET("boldrin/admins")
    Call<Admin> getAdminBy(@Query(value = "filter", encoded = true) String query);
}
