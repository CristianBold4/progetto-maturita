package com.example.cristian.biblioteca.RecyclerView;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.biblioteca.EmailSender;
import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.entities.Prenotazione;
import com.example.cristian.biblioteca.R;
import com.example.cristian.biblioteca.connection.rf.RFClient;
import com.example.cristian.biblioteca.connection.struct.libri.ServiceLibri;
import com.example.cristian.biblioteca.connection.struct.prenotazioni.ServicePrenotazioni;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PrenotazioneAdapter extends RecyclerView.Adapter<PrenotazioneAdapter.ViewHolder> {

    private static ArrayList<Prenotazione> prenotazioni;
    private Context context;

    public PrenotazioneAdapter(Context context, ArrayList<Prenotazione> prenotazioni) {
        this.context = context;
        this.prenotazioni = prenotazioni;
    }

    @Override
    public PrenotazioneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_prenotazione, parent, false);
        return new PrenotazioneAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(PrenotazioneAdapter.ViewHolder holder, int position) {
        Prenotazione prenotazione = prenotazioni.get(position);
        holder.setItem(prenotazione);
    }

    @Override
    public int getItemCount() {
        return prenotazioni.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView copertinaLibro;
        public TextView titolo;
        public TextView data;
        public Button button;
        public ProgressBar progressBar;
        private Prenotazione prenotazione;
        public Libro libro;

        public ViewHolder(View itemView) {
            super(itemView);
            titolo =  itemView.findViewById(R.id.titoloLibro);
            data = itemView.findViewById(R.id.data);
            copertinaLibro = itemView.findViewById(R.id.copertinaLibro);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            button = itemView.findViewById(R.id.delete);
            button.setOnClickListener(v -> {
                eliminaPrenotazione();
            });
            if(progressBar != null) {
                progressBar.getIndeterminateDrawable().setColorFilter(0xff888888,
                        android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }

        public void setItem(Prenotazione prenotazione) {
            this.prenotazione = prenotazione;
            ServiceLibri serviceLibri = RFClient.getClient().create(ServiceLibri.class);
            serviceLibri.getLibroById(prenotazione.getIdLibro().get$oid()).enqueue(new Callback<Libro>() {
                @Override
                public void onResponse(Call<Libro> call, Response<Libro> response) {
                    libro = response.body();
                    System.out.println(libro == null);
                    if (libro != null) {
                        titolo.setText(prenotazione.getNumeroCopie() + " x " + libro.getTitolo());
                        data.setText(libro.getAutore());
                        Picasso.with(context).load(libro.getUrl())
                                .error(R.mipmap.ic_launcher)
                                .into(copertinaLibro, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });

                    } else {
                        titolo.setText("Libro non più in archivio");
                    }

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Libro> call, Throwable t) {
                    Toast.makeText(context, R.string.error_server_non_raggiungibile, Toast.LENGTH_SHORT).show();
                }
            });

        }

        public void eliminaPrenotazione() {
            ServicePrenotazioni servicePrenotazioni = RFClient.getClient().create(ServicePrenotazioni.class);
            servicePrenotazioni.deletePrenotazione(prenotazione.getId().get$oid()).enqueue(new Callback<Prenotazione>() {
                @Override
                public void onResponse(Call<Prenotazione> call, Response<Prenotazione> response) {
                    Toast.makeText(context, "Prenotazione eliminata con successo", Toast.LENGTH_SHORT).show();
                    EmailSender.prenotazioneEliminata(context, prenotazione, libro);
                    prenotazioni.remove(prenotazione);
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Prenotazione> call, Throwable t) {

                }
            });


        }

    }
}


