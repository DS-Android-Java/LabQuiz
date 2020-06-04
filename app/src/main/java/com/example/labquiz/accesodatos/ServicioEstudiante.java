package com.example.labquiz.accesodatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.labquiz.logicaNegocio.Estudiante;

import java.util.ArrayList;

public class ServicioEstudiante {

    final static String path = "/data/data/cis493.sqldatabases/bdLab10";
    private SQLiteDatabase db;

    public ServicioEstudiante() {}

    public ArrayList<Estudiante> listaEstudiantes(Context context){
        ArrayList<Estudiante> misEstudiantes = new ArrayList<>();
        misEstudiantes = transactionShowInfo(1, context);
        return  misEstudiantes;
    }

    public ArrayList<Estudiante> consultaEstudiantes(Context context){
        ArrayList<Estudiante> misEstudiantes = new ArrayList<>();
        misEstudiantes = transactionShowInfo(1, context);
        return  misEstudiantes;
    }

    public void open(String path) {
        try {
            db = SQLiteDatabase.openDatabase(
                    path,
                    null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
        } catch (SQLiteException e) {
            // Toast.makeText(this, e.getMessage(), 1).show();
        }
    }

    private ArrayList<Estudiante> transactionShowInfo(int op, Context context) {
        ArrayList<Estudiante> misEstudiantes = new ArrayList<>();
        Estudiante miEst;

        try {
            switch (op){
                case 1://Listar estudiantes
                    BaseDatos conn = new BaseDatos(context, "bdLab10", null,1);//Aca se abre la conexion
                    db = conn.getWritableDatabase();

                    //Primero se saca el idSemana de la semana en curso
                    Cursor fila = db.rawQuery("select * from Estudiante;", null);
                    while(fila.moveToNext()) {
                        miEst = new Estudiante();
                        miEst.setIdP(fila.getString(0));
                        miEst.setNombre(fila.getString(1));
                        miEst.setApellidos(fila.getString(2));
                        miEst.setEdad(fila.getString(3));

                        misEstudiantes.add(miEst);
                    }
                    conn.close();//Aca se cierra
                    break;
            }
            //perform your database operations here ... Se debería hacer un switch
            //db.setTransactionSuccessful(); //commit your changes
        } catch (SQLiteException e) {
            //report problem
        } finally {
            //db.endTransaction();
            //close();
        }

        return misEstudiantes;
    }
    public void close(){
        db.close();
    }
}
