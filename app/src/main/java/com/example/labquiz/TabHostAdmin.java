package com.example.labquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.labquiz.logicaNegocio.Curso;
import com.example.labquiz.logicaNegocio.Estudiante;

public class TabHostAdmin extends TabActivity {

    public TabHost tabHost;
    private Estudiante est;
    private Curso curs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_host_admin);

        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        //tab1
        //TabHost.TabSpec spec = tabHost.newTabSpec("Cursos");
        /*Intent intentM = new Intent(this,AnimalList.class);
        spec.setContent(intentM);
        spec.setIndicator("",getResources().getDrawable(R.drawable.estudiante));
        tabHost.addTab(spec);*/

        //tab2
        TabHost.TabSpec spec = tabHost.newTabSpec("Estudantes");
        Intent intent = new Intent(this, MainActivity.class);
        spec.setContent(intent);
        spec.setIndicator("",getResources().getDrawable(R.drawable.estudiante));
        tabHost.addTab(spec);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // display the name of the tab whenever a tab is changed
                Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }
}