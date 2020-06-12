package com.example.labquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labquiz.logicaNegocio.Curso;
import com.example.labquiz.logicaNegocio.Estudiante;
import com.example.labquiz.model.Modelo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListCursosAsignados extends AppCompatActivity {

    private ArrayList<Curso> miCursos;
    private ArrayList<Curso> allCursos;
    private Modelo model;
    private ListView list_cursos;
    private TextView textInfo;
    private Estudiante aux;
    private Curso curs;
    private ArrayAdapter<Curso> adapter;
    private ArrayAdapter<Curso> adapterC;
    private Spinner spinnerCurso;
    private FloatingActionButton addCursoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cursos_asignados);
        model = Modelo.getIntance(this);

        list_cursos = findViewById(R.id.list_cursos);
        textInfo = findViewById(R.id.textInfo);
        addCursoBtn = findViewById(R.id.addCursoBtn);
        spinnerCurso = findViewById(R.id.spinnerCurso);
        allCursos = model.listCurso();
        adapterC = new ArrayAdapter<Curso>(ListCursosAsignados.this, R.layout.spinner_curso, allCursos);
        spinnerCurso.setAdapter(adapterC);

        miCursos = new ArrayList<>();
        aux = (Estudiante) getIntent().getSerializableExtra("idEstudiante");
        miCursos = model.listCursoByEstudent(aux.getIdP());
        textInfo.setText("Nombre: " + aux.getNombre() + " " + aux.getApellidos() + "\n"
                + "Id: " + aux.getIdP() + "\n"
                + "Edad: " + aux.getEdad());

        adapter = new ArrayAdapter<Curso>(this, R.layout.list_item_cursos, miCursos);
        list_cursos.setAdapter(adapter);

        //Menu contextual
        registerForContextMenu(list_cursos);

        addCursoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarAsignacion();
            }
        });

        //Aca se prepara el popup
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int) (ancho * 0.90), (int) (alto * 0.90));
    }

    public void AgregarAsignacion() {
        curs = ((Curso) spinnerCurso.getSelectedItem());

        if (validaCursoAsignado(curs)) {
            Toast.makeText(this, "Este curso ya se encuentra asignado a " + aux.getNombre(), Toast.LENGTH_LONG).show();
        } else {//Si no procesa a asignarlo
            boolean result = model.insertAsignacion(aux.getIdP(), curs.getIdC());
            if (result) {
                //Se refreca la lista con los cambios realizados
                miCursos = model.listCursoByEstudent(aux.getIdP());
                adapter = new ArrayAdapter<Curso>(this, R.layout.list_item_cursos, miCursos);
                list_cursos.setAdapter(adapter);
                Toast.makeText(this, "Curso asignado correctamente!!!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Curso no se pudo asignar!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validaCursoAsignado(Curso curs) {
        boolean cursoAsignado = false;
        for (Curso c : miCursos) {
            if (c.getIdC().equals(curs.getIdC())) {
                cursoAsignado = true;//Quiere decir que encontro el curso y que ya lo tenia asignado
                break;
            }
        }
        return cursoAsignado;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        int id = v.getId();
        switch (id) {
            case R.id.list_cursos:
                inflater.inflate(R.menu.menu_contextual_est, menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.actionEliminarAsignacion:
                //Eliminar curso de base
                model.deleteAsignacion(aux.getIdP(), miCursos.get(info.position).getIdC());
                Toast.makeText(this, "Curso " + miCursos.get(info.position).getDescripcion() + " ha sido retirado exitosamente", Toast.LENGTH_LONG).show();
                //Se refreca la lista con los cambios realizados
                miCursos = model.listCursoByEstudent(aux.getIdP());
                adapter = new ArrayAdapter<Curso>(this, R.layout.list_item_cursos, miCursos);
                list_cursos.setAdapter(adapter);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}