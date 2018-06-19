package com.example.cristian.biblioteca.connection.struct.libri;

import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.connection.struct.mongo.Etag;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Cristian on 13/04/2018.
 */

public class EmbeddedLibri {

    @SerializedName("_embedded")
    @Expose
    private List<Libro> embedded = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("_etag")
    @Expose
    private Etag etag;
    @SerializedName("_returned")
    @Expose
    private Integer returned;

    public List<Libro> getEmbedded() {
        return embedded;
    }

    public void setEmbedded(List<Libro> embedded) {
        this.embedded = embedded;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public Etag getEtag() {
        return etag;
    }

    public void setEtag(Etag etag) {
        this.etag = etag;
    }

    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return "EmbeddedPrenotazioni{" +
                "embedded=" + embedded +
                ", returned=" + returned +
                '}';
    }
}
