package com.example.adrin.detectorappsinseguras;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Descripcion_app extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_app);



        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("icono");



        String nameApp=getIntent().getExtras().getString("nombre");

        String riesgoPermisos=getIntent().getExtras().getString("riesgoPermisos");
        String riesgoEncriptacion=getIntent().getExtras().getString("riesgoEncriptacion");
        String riesgoPublicidad=getIntent().getExtras().getString("riesgoPublicidad");

        String riesgoTotal=getIntent().getExtras().getString("riesgoTotal");


        final String nombrePaquete=getIntent().getExtras().getString("nombrePaquete");

        RelativeLayout fondo_app=(RelativeLayout)findViewById(R.id.fondo_app);



        if(riesgoTotal!="-")
            fondo_app.setBackground(getColor(riesgoTotal));


        TextView nombre= (TextView)findViewById(R.id.textView);

        TextView riskPerm=(TextView)findViewById(R.id.riesgoPermisos);
        TextView riskEnc=(TextView)findViewById(R.id.riesgoEncriptacion);
        TextView riskPub=(TextView)findViewById(R.id.riesgoPublicidad);

        TextView totalRisk=(TextView) findViewById(R.id.riesgoTotal);


        nombre.setText(nameApp);

        riskPerm.setText(riesgoPermisos);
        riskEnc.setText(riesgoEncriptacion);
        riskPub.setText(riesgoPublicidad);
        totalRisk.setText(riesgoTotal);

        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        ImageView image = (ImageView) findViewById(R.id.imageView2);

        image.setImageBitmap(bmp);

        Drawable d = new BitmapDrawable(getResources(), bmp);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Resultado para " + nameApp);

        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.color_app));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button desinstalar = (Button) findViewById(R.id.button3);


        desinstalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" +nombrePaquete));
                startActivity(i);





            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_descripcion_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        onBackPressed();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public ColorDrawable getColor(String riesgo){

        ColorDrawable colorDrawable;

        TextView advertencia=(TextView) findViewById(R.id.warning);
        TextView desc_advertencia=(TextView) findViewById(R.id.desc_advertencia);

        int nivel_riesgo;

        if(riesgo.compareTo("-")!=0) {

            nivel_riesgo=Integer.parseInt(riesgo.substring(0,riesgo.length()-1));


            if (nivel_riesgo < 40 && nivel_riesgo != 0) {
                colorDrawable = new ColorDrawable(getResources().getColor(R.color.verde_riesgo));
                return colorDrawable;
            } else if (nivel_riesgo >= 40 && nivel_riesgo < 75) {
                colorDrawable = new ColorDrawable(getResources().getColor(R.color.amarillo_riesgo));
                return colorDrawable;

            } else if (nivel_riesgo > 75) {
                colorDrawable = new ColorDrawable(getResources().getColor(R.color.rojo_riesgo));
                return colorDrawable;

            }

        }

        else{
            advertencia.setText("Advertencia:");
            desc_advertencia.setText("No tenemos registro de esta app");

        }

        colorDrawable = new ColorDrawable(getResources().getColor(R.color.white));
        return colorDrawable;

    }
}
