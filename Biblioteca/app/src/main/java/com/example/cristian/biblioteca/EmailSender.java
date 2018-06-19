package com.example.cristian.biblioteca;

import android.content.Context;
import android.util.Log;

import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.entities.Prenotazione;
import com.example.cristian.biblioteca.mailSender.provider.GMailSender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailSender {

    public static void prenotazioneEliminata(Context context, Prenotazione prenotazione, Libro l) {
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            GMailSender sender = new GMailSender(context.getString(R.string.email_sender), Impostazioni.PSW_EMAIL);
            sender.sendMail("Prenotazione disdetta",
                    "Salve " + currentUser.getDisplayName() + ", vogliamo confermarLe che la sua prenotazione " + prenotazione.getNumeroCopie() + " x " + l.getTitolo() +
                            " è stata disdetta con successo. " +
                            "\n\nBiblioteca Zuccante.",
                    currentUser.getEmail(),
                    currentUser.getEmail());
        } catch (Exception e) {
            Log.d("Sending email", "Error mail");
        }
    }

    public static void prenotazioneConfermata(Context context, Prenotazione prenotazione, Libro l){
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            GMailSender sender = new GMailSender(context.getString(R.string.email_sender), Impostazioni.PSW_EMAIL);
            sender.sendMail("Prenotazione confermata",
                    "Salve " + currentUser.getDisplayName() + ", vogliamo confermarLe che la sua prenotazione è stata confermata. " +
                            "Ha 48 ore di tempo per ritirare " + prenotazione.getNumeroCopie() + " x " + l.getTitolo() + " nella nostra biblioteca." +
                            "\n\nBiblioteca Zuccante.",
                    currentUser.getEmail(),
                    currentUser.getEmail());
        } catch (Exception e) {
            Log.d("Sending email", "Error mail");
        }
    }
}
