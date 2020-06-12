package com.example.labquiz.accesodatos;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.labquiz.logicaNegocio.Curso;

import java.util.ArrayList;

public class ServicioCurso {
    private SQLiteDatabase db;
    private BaseDatos conexion;

    public ServicioCurso(Context context) {
        conexion = new BaseDatos(context);
    }

    public ArrayList<Curso> listarCurso() {
        ArrayList<Curso> misCursos = new ArrayList<>();
        misCursos = transactionShowInfoCurso(1);
        return misCursos;
    }

    private ArrayList<Curso> transactionShowInfoCurso(int op) {
        ArrayList<Curso> misCursos = new ArrayList<>();
        Curso curso;

        try {
            switch (op) {
                case 1: //Listar cursos
                    db = conexion.getReadableDatabase();
                    Cursor fila = db.rawQuery("select * from Cursos;", null);
                    while (fila.moveToNext()) {
                        curso = new Curso();
                        curso.setIdC(fila.getString(0));
                        curso.setDescripcion(fila.getString(1));
                        curso.setCreditos(fila.getString(2));

                        misCursos.add(curso);
                    }
                    close();//Aca se cierra
                    break;
            }
        } catch (SQLException e) {
            //problem
        } finally {
            //close
        }
        return misCursos;
    }

    public ArrayList<Curso> listCursoByEstudent(String id) {
        ArrayList<Curso> misC = new ArrayList<>();
        try {
            db = conexion.getReadableDatabase();
            Curso curso;

            Cursor fila = db.rawQuery("select id_c,descripcion,creditos from Cursos C " +
                    "inner join Asignacion A ON C.id_c = A.fk_id_c " +
                    "inner join Estudiante E ON A.fk_id_e = E.id " +
                    "where E.id = '" + id + "';", null);

            while (fila.moveToNext()) {
                curso = new Curso();
                curso.setIdC(fila.getString(0));
                curso.setDescripcion(fila.getString(1));
                curso.setCreditos(fila.getString(2));

                misC.add(curso);
            }
        } catch (SQLiteException e) {
        } finally {
            close();//Aca se cierra
        }
        return misC;
    }

    public void close() {
        db.close();
    }
}
