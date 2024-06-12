package com.example.readly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class RegistrosActivity extends AppCompatActivity {
    private TextView txtMostrarRegistros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registros);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtMostrarRegistros = findViewById(R.id.txtMostrarRegistros);

        String mostrarRegistros = leerDatosDeArchivo();
        if (mostrarRegistros != null) {
            txtMostrarRegistros.setText(mostrarRegistros);
        } else {
            Toast.makeText(this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
        }
    }
    private String leerDatosDeArchivo() {
        try {
            File file = new File(getExternalFilesDir(null), "RegistroUsuario");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            fis.close();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al leer el archivo", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    public void regresarInicio(View v) {
        Intent ventanaInicio = new Intent(v.getContext(), InicioActivity.class);
        startActivity(ventanaInicio);
    }
}