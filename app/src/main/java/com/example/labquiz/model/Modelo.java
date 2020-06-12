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

    private Modelo(Context context) {
        this.misEstudiantes = new ArrayList<>();
        this.misCursos = new ArrayList<>();
        this.miSE = new ServicioEstudiante(context);
        this.miSC = new ServicioCurso(context);
    }


    public static Modelo getIntance(Context context) {
        if(mModelo == null){
            mModelo = new Modelo(context);
        }
        return mModelo;
    }

    public ArrayList<Estudiante> listEstudiantes(){
        misEstudiantes = miSE.listaEstudiantes();
        return misEstudiantes;
    }

    public boolean insertEstudiante(Estudiante miEst, String codCurso){
        return miSE.insertEstudiante(miEst,codCurso);
    }

    public int deleteEstudiante(String idEst){
        return miSE.deleteEstudiante(idEst);
    }

    public boolean updateEstudiante(Estudiante miEst){
        return miSE.updateEstudiante(miEst);
    }

    public int deleteAsignacion( String idEst,String idCurso){
        return miSE.deleteAsignacion(idEst,idCurso);
    }

    public boolean insertAsignacion(String idEst, String idCurso){
        return miSE.insertAsignacion(idEst,idCurso);
    }

    public ArrayList<Curso> listCursoByEstudent(String id){
        return miSC.listCursoByEstudent(id);
    }

    public ArrayList<Curso> listCurso(){
        misCursos = miSC.listarCurso();
        return  misCursos;
    }
}
