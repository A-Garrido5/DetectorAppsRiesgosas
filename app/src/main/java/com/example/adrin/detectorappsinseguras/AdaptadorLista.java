package com.example.adrin.detectorappsinseguras;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adri�n on 26-05-2015.
 */
public class AdaptadorLista extends BaseAdapter {

    String nombre_app;

    Context ctx;

    ArrayList<Aplicacion> apps;



    public AdaptadorLista(Context ctx, ArrayList<Aplicacion> apps){
        this.apps=apps;
        this.ctx=ctx;
    }



    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        HashMap<String, Object> item = new HashMap<>();
        item.put("risk",apps.get(position).riesgo);
        item.put("name", apps.get(position).nombre);
        item.put("icon", apps.get(position).icono);
        //item.put("risk",mRiesgos.get(position));

        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.list_single, parent, false);
        }

        ImageView mIconView = (ImageView) convertView.findViewById(R.id.icon_app);
        TextView mNameView = (TextView) convertView.findViewById(R.id.nombre_app);
        TextView mRiskView =(TextView) convertView.findViewById(R.id.riesgo);

        RelativeLayout color = (RelativeLayout) convertView.findViewById(R.id.fondo);


        HashMap<String,Object> mObjectApp = (HashMap<String,Object>)getItem(position);
        mNameView.setText((String) mObjectApp.get("name"));
        mRiskView.setText((String) mObjectApp.get("risk"));


        RelativeLayout RL=(RelativeLayout) convertView.findViewById(R.id.fondo);

        RL.setBackground(apps.get(position).colorDrawable);



        mIconView.setImageDrawable((Drawable) mObjectApp.get("icon"));


        return convertView;
    }



}
