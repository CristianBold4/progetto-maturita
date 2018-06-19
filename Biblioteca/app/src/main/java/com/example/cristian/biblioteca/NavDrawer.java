package com.example.cristian.biblioteca;

/**
 * Created by Cristian on 15/03/2018.
 */

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.cristian.biblioteca.navbar_activities.ArchivioLibriActivity;
import com.example.cristian.biblioteca.navbar_activities.QrReader;
import com.example.cristian.biblioteca.navbar_activities.SignInActivity;
import com.example.cristian.biblioteca.navbar_activities.VisualizzaPrenotazioniActivity;
import com.example.cristian.biblioteca.navbar_activities.ZonaAdminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class NavDrawer extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    private FrameLayout view_stub; //This is the framelayout to keep your content view
    private NavigationView navigation_view; // The new navigation view from Android Design Library. Can inflate menu resources.
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private static Menu drawerMenu;
    private static MenuItem login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.navigation_drawer);// The base layout that contains your navigation drawer.
        view_stub = (FrameLayout) findViewById(R.id.view_stub);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerMenu = navigation_view.getMenu();
        login = drawerMenu.findItem(R.id.googleSignIn);
        for(int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }
        setLoginText(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Override all setContentView methods to put the content view to the FrameLayout view_stub
     * so that, we can make other activity implementations looks like normal activity subclasses.
     */
    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setLoginText(FirebaseUser currentFirebaseUser) {
        if (currentFirebaseUser == null){
            login.setTitle("Sign in");
        } else {
            System.out.println(currentFirebaseUser.getDisplayName());
            login.setTitle("Sign Out");
        }
    }

    public static void setActiveAdmin(boolean isAdmmin){
        if (isAdmmin){
            drawerMenu.findItem(R.id.zonaAdmin).setVisible(true);
        } else {
            drawerMenu.findItem(R.id.zonaAdmin).setVisible(false);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        switch (id) {
            case  R.id.scanBook :
                if (currentFirebaseUser == null) {
                    alertDialogLogin();
                } else {
                    i = new Intent(getApplicationContext(), QrReader.class);
                    startActivity(i);
                    finish();
                }

                break;
            case R.id.archivioLibri :
                if (currentFirebaseUser == null){
                    alertDialogLogin();
                } else {
                    i = new Intent(getApplicationContext(), ArchivioLibriActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
            case  R.id.googleSignIn :
                i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
                finish();
                break;
            case  R.id.miePrenotazioni :
                if (currentFirebaseUser == null){
                    alertDialogLogin();
                }
                else {
                    i = new Intent(getApplicationContext(), VisualizzaPrenotazioniActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), R.string.attendere_prego, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.location :
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(Impostazioni.MY_LOCATION));
                startActivity(i);
                break;
            case R.id.zonaAdmin :
                i = new Intent(getApplicationContext(), ZonaAdminActivity.class);
                startActivity(i);
                finish();
                break;
        }

        return true;
    }

    public void alertDialogLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.richiesta_sign_in)
                .setTitle(R.string.titolo_sign_in)
                .setPositiveButton(R.string.si, (dialog, which) -> {
                    Intent signin = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(signin);
                    finish();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> { });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
