package com.example.cristian.biblioteca.entities;

import com.example.cristian.biblioteca.connection.struct.mongo.Id;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Cristian on 22/04/2018.
 */

public class Prenotazione {

    @SerializedName("_id")
    @Expose
    private Id id;
    @SerializedName("MailUtente")
    @Expose
    private String utente;
    @SerializedName("IdLibro")
    @Expose
    private Id idLibro;
    @SerializedName("NumeroCopie")
    @Expose
    private int numeroCopie;
    @SerializedName("Data")
    @Expose
    private String data;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public Id getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Id idLibro) {
        this.idLibro = idLibro;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getNumeroCopie() {
        return numeroCopie;
    }

    public void setNumeroCopie(int numeroCopie) {
        this.numeroCopie = numeroCopie;
    }

    public Prenotazione(String utente, Id idLibro, int numeroCopie, String data) {
        this.utente = utente;
        this.idLibro = idLibro;
        this.numeroCopie = numeroCopie;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", utente='" + utente + '\'' +
                ", idLibro=" + idLibro +
                ", numeroCopie=" + numeroCopie +
                ", data='" + data + '\'' +
                '}';
    }
}
