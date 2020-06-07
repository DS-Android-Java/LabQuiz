package com.example.labquiz.logicaNegocio;

import java.io.Serializable;
import java.util.ArrayList;

public class Estudiante implements Serializable {
    private String idP;
    private String nombre;
    private String apellidos;
    private String edad;
    //private Curso cursosAsignados;
    private ArrayList<Curso> cursosAsignados;

    public Estudiante(String idP, String nombre, String apellidos, String edad, ArrayList<Curso> cursosAsignados){//Curso cursosAsignados) {
        this.idP = idP;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.cursosAsignados = cursosAsignados;
    }

    public Estudiante() {
        this.idP = new String();
        this.nombre = new String();
        this.apellidos = new String();
        this.edad = new String();
        this.cursosAsignados = new ArrayList<>();
    }

    public String getIdP() {
        return idP;
    }

    public void setIdP(String idP) {
        this.idP = idP;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public ArrayList<Curso> getCursosAsignados() {
        return cursosAsignados;
    }

    public void setCursosAsignados(ArrayList<Curso> cursosAsignados) {
        this.cursosAsignados = cursosAsignados;
    }

    @Override
    public String toString() {
        return "Estudiante{" + "idP=" + idP + ", nombre=" + nombre + ", apellidos=" + apellidos + ", edad=" + edad + ", cursosAsignados=" + cursosAsignados + '}';
    }
}
