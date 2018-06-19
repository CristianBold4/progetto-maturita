package com.example.cristian.biblioteca;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cristian.biblioteca.entities.Libro;
import com.example.cristian.biblioteca.navbar_activities.ZonaAdminActivity;

public class ModificaLibroFragment extends DialogFragment {

    private ModificaLibroFragment.OnItemModified listener;
    private ModificaLibroFragment.OnItemDeleted listenerDelete;

    private Libro current;

    public interface OnItemModified {
        void onItemModified(Libro libro);
    }

    public interface OnItemDeleted {
        void onItemDeleted(Libro libro);
    }

    public void setCurrentLibro(Libro l){
        this.current = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_modifica_libro, container);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ZonaAdminActivity activity = context instanceof ZonaAdminActivity ? (ZonaAdminActivity) context : null;
        try {
            listener = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

        ZonaAdminActivity activity1 = context instanceof ZonaAdminActivity ? (ZonaAdminActivity) context : null;
        try {
            listenerDelete = (OnItemDeleted) activity1;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        String titleText = getString(R.string.modifica_libro);

        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorAccent));

        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        alertDialog.setTitle(ssBuilder);
        //set params
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_modifica_libro, null);
        EditText titoloET = view.findViewById(R.id.editTitolo);
        titoloET.setText(current.getTitolo());
        EditText genereET = view.findViewById(R.id.editGenere);
        genereET.setText(current.getGenere());
        EditText autoreET = view.findViewById(R.id.editAutore);
        autoreET.setText(current.getAutore());
        EditText prezzoET = view.findViewById(R.id.editPrezzo);
        prezzoET.setText(current.getPrezzo().toString());
        EditText editoreET = view.findViewById(R.id.editEditore);
        editoreET.setText(current.getEditore());
        EditText etCopie = view.findViewById(R.id.editNCopie);
        etCopie.setText(current.getCopie() + "");
        EditText descrizioneET = view.findViewById(R.id.editDescrizione);
        descrizioneET.setText(current.getDescrizione());
        EditText urlET = view.findViewById(R.id.editURL);
        urlET.setText(current.getUrl());
        alertDialog.setView(view);

        alertDialog.setPositiveButton("OK",
                (dialog, whichButton) -> {

                    String titolo = ((EditText) getDialog().findViewById(R.id.editTitolo)).getText().toString();
                    String genere = ((EditText) getDialog().findViewById(R.id.editGenere)).getText().toString();
                    String autore = ((EditText) getDialog().findViewById(R.id.editAutore)).getText().toString();
                    String descrizione = ((EditText) getDialog().findViewById(R.id.editDescrizione)).getText().toString();
                    String prezzo = ((EditText) getDialog().findViewById(R.id.editPrezzo)).getText().toString();
                    String editore = ((EditText) getDialog().findViewById(R.id.editEditore)).getText().toString();
                    String url = ((EditText) getDialog().findViewById(R.id.editURL)).getText().toString();
                    String nCopie = ((EditText) getDialog().findViewById(R.id.editNCopie)).getText().toString();
                    if (titolo.equals("") || autore.equals("") || descrizione.equals("") || prezzo.equals("") || descrizione.equals("") || url.equals("") || editore.equals("")
                            || nCopie.equals("") || isTheSame(titolo, autore, descrizione, prezzo, editore, url, nCopie)) {
                        Toast.makeText(getContext(), R.string.errore_campi_vuoti, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        if (listener != null)
                            listener.onItemModified(new Libro(current.getId(), titolo, Double.parseDouble(prezzo), editore, url, descrizione, autore, Integer.parseInt(nCopie), genere));
                    }

                }
        );
        alertDialog.setNegativeButton("Elimina libro", (dialog, whichButton) -> {
            listenerDelete.onItemDeleted(current);
        });

        alertDialog.setNeutralButton("Annulla",
                (dialog, whichButton) -> dialog.dismiss()
        );

        return alertDialog.create();
    }

    public boolean isTheSame(String titolo, String autore, String desc, String prezzo, String editore, String url, String nCopie){ //non fa richiesta post se i dati sono gli stessi
        return (current.getTitolo().equals(titolo) && current.getAutore().equals(autore) && current.getDescrizione().equals(desc)
        && current.getPrezzo().equals(Double.parseDouble(prezzo)) && current.getEditore().equals(editore) && url.equals(current.getUrl()) && Integer.parseInt(nCopie) == (current.getCopie()));
    }
}
