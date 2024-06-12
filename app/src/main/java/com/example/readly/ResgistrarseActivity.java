package com.example.readly;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class ResgistrarseActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_resgistrarse);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        ivCalendario = findViewById(R.id.ivCalendario);
        txtNombres = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtCedula = findViewById(R.id.txtCedula);
        txtCorreo = findViewById(R.id.txtCorreo);
        spNacionalidad = findViewById(R.id.spnNacionalidad);
        radioGroup = findViewById(R.id.radioGroup);
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

        addValidations();
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
    private int verificarStatusSD() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Memoria montada correctamente", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Toast.makeText(this, "Memoria solo de lectura", Toast.LENGTH_SHORT).show();
            return 1;
        } else {
            Toast.makeText(this, "Error en memoria, no se puede guardar", Toast.LENGTH_SHORT).show();
            return 2;
        }
    }

    private boolean guardarEnSD(String datos) {
        try{
            File f = new File(getExternalFilesDir(null), "RegistroUsuario");
            OutputStreamWriter out =new OutputStreamWriter(new FileOutputStream(f, true));
            out.write(datos);
            out.close();
            return true;
        }
        catch(Exception io)
        {
            Log.e("Fichero", "Error al guardar en SD");
            return false;

        }
    }


    //metodo que guarda en la BD y en la SD
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
            if (guardarEnSD(obtenerInformacionRegistrada())) {
                    Toast.makeText(this, "Datos Ingresados SD", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Los datos no han sido guardados, consultar" +
                            " con administrador", Toast.LENGTH_LONG).show();
            }
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

    public void obtenerInformacion(View view) {
        String nombres = txtNombres.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String cedula = txtCedula.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        String nacionalidad = spNacionalidad.getSelectedItem().toString();
        int selectedGenderId = radioGroup.getCheckedRadioButtonId();

        if (nombres.isEmpty()) {
            txtNombres.setError("Ingrese sus nombres");
            return;
        }
        if (apellidos.isEmpty()) {
            txtApellidos.setError("Ingrese sus apellidos");
            return;
        }
        if (cedula.isEmpty()) {
            txtCedula.setError("Ingrese su cédula");
            return;
        }
        if (correo.isEmpty()) {
            txtCorreo.setError("Ingrese su correo");
            return;
        }
        if (fechaNacimiento.isEmpty()) {
            etFechaNacimiento.setError("Seleccione su fecha de nacimiento");
            return;
        }
        if (nacionalidad.equals("Seleccione nacionalidad")) {
            Toast.makeText(this, "Seleccione su nacionalidad", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Seleccione su género", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String genero = selectedGenderButton.getText().toString();

        if (verificarStatusSD() == 0) {
            if (guardarEnSD(obtenerInformacionRegistrada())) {
                Toast.makeText(this, "Los datos han sido ingresados", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Los datos no han sido guardados, consultar con administrador", Toast.LENGTH_LONG).show();
            }
        }

        // Mostrar la información registrada
        obtenerInformacionRegistrada();
    }
    private String obtenerInformacionRegistrada() {
        String nombres = txtNombres.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String cedula = txtCedula.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        String nacionalidad = spNacionalidad.getSelectedItem().toString();
        int selectedGenderId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String genero = selectedGenderButton.getText().toString();

        String informacion = "Nombres: " + nombres + "\n" +
                "Apellidos: " + apellidos + "\n" +
                "Cédula: " + cedula + "\n" +
                "Correo: " + correo + "\n" +
                "Fecha de nacimiento: " + fechaNacimiento + "\n" +
                "Nacionalidad: " + nacionalidad + "\n" +
                "Género: " + genero + "\n";

        return informacion;
    }
    public void borrarInformacion(View view) {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCedula.setText("");
        txtCorreo.setText("");
        etFechaNacimiento.setText("");
        spNacionalidad.setSelection(0);
        radioGroup.clearCheck();
    }
    public void regresarLogin(View v) {
        Intent ventanaLogin = new Intent(v.getContext(), LoginActivity.class);
        startActivity(ventanaLogin);
    }

}
