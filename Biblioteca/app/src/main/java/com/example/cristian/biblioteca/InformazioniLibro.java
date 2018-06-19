package com.example.cristian.biblioteca;

/**
 * Created by Cristian on 03/04/2018.
 */

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
        import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
        import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
        import android.widget.Toast;

import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.entities.Prenotazione;
import com.example.cristian.biblioteca.connection.rf.RFClient;
        import com.example.cristian.biblioteca.connection.struct.libri.ServiceLibri;
import com.example.cristian.biblioteca.connection.struct.prenotazioni.ServicePrenotazioni;
import com.example.cristian.biblioteca.mailSender.provider.GMailSender;
import com.example.cristian.biblioteca.navbar_activities.VisualizzaPrenotazioniActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        import static android.widget.Toast.LENGTH_LONG;

public class InformazioniLibro extends NavDrawer {

    private ServiceLibri mService;

    protected Libro currentLibro = new Libro();

    protected String uid;
    protected Libro libro;
    protected ProgressBar progressBar;
    protected TextView nomeLibro;
    protected ImageView immagineProdotto;
    protected TextView descrizione;
    protected TextView prezzo;
    protected TextView autore;
    protected TextView disponibilita;
    protected Button prenotaLibro;
    private SeekBar mSeek;
    private TextView copieSelezionate;

    public InterazioneServer interazioneServer = new InterazioneServer(InformazioniLibro.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informazioni_libro);

        progressBar = findViewById(R.id.progressBar);
        if(progressBar != null) {
            progressBar.getIndeterminateDrawable().setColorFilter(0xff888888,
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        nomeLibro = findViewById(R.id.nome_prodotto);
        immagineProdotto =  findViewById(R.id.immagine_prodotto);
        descrizione = findViewById(R.id.descrizione_ridotta_prodotto);
        autore =  findViewById(R.id.autoreLibro);
        disponibilita = findViewById(R.id.disponibilita);
        mSeek =  findViewById(R.id.seekBar);
        copieSelezionate =  findViewById(R.id.curentValue);

        prenotaLibro = findViewById(R.id.prenota_libro);
        prenotaLibro.setOnClickListener(v -> confermaPrenotazione());
    }


    @Override
    protected void onStart() {
        super.onStart();
        libro = null;
        mostraContenitore(false);

        Bundle datiPassati = getIntent().getExtras();
        try{
            String uid = datiPassati.getString("uid").replaceAll("\r\n|\r|\n", "").trim();
            this.uid = uid;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.getApplicationContext(), R.string.errore_generico_fatale, LENGTH_LONG).show();
        }

        contactDB(uid);

    }

    public void contactDB(final String id) {
        mService = RFClient.getClient().create(ServiceLibri.class);
        mService.getLibroById(id).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()) {
                    currentLibro = response.body();
                    setPage();
                } else {
                    int statusCode = response.code();
                    Log.d("Contacting server", "Error " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                Toast.makeText(InformazioniLibro.this, R.string.error_server_non_raggiungibile, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setPage() {
        Picasso.with(this).load(currentLibro.getUrl())
                .error(R.mipmap.ic_launcher)
                .into(immagineProdotto, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {}
                });
        nomeLibro.setText(currentLibro.getTitolo());
        descrizione.setText(currentLibro.getDescrizione());
        autore.setText("Autore: " + currentLibro.getAutore());
        int copieDisponibili = currentLibro.getCopie();
        disponibilita.setText("Disponibilità: " + (copieDisponibili) + " rimanenti");
        copieSelezionate.setText("Copie selezionate: 1");
        mSeek.setProgress(1);
        upgradeCopieDisponibili(copieDisponibili);
        if (copieDisponibili == 0){
            disponibilita.setText("Copie esaurite. Riprova prossimamente");
            disponibilita.setTextColor(getResources().getColor(R.color.copie_non_disponibile));
            progressBar.setVisibility(View.GONE);

        } else {
            mostraContenitore(true);
            mSeek.setMax(copieDisponibili);
            mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    upgradeCopieDisponibili(copieDisponibili - progress);
                    copieSelezionate.setText("Copie selezionate: " + progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }


    }

    public void upgradeCopieDisponibili(int copieDisponibili) {
        if (copieDisponibili > 0) {
            disponibilita.setText("Disponibilità: " + (copieDisponibili) + " rimanenti");
            if (copieDisponibili > 10) {
                disponibilita.setTextColor(getResources().getColor(R.color.copie_disponibile));
            } else {
                disponibilita.setTextColor(getResources().getColor(R.color.copia_in_esaurimeneto));
            }
        } else {
            disponibilita.setText("Disponibilità: " + (copieDisponibili) + " rimanenti");
            disponibilita.setTextColor(getResources().getColor(R.color.copie_non_disponibile));
        }
    }


    protected void mostraContenitore(final boolean show){
        if(show) {
            prenotaLibro.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            mSeek.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.VISIBLE);
            prenotaLibro.setVisibility(View.GONE);
            mSeek.setVisibility(View.GONE);
        }
    }

    public void confermaPrenotazione(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (mSeek.getProgress()==0){
            Toast.makeText(InformazioniLibro.this, R.string.errore_copie_0, Toast.LENGTH_SHORT).show();
        }
        else {
            builder.setMessage(R.string.confermi_certificazione_domanda)
                    .setTitle(R.string.confermi_domanda)
                    .setPositiveButton(R.string.si, (dialog, id) -> {
                        Toast.makeText(InformazioniLibro.this, R.string.attendere_prego, Toast.LENGTH_LONG).show();
                        prenotaLibro.setEnabled(false);
                        Toast.makeText(getApplicationContext(), R.string.attendere_prego, Toast.LENGTH_SHORT).show();
                        String userMail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        Prenotazione prenotazione = new Prenotazione(userMail, currentLibro.getId(), mSeek.getProgress(), df.format(c));
                        certificaAcquisto(prenotazione);
                        updateScorta(currentLibro);
                        //send Email
                        EmailSender.prenotazioneConfermata(this, prenotazione, currentLibro);

                        Intent i = new Intent(getApplicationContext(), VisualizzaPrenotazioniActivity.class);
                        startActivity(i);
                        finish();
                    })
                    .setNegativeButton(R.string.no, (dialog, id) -> {
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    public void certificaAcquisto(Prenotazione prenotazione) {
        interazioneServer.postPrenotazione(prenotazione);
    }

    public void updateScorta(Libro l) {
        l.setCopie(l.getCopie() - 1);
        interazioneServer.updateScorta(l);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

}

