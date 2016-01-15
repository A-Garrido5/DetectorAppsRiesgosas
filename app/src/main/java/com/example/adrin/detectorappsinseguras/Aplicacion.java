package com.example.adrin.detectorappsinseguras;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Adri�n on 27-05-2015.
 */
public class Aplicacion {


        String nombre;
        String riesgo;
        String paquete;
        String riesgoPermisos;
        String riesgoEncriptacion;
        String riesgoPublicidad;

        Drawable icono;

        ColorDrawable colorDrawable;

        Context context;

        public Aplicacion(String nombre, String riesgo, Drawable icono, String paquete, String riesgoPermisos, String riesgoEncriptacion, String riesgoPublicidad, Context context) {
            this.nombre = nombre;
            this.riesgo=riesgo;
            this.icono=icono;
            this.paquete=paquete;

            this.riesgoPermisos=riesgoPermisos;
            this.riesgoEncriptacion=riesgoEncriptacion;
            this.riesgoPublicidad=riesgoPublicidad;

            this.context=context;

        }

    public ColorDrawable getColor(String riesgo){

        int nivel_riesgo;

        if(riesgo=="-")
            nivel_riesgo=0;
        else
            nivel_riesgo=Integer.parseInt(riesgo.substring(0,riesgo.length()-1));


        if(nivel_riesgo<40 && nivel_riesgo!=0) {
            colorDrawable = new ColorDrawable(context.getResources().getColor(R.color.verde_riesgo));
            return colorDrawable;
        }

        else if(nivel_riesgo>=40 && nivel_riesgo<75){
            colorDrawable = new ColorDrawable(context.getResources().getColor(R.color.amarillo_riesgo));
            return colorDrawable;

        }

        else if(nivel_riesgo>75){
            colorDrawable = new ColorDrawable(context.getResources().getColor(R.color.rojo_riesgo));
            return colorDrawable;

        }

        else {
            colorDrawable = new ColorDrawable(context.getResources().getColor(R.color.white));
            return colorDrawable;
        }





    }

}
