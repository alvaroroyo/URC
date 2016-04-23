package com.freeworld.mando;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aroyo on 04/12/2015.
 */
public class Config {

    private SharedPreferences preferencias;
    private SharedPreferences.Editor edit;
    private Activity activity;

    public static final String NOT_EXIST = "not_exist";

    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String DEFAULT_CONTROLER = "DEFAULT_CONTROLER";
    public static final String DEFAULT_CONEXION = "DEFAULT_CONEXION";

    public static final String SERVER_NAMES = "SERVER_NAMES";
    public static final String SERVER_INFOS = "SERVER_INFOS";

    private static Config instance;
    public static Config getInstance(Activity activity){
        if(instance == null){
            instance = new Config(activity);
        }
        return instance;
    }
    private Config(Activity activity){
        this.activity = activity;
        preferencias = activity.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        edit = preferencias.edit();
    }

    /**
     * Crea o actualiza una propiedad interna de tipo String
     * @param key
     * @param sms
     */
    public void setStringConfig(String key, String sms){
        edit.putString(key,sms);
        edit.commit();
    }

    /**
     * Obtiene una propiedad interna de tipo String
     * @param key
     * @return
     */
    public String getStringConfig(String key){
        return preferencias.getString(key,NOT_EXIST);
    }

    /**
     * Crea o actualiza una propiedad interna de tipo int
     * @param key
     * @param sms
     */
    public void setIntConfig(String key, int sms){
        edit.putInt(key, sms);
        edit.commit();
    }

    /**
     * Obtiene una propiedad interna de tipo int
     * @param key
     * @return
     */
    public int getIntConfig(String key){
        return preferencias.getInt(key, 0);
    }

    /**
     * Crea o actualiza una propiedad interna de tipo Boolean
     * @param key
     * @param sms
     */
    public void setBooleanConfig(String key, boolean sms){
        edit.putBoolean(key, sms);
        edit.commit();
    }

    /**
     * Obtiene una propiedad interna de tipo Boolean
     * @param key
     * @return
     */
    public boolean getBooleanConfig(String key) {
        return preferencias.getBoolean(key, false);
    }

    /**
     * Elimina una propiedad interna
     * @param key
     */
    public void removeConfig(String key){
        edit.remove(key);
    }

    ///////////////////////////////////////// SERVER CONFIGS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Save a server config and a server name in memory
     * @param name
     * @param ip
     * @param port
     * Tested: Y
     */
    public void setServerConfig(String name, String ip, Object port) throws Exception{
        String serverInfo = ip + ":" + port;
        if(!serverNameExist(name)){
            String allServers = preferencias.getString(SERVER_NAMES, "");
            String allInfos = preferencias.getString(SERVER_INFOS, "");

            allServers = (allServers.length() > 0) ? allServers + "," + name : name;
            allInfos = (allInfos.length() > 0) ? allInfos + "," + serverInfo : serverInfo;

            setStringConfig(SERVER_NAMES, allServers);
            setStringConfig(SERVER_INFOS, allInfos);
        }else{
            throw new Exception(activity.getResources().getText(R.string.sms_serverNameUsed).toString());
        }
    }

    /**
     * Update server info in memory
     * @param position
     * @param name
     * @param ip
     * @param port
     * Tested: Y
     */
    public void updateServer(int position,String name,String ip,String port) throws Exception{
        try{
            String[] serverName = preferencias.getString(SERVER_NAMES, "").split(",");
            String[] serverInfo = preferencias.getString(SERVER_INFOS, "").split(",");

            if(serverName.length-1 >= position && serverInfo.length-1 >= position) {

                serverName[position] = name;
                serverInfo[position] = ip + ":" + port;

                setStringConfig(SERVER_NAMES, Arrays.toString(serverName).replace("[", "").replace("]", ""));
                setStringConfig(SERVER_INFOS, Arrays.toString(serverInfo).replace("[", "").replace("]", ""));

            }else{
                throw new Exception(activity.getResources().getText(R.string.sms_serverPosition).toString());
            }

        }catch(Exception e){
            Log.e("Config","updateServer");
        }
    }

    /**
     * Remove a server config from preferences.
     * @param position
     * Tested: Y
     */
    public void removeServerConfig(int position){
        try {
            List<String> serverName = Arrays.asList(preferencias.getString(SERVER_NAMES, "").split(","));
            List<String> serverInfo = Arrays.asList(preferencias.getString(SERVER_INFOS, "").split(","));

            if(serverName.size()-1 >= position && serverInfo.size()-1 >= position) {

                List<String> serverNames = new ArrayList<>();
                List<String> serverInfos = new ArrayList<>();

                for (int i = 0; i < serverName.size() || i < serverInfo.size(); i++) {
                    if(i != position){
                        serverNames.add(serverName.get(i));
                        serverInfos.add(serverInfo.get(i));
                        if(position == getIntConfig(DEFAULT_CONEXION)){
                            removeConfig(DEFAULT_CONEXION);
                        }
                    }
                }

                setStringConfig(SERVER_NAMES, Arrays.toString(serverNames.toArray(new String[serverNames.size()])).replace("[","").replace("]",""));
                setStringConfig(SERVER_INFOS, Arrays.toString(serverInfos.toArray(new String[serverInfos.size()])).replace("[","").replace("]",""));
            }

        }catch(Exception e){
            Log.e("Config","removeServerConfig");
        }
    }

    /**
     * Get the server config by server name
     * @param name
     * @return
     * Tested: Y
     */
    public String[] getServerConfig(String name){
        String[] serverInfo = null;
        try {
            String[] names = preferencias.getString(SERVER_NAMES,"").split(",");
            String[] info = preferencias.getString(SERVER_INFOS,"").split(",");
            for (int i = 0; i < info.length || i < names.length; i++) {
                if(names[i].equals(name)){
                    serverInfo = info[i].split(":");
                }
            }
        }catch(Exception e){
            Log.e("Config","getServerConfig");
        }
        return serverInfo;
    }

    /**
     * Get a list with all server info.
     * @return
     * Tested: Y
     */
    public List<String[]> getServerList(){
        List<String[]> servers = new ArrayList<>();

        try{
            String[] serverNames = preferencias.getString(SERVER_NAMES,"").split(",");
            String[] serverInfos = preferencias.getString(SERVER_INFOS,"").split(",");

            for(int i = 0; i<serverNames.length || i<serverInfos.length; i++){
                String[] server = new String[3];
                server[0] = serverNames[i];
                server[1] = serverInfos[i].split(":")[0];
                server[2] = serverInfos[i].split(":")[1];

                servers.add(server);
            }

        }catch(Exception e){
            Log.e("Config","getServerList");
        }

        return servers;
    }

    /**
     * Check if a server is saved in memory
     * @param serverInfo
     * @return
     * Tested: Y
     */
    public boolean serverInfoExist(String serverInfo){
        boolean exist = false;
        try{
            String[] info = preferencias.getString(SERVER_INFOS,"").split(",");
            for(String isInfo : info){
                if(isInfo.equals(serverInfo)){
                    exist = true;
                }
            }
        }catch(Exception e){
            Log.e("Config","serverExist");
        }
        return exist;
    }

    /**
     * Check if a server name is saved in memory
     * @param name
     * @return
     * Tested: Y
     */
    public boolean serverNameExist(String name){
        boolean exist = false;
        String[] servers = preferencias.getString(SERVER_NAMES,"").split(",");
        for(String servername : servers){
            if(servername.equals(name)){
                exist = true;
            }
        }
        return exist;
    }

}
