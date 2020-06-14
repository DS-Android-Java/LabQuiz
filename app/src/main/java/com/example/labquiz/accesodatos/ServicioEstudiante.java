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

    public ServicioEstudiante(Context context) {
        misEstudiantes = new ArrayList<>();
        conexion = new BaseDatos(context);
    }

    public ArrayList<Estudiante> listaEstudiantes() {
        misEstudiantes = transactionShowInfo(1,"");
        return misEstudiantes;
    }

    public ArrayList<Estudiante> findEstudiante(String idEst) {
        misEstudiantes = transactionShowInfo(2,idEst);
        return misEstudiantes;
    }



    private ArrayList<Estudiante> transactionShowInfo(int op,String idEst) {
        Estudiante miEst;
        ArrayList<Estudiante> miEsts = new ArrayList<>();
        try {
            switch (op) {
                case 1://Listar estudiantes
                    db = conexion.getReadableDatabase();
                    //Primero se saca el idSemana de la semana en curso
                    Cursor fila = db.rawQuery("select * from Estudiante;", null);
                    while (fila.moveToNext()) {
                        miEst = new Estudiante();
                        miEst.setIdP(fila.getString(0));
                        miEst.setNombre(fila.getString(1));
                        miEst.setApellidos(fila.getString(2));
                        miEst.setEdad(fila.getString(3));

                        miEsts.add(miEst);
                    }
                    break;
                case 2:
                    db = conexion.getReadableDatabase();
                    Cursor fila2 = db.rawQuery("select * from Estudiante where id ='"+idEst+"';", null);
                    while (fila2.moveToNext()) {
                        miEst = new Estudiante();
                        miEst.setIdP(fila2.getString(0));
                        miEst.setNombre(fila2.getString(1));
                        miEst.setApellidos(fila2.getString(2));
                        miEst.setEdad(fila2.getString(3));

                        miEsts.add(miEst);
                    }
                    break;
            }
        } catch (SQLiteException e) {
            //report problem
        } finally {
            close();
        }

        return miEsts;
    }

    public boolean insertEstudiante(Estudiante miEst, String codCurso) {
        long resultado = 0;
        long resultado2 = 0;
        long resultado3 = 0;
        try {
            db = conexion.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", miEst.getIdP());
            contentValues.put("nombre", miEst.getNombre());
            contentValues.put("apellidos", miEst.getApellidos());
            contentValues.put("edad", miEst.getEdad());

            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("fk_id_e", miEst.getIdP());
            contentValues1.put("fk_id_c", miEst.getCursosAsignados().get(0).getIdC());

            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("idUsuario", miEst.getIdP());
            contentValues2.put("clave", miEst.getIdP());
            contentValues2.put("rol", "estudiante");

            //Se ralizan las inserciones en la base
            resultado = db.insert("estudiante", null, contentValues);//Se inserta el estudiante
            //boolean r = insertAsignacion(miEst.getIdP(),codCurso);
            resultado2 = db.insert("asignacion", null, contentValues1);//Se inserta la asignacion
            resultado3 = db.insert("usuario", null, contentValues2);//Se inserta la asignacion
        } catch (SQLiteException e) {
        } finally {
            close();
        }
        if (resultado == -1 || resultado2 == -1 || resultado3 == -1) {// r == false){//Es error
            return false;
        } else {
            return true;
        }
    }

    public boolean updateEstudiante(Estudiante miEst) {
        long resultado = 0;
        try {
            db = conexion.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", miEst.getIdP());
            contentValues.put("nombre", miEst.getNombre());
            contentValues.put("apellidos", miEst.getApellidos());
            contentValues.put("edad", miEst.getEdad());

            //Se ralizan las inserciones en la base
            resultado = db.update("estudiante", contentValues, "id=?", new String[]{miEst.getIdP()});//Se inserta el estudiante
        } catch (SQLiteException e) {
        } finally {
            close();
        }
        if (resultado == -1) {//Es error
            return false;
        } else {
            return true;
        }
    }

    public int deleteEstudiante(String idEst) {
        int resultado = 0;
        try {
            db = conexion.getWritableDatabase();
            db.delete("asignacion", "fk_id_e=?", new String[]{idEst});
            resultado = db.delete("estudiante", "id=?", new String[]{idEst});
        } catch (SQLiteException e) {
        } finally {
            close();
        }
        return resultado;
    }

    public int deleteAsignacion(String idEst, String idCurso) {
        int resultado = 0;
        try {
            db = conexion.getWritableDatabase();
            resultado = db.delete("asignacion", "fk_id_e=? and fk_id_c=?", new String[]{idEst, idCurso});
        } catch (SQLiteException e) {
        } finally {
            close();
        }
        return resultado;
    }

    public boolean insertAsignacion(String idEst, String idCurso) {
        long resultado = 0;
        try {
            db = conexion.getWritableDatabase();
            //Se cargan los datos en el content para la insercion
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("fk_id_e", idEst);
            contentValues1.put("fk_id_c", idCurso);
            resultado = db.insert("asignacion", null, contentValues1);//Se inserta la asignacion
        } catch (SQLiteException e) {
        } finally {
            close();
        }
        if (resultado == -1) {//Es error
            return false;
        } else {
            return true;
        }
    }

    public void close() {
        db.close();
    }
}
