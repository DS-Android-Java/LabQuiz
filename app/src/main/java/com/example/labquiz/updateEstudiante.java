package com.example.labquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.labquiz.logicaNegocio.Curso;
import com.example.labquiz.logicaNegocio.Estudiante;
import com.example.labquiz.model.Modelo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class updateEstudiante extends AppCompatActivity {

    private FloatingActionButton fBtn;
    private boolean editable = true;
    private EditText etCodigo;
    private EditText etNombre;
    private EditText etApellidos;
    private EditText etEdad;
    private Modelo model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_estudiante);

        fBtn = findViewById(R.id.addEst);
        etCodigo = findViewById(R.id.etCedU);
        etNombre = findViewById(R.id.etNombreU);
        etApellidos = findViewById(R.id.etApellidosU);
        etEdad = findViewById(R.id.etEdadU);

        Estudiante aux = (Estudiante) getIntent().getSerializableExtra("estudianteU");
        etCodigo.setText(aux.getIdP());
        etCodigo.setEnabled(false);
        etNombre.setText(aux.getNombre());
        etApellidos.setText(aux.getApellidos());
        etEdad.setText(aux.getEdad());

        //edit action
        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEstudiante();
            }
        });

        //Aca se prepara el popup
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int) (ancho * 0.90), (int) (alto * 0.50));
    }

    public void editEstudiante() {//Funcion para editar en la base y en la lista que muestra en la Recycler View
        if (validateForm()) {

            ArrayList<Curso> miC = new ArrayList<>();
            Estudiante est = new Estudiante(
                    etCodigo.getText().toString(),
                    etNombre.getText().toString(),
                    etApellidos.getText().toString(),
                    etEdad.getText().toString(),
                    miC);

            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            //sending curso data
            intent.putExtra("editEstudiante", est);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.etNombre.getText())) {
            etNombre.setError("Nombre requerido");//Asi se le coloca un mensaje de error en los campos en android
            error++;
        }
        if (TextUtils.isEmpty(this.etApellidos.getText())) {
            etApellidos.setError("Apellidos requeridos");
            error++;
        }
        if (TextUtils.isEmpty(this.etEdad.getText())) {
            etEdad.setError("Edad requerida");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}