package com.freeworld.mando;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.freeworld.mando.Adapters.NavDrawerAdapter;
import com.freeworld.mando.Dialogs.SaveServerDialog;
import com.freeworld.mando.Dialogs.SetDeviceName;
import com.freeworld.mando.Dialogs.SetGroups;
import com.freeworld.mando.Fragments_Mandos.Teclado;
import com.freeworld.mando.NavDrawerListener.DrawerItemClickListener;
import com.freeworld.mando.Network.ClientConnection;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Mando extends Activity implements View.OnClickListener, Observer,IMain {
    //Imagenes estaticas conexion
    ImageView connectY, connectN, groupBtn;

    //Clase de configuracion
    Config config;

    //Fragment in screen
    Fragment fragment;

    //Lista de mandos
    public List<String[]> listaMandos;

    //Nav drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    //Connection
    private ClientConnection connection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        config = Config.getInstance(this);

        connection = ClientConnection.getInstance();
        connection.addObserver(this);

        listaMandos = parseMandos();

        setContentView(R.layout.main_topactivity);

        setFragmentNewMando(listaMandos.get(config.getIntConfig(Config.DEFAULT_CONTROLER))[2]);

        connectN = (ImageView) findViewById(R.id.connectN);
        connectY = (ImageView) findViewById(R.id.connectY);

        groupBtn = (ImageView) findViewById(R.id.btnGroups);
        groupBtn.setOnClickListener(this);

        findViewById(R.id.btnMenu).setOnClickListener(this);
        findViewById(R.id.contentCon).setOnClickListener(this);

        //Nav drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new NavDrawerAdapter(this, R.layout.adapter_navdrawer_row));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(this));
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Comprobar si hay nombre para el dispositivo
        if (config.getStringConfig(Config.DEVICE_NAME).equals(Config.NOT_EXIST)) {
            SetDeviceName deviceName = new SetDeviceName(this);  // Si no hay ningun nombre para el dispositivo se abre un Dialog
            deviceName.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.disconnect(); //Cerrar la conexion
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawers();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connectN:
            case R.id.connectY:
            case R.id.contentCon:
                if (!connection.isConnected()) {
                    try{

                        String[] datosConexion = config.getServerList().get(config.getIntConfig(config.DEFAULT_CONEXION));
                        connection.setConnection(this, datosConexion[1], datosConexion[2]);

                    } catch (Exception e) {
                        Toast.makeText(this,getResources().getText(R.string.fastConnect_fail),Toast.LENGTH_LONG).show();
                        Log.e("SavedServers", "setConnection - New thread");
                    }
                } else {
                    Toast.makeText(this, getResources().getText(R.string.disconnecting), Toast.LENGTH_LONG).show();
                    connection.disconnect();
                }
                break;
            case R.id.btnMenu:
                if(mDrawerLayout.isDrawerOpen(mDrawerList)){
                    mDrawerLayout.closeDrawers();
                }else{
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                break;
            case R.id.btnGroups:
                SetGroups grupos = new SetGroups(this);
                grupos.show();
                break;
        }
    }

    /**
     * Método del patrón observer.
     * @param observable
     * @param data
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable == connection) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (connection.isConnected()) {
                        connectN.setVisibility(View.GONE);
                        connectY.setVisibility(View.VISIBLE);
                    } else {
                        connectY.setVisibility(View.GONE);
                        connectN.setVisibility(View.VISIBLE);
                        groupBtn.setVisibility(View.GONE);
                    }
                    if (connection.getReceivers().length > 1) {
                        groupBtn.setVisibility(View.VISIBLE);
                    } else {
                        groupBtn.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public List<String[]> getListControls(){
        return listaMandos;
    }

    /**
     * Cierra el drawer layout
     */
    @Override
    public void closeNavDrawer(){
        mDrawerLayout.closeDrawers();
    }

    /**
     * Cambia el fragment de pantalla
     * @param newFragment String. Nombre del nuevo fragment.
     */
    @Override
    public void setFragment(String newFragment) {
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFragment, (Fragment) Class.forName(getPackageName() + ".Fragments_InterfacesDeUsuario." + newFragment).newInstance())
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.e("setFragment", "No se encontró la clase: " + newFragment);
        }
    }

    /**
     * Poner un nuevo fragment de mando. Almacenda el fragment en la variable "Fragment fragment"
     * @param newFragment String. Nombre de la clase del nuevo fragment
     */
    @Override
    public void setFragmentNewMando(String newFragment) {
        try {
            fragment = (Fragment) Class.forName(getPackageName() + ".Fragments_Mandos." + newFragment).newInstance();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction
                    .setCustomAnimations(R.anim.alpha_come,R.anim.alpha_go)
                    .replace(R.id.mainFragment, fragment)
                    .commit();
            connection.sendDeviceInfo();
        } catch (Exception e) {
            Log.e("setFragment", "No se encontró la clase: " + newFragment);
        }
    }

    /**
     * Obtiene el nombre de la clase del fragment mando actual.
     *
     * @return
     */
    @Override
    public String getFragment() {
        String fragmentName = "NO_FRAGMENT";
        try {
            fragmentName = fragment.getClass().toString().split("\\.")[4];
        } catch (Exception e) {}
        return fragmentName;
    }

    /**
     * Poner el fragment de mando actual
     */
    @Override
    public void setFragmentMando() {
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction
                    .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
                    .replace(R.id.mainFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.e("setFragment", "No se encontró la clase: " + fragment);
        }
    }

    /**
     * Poner el fragment de mando actual
     * Este método se llama desde la clase Connection
     */
    @Override
    public void setFragmentMandoSaveServer() {
        try {
            //Mostrar mensaje de guardar server.
            if (!config.serverInfoExist(connection.getIP() + ":" + connection.getPort())) {
                SaveServerDialog saveServer = new SaveServerDialog(this, connection.getIP(), connection.getPort());
                saveServer.show();
            }
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.e("setFragment", "No se encontró la clase: " + fragment);
        }
    }

    /**
     * Parsea el documento xml res/raw/mandos.xml y obtiene una Lista con los datos de los mandos
     *
     * @return List del tipo String[]
     */
    private List<String[]> parseMandos() {
        List<String[]> listaMandos = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.mandos);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            NodeList xml_menu = document.getElementsByTagName("mando");

            for (int i = 0; i < xml_menu.getLength(); i++) {
                String[] mando = new String[]{
                        xml_menu.item(i).getAttributes().getNamedItem("name").getTextContent(),
                        xml_menu.item(i).getAttributes().getNamedItem("icon").getTextContent(),
                        xml_menu.item(i).getAttributes().getNamedItem("href").getTextContent()
                };
                listaMandos.add(mando);
            }

        } catch (Exception e) {
            Log.e("parseMandos", "Error al parsear documento mandos.xml");
        }

        return listaMandos;
    }
}
