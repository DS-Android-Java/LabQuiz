package com.example.labquiz.logicaNegocio;

import java.io.Serializable;

public class Curso implements Serializable {
    private String idC;
    private String descripcion;
    private String creditos;

    public Curso(String idC, String descripcion, String creditos) {
        this.idC = idC;
        this.descripcion = descripcion;
        this.creditos = creditos;
    }

    public Curso() {
        this.idC = new String();
        this.descripcion = new String();
        this.creditos = new String();
    }

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreditos() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos = creditos;
    }

    @Override
    public String toString() {
        return "Codigo: " + idC + "\n Descripcion: " + descripcion + "\n Creditos: " + creditos;
    }
}
