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
import android.widget.TextView;
import android.widget.Toast;

import com.example.labquiz.logicaNegocio.Curso;
import com.example.labquiz.logicaNegocio.Estudiante;
import com.example.labquiz.model.Modelo;

import java.util.ArrayList;

public class ListCursosAsignados extends AppCompatActivity {

    private ArrayList<Curso> miCursos;
    private Modelo model;
    private ListView list_cursos;
    private TextView textInfo;
    private Estudiante aux;
    private ArrayAdapter<Curso> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cursos_asignados);
        model = Modelo.getIntance(this);

        list_cursos = findViewById(R.id.list_cursos);
        textInfo = findViewById(R.id.textInfo);

        miCursos = new ArrayList<>();
        aux = (Estudiante) getIntent().getSerializableExtra("idEstudiante");
        miCursos = model.listCursoByEstudent(aux.getIdP());
        textInfo.setText("Nombre: " + aux.getNombre() + " " + aux.getApellidos() + "\n"
                + "Id: " + aux.getIdP() + "\n"
                + "Edad: " + aux.getEdad());

        adapter = new ArrayAdapter<Curso>(this, R.layout.list_item_cursos,miCursos);
        list_cursos.setAdapter(adapter);

        //Menu contextual
        registerForContextMenu(list_cursos);

        //Aca se prepara el popup
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int)(ancho * 0.90), (int)(alto * 0.90));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        int id=v.getId();
        switch (id){
            case R.id.list_cursos:
                inflater.inflate(R.menu.menu_contextual_est,menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.actionEliminarAsignacion:
                //Eliminar curso de base
                model.deleteAsignacion(aux.getIdP(),miCursos.get(info.position).getIdC());
                Toast.makeText(this,"Curso " + miCursos.get(info.position).getDescripcion()+ " ha sido retirado exitosamente",Toast.LENGTH_LONG).show();
                //Se refreca la lista con los cambios realizados
                miCursos = model.listCursoByEstudent(aux.getIdP());
                adapter = new ArrayAdapter<Curso>(this, R.layout.list_item_cursos,miCursos);
                list_cursos.setAdapter(adapter);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}