package com.example.labquiz.accesodatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.labquiz.logicaNegocio.Asignacion;
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

    public ArrayList<Asignacion> findAsignacion(String idCurso){
        ArrayList<Asignacion> misAsignacion = new ArrayList<>();
        Asignacion asignacion;
        db = conexion.getReadableDatabase();
        Cursor fila = db.rawQuery("select * from Asignacion where fk_id_c='"+idCurso+"';", null);
        while (fila.moveToNext()) {
            asignacion = new Asignacion();
            asignacion.setIdE(fila.getString(0));
            asignacion.setIdC(fila.getString(1));

            misAsignacion.add(asignacion);
        }
        close();//Aca se cierra

        return misAsignacion;
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
    public boolean insertCurso (Curso miCurso){
        long resultado = 0;
        try{
            db = conexion.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_c", miCurso.getIdC());
            contentValues.put("descripcion", miCurso.getDescripcion());
            contentValues.put("creditos", miCurso.getCreditos());

            resultado = db.insert("cursos", null, contentValues);
        }catch (SQLiteException e) {
        } finally {
            close();
        }
        if (resultado == -1) {// r == false){//Es error
            return false;
        } else {
            return true;
        }
    }

    public boolean updateCurso(Curso miCurso){
        long resultado= 0;
        try{
            db= conexion.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_c", miCurso.getIdC());
            contentValues.put("descripcion", miCurso.getDescripcion());
            contentValues.put("creditos", miCurso.getCreditos());

            resultado=db.update("cursos", contentValues, "id_c=?", new String[]{miCurso.getIdC()});
        }catch (SQLiteException e) {
        } finally {
            close();
        }
        if (resultado == -1) {//Es error
            return false;
        } else {
            return true;
        }
    }
    public int deleteCurso(String idCurso){
        int resultado =0;
        try{
            db=conexion.getWritableDatabase();
            db.delete("cursos", "id_c=?", new String[]{idCurso});
        }catch (SQLiteException e) {
        } finally {
            close();
        }
        return resultado;
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
