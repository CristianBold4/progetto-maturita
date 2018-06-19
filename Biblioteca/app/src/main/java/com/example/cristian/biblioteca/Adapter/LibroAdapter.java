package com.example.cristian.biblioteca.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.InformazioniLibro;
import com.example.cristian.biblioteca.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.ViewHolder> implements Filterable{

    private static ArrayList<Libro> libri;
    private ArrayList<Libro> libriFiltrati;
    private Context context;

    public LibroAdapter(Context context, ArrayList<Libro> libri) {
        this.context = context;
        this.libri = libri;
        this.libriFiltrati =  libri;
    }

    @Override
    public LibroAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_libro, parent, false);
        return new LibroAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(LibroAdapter.ViewHolder holder, int position) {
        System.out.println("POSTION ++++++++++++++++++" + position);
        final Libro libro = libriFiltrati.get(position);
        holder.setItem(libro);
    }

    @Override
    public int getItemCount() {
        return libriFiltrati.size();
    }

    @Override
    public Filter getFilter() {
        System.out.println("Returning a new Filter");
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    libriFiltrati = libri;
                } else {
                    ArrayList<Libro> filteredList = new ArrayList<>();
                    for (Libro l : libri) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (l.getTitolo().toLowerCase().contains(charString.toLowerCase())) {
                            System.out.println("match trovato");
                            filteredList.add(l);
                        }
                    }

                    libriFiltrati = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = libriFiltrati;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                libriFiltrati = (ArrayList<Libro>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView copertinaLibro;
        public TextView titolo;
        public TextView autore;
        public ProgressBar progressBar;
        private Libro libro;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titolo =  itemView.findViewById(R.id.titoloLibro);
            autore = itemView.findViewById(R.id.autoreLibro);
            copertinaLibro = itemView.findViewById(R.id.copertinaLibro);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.getIndeterminateDrawable().setColorFilter(0xff888888,
                        android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }

        public void setItem(Libro libro) {
            this.titolo.setText(libro.getTitolo());
            this.titolo.setTextColor(context.getResources().getColor(R.color.copie_non_disponibile));
            this.autore.setText("di " + libro.getAutore());
            Picasso.with(context).load(libro.getUrl())
                    .error(R.mipmap.ic_launcher)
                    .into(this.copertinaLibro, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {}

                        @Override
                        public void onError() {}
                    });
            this.progressBar.setVisibility(View.GONE);
            this.libro = libro;
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, InformazioniLibro.class);
            i.putExtra("uid", libro.getId().get$oid());
            context.startActivity(i);
        }
    }
}


