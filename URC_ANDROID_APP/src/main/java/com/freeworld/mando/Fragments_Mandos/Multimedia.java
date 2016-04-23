package com.freeworld.mando.Fragments_Mandos;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.freeworld.mando.Network.IServerSend;
import com.freeworld.mando.Network.ClientConnection;
import com.freeworld.mando.R;

/**
 * Created by Alvaro Royo on 08/12/2015.
 */
public class Multimedia extends Fragment implements View.OnClickListener,View.OnTouchListener,IServerSend {

    private ClientConnection connection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        connection = ClientConnection.getInstance();

        View view = inflater.inflate(R.layout.fragment_mando_multimedia,container,false);

        view.findViewById(R.id.multimedia_playpause).setOnClickListener(this);
        view.findViewById(R.id.multimedia_stop).setOnClickListener(this);
        view.findViewById(R.id.multimedia_shutdown).setOnClickListener(this);

        view.findViewById(R.id.multimedia_volDown).setOnTouchListener(this);
        view.findViewById(R.id.multimedia_volUp).setOnTouchListener(this);
        view.findViewById(R.id.multimedia_before).setOnTouchListener(this);
        view.findViewById(R.id.multimedia_after).setOnTouchListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.multimedia_playpause:
                connection.sendPackage(BTN_PLAYPAUSE);
                break;
            case R.id.multimedia_stop:
                connection.sendPackage(BTN_STOP);
                break;
            case R.id.multimedia_shutdown:
                AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                build.setTitle("Apagar");
                build.setMessage("Desea apagar el dispositivo servidor?");
                build.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        connection.sendPackage(BTN_SHUTDOWN);
                        connection.disconnect();
                        dialog.dismiss();
                    }
                });
                build.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = build.create();
                dialog.show();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                switch(v.getId()){
                    case R.id.multimedia_volDown:
                        connection.sendPackage(BTN_VOLDOWN_PRESS);
                        break;
                    case R.id.multimedia_volUp:
                        connection.sendPackage(BTN_VOLUP_PRESS);
                        break;
                    case R.id.multimedia_after:
                        connection.sendPackage(BTN_AFTER_PRESS);
                        break;
                    case R.id.multimedia_before:
                        connection.sendPackage(BTN_BEFORE_PRESS);
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                switch(v.getId()){
                    case R.id.multimedia_volDown:
                        connection.sendPackage(BTN_VOLDOWN_UP);
                        break;
                    case R.id.multimedia_volUp:
                        connection.sendPackage(BTN_VOLUP_UP);
                        break;
                    case R.id.multimedia_after:
                        connection.sendPackage(BTN_AFTER_UP);
                        break;
                    case R.id.multimedia_before:
                        connection.sendPackage(BTN_BEFORE_UP);
                        break;
                }
                break;
        }
        return true;
    }
}
