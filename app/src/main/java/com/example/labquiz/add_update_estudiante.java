package com.example.labquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.labquiz.logicaNegocio.Curso;
import com.example.labquiz.logicaNegocio.Estudiante;
import com.example.labquiz.model.Modelo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class add_update_estudiante extends AppCompatActivity {

    private FloatingActionButton fBtn;
    private boolean editable = true;
    private EditText etCodigo;
    private EditText etNombre;
    private EditText etCreditos;
    private EditText etHorasSemanales;
    private Spinner spinnerCurso;
    ArrayAdapter<Curso> adaptadorC;

    private List<Curso> cursos;
    private Modelo model;

    //Url cargar combo carreras
    //String apiUrlCargaComboCurso = "http://192.168.0.6:8080/Backend_LabQuiz/Curso/List?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_estudiante);

        editable = true;

        // button check
        fBtn = findViewById(R.id.addUpdCursoBtn);

        //cleaning stuff
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etCreditos = findViewById(R.id.etCreditos);
        etHorasSemanales = findViewById(R.id.etHorasSemanales);
        etCodigo.setText("");
        etNombre.setText("");
        etCreditos.setText("");
        etHorasSemanales.setText("");

        model = Modelo.getIntance();

        //Cargado spinners o combos
        spinnerCurso = findViewById(R.id.spinnerCurso);
        cursos = new ArrayList<>();
        cursos = model.listCurso(this);

        adaptadorC = new ArrayAdapter<Curso>(this, R.layout.spinner_curso,cursos);
        spinnerCurso.setAdapter(adaptadorC);
      ////////////////////

        //receiving data from admCursoActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            editable = extras.getBoolean("editable");
            if (editable) {   // is editing some row
                Estudiante aux = (Estudiante) getIntent().getSerializableExtra("estudiante");
                etCodigo.setText(aux.getIdP());
                etCodigo.setEnabled(false);
                etNombre.setText(aux.getNombre());
                etCreditos.setText(aux.getApellidos());
                etHorasSemanales.setText(aux.getEdad());

                //edit action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editCurso();
                    }
                });
            } else {         // is adding new Carrera object
                //add new action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addCurso();
                    }
                });
            }
        }

        //Aca se prepara el popup
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int)(ancho * 0.90), (int)(alto * 0.60));
    }

    public void addCurso() {
        if (validateForm()) {
            //do something
            Curso curs = new Curso();

            ArrayList<Curso> miC = new ArrayList<>();
            curs = ((Curso) spinnerCurso.getSelectedItem());
            System.out.println("Curso data:" + curs.getDescripcion() + " Cod: " + curs.getIdC());
            miC.add(curs);

            Estudiante est = new Estudiante(
                    etCodigo.getText().toString(),
                    etNombre.getText().toString(),
                    etCreditos.getText().toString(),
                    etHorasSemanales.getText().toString(),
                    curs);

            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            //sending curso data
            intent.putExtra("addEstudiante", est);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public void editCurso() {//Funcion para editar en la base y en la lista que muestra en la Recycler View
        if (validateForm()) {

            Curso curs = new Curso();

            ArrayList<Curso> miC = new ArrayList<>();
            curs = ((Curso) spinnerCurso.getSelectedItem());
            System.out.println("Curso data:" + curs.getDescripcion() + " Cod: " + curs.getIdC());
            miC.add(curs);

            Estudiante est = new Estudiante(
                    etCodigo.getText().toString(),
                    etNombre.getText().toString(),
                    etCreditos.getText().toString(),
                    etHorasSemanales.getText().toString(),
                    curs);

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
        if (TextUtils.isEmpty(this.etCodigo.getText())) {
            etCodigo.setError("Cedula requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.etCreditos.getText())) {
            etCreditos.setError("Apellidos requeridos");
            error++;
        }
        if (TextUtils.isEmpty(this.etHorasSemanales.getText())) {
            etHorasSemanales.setError("Edad requerida");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}

