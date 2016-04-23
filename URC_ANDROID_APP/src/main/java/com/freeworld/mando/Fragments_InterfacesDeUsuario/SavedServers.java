package com.freeworld.mando.Fragments_InterfacesDeUsuario;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.freeworld.mando.Adapters.ElegirMandoAdapter;
import com.freeworld.mando.Config;
import com.freeworld.mando.Dialogs.SaveServerDialog;
import com.freeworld.mando.Network.ClientConnection;
import com.freeworld.mando.R;

/**
 * Created by Alvaro Royo on 11/01/2016.
 */
public class SavedServers extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    View vista;
    Config config;

    ListView lista;

    AlertDialog dialog;

    final SavedServers me = this;

    private ClientConnection connection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        connection = ClientConnection.getInstance();

        config = Config.getInstance(getActivity());

        vista = inflater.inflate(R.layout.fragment_ui_servidores,container,false);

        vista.findViewById(R.id.saveNewServerBtn).setOnClickListener(this);

        lista = (ListView) vista.findViewById(R.id.serverSaveList);

        lista.setAdapter(new ElegirMandoAdapter(getActivity(), R.layout.adapter_mandos_row, config.getServerList(), false));

        lista.setOnItemClickListener(this);
        lista.setOnItemLongClickListener(this);

        return vista;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.saveNewServerBtn:
                SaveServerDialog saveServer = new SaveServerDialog(getActivity(),this);
                saveServer.show();
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
        final TextView serverName = (TextView) view.findViewById(R.id.serverSavedName);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        try {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }catch(Exception e){
            Log.e("SavedServers","onItemLongClick");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.select_dialog_item);
        arrayAdapter.add(getText(R.string.Connection).toString());
        arrayAdapter.add(getText(R.string.Modify).toString());
        arrayAdapter.add(getText(R.string.Remove).toString());

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0: //Conectar
                        String[] info = config.getServerConfig(serverName.getText().toString());
                        connection.setConnection(getActivity(), info[0], info[1]);
                        break;
                    case 1: //Modificar
                        TextView serverName = (TextView) view.findViewById(R.id.serverSavedName);
                        TextView serverIp = (TextView) view.findViewById(R.id.serverSavedIp);
                        TextView serverPort = (TextView) view.findViewById(R.id.serverSavedPort);

                        SaveServerDialog saveServerDialog = new SaveServerDialog(getActivity(),
                                position,
                                serverName.getText().toString(),
                                serverIp.getText().toString(),
                                serverPort.getText().toString(),
                                me);
                        saveServerDialog.show();
                        break;
                    case 2: //Borrar
                        config.removeServerConfig(position);
                        updateList();
                        break;
                }
            }
        });
        dialog = builderSingle.create();
        dialog.setCancelable(true);
        dialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView serverName = (TextView) view.findViewById(R.id.serverSavedName);
        String[] info = config.getServerConfig(serverName.getText().toString());

        connection.setConnection(getActivity(), info[0], info[1]);
    }

    /**
     * Update list in view when make changes
     */
    public void updateList(){
        lista.setAdapter(new ElegirMandoAdapter(getActivity(),R.layout.adapter_mandos_row,config.getServerList(),false)); //Actualizacion de la lista.
    }
}
