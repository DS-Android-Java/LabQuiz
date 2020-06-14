package com.example.labquiz.logicaNegocio;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String idUsuario;
    private String clave;
    private String rol;

    public Usuario() {
    }

    public Usuario(String idUsuario, String clave, String rol) {
        this.idUsuario = idUsuario;
        this.clave = clave;
        this.rol = rol;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
