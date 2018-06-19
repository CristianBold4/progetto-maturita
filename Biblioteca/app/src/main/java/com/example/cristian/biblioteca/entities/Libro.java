package com.example.cristian.biblioteca.entities;

import com.example.cristian.biblioteca.connection.struct.mongo.Id;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Cristian on 13/04/2018.
 */

public class Libro {

    @SerializedName("_id")
    @Expose
    private Id id;
    @SerializedName("Titolo")
    @Expose
    private String titolo;
    @SerializedName("Prezzo")
    @Expose
    private Double prezzo;
    @SerializedName("CodiceGenere")
    @Expose
    private int CodiceGenere;
    @SerializedName("Editore")
    @Expose
    private String editore;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Genere")
    @Expose
    private String genere;
    @SerializedName("Descrizione")
    @Expose
    private String descrizione;
    @SerializedName("Autore")
    @Expose
    private String autore;
    @SerializedName("Copie")
    @Expose
    private int copie;

    public Libro() {
    }

    public Libro(String titolo, Double prezzo, String editore, String url, String descrizione, String autore, int copie, String genere) {
        this.titolo = titolo;
        this.prezzo = prezzo;
        this.editore = editore;
        this.url = url;
        this.descrizione = descrizione;
        this.autore = autore;
        this.copie = copie;
        this.genere = genere;
    }

    public Libro(Id id, String titolo, Double prezzo, String editore, String url, String descrizione, String autore, int copie, String genere) {
        this.id = id;
        this.titolo = titolo;
        this.prezzo = prezzo;
        this.editore = editore;
        this.url = url;
        this.descrizione = descrizione;
        this.autore = autore;
        this.copie = copie;
        this.genere = genere;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getAutore() {
        return autore;
    }

    public int getCopie() {
        return copie;
    }

    public void setCopie(int copie) {
        this.copie = copie;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public int getCodiceGenere() { return CodiceGenere; }

    public String getEditore() { return editore; }

    public void setCodiceGenere(int codiceGenere) { CodiceGenere = codiceGenere; }

    public void setEditore(String editore) { this.editore = editore; }

    @Override
    public String toString() {
        return "Libro{" +
                "name='" + titolo + '\'' +
                ", prezzo=" + prezzo +
                '}';
    }
}
