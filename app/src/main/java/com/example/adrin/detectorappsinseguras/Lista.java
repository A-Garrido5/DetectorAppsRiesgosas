package com.example.adrin.detectorappsinseguras;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrin.detectorappsinseguras.controlador.LectDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class Lista extends ActionBarActivity {

    //Atributos
    private Context mcontext=this;
    private LectDB lecDBExterna;

    private int cambio=0;



    ListView listaApps;
    AdaptadorLista adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        this.mcontext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.color_app));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        listaApps = (ListView) findViewById(R.id.lista_apps);

        //Cargando DB (?):
        this.lecDBExterna = new LectDB(this);
        this.lecDBExterna.execute();




        //Carga los programas:
        CargandoProgramas cProg = new CargandoProgramas(this.lecDBExterna);
        cProg.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return true;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        onBackPressed();


        return super.onOptionsItemSelected(item);
    }




    /**
     * Clase que se encarga de leer los programas del telefono, al temrinar
     * cruzaral ai nfo con lo obtenido por la web y enviara a la pantalla prinzipal
     * la lista con los programas completo y su nivel de riesgo.
     */
    private class CargandoProgramas extends AsyncTask<Void, Integer, Boolean> {

        private ArrayList<String> programasLista;
        private ArrayList<String> packetProgramasLista;
        private ArrayList<Drawable> imgProgramas;
        private ArrayList<Aplicacion> apps;

        private LectDB dbLecturaExterna;

        public CargandoProgramas(LectDB dbLecturaExterna)
        {
            this.programasLista = new ArrayList<>();
            this.packetProgramasLista = new ArrayList<>();
            this.imgProgramas = new ArrayList<>();

            this.apps = new ArrayList<>();

            this.dbLecturaExterna = dbLecturaExterna;
        }

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            getInstalledApps();

            JSONObject elementosDB = dbLecturaExterna.getDB();
            String str = elementosDB.toString();

            //if you want to convert list to json (with Gson):
            //foo - will be your list
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mcontext);
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            prefEditor.putString("nombre", str);
            prefEditor.commit();





            try
            {
                JSONArray programas = elementosDB.getJSONArray("nombre");

                HashMap<String, Integer> progHashMap = new HashMap<String, Integer>();
                HashMap<String, Integer> riskPerm = new HashMap<String, Integer>();
                HashMap<String, Integer> riskPub = new HashMap<String, Integer>();
                HashMap<String, Integer> riskEnc = new HashMap<String, Integer>();

                Integer bla=0;

                for (int i = 0; i < programas.length(); i++)
                {
                    JSONObject j = programas.optJSONObject(i);
                    Iterator it = j.keys();

                    String keyPacket="";

                    String n="";

                    while (it.hasNext())
                    {
                        n = (String) it.next();

                        if(n.equals("riesgoPermisos")){
                            bla = Integer.parseInt(j.getString(n));
                        }

                        if(n.equals("Name")){
                            keyPacket += j.getString(n);

                            //System.out.println("Nombre: " + keyPacket);
                        }


                        if(n.equals("riesgoPublicidad")){
                            Integer seg = Integer.parseInt(j.getString(n));
                            riskPub.put(keyPacket, seg);

                        }

                        if(n.equals("riesgoEncriptacion")){
                            Integer seg = Integer.parseInt(j.getString(n));
                            riskEnc.put(keyPacket, seg);
                            riskPerm.put(keyPacket,bla);
                        }

                        if(n.equals("riesgoTotal") && keyPacket!=null )
                        {
                            Integer seg = Integer.parseInt(j.getString(n));
                            progHashMap.put(keyPacket, seg);

                        }
                    }
                }

                for(int i = 0; i < packetProgramasLista.size(); i++)
                {
                    if(progHashMap.containsKey(packetProgramasLista.get(i)))
                    {
                        String newName = programasLista.get(i);

                        String risk =  progHashMap.get(packetProgramasLista.get(i)) +"%";
                        String packet = packetProgramasLista.get(i);
                        Drawable imgP = imgProgramas.get(i);

                        apps.get(i).riesgoPermisos=String.valueOf(riskPerm.get(packetProgramasLista.get(i))+"%");
                        apps.get(i).riesgoPublicidad=String.valueOf(riskPub.get(packetProgramasLista.get(i))+"%");
                        apps.get(i).riesgoEncriptacion=String.valueOf(riskEnc.get(packetProgramasLista.get(i)) + "%");

                        packetProgramasLista.remove(i);
                        programasLista.remove(i);
                        imgProgramas.remove(i);

                        packetProgramasLista.add(0, packet);
                        programasLista.add(0, newName);
                        imgProgramas.add(0, imgP);
                        apps.get(i).riesgo=risk;
                        apps.get(i).colorDrawable=apps.get(i).getColor(apps.get(i).riesgo);

                    }

                    else{
                        apps.get(i).riesgo="-";
                    }
                }


            }
            catch (JSONException e)
            {
                System.out.println("Fallo la conversaion de json..."+ e);
            }


            return true;
        }

        public void getInstalledApps()
        {
            List<PackageInfo> PackList = getPackageManager().getInstalledPackages(0);
            for (int i=0; i < PackList.size(); i++)
            {
                PackageInfo PackInfo = PackList.get(i);
                if(((PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) != true)
                {

                    String packetName = PackInfo.applicationInfo.packageName;
                    System.out.println(packetName);
                    String AppName = PackInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                    Drawable icon = PackInfo.applicationInfo.loadIcon(getPackageManager());


                    if(!packetName.equals("com.example.adrin.detectorappsinseguras"))
                    {
                        Aplicacion app = new Aplicacion(AppName,"-", icon,packetName,"-","-","-",mcontext);
                        this.apps.add(app);

                        this.imgProgramas.add(icon);
                        this.packetProgramasLista.add(packetName);
                        this.programasLista.add(AppName);


                    }
                }
            }


        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
            {


                listaApps.setDrawSelectorOnTop(true);
                //Ocultamos el "cargando"
                ((ProgressBar) findViewById(R.id.progress_programa)).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.textView2)).setVisibility(View.GONE);






                //Creando Adapter:

                adaptador = new AdaptadorLista(Lista.this,apps);
                listaApps.setAdapter(adaptador);







                System.out.println("Estamos listos para mostrar los programas.");

                Toast.makeText(Lista.this,"Análisis realizado exitosamente", Toast.LENGTH_LONG).show();

                listaApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Intent describir = new Intent(Lista.this, Descripcion_app.class);
                        describir.putExtra("nombre", apps.get(position).nombre);

                        describir.putExtra("riesgoTotal",apps.get(position).riesgo);

                        describir.putExtra("riesgoPermisos", apps.get(position).riesgoPermisos);
                        describir.putExtra("riesgoEncriptacion", apps.get(position).riesgoEncriptacion);
                        describir.putExtra("riesgoPublicidad", apps.get(position).riesgoPublicidad);

                        describir.putExtra("nombrePaquete", apps.get(position).paquete);


                        Drawable icon = apps.get(position).icono;

                        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();

                        describir.putExtra("icono", b);

                        startActivity(describir);


                    }
                });

            } else {
                System.out.println("Error");
            }
        }
    }
}
