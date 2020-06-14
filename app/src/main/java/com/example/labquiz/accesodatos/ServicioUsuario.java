package com.example.labquiz.accesodatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.labquiz.logicaNegocio.Estudiante;
import com.example.labquiz.logicaNegocio.Usuario;

import java.util.ArrayList;

public class ServicioUsuario {
    private SQLiteDatabase db;
    private BaseDatos conexion;
    ArrayList<Usuario> misUsuarios;

    public ServicioUsuario(Context context) {
        misUsuarios = new ArrayList<>();
        conexion = new BaseDatos(context);
    }

    public ArrayList<Usuario> listaUsuarios() {
        misUsuarios = transactionShowInfo(1);
        return misUsuarios;
    }

    private ArrayList<Usuario> transactionShowInfo(int op) {
        Usuario miUsuario;
        ArrayList<Usuario> miUsuarios = new ArrayList<>();
        try {
            switch (op) {
                case 1://Listar estudiantes
                    db = conexion.getReadableDatabase();
                    //Primero se saca el idSemana de la semana en curso
                    Cursor fila = db.rawQuery("select * from Usuario;", null);
                    while (fila.moveToNext()) {
                        miUsuario = new Usuario();
                        miUsuario.setIdUsuario(fila.getString(0));
                        miUsuario.setClave(fila.getString(1));
                        miUsuario.setRol(fila.getString(2));
                        miUsuarios.add(miUsuario);
                    }
                    break;
            }
        } catch (SQLiteException e) {
            //report problem
        } finally {
            close();
        }

        return miUsuarios;
    }

    public void close() {
        db.close();
    }

}
