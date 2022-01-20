package ma.content.tarea4_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private int centrox, centroy;
    private Lienzo lienzo;
    private Paint pincel = new Paint();
    private ConstraintLayout fondo;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        fondo = findViewById(R.id.lienzo);
        lienzo = new Lienzo(this);
        lienzo.setOnTouchListener(this);
        pincel.setARGB(255, 255, 255, 255); //Ponemos color del pincel en blanco para que no se vea inicialmente
        pincel.setStrokeWidth(4);


        fondo.addView(lienzo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.it_preferences:
                Intent intent5 = new Intent(this, MenuPreferencias.class);
                startActivity(intent5);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean relleno = preferences.getBoolean("relleno", false);
        Boolean tipocolor = preferences.getBoolean("tipocolor", false);

        centrox = (int) event.getX();
        centroy = (int) event.getY();


        if (tipocolor){
            String colorR = preferences.getString("colorR", "false");
            String colorG = preferences.getString("colorG", "false");
            String colorB = preferences.getString("colorB", "false");
            pincel.setARGB(255, Integer.valueOf(colorR), Integer.valueOf(colorG), Integer.valueOf(colorB));
        } else {
            //generamos un color aleatorio para el círculo que hay que dibujar
            pincel.setARGB(255, (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        }

        if (relleno){
            pincel.setStyle(Paint.Style.STROKE);
        } else{
            pincel.setStyle(Paint.Style.FILL);
        }
        lienzo.invalidate();
        return true;
    }

    class Lienzo extends View {
        public Lienzo(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            int size = 100;
            canvas.drawRGB(255, 255, 255);

            String tamaño = preferences.getString("tamaño", "false");

            if (tamaño.equals("Grande")){
                size = 200;
            } else if(tamaño.equals("Mediano")){
                size = 100;
            } else if (tamaño.equals("Pequeño")){
                size = 50;
            }

            String figura = preferences.getString("forma", "false");

            if (figura.equals("Circulo")){
                canvas.drawCircle(centrox, centroy, size, pincel);
            } else if(figura.equals("Rectángulo")){
                canvas.drawRect(centrox - (size), centroy + (size/2), centrox + (size), centroy - (size/2), pincel) ;
            } else if (figura.equals("Cuadrado")){
                canvas.drawRect(centrox - (size/2), centroy - (size/2), centrox + (size/2), centroy + (size/2), pincel) ;
            } else if (figura.equals("Estrella")){
                canvas.drawPath(creaEstrella(centrox, centroy, size), pincel);
            }


        }

        public Path creaEstrella (int x, int y, int radio){
            Point centro = new Point(x, y);

            //Creamos 10 puntos para trazar la estrella
            Point[] starP = new Point[10];
            //Creamos los puntos utilizando coordenadas polares
            //En este bucle for tenemos dos variables incrementales, la del índice del punto 'i' y la del ángulo
            //que parte de un valor aleatorio entre 0~180 y se irá incrementando en cada iteración 360/(nº de puntos)
            for (int i = 0, angulo = (int)(Math.random()*180); i < starP.length; i++, angulo += 360/starP.length) {
                if (i % 2 == 0) //Los puntos pares tendrán un módulo 'radio' (puntas de la estrella)
                    starP[i] = polar2rect(radio, angulo);
                else //Los puntos impares tendrán un módulo 'radio/2' (puntos interiores de la estrella)
                    starP[i] = polar2rect(radio / 2, angulo);
            }
            //Creamos el Path: desde el punto 0 vamos creando líneas hasta cerrar la forma
            Path star = new Path();
            star.moveTo(starP[0].x + centro.x, starP[0].y + centro.y);
            for(int i=1; i<starP.length; i++)
                star.lineTo(starP[i].x + centro.x, starP[i].y + centro.y);
            star.lineTo(starP[0].x + centro.x, starP[0].y + centro.y); //Última línea para cerrar la estrella
            return star;
        }

        //Método conversor de coordenadas polares (módulo, ángulo en grados) a coordenadas binomiales (o rectangulares)
        public Point polar2rect (double radio, int grados){
            double rad = grados * Math.PI / 180; //para pasar el ángulo de grados a radianes
            return new Point(
                    (int) Math.round(radio * Math.cos(rad)), //coordenada x
                    (int) Math.round(radio * Math.sin(rad))  //coordenada y
            );
        }

    }
}