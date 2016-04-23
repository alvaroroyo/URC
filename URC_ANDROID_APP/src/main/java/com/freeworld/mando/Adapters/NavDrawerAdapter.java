package com.freeworld.mando.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freeworld.mando.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Alvaro Royo on 08/12/2015.
 */
public class NavDrawerAdapter extends ArrayAdapter<String[]> {

    Context context;
    int layoutResourceId;
    List<String[]> array;

    public NavDrawerAdapter(Context context, int resource) {
        super(context, resource);
        this.layoutResourceId = resource;
        this.context = context;
        this.array = getRows();
    }

    @Override
    public int getCount(){
        return array.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DrawerHolder holder = null;

        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId,parent,false);

            holder = new DrawerHolder();
            holder.setImgIcon((ImageView)row.findViewById(R.id.menu_row_image));
            holder.setTxtTitle((TextView)row.findViewById(R.id.menu_row_text));

            row.setTag(holder);
        }else{
            holder = (DrawerHolder)row.getTag();
        }

        holder.getTxtTitle().setText(array.get(position)[0]);
        holder.getImgIcon().setImageResource(context.getResources().getIdentifier(array.get(position)[1], "drawable", context.getPackageName()));
        holder.setInfo(new String[]{
                array.get(position)[2],
                array.get(position)[3]
        });

        return row;
    }

    /**
     * Establece la view del menú
     * @return List -> String[]:
     * 0 -> Name: Name for menu row
     * 1 -> Icon: Icon for menu row
     * 2 -> Id: Layout id of fragment.
     * 3 -> Href: Name for class to execute
     */
    private List<String[]> getRows(){
        List<String[]> row_values = new ArrayList<>();

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.menu);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            NodeList xml_menu = document.getElementsByTagName("menu_row");

            for(int i = 0; i < xml_menu.getLength(); i++){

                NamedNodeMap menu_row = xml_menu.item(i).getAttributes();

                row_values.add(new String[]{
                        menu_row.getNamedItem("name").getTextContent(),
                        menu_row.getNamedItem("icon").getTextContent(),
                        menu_row.getNamedItem("id").getTextContent(),
                        menu_row.getNamedItem("href").getTextContent()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return row_values;
    }
}
