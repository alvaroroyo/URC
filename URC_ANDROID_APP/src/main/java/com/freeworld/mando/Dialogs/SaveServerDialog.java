package com.freeworld.mando.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freeworld.mando.Adapters.ElegirMandoAdapter;
import com.freeworld.mando.Config;
import com.freeworld.mando.Fragments_InterfacesDeUsuario.SavedServers;
import com.freeworld.mando.R;

/**
 * Created by aroyo on 11/01/2016.
 */
public class SaveServerDialog extends Dialog implements android.view.View.OnClickListener {
    Activity activity;

    Config config;

    SavedServers savedServers;

    String nameServer;
    String ip;
    int port;

    int position;
    boolean isModify;

    EditText serverName,setIp,setPort;

    public SaveServerDialog(Activity activity, SavedServers savedServers) { // Nuevo server
        super(activity);
        this.activity = activity;
        this.ip = "";
        this.port = -1;
        this.config = Config.getInstance(activity);
        this.savedServers = savedServers;
        this.isModify = false;
    }

    public SaveServerDialog(Activity activity, String ip, int port) { // Guardado automatico
        super(activity);
        this.activity = activity;
        this.ip = ip;
        this.port = port;
        this.config = Config.getInstance(activity);
        this.isModify = false;
    }

    public SaveServerDialog(Activity activity,int position, String nameServer, String ip, String port, SavedServers savedServers) { // Modificar
        super(activity);
        this.activity = activity;
        this.nameServer = nameServer;
        this.ip = ip;
        this.port = Integer.parseInt(port);
        this.position = position;
        this.savedServers = savedServers;
        this.config = Config.getInstance(activity);
        this.isModify = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_saveserver);

        this.findViewById(R.id.saveServer_saveBtn).setOnClickListener(this); //Boton
        serverName = (EditText) findViewById(R.id.saveServer_setServerName);

        if(ip.length() > 0 && port > 0 && nameServer == null) {
            this.findViewById(R.id.addServerAuto).setVisibility(View.VISIBLE);

            TextView direccionIp = (TextView) this.findViewById(R.id.saveServer_showIp);
            direccionIp.setText("IP: " + ip);

            TextView puerto = (TextView) this.findViewById(R.id.saveServer_showPort);
            puerto.setText(activity.getResources().getText(R.string.serverSave_port) + ": " + port);
        }else if(ip.length() > 0 && port > 0 && nameServer.length() > 0){
            this.findViewById(R.id.addServerManual).setVisibility(View.VISIBLE);

            serverName.setText(nameServer);

            setIp = (EditText) this.findViewById(R.id.saveServer_setIp);
            setIp.setText(ip);

            setPort = (EditText) this.findViewById(R.id.saveServer_setPort);
            setPort.setText(port + "");
        }else{
            this.findViewById(R.id.addServerManual).setVisibility(View.VISIBLE);

            setIp = (EditText) this.findViewById(R.id.saveServer_setIp);

            setPort = (EditText) this.findViewById(R.id.saveServer_setPort);
        }
    }

    @Override
    public void onClick(View v) {
        //Cuando se pulse el boton:
        if(ip.length() > 0 && port > 0 && nameServer == null) { //Guardado automatico de conexion nueva.
            if (serverName.getText().toString().length() > 0) {
                try {
                    config.setServerConfig(serverName.getText().toString(), ip, port);
                } catch (Exception e) {
                    //El nombre de servidor ya existe
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dismiss();
            } else {
                //El nombre de servidor debe contener mas de 0 caracteres
                Toast.makeText(activity, activity.getResources().getText(R.string.sms_serverNameLength), Toast.LENGTH_SHORT).show();
            }
        }else if(setIp != null && setPort != null){ //Guardado manual desde lista.
            if(serverName.getText().toString().length() > 0) {
                try {
                    if(isModify){
                        config.updateServer(position,
                                serverName.getText().toString(),
                                setIp.getText().toString(),
                                setPort.getText().toString());
                    }else{
                        config.setServerConfig(serverName.getText().toString(), setIp.getText().toString(), setPort.getText().toString());
                    }
                    savedServers.updateList();
                } catch (Exception e) {
                    //El nombre del servidor ya existe
                    Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }else{
                //El nombre de servidor debe contener mas de 0 caracteres
                Toast.makeText(activity,activity.getResources().getText(R.string.sms_serverNameLength),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
