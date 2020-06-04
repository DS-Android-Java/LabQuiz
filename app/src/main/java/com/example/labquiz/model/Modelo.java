package com.example.labquiz.model;

import android.content.Context;

import com.example.labquiz.accesodatos.ServicioEstudiante;
import com.example.labquiz.logicaNegocio.Estudiante;

import java.util.ArrayList;

public class Modelo {
    private ServicioEstudiante miSE;
    private ArrayList<Estudiante> misEstudiantes;

    private static Modelo mModelo;

    private Modelo() {
        this.misEstudiantes = new ArrayList<>();
        this.miSE = new ServicioEstudiante();
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
}
