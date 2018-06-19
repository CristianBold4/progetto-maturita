package com.example.cristian.biblioteca.navbar_activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.biblioteca.Home;
import com.example.cristian.biblioteca.NavDrawer;
import com.example.cristian.biblioteca.R;
import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.RecyclerView.LibroAdapter;
import com.example.cristian.biblioteca.connection.rf.RFClient;
import com.example.cristian.biblioteca.connection.struct.libri.EmbeddedLibri;
import com.example.cristian.biblioteca.connection.struct.libri.ServiceLibri;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cristian on 25/05/2018.
 */

public class ArchivioLibriActivity extends NavDrawer {


    private ArrayList<Libro> listLibri = new ArrayList<>();
    private LibroAdapter adapter;
    private RecyclerView recyclerView;

    private ServiceLibri mService;

    private TextView status;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivio_libri);
        SignInActivity.isAdmin();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new LibroAdapter(this, listLibri);


        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        status = findViewById(R.id.status);

        getArchivioLibri();
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
                Toast.makeText(ArchivioLibriActivity.this, R.string.error_server_non_raggiungibile, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    public void showArchivioLibri(ArrayList<Libro> app) {
        this.listLibri.clear();
        this.listLibri.addAll(app);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
    }

}
