package com.example.readly;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EjerciciosActivity extends AppCompatActivity {

    private SoundPool sp;
    private int sonidoReproducir;
    private TextView txtD;
    private TextView txtP;
    private TextView txtQ;
    private TextView txtB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ejercicios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtD= findViewById(R.id.text_D);
        txtP= findViewById(R.id.text_P);
        txtQ= findViewById(R.id.text_Q);
        txtB= findViewById(R.id.text_B);

        sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);
        sonidoReproducir = sp.load(this, R.raw.letrap, 1);
    }


    public void reproduceSound(View view) {
        sp.play(sonidoReproducir, 1f, 1f, 1, 0, 1f);
    }
    public void seleccionarOpcion(View view){
        //crear un metodo que coloree solo el que seleciono
        String bg = "#F4DFA2";
        int opcion = view.getId();
        if( opcion == R.id.text_D ) {
            // D P Q B
            txtD.setBackgroundColor(Color.parseColor(bg));
            txtP.setBackgroundColor(Color.TRANSPARENT);
            txtQ.setBackgroundColor(Color.TRANSPARENT);
            txtB.setBackgroundColor(Color.TRANSPARENT);

        } else if ( opcion == R.id.text_P ){
            txtD.setBackgroundColor(Color.TRANSPARENT);
            txtP.setBackgroundColor(Color.parseColor(bg));
            txtQ.setBackgroundColor(Color.TRANSPARENT);
            txtB.setBackgroundColor(Color.TRANSPARENT);

        } else if ( opcion == R.id.text_Q ) {
            txtD.setBackgroundColor(Color.TRANSPARENT);
            txtP.setBackgroundColor(Color.TRANSPARENT);
            txtQ.setBackgroundColor(Color.parseColor(bg));
            txtB.setBackgroundColor(Color.TRANSPARENT);

        } else if ( opcion == R.id.text_B ){
            txtD.setBackgroundColor(Color.TRANSPARENT);
            txtP.setBackgroundColor(Color.TRANSPARENT);
            txtQ.setBackgroundColor(Color.TRANSPARENT);
            txtB.setBackgroundColor(Color.parseColor(bg));
        }
    }

    public void borrar(View view){
        txtD.setBackgroundColor(Color.TRANSPARENT);
        txtP.setBackgroundColor(Color.TRANSPARENT);
        txtQ.setBackgroundColor(Color.TRANSPARENT);
        txtB.setBackgroundColor(Color.TRANSPARENT);
    }

    public void regresarInicio(View v) {
        Intent ventanaInicio = new Intent(v.getContext(), InicioActivity.class);
        startActivity(ventanaInicio);
    }

}