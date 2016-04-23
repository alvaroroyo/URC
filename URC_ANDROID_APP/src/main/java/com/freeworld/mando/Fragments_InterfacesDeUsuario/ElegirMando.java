package com.freeworld.mando.Fragments_InterfacesDeUsuario;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.freeworld.mando.Adapters.ElegirMandoAdapter;
import com.freeworld.mando.IMain;
import com.freeworld.mando.Mando;
import com.freeworld.mando.R;
import java.util.List;

/**
 * Created by Alvaro Royo on 08/12/2015.
 */
public class ElegirMando extends ListFragment {
    IMain mListener;

    List<String[]> listaMandos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        return inflater.inflate(R.layout.fragment_ui_elegirmando,container,false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IMain) {
            mListener = (IMain) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IMain");
        }
    }
    @Override
     public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IMain) {
            mListener = (IMain) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement IMain");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaMandos = mListener.getListControls(); //((Mando)getActivity()).listaMandos;
        setListAdapter( new ElegirMandoAdapter(getActivity(),R.layout.adapter_mandos_row,listaMandos,true));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try {
            mListener.setFragmentNewMando(listaMandos.get(position)[2]);
            //((Mando)getActivity()).setFragmentNewMando(listaMandos.get(position)[2]);
        }catch(Exception e){
            Log.e("onListItemClick","El fragment no se encuentra alojado en la actividad Mando");
        }
    }

}
