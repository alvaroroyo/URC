package com.freeworld.mando.NavDrawerListener;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeworld.mando.Adapters.DrawerHolder;
import com.freeworld.mando.Mando;

/**
 * Created by aroyo on 03/02/2016.
 */
public class DrawerItemClickListener implements AdapterView.OnItemClickListener {
    Activity activity;
    public DrawerItemClickListener(Activity principalActivity){
        this.activity = principalActivity;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((Mando)activity).closeNavDrawer();
        DrawerHolder holder = (DrawerHolder)view.getTag();
        String href = holder.getInfo()[1];
        switch(href){
            case "Mando":
                ((Mando)activity).setFragmentMando();
                break;
            default:
                ((Mando)activity).setFragment(href);
        }
    }

}
