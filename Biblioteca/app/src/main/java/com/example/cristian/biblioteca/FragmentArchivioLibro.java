package com.example.cristian.biblioteca;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.biblioteca.RecyclerView.LibroAdapter;
import com.example.cristian.biblioteca.connection.rf.RFClient;
import com.example.cristian.biblioteca.connection.struct.libri.EmbeddedLibri;
import com.example.cristian.biblioteca.connection.struct.libri.ServiceLibri;
import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.navbar_activities.ArchivioLibriActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentArchivioLibro extends Fragment {

    private ArrayList<Libro> listLibri = new ArrayList<>();
    private LibroAdapter adapter;
    private RecyclerView recyclerView;

    private ServiceLibri mService;

    private TextView status;

    public FragmentArchivioLibro() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_archivio_libri, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        adapter = new LibroAdapter(getActivity(), listLibri);

        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);

        status = rootView.findViewById(R.id.status);

        getArchivioLibri();
        return rootView;
    }

    public void getArchivioLibri() {
        mService = RFClient.getClient().create(ServiceLibri.class);
        mService.getEmbedded().enqueue(new Callback<EmbeddedLibri>() {
            @Override
            public void onResponse(Call<EmbeddedLibri> call, Response<EmbeddedLibri> response) {
                if (response.isSuccessful()){
                    ArrayList<Libro> app = new ArrayList<>(response.body().getEmbedded());
                    if (app.isEmpty()){
                        status.setText(R.string.nessun_libro_in_archivio);
                    } else {
                        showArchivioLibri(app);
                        status.setText(R.string.archivio_libri);
                    }
                } else {
                    Log.d("Contacting server", "Error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EmbeddedLibri> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.error_server_non_raggiungibile, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showArchivioLibri(ArrayList<Libro> app) {
        this.listLibri.clear();
        this.listLibri.addAll(app);
        adapter.notifyDataSetChanged();
    }
}
