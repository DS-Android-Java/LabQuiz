package com.example.labquiz.model;

import android.content.ContentValues;
import android.content.Context;

import com.example.labquiz.accesodatos.ServicioCurso;
import com.example.labquiz.accesodatos.ServicioEstudiante;
import com.example.labquiz.logicaNegocio.Curso;
import com.example.labquiz.logicaNegocio.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class Modelo {
    private ServicioEstudiante miSE;
    private ServicioCurso miSC;
    private ArrayList<Estudiante> misEstudiantes;
    private  ArrayList<Curso> misCursos;

    private static Modelo mModelo;

    private Modelo() {
        this.misEstudiantes = new ArrayList<>();
        this.miSE = new ServicioEstudiante();
        this.misCursos = new ArrayList<>();
        this.miSC = new ServicioCurso();
    }


    public static Modelo getIntance() {
        if(mModelo == null){
            mModelo = new Modelo();
        }
        return mModelo;
    }

    public ArrayList<Estudiante> listEstudiantes(Context context){
        misEstudiantes = miSE.listaEstudiantes(context);
        return misEstudiantes;
    }

    public boolean insertEstudiante(Estudiante miEst, String codCurso, Context context){
        return miSE.insertEstudiante(miEst,codCurso, context);
    }

    public int deleteEstudiante(String idEst, Context context){
        return miSE.deleteEstudiante(idEst,context);
    }

    public ArrayList<Curso> listCurso(Context context){
        misCursos = miSC.listarCurso(context);
        return  misCursos;
    }
}
