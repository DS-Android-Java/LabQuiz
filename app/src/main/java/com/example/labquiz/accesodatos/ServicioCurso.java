package com.example.labquiz.accesodatos;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.labquiz.logicaNegocio.Curso;

import java.util.ArrayList;

public class ServicioCurso {
    final static String path = "/data/data/cis493.sqldatabases/bdLab10";
    private SQLiteDatabase db;

    public ServicioCurso() {
    }

    public ArrayList<Curso> listarCurso(Context context){
        ArrayList<Curso> misCursos =  new ArrayList<>();
        misCursos = transactionShowInfoCurso(1, context);
        return misCursos;
    }

    public ArrayList<Curso> consultaCurso(Context context){
        ArrayList<Curso> misCursos =  new ArrayList<>();
        misCursos = transactionShowInfoCurso(1, context);
        return misCursos;
    }
    public void open(String path){
        try {
            db = SQLiteDatabase.openDatabase(
                    path,
                    null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
        } catch (SQLiteException e) {
            // Toast.makeText(this, e.getMessage(), 1).show();
        }
    }
    private  ArrayList<Curso> transactionShowInfoCurso(int op, Context context) {
        ArrayList<Curso> misCursos = new ArrayList<>();
        Curso curso;

        try{
            switch (op){
                case 1: //Listar cursos
                    BaseDatos conexion = new BaseDatos(context);//Aca se abre la conexion
                    db = conexion.getWritableDatabase();
                    Cursor fila = db.rawQuery("select * from Cursos;", null);
                    while(fila.moveToNext()) {
                        curso = new Curso();
                        curso.setIdC(fila.getString(0));
                        curso.setDescripcion(fila.getString(1));
                        curso.setCreditos(fila.getString(2));

                        misCursos.add(curso);
                    }
                    conexion.close();//Aca se cierra
                    break;
            }
        }catch (SQLException e){
            //problem
        }finally {
            //close
        }
        return misCursos;
    }

    public ArrayList<Curso> listCursoByEstudent(Context context,String id){
        ArrayList<Curso>misC = new ArrayList<>();
        BaseDatos conexion = new BaseDatos(context);//Aca se abre la conexion
        db = conexion.getWritableDatabase();
        Curso curso;

        Cursor fila = db.rawQuery("select id_c,descripcion,creditos from Cursos C " +
                "inner join Asignacion A ON C.id_c = A.fk_id_c " +
                "inner join Estudiante E ON A.fk_id_e = E.id " +
                "where E.id = '"+id+"';", null);

        while(fila.moveToNext()) {
            curso = new Curso();
            curso.setIdC(fila.getString(0));
            curso.setDescripcion(fila.getString(1));
            curso.setCreditos(fila.getString(2));

            misC.add(curso);
        }
        conexion.close();//Aca se cierra
        return misC;
    }

    public  void close(){
        db.close();
    }
}
