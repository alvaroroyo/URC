package com.freeworld.mando.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeworld.mando.R;

import java.util.List;

/**
 * Created by Alvaro Royo on 08/12/2015.
 */
public class ElegirMandoAdapter extends ArrayAdapter<String[]> {

    Context context;
    int layoutResourceId;
    List<String[]> array;

    boolean isElegirMando;

    public ElegirMandoAdapter(Context context, int resource, List<String[]> array,boolean isElegirMando) {
        super(context, resource,array);
        this.layoutResourceId = resource;
        this.context = context;
        this.array = array;
        this.isElegirMando = isElegirMando;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ElegirMandoHolder holder = null;

        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId,parent,false);

            if(isElegirMando){  //ELEGIR MANDO
                row.findViewById(R.id.mandos_text).setVisibility(View.VISIBLE);

                holder = new ElegirMandoHolder();
                holder.imgIcon = (ImageView)row.findViewById(R.id.mandos_icon);
                holder.txtTitle = (TextView)row.findViewById(R.id.mandos_text);
            }else{ //SaveServer
                row.findViewById(R.id.serverSavedContent).setVisibility(View.VISIBLE);

                holder = new ElegirMandoHolder();
                holder.imgIcon = (ImageView)row.findViewById(R.id.mandos_icon);
                holder.serverName = (TextView)row.findViewById(R.id.serverSavedName);
                holder.serverIp = (TextView)row.findViewById(R.id.serverSavedIp);
                holder.serverPort = (TextView)row.findViewById(R.id.serverSavedPort);
            }

            row.setTag(holder);
        }else{
            holder = (ElegirMandoHolder)row.getTag();
        }

        if(isElegirMando){ //ELEGIR MANDO
            holder.txtTitle.setText(array.get(position)[0]);
            holder.imgIcon.setImageResource(context.getResources().getIdentifier(array.get(position)[1],"drawable",context.getPackageName()));
        }else{ //SaveServer
            holder.imgIcon.setImageResource(R.drawable.ic_serversave);

            holder.serverName.setText(array.get(position)[0]);
            holder.serverIp.setText(array.get(position)[1]);
            holder.serverPort.setText(array.get(position)[2]);
        }

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    /*public View getCustomView(int position, View convertView, ViewGroup parent){

    }*/

    static class ElegirMandoHolder{
        ImageView imgIcon;
        TextView txtTitle;

        TextView serverName;
        TextView serverIp;
        TextView serverPort;
    }
}
