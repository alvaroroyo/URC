package com.freeworld.mando.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freeworld.mando.Network.ClientConnection;
import com.freeworld.mando.R;

/**
 * Created by Alvaro Royo on 28/12/2015.
 */
public class SetGroups extends Dialog implements View.OnClickListener{

    private String[] groups;

    private LinearLayout list;

    private Context contexto;

    private ClientConnection connection;

    public SetGroups(Context context) {
        super(context);
        this.connection = ClientConnection.getInstance();
        this.contexto = context;
        this.groups = connection.getReceivers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setgroups);

        this.setCancelable(true);

        list = (LinearLayout) findViewById(R.id.groupList);

        LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i = 0; i<groups.length;i++){

            View row = inflater.inflate(R.layout.adapter_grupos,null);

            TextView groupName = (TextView)row.findViewById(R.id.groupName);
            groupName.setText(groups[i]);

            row.setTag(groups[i]);
            row.setOnClickListener(this);

            list.addView(row);
        }
    }

    @Override
    public void onClick(View v) {
        connection.setSelectedGroup((String)v.getTag());
        connection.sendDeviceInfo();
        dismiss();
    }
}
