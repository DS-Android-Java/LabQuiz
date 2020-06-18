package com.example.labquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labquiz.logicaNegocio.Curso;
import com.example.labquiz.model.Modelo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class add_update_curso extends AppCompatActivity {

    private FloatingActionButton fBtn;
    private boolean editable = true;
    private EditText etCodigo;
    private EditText etNombre;
    private EditText etCreditos;
    private Modelo model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_curso);

        editable = true;

        // button check
        fBtn = findViewById(R.id.addUpdCursoBtn);

        //cleaning stuff
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etCreditos = findViewById(R.id.etCreditos);
        model =  Modelo.getIntance(this);
        etCodigo.setText("");
        etNombre.setText("");
        etCreditos.setText("");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            editable = extras.getBoolean("editable");
            if (editable) {   // is editing some row
                Curso aux = (Curso) getIntent().getSerializableExtra("curso");
                etCodigo.setText(aux.getIdC());
                etCodigo.setEnabled(false);
                etNombre.setText(aux.getDescripcion());
                etCreditos.setText(aux.getCreditos());
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

        getWindow().setLayout((int)(ancho * 0.90), (int)(alto * 0.50));
    }

    public void addCurso() {
        if (validateForm()) {
            //do something
            Curso cur = new Curso(
                    etCodigo.getText().toString(),
                    etNombre.getText().toString(),
                    etCreditos.getText().toString());

            Intent intent = new Intent(getBaseContext(), MainActivityCurso.class);
            //sending curso data
            intent.putExtra("addCurso", cur);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public void editCurso() {//Funcion para editar en la base y en la lista que muestra en la Recycler View
        if (validateForm()) {

            Curso cur = new Curso(
                    etCodigo.getText().toString(),
                    etNombre.getText().toString(),
                    etCreditos.getText().toString());

            Intent intent = new Intent(getBaseContext(), MainActivityCurso.class);
            //sending curso data
            intent.putExtra("editCurso", cur);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.etNombre.getText())) {
            etNombre.setError("Descripción requerida");//Asi se le coloca un mensaje de error en los campos en android
            error++;
        }
        if (TextUtils.isEmpty(this.etCodigo.getText())) {
            etCodigo.setError("Codigo requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.etCreditos.getText())) {
            etCreditos.setError("Creditos requerido");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}