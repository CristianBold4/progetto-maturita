package com.example.cristian.biblioteca;

import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.connection.rf.RFClient;
import com.example.cristian.biblioteca.connection.struct.libri.ServiceLibri;
import com.example.cristian.biblioteca.connection.struct.prenotazioni.ServicePrenotazioni;

public class InterazioneServer {

    private ServicePrenotazioni servicePrenotazioni;
    private static ServiceLibri serviceLibri = RFClient.getClient().create(ServiceLibri.class);

    private static Libro l;

}
