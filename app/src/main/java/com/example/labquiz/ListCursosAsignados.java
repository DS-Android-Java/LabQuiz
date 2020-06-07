package com.example.labquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.labquiz.logicaNegocio.Curso;
import com.example.labquiz.logicaNegocio.Estudiante;
import com.example.labquiz.model.Modelo;

import java.util.ArrayList;

public class ListCursosAsignados extends AppCompatActivity {

    private ArrayList<Curso> miCursos;
    private Modelo model;
    private ListView list_cursos;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cursos_asignados);
        model = Modelo.getIntance();

        list_cursos = findViewById(R.id.list_cursos);
        textInfo = findViewById(R.id.textInfo);

        miCursos = new ArrayList<>();
        Estudiante aux = (Estudiante) getIntent().getSerializableExtra("idEstudiante");
        miCursos = model.listCursoByEstudent(this,aux.getIdP());
        textInfo.setText("Nombre: " + aux.getNombre() + " " + aux.getApellidos() + "\n"
                + "Id: " + aux.getIdP() + "\n"
                + "Edad: " + aux.getEdad());

        ArrayAdapter<Curso> adapter = new ArrayAdapter<Curso>(this, R.layout.list_item_cursos,miCursos);
        list_cursos.setAdapter(adapter);

        //Aca se prepara el popup
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int)(ancho * 0.90), (int)(alto * 0.90));
    }
}