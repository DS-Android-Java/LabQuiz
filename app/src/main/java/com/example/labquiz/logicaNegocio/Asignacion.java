package com.example.labquiz.logicaNegocio;

import java.io.Serializable;

public class Asignacion implements Serializable {
    private String idC;
    private String idE;

    public Asignacion(String idC, String idE) {
        this.idC = idC;
        this.idE = idE;
    }

    public Asignacion() {
    }

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getIdE() {
        return idE;
    }

    public void setIdE(String idE) {
        this.idE = idE;
    }
}
