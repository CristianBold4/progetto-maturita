package com.example.cristian.biblioteca.navbar_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.cristian.biblioteca.ExpandableListAdapter;
import com.example.cristian.biblioteca.Home;
import com.example.cristian.biblioteca.InserisciLibroFragment;
import com.example.cristian.biblioteca.InterazioneServer;
import com.example.cristian.biblioteca.ModificaLibroFragment;
import com.example.cristian.biblioteca.NavDrawer;
import com.example.cristian.biblioteca.R;
import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.entities.Prenotazione;
import com.example.cristian.biblioteca.connection.rf.RFClient;
import com.example.cristian.biblioteca.connection.struct.libri.EmbeddedLibri;
import com.example.cristian.biblioteca.connection.struct.libri.ServiceLibri;
import com.example.cristian.biblioteca.connection.struct.mongo.Id;
import com.example.cristian.biblioteca.connection.struct.prenotazioni.EmbeddedPrenotazioni;
import com.example.cristian.biblioteca.connection.struct.prenotazioni.ServicePrenotazioni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZonaAdminActivity extends NavDrawer implements InserisciLibroFragment.OnItemInserted, ModificaLibroFragment.OnItemModified, ModificaLibroFragment.OnItemDeleted{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    List<Id> idLibriList = new ArrayList<>();

    private InterazioneServer interazioneServer = new InterazioneServer(ZonaAdminActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zona_admin);

        expListView = findViewById(R.id.list_view_prenotazioni);

        expListView.setOnGroupClickListener((expandableListView, view, i, l) -> {
            if (i == 2){
                showInserisciLibroFragment();
                return true;
            } else return false;
        });

        expListView.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
           if (i == 1){
               showModificaLibroFragment(i1);
               return true;
           } else return false;
        });

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add(getString(R.string.visualizza_prenotazioni));
        listDataHeader.add(getString(R.string.visualizza_archivio_libri));
        listDataHeader.add(getString(R.string.inserisci_nuovo_libro));

        // Adding child data

        listDataChild.put(listDataHeader.get(0), getPrenotazioni()); // Header, Child data
        listDataChild.put(listDataHeader.get(1), getArchivioLibri());
        listDataChild.put(listDataHeader.get(2), null);
    }

    public ArrayList<String> getArchivioLibri() {

        ArrayList<String> result = new ArrayList<>();
        ServiceLibri serviceLibri = RFClient.getClient().create(ServiceLibri.class);
        serviceLibri.getEmbedded().enqueue(new Callback<EmbeddedLibri>() {
            @Override
            public void onResponse(Call<EmbeddedLibri> call, Response<EmbeddedLibri> response) {
                if (response.isSuccessful()){
                    ArrayList<Libro> resp = new ArrayList<>(response.body().getEmbedded());
                    for (Libro l : resp){
                        String out = l.getCopie() + " x " + l.getTitolo() + ", " + l.getAutore() + ", "+ l.getEditore() + ", € " + l.getPrezzo();
                        result.add(out);
                        idLibriList.add(l.getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<EmbeddedLibri> call, Throwable t) { }
        });

        return result;
    }

    private void showInserisciLibroFragment() {
        FragmentManager fm = getSupportFragmentManager();
        InserisciLibroFragment editNameDialogFragment = new InserisciLibroFragment();
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    private void showModificaLibroFragment(int id) {
        ServiceLibri serviceLibri = RFClient.getClient().create(ServiceLibri.class);
        serviceLibri.getLibroById(idLibriList.get(id).get$oid()).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()){
                    Libro l = response.body();
                    FragmentManager fm = getSupportFragmentManager();
                    ModificaLibroFragment modificaLibroFragment = new ModificaLibroFragment();
                    modificaLibroFragment.setCurrentLibro(l);
                    modificaLibroFragment.show(fm, "fragment_edit_name");
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {

            }
        });

    }



    public ArrayList<String> getPrenotazioni() {

        ArrayList<String> result = new ArrayList<>();
        ServicePrenotazioni servicePrenotazioni = RFClient.getClient().create(ServicePrenotazioni.class);
        servicePrenotazioni.getEmbedded().enqueue(new Callback<EmbeddedPrenotazioni>() {
            @Override
            public void onResponse(Call<EmbeddedPrenotazioni> call, Response<EmbeddedPrenotazioni> response) {
                if (response.isSuccessful()) {
                    ArrayList<Prenotazione> listPrenotazioni = new ArrayList<>(response.body().getEmbedded());
                    for (Prenotazione p : listPrenotazioni) {
                        //getLibro
                        ServiceLibri serviceLibri = RFClient.getClient().create(ServiceLibri.class);
                        serviceLibri.getLibroById(p.getIdLibro().get$oid()).enqueue(new Callback<Libro>() {
                            @Override
                            public void onResponse(Call<Libro> call, Response<Libro> response) {
                                try {
                                    String out = p.getUtente() + " ha prenotato " + response.body().getTitolo() + " in data " + p.getData() + "\n";
                                    System.out.println("out: " + out);
                                    result.add(out);
                                }catch (NullPointerException e){ }
                            }

                            @Override
                            public void onFailure(Call<Libro> call, Throwable t) { }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<EmbeddedPrenotazioni> call, Throwable t) {
            }
        });

        return result;
    }

    @Override
    public void onItemInserted(Libro libro) {
        interazioneServer.postLibro(libro);
        Toast.makeText(ZonaAdminActivity.this, R.string.libro_inserito, Toast.LENGTH_LONG).show();
        prepareListData();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemModified(Libro libro) {
        interazioneServer.modifyLibro(libro);
        Toast.makeText(ZonaAdminActivity.this, R.string.libro_modificato, Toast.LENGTH_LONG).show();
        prepareListData();
        listAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemDeleted(Libro libro) {
        interazioneServer.deleteLibro(libro);
        prepareListData();
        listAdapter.notifyDataSetChanged();
    }
}



