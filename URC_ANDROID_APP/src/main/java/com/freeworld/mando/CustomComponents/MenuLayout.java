package com.freeworld.mando.CustomComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freeworld.mando.R;

/**
 * Created by Alvaro Royo on 07/12/2015.
 */
public class MenuLayout extends LinearLayout {

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(10, 10, 10, 10);
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.xx_menu);

        TextView titulo = new TextView(context);
        titulo.setTextSize(18);
        titulo.setGravity(Gravity.CENTER_VERTICAL);
        titulo.setPadding(0, 10, 0, 20);
        titulo.setTextColor(Color.rgb(33,33,33));
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuLayoutAjustes);

        for(int i = 0;i<a.getIndexCount();i++){
            int attr = a.getIndex(i);
            switch(attr){
                case R.styleable.MenuLayoutAjustes_titulo:
                    titulo.setText(a.getString(attr));
                    break;
                case R.styleable.MenuLayoutAjustes_tituloColor:
                    titulo.setTextColor(a.getColor(attr,Color.rgb(33,33,33)));
                    break;
            }
        }
        a.recycle();
        this.addView(titulo);

    }
}
