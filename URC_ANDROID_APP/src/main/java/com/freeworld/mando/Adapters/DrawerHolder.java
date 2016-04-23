package com.freeworld.mando.Adapters;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by aroyo on 03/02/2016.
 */
public class DrawerHolder {
    private ImageView imgIcon;
    private TextView txtTitle;
    private String[] info;

    public ImageView getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(ImageView imgIcon) {
        this.imgIcon = imgIcon;
    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(TextView txtTitle) {
        this.txtTitle = txtTitle;
    }

    public String[] getInfo() {
        return info;
    }

    public void setInfo(String[] info) {
        this.info = info;
    }
}
