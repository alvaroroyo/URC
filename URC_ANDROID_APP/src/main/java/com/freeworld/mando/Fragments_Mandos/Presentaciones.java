package com.freeworld.mando.Fragments_Mandos;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freeworld.mando.Network.IServerSend;
import com.freeworld.mando.Network.ClientConnection;
import com.freeworld.mando.R;

/**
 * Created by Alvaro Royo on 08/12/2015.
 */
public class Presentaciones extends Fragment implements View.OnClickListener,IServerSend{

    private ClientConnection connection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        connection = ClientConnection.getInstance();

        View view = inflater.inflate(R.layout.fragment_mando_pdf,container,false);

        view.findViewById(R.id.pdf_front).setOnClickListener(this);
        view.findViewById(R.id.pdf_back).setOnClickListener(this);

        view.findViewById(R.id.presentationWhite).setOnClickListener(this);
        view.findViewById(R.id.presentationBlack).setOnClickListener(this);
        view.findViewById(R.id.presentationEsc).setOnClickListener(this);
        view.findViewById(R.id.presentationStart).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.pdf_front:
                connection.sendPackage(BTN_NEXT);
                break;
            case R.id.pdf_back:
                connection.sendPackage(BTN_LAST);
                break;
            case R.id.presentationWhite:
                connection.sendPackage(BTN_PRESENTATION_WHITE);
                break;
            case R.id.presentationBlack:
                connection.sendPackage(BTN_PRESENTATION_BLACK);
                break;
            case R.id.presentationEsc:
                connection.sendPackage(BTN_PRESENTATION_ESC);
                break;
            case R.id.presentationStart:
                connection.sendPackage(BTN_PRESENTATION_START);
                break;
        }
    }
}
