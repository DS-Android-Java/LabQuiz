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

    public BaseDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//Aca se crea la base
        db.execSQL("create table Estudiante("
                +" id text,"+"nombre text,"+"apellidos text,"+"edad text );");
        //Se insertan unos valores para probar
        db.execSQL( "insert into Estudiante(id, nombre, apellidos,edad) values ('207830003', 'Diego','Salazar Perez','21');" );
        db.execSQL( "insert into Estudiante(id, nombre, apellidos,edad) values ('623929292', 'Allison','Madriz Valverde', '25');" );
        db.execSQL( "insert into Estudiante(id, nombre, apellidos,edad) values ('202700678', 'Vanessa','Perez Cruz','43' );" );

        /*db.execSQL("create table semana(idSemana int primary key AUTOINCREMENT, fechaInicio text, fechaFinal text," +
                " horasLaboradasFinal real , salario real ,estado text , adelanto real)");*/

        //execSQL("create table tblAMIGO(" + " recIDinteger PRIMARY KEY autoincrement, " + " name text, "+ " phone text ); " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Estudiante");
        onCreate(db);
    }


}
