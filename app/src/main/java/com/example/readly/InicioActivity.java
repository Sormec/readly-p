package com.example.readly;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuPrincipal = getMenuInflater();
        menuPrincipal.inflate(R.menu.menuprincipal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemInicio) {
            Intent ventanaInicio = new Intent(this, InicioActivity.class);
            startActivity(ventanaInicio);
            return true;
        } else if (id == R.id.itemRegistros) {
            Intent ventanaRegistros = new Intent(this, RegistrosActivity.class);
            startActivity(ventanaRegistros);
            return true;
        } else if (id == R.id.itemCerrarSesion) {
            Intent ventanaLogin = new Intent(this, LoginActivity.class);
            startActivity(ventanaLogin);
            return true;
        }else if (id == R.id.itemConsultarDatos) {
            Intent ventanaConsultardatos = new Intent(this, ConsultardatosActivity.class);
            startActivity(ventanaConsultardatos);
            return true;
        } else if(id == R.id.itemEjercicios) {
            Intent ventanaEjercicios = new Intent(this, EjerciciosActivity.class);
            startActivity(ventanaEjercicios);
        }

        return super.onOptionsItemSelected(item);
    }
}