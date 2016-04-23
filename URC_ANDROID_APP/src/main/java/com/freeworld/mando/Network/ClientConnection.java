package com.freeworld.mando.Network;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.freeworld.mando.Config;
import com.freeworld.mando.IMain;
import com.freeworld.mando.Mando;
import com.freeworld.mando.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

/**
 * Created by AlvaroRoyo on 18/11/2015.
 */
public class ClientConnection extends Observable implements Runnable {

    //Variables de sistema
    //public static Client cliente;
    //public static String IP;
    //public static int Port = 52343;

    //public static String[] receiverGroup = new String[]{"Default"};
    //public static String selectedGroup = "Default";

    private String IP;
    private int port = 52343;
    private String[] receiverGroup;
    private String selectedGroup;
    private Socket socket;
    private DataOutputStream sender;
    private Config config;
    private Activity activity;

    private ClientConnection(){
        receiverGroup = new String[]{"Default"};
        selectedGroup = "Default";
    }
    private static ClientConnection instance;
    public static ClientConnection getInstance(){
        if(instance == null){
            instance = new ClientConnection();
        }
        return instance;
    }

    /**
     * Conexión del cliente.
     */
    @Override
    public void run() {
        try {
            socket = new Socket(IP, port);
            socket.setTcpNoDelay(true); // No blocking queue

            super.setChanged();   //Notificamos la conexion
            super.notifyObservers();

            sender = new DataOutputStream(socket.getOutputStream());
            sendDeviceInfo(); //Enviamos la informacion del dispositivo

            DataInputStream br = new DataInputStream(socket.getInputStream());
            String smsRecived;
            while ((smsRecived = br.readLine()) != null) {

                String[] receiver = smsRecived.split("\\|");

                switch (receiver[0]) {
                    case "Groups":
                        receiverGroup = receiver[1].split(",");
                        selectedGroup = receiverGroup[0];
                        super.setChanged();
                        super.notifyObservers();
                        break;
                }
            }
            disconnect();
        } catch (Exception e) {
            disconnect();
            IP = null;
        }
    }

    /**
     * Crea una nueva conexión y un dialog para el usuario.
     * @param activity
     * @param ip
     * @param port
     */
    public void setConnection(final Activity activity, String ip, String port){
        final AlertDialog dialog;
        Handler handler = new Handler();
        config = Config.getInstance(activity);
        this.activity = activity;

        AlertDialog.Builder adialog = new AlertDialog.Builder(activity);
        adialog.setTitle(activity.getText(R.string.connectionDialog_title));
        adialog.setMessage(activity.getText(R.string.connectionDialog_text)).setCancelable(false);

        dialog = adialog.create();

        this.IP = ip;
        this.port = Integer.parseInt(port);
        try {
            (new Thread(instance)).start();
        }catch(Exception e){
            Log.e("SetConnection-Start",e.getMessage());
        }
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isConnected()) {
                    dialog.dismiss();
                    try {
                        IMain mListener = (IMain) activity;
                        mListener.setFragmentMandoSaveServer();
                        //((Mando) activity).setFragmentMandoSaveServer();
                    } catch (Exception e) {
                        Log.e("SetConnection",e.getMessage());
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(activity, activity.getText(R.string.conexion_toast_fail), Toast.LENGTH_LONG).show();
                }
            }
        },2000);
    }

    /**
     * Envia un mensaje al servidor.
     * @param msg
     */
    public void sendPackage(String msg) {
        byte[] mensaje = (":"+msg).getBytes();
        try {
            sender.write(mensaje, 0, mensaje.length);
        } catch (Exception e) {}
    }

    /**
     * Envia al servidor la configuración actual del dispositivo.
     */
    public void sendDeviceInfo() {
        try {
            String info = "::" + config.getStringConfig(Config.DEVICE_NAME) +  //Nombre del dispositivo
                    ":" + activity.getResources().getBoolean(R.bool.isTablet) +    //Tipo de dispositivo
                    ":" + ((Mando) activity).getFragment() +    //Tipo de mando
                    ":" + selectedGroup;
            sender.write(info.getBytes());

        } catch (IOException e) {
        }
    }

    /**
     * Desconecta el cliente.
     */
    public void disconnect() {
        try {
            sender.close();
        } catch (Exception e) {
        }
        try {
            socket.close();
        } catch (Exception e) {
        }
        socket = null;
        super.setChanged();  //Notificamos la desconexion
        super.notifyObservers();
    }

    /**
     * Obtiene si el cliente está conectado
     * @return
     */
    public boolean isConnected() {
        boolean connect = true;
        if (socket == null || !socket.isConnected()) {
            connect = false;
        }
        return connect;
    }

    public String[] getReceivers(){
        return receiverGroup;
    }

    public void setSelectedGroup(String selectedGroup){
        this.selectedGroup = selectedGroup;
    }

    /**
     * Obtiene la IP del servidor.
     * @return
     */
    public String getIP() {
        return IP;
    }

    public int getPort(){return port;}

    /**
     * Establece la IP del servidor.
     * @param ip
     * @return
     */
    public boolean setIP(String ip) {
        boolean ipsetted = false;
        if (ip != null) {
            IP = ip;
            ipsetted = true;
        }
        return ipsetted;
    }

    /**
     * Establece el puerto de conexión al servidor
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Establece el puerto de conexión al servidor
     *
     * @param port
     */
    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

}