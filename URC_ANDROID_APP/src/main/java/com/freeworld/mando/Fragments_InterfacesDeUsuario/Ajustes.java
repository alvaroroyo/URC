package com.freeworld.mando.Fragments_InterfacesDeUsuario;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.freeworld.mando.Adapters.ElegirMandoAdapter;
import com.freeworld.mando.Config;
import com.freeworld.mando.IMain;
import com.freeworld.mando.Mando;
import com.freeworld.mando.R;
import java.util.List;

/**
 * Created by Alvaro Royo on 07/12/2015.
 */
public class Ajustes extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    IMain mListener;

    Animation btnAnimationGood,btnAnimationFail;

    Config config;

    EditText device_name;

    Spinner mandoDefault,conexionDefault;

    ImageView btn_save;

    List<String[]> listaMandos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View vista = inflater.inflate(R.layout.fragment_ui_ajustes,container,false);

        config = Config.getInstance(getActivity());

        btnAnimationGood = AnimationUtils.loadAnimation(getActivity(),R.anim.btn_save_good); //Cargar la animación del boton guardar
        btnAnimationFail = AnimationUtils.loadAnimation(getActivity(),R.anim.btn_save_fail);

        //Nombre del dispositivo
        device_name = (EditText) vista.findViewById(R.id.et_ajustes_name);
        device_name.setText(config.getStringConfig(Config.DEVICE_NAME));

        //Mando por defecto
        listaMandos = mListener.getListControls(); //((Mando)getActivity()).listaMandos;
        mandoDefault = (Spinner) vista.findViewById(R.id.MandoDefaultSpinner);
        mandoDefault.setAdapter(new ElegirMandoAdapter(getActivity(),R.layout.adapter_mandos_row,listaMandos,true));
        mandoDefault.setOnItemSelectedListener(this);
        mandoDefault.setSelection(config.getIntConfig(Config.DEFAULT_CONTROLER));

        //Conexion por defecto
        conexionDefault = (Spinner) vista.findViewById(R.id.MandoDefaultConexion);
        conexionDefault.setAdapter(new ElegirMandoAdapter(getActivity(), R.layout.adapter_mandos_row, config.getServerList(), false));
        conexionDefault.setOnItemSelectedListener(this);
        conexionDefault.setSelection(config.getIntConfig(Config.DEFAULT_CONEXION));


        //Boton guardar
        btn_save = (ImageView) vista.findViewById(R.id.btn_ajustesSave);
        btn_save.setOnClickListener(this);

        return vista;
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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_ajustesSave:
                if(comprovaciones()){
                    btn_save.startAnimation(btnAnimationGood);
                    guardar();
                }else{
                    btn_save.startAnimation(btnAnimationFail);
                    Toast.makeText(getActivity(), getResources().getText(R.string.checkData), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.MandoDefaultSpinner:
                config.setIntConfig(Config.DEFAULT_CONTROLER,position);
                break;
            case R.id.MandoDefaultConexion:
                config.setIntConfig(Config.DEFAULT_CONEXION,position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /**
     * Check the info to save and return true if all is good
     * @return
     */
    private boolean comprovaciones(){
        boolean allGood = true;
        if(device_name.getText().length()<1){
            device_name.setBackgroundColor(Color.rgb(245, 215, 215));
            allGood = false;
        }else{
            device_name.setBackgroundColor(Color.WHITE);
        }
        return allGood;
    }

    /**
     * Save changes
     */
    private void guardar(){
        config.setStringConfig(Config.DEVICE_NAME,device_name.getText().toString());

        Toast.makeText(getActivity(), getResources().getText(R.string.dataSaved), Toast.LENGTH_SHORT).show();
    }
}
