package com.example.labquiz.accesodatos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.labquiz.logicaNegocio.Estudiante;

import java.util.ArrayList;

public class BaseDatos extends SQLiteOpenHelper {

    final static String tableName = "bdLab10";
    public BaseDatos(@Nullable Context context) {
        super(context, tableName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//Aca se crea la base
        db.execSQL("create table Estudiante("
                +" id text,"+"nombre text,"+"apellidos text,"+"edad text );");
        //Se insertan unos valores para probar
        db.execSQL( "insert into Estudiante(id, nombre, apellidos,edad) values ('207830003', 'Diego','Salazar Perez','21');" );
        db.execSQL( "insert into Estudiante(id, nombre, apellidos,edad) values ('623929292', 'Allison','Madriz Valverde', '25');" );
        db.execSQL( "insert into Estudiante(id, nombre, apellidos,edad) values ('202700678', 'Vanessa','Perez Cruz','43' );" );

        db.execSQL("create table Cursos("
            +"id_c text,"+"descripcion text," +"creditos text);");

        db.execSQL("insert into Cursos(id_c, descripcion,creditos) values ('EIF204','Dispositivos Moviles','8');" );
        db.execSQL("insert into Cursos(id_c, descripcion,creditos) values ('EIF205','Bases de Datos','4');" );
        db.execSQL("insert into Cursos(id_c, descripcion,creditos) values ('EIF206','Ingenieria I','3');" );

        db.execSQL("create table Asignacion("
                + "fk_id_e text,"+"fk_id_c text," +"FOREIGN KEY(fk_id_c) REFERENCES Cursos(id_c),"+
                "FOREIGN KEY(fk_id_e) REFERENCES Estudiante(id));");

        db.execSQL("insert into Asignacion(fk_id_e, fk_id_c) values ('207830003','EIF204');");
        db.execSQL("insert into Asignacion(fk_id_e, fk_id_c) values ('207830003','EIF205');");
        db.execSQL("insert into Asignacion(fk_id_e, fk_id_c) values ('207830003','EIF206');");
        db.execSQL("insert into Asignacion(fk_id_e, fk_id_c) values ('623929292','EIF205');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Estudiante");
        db.execSQL("DROP TABLE IF EXISTS " + "Cursos");
        db.execSQL("DROP TABLE IF EXISTS " + "Asginacion");
        onCreate(db);
    }
}
