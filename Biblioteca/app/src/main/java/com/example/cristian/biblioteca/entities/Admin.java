package com.example.cristian.biblioteca.entities;

import com.example.cristian.biblioteca.connection.struct.mongo.Id;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Cristian on 13/04/2018.
 */

public class Admin {

    @SerializedName("_id")
    @Expose
    private Id id;
    @SerializedName("Mail")
    @Expose
    private String mail;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                '}';
    }
}
