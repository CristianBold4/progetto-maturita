package com.example.cristian.biblioteca;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

public class InserisciLibroFragment extends android.support.v4.app.DialogFragment {

    private OnItemInserted listener;

    public interface OnItemInserted {
        void onItemInserted(Libro libro);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_inserisci_libro, container);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ZonaAdminActivity activity = context instanceof ZonaAdminActivity ? (ZonaAdminActivity) context : null;
        try {
            listener = (OnItemInserted) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        String titleText = getString(R.string.inserisci_nuovo_libro);

        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorAccent));

        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        alertDialog.setTitle(ssBuilder);
        alertDialog.setView(R.layout.dialog_fragment_inserisci_libro);
        alertDialog.setPositiveButton("OK",
                (dialog, whichButton) -> {

                        try {
                            String titolo = ((EditText) getDialog().findViewById(R.id.etTitolo)).getText().toString();
                            String autore = ((EditText) getDialog().findViewById(R.id.etAutore)).getText().toString();
                            String descrizione = ((EditText) getDialog().findViewById(R.id.etDescrizione)).getText().toString();
                            String prezzo = ((EditText) getDialog().findViewById(R.id.etPrezzo)).getText().toString();
                            String editore = ((EditText) getDialog().findViewById(R.id.etEditore)).getText().toString();
                            String genere = ((EditText) getDialog().findViewById(R.id.editGenere)).getText().toString();
                            String url = ((EditText) getDialog().findViewById(R.id.etURL)).getText().toString();
                            String nCopie = ((EditText) getDialog().findViewById(R.id.etNCopie)).getText().toString();
                            if (titolo.equals("") || autore.equals("") || descrizione.equals("") || prezzo.equals("") || descrizione.equals("") || url.equals("") || editore.equals("")
                                    || nCopie.equals("")) {
                                Toast.makeText(getContext(), R.string.errore_campi_vuoti, Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                if (listener != null)
                                    listener.onItemInserted(new Libro(titolo, Double.parseDouble(prezzo), editore, url, descrizione, autore, Integer.parseInt(nCopie), genere));
                            }

                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Campi non inseriti correttamente", Toast.LENGTH_SHORT).show();
                        }


                }
        );
        alertDialog.setNegativeButton("Annulla",
                (dialog, whichButton) -> dialog.dismiss()
        );
        return alertDialog.create();
    }

}

