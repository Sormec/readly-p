package com.example.readly;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class ConsultardatosActivity extends AppCompatActivity {

    private EditText txtCedulaEd;
    private EditText txtNombres;
    private EditText txtApellidos;
    private EditText txtCedula;
    private EditText txtCorreo;
    private Spinner spNacionalidad;
    private RadioGroup radioGroup;
    private EditText etFechaNacimiento;
    private ImageView ivCalendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultardatos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtCedulaEd = findViewById(R.id.edCedula);
        etFechaNacimiento = findViewById(R.id.etConsulFechaNacimiento);
        ivCalendario = findViewById(R.id.ivCalendarioC);
        txtNombres = findViewById(R.id.txtNombresC);
        txtApellidos = findViewById(R.id.txtApellidosC);
        txtCedula = findViewById(R.id.txtCedulaC);
        txtCorreo = findViewById(R.id.txtCorreoC);
        spNacionalidad = findViewById(R.id.spnConsulNacionalidad);
        radioGroup = findViewById(R.id.radioGroupC);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nacionalidad, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNacionalidad.setAdapter(adapter);

        View.OnClickListener dateClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        };

        etFechaNacimiento.setOnClickListener(dateClickListener);
        ivCalendario.setOnClickListener(dateClickListener);
    }

    @SuppressLint("Range")
    public void consultarUsuario(View v){
        MyOpenHelper dbReadly = new MyOpenHelper(this);
        final SQLiteDatabase dbReadlyMode = dbReadly.getReadableDatabase();
        String cedulaUsuario = String.valueOf(txtCedulaEd.getText());

        Cursor c = dbReadlyMode.rawQuery("SELECT * FROM usuario " +
                "WHERE cedula = '" + cedulaUsuario + "'",null);

        if (c != null){
            c.moveToFirst();
            txtCedula.setText(c.getString(c.getColumnIndex("cedula")).toString());
            txtNombres.setText(c.getString(c.getColumnIndex("nombres")).toString());
            txtApellidos.setText(c.getString(c.getColumnIndex("apellidos")).toString());
            txtCorreo.setText(c.getString(c.getColumnIndex("correo")).toString());
            etFechaNacimiento.setText(c.getString(c.getColumnIndex("fechaNac")).toString());
            //spNacionalidad.setAdapter(c.getString(c.getColumnIndex("nacionalidad")).toString());
        }
        c.close();
        dbReadlyMode.close();
    }

    public void actualizarUsuario(View v){
        addValidations();
        String nombres = txtNombres.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String cedula = txtCedula.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        String nacionalidad = spNacionalidad.getSelectedItem().toString();
        int selectedGenderId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String genero = selectedGenderButton.getText().toString();

        if(verificarStatusSD() == 0){
            actualizarBD(cedula, nombres, apellidos, correo, fechaNacimiento, nacionalidad, genero);
            Toast.makeText(v.getContext(), "Datos ACTUALIZADOS en la BD", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Los datos no han sido ACTUALIZADOS, consultar" +
                    " con administrador", Toast.LENGTH_LONG).show();
        }
    }

    public void eliminarUsuario(View v){
        MyOpenHelper dbReadly = new MyOpenHelper(this);
        final SQLiteDatabase dbReadlyMode = dbReadly.getWritableDatabase();
        String cedulaUsuario = String.valueOf(txtCedulaEd.getText());

        if(dbReadlyMode != null){
            dbReadlyMode.delete("usuario","cedula="+cedulaUsuario,null);
            Toast.makeText(v.getContext(), "Datos ELIMINDAOS en la BD", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Los datos no han sido ELIMINADOS, consultar" +
                    " con administrador", Toast.LENGTH_LONG).show();
        }
        dbReadlyMode.close();
    }
    public void guardar(View v){
        addValidations();
        String nombres = txtNombres.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String cedula = txtCedula.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        String nacionalidad = spNacionalidad.getSelectedItem().toString();
        int selectedGenderId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String genero = selectedGenderButton.getText().toString();

        if(verificarStatusSD() == 0){
            guardarBD(cedula, nombres, apellidos, correo, fechaNacimiento, nacionalidad, genero);
            Toast.makeText(v.getContext(), "Datos Ingresado en la BD", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Los datos no han sido guardados, consultar" +
                        " con administrador", Toast.LENGTH_LONG).show();
        }
    }
    public void guardarBD(String cedula, String nombres, String apellidos, String correo,
                          String fechaNacimiento, String nacionalidad, String genero){
        MyOpenHelper dbReadly = new MyOpenHelper(this);
        final SQLiteDatabase dbReadlyMode = dbReadly.getWritableDatabase();

        if (dbReadlyMode != null){
            ContentValues cv = new ContentValues();
            cv.put("cedula", cedula);
            cv.put("nombres", nombres);
            cv.put("apellidos", apellidos);
            cv.put("correo", correo);
            cv.put("fechaNac", fechaNacimiento);
            cv.put("nacionalidad", nacionalidad);
            cv.put("genero", genero);

            dbReadlyMode.insert("usuario",null,cv);
        }
        //dbReadlyMode.close();
    }

    public void actualizarBD(String cedula, String nombres, String apellidos, String correo,
                          String fechaNacimiento, String nacionalidad, String genero){
        MyOpenHelper dbReadly = new MyOpenHelper(this);
        final SQLiteDatabase dbReadlyMode = dbReadly.getWritableDatabase();

        if (dbReadlyMode != null){
            ContentValues cv = new ContentValues();
            cv.put("cedula", cedula);
            cv.put("nombres", nombres);
            cv.put("apellidos", apellidos);
            cv.put("correo", correo);
            cv.put("fechaNac", fechaNacimiento);
            cv.put("nacionalidad", nacionalidad);
            //cv.put("genero", genero);

            dbReadlyMode.update("usuario",cv,"cedula="+cedula,null);
        }
        dbReadlyMode.close();
    }

    private int verificarStatusSD() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            //Toast.makeText(this, "Memoria montada correctamente", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Toast.makeText(this, "Memoria solo de lectura", Toast.LENGTH_SHORT).show();
            return 1;
        } else {
            Toast.makeText(this, "Error en memoria, no se puede guardar", Toast.LENGTH_SHORT).show();
            return 2;
        }
    }
    private void addValidations() {
        //  nombres
        txtNombres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[a-zA-Z ]+")) {
                    txtNombres.setError("Ingrese solo letras y espacios");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //apellidos
        txtApellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[a-zA-Z ]+")) {
                    txtApellidos.setError("Ingrese solo letras y espacios");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //  cédula
        txtCedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("\\d{0,14}$")) {
                    txtCedula.setError("Ingrese solo números, máximo 14 dígitos");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        // correo
        txtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")) {
                    txtCorreo.setError("Ingrese un correo válido");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        etFechaNacimiento.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }
    public void regresarInicio(View v) {
        Intent ventanaInicio = new Intent(v.getContext(), InicioActivity.class);
        startActivity(ventanaInicio);
    }
}