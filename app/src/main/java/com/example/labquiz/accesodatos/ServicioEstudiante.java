package com.example.labquiz.accesodatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.labquiz.logicaNegocio.Estudiante;

import java.util.ArrayList;

public class ServicioEstudiante {
    private SQLiteDatabase db;
    private BaseDatos conexion;
    ArrayList<Estudiante> misEstudiantes;
    public ServicioEstudiante(Context context)
    {
        misEstudiantes = new ArrayList<>();
        conexion = new BaseDatos(context);
    }

    public ArrayList<Estudiante> listaEstudiantes(){
        misEstudiantes = transactionShowInfo(1);
        return  misEstudiantes;
    }

    private ArrayList<Estudiante> transactionShowInfo(int op) {
        Estudiante miEst;
        ArrayList<Estudiante> miEsts = new ArrayList<>();

        try {
            switch (op){
                case 1://Listar estudiantes
                    db = conexion.getReadableDatabase();
                    //Primero se saca el idSemana de la semana en curso
                    Cursor fila = db.rawQuery("select * from Estudiante;", null);
                    while(fila.moveToNext()) {
                        miEst = new Estudiante();
                        miEst.setIdP(fila.getString(0));
                        miEst.setNombre(fila.getString(1));
                        miEst.setApellidos(fila.getString(2));
                        miEst.setEdad(fila.getString(3));

                        miEsts.add(miEst);
                    }
                    //close();//Aca se cierra
                    break;
            }
            //perform your database operations here ... Se debería hacer un switch
            //db.setTransactionSuccessful(); //commit your changes
        } catch (SQLiteException e) {
            //report problem
        } finally {
            //db.endTransaction();
            close();
        }

        return miEsts;
    }

    public boolean insertEstudiante(Estudiante miEst, String codCurso){
        //BaseDatos conn = new BaseDatos(context);//Aca se abre la conexion
        db = conexion.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",miEst.getIdP());
        contentValues.put("nombre",miEst.getNombre());
        contentValues.put("apellidos",miEst.getApellidos());
        contentValues.put("edad",miEst.getEdad());

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("fk_id_e",miEst.getIdP());
        contentValues1.put("fk_id_c",miEst.getCursosAsignados().get(0).getIdC());
        //Se ralizan las inserciones en la base
        long resultado = db.insert("estudiante",null,contentValues);//Se inserta el estudiante
        long resultado2 = db.insert("asignacion",null,contentValues1);//Se inserta la asignacion
        close();
        if(resultado == -1 || resultado2 == -1){//Es error
            return false;
        }else {
            return true;
        }
    }

    public int deleteEstudiante(String idEst){
        //BaseDatos conn = new BaseDatos(context);//Aca se abre la conexion
        db = conexion.getWritableDatabase();
        db.delete("asignacion","fk_id_e=?", new String[] {idEst});
        int resultado =  db.delete("estudiante","id=?", new String[] {idEst});
        close();
        return resultado;
    }

    public int deleteAsignacion(String idEst, String idCurso){
        db = conexion.getWritableDatabase();
        int resultado = db.delete("asignacion","fk_id_e=? and fk_id_c=?", new String[] {idEst,idCurso});
        close();
        return resultado;
    }

    public void close(){
        db.close();
    }
}
