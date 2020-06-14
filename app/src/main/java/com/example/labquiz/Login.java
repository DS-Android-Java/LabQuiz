package com.example.labquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labquiz.logicaNegocio.Usuario;
import com.example.labquiz.model.Modelo;

import java.util.List;

public class Login extends AppCompatActivity {

    private Button loginButton;
    private EditText username;
    private EditText password;
    private Modelo modelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginBtn);//Primero casting con el boton
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        modelData = Modelo.getIntance(this);

        // perform click event on the button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Evento escuchador del boton
                valida();
            }
        });
    }

    public void valida() {
        //ModelData md = new ModelData();
        Usuario userSingin = new Usuario();
        List<Usuario> users = modelData.listaUsuarios();
        Boolean founded = false;

        if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(this, "You must complete all the fields", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Validating", Toast.LENGTH_LONG).show();
            for (Usuario u : users) {
                if (u.getIdUsuario().equals(username.getText().toString()) && u.getClave().equals(password.getText().toString())) {
                    founded = true;
                    userSingin = u;
                }
            }
            if (founded == false) {
                Toast.makeText(this, "El usuario o la clave son incorrectos", Toast.LENGTH_LONG).show();
            } else {
                if (userSingin.getRol().equals("admin")) {
                    ///Se dirige al nav drawer
                } else if (userSingin.getRol().equals("estudiante")) {
                    Intent i = new Intent(this, ListCursosAsignados.class);
                    i.putExtra("usuarioLogueado", userSingin);
                    startActivity(i);
                    finish();
                }
            }
        }
    }
}