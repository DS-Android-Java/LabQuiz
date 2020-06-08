package com.example.labquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.labquiz.adaptador.AdaptadorEstudiante;
import com.example.labquiz.helper.RecyclerItemTouchHelper;
import com.example.labquiz.logicaNegocio.Estudiante;
import com.example.labquiz.model.Modelo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AdaptadorEstudiante.AdaptadorEstudianteListener {

    private RecyclerView mRecyclerView;
    private AdaptadorEstudiante mAdapter;
    private ArrayList<Estudiante> estudianteList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton fab;
    private Modelo model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarC);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        getSupportActionBar().setTitle(getString(R.string.my_curso));

        model = Modelo.getIntance(this);
        mRecyclerView = findViewById(R.id.recycler_estudiantesFld);
        estudianteList = new ArrayList<>();
        estudianteList = model.listEstudiantes();//Aca se cargan los estudiantes de la base
        mAdapter = new AdaptadorEstudiante(estudianteList, this);
        coordinatorLayout = findViewById(R.id.coordinator_layoutC);

        // white background notification bar
        whiteNotificationBar(mRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        // go to update or add career
        fab = findViewById(R.id.addBtnC);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddUpdCurso();
            }
        });

        //Registro de controles de menus contextuales
        registerForContextMenu(mRecyclerView);
        //delete swiping left and right
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        //should use database info

        // Receive the Carrera sent by AddUpdCarreraActivity
        checkIntentInformation();

        //refresh view
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.START) {
            if (viewHolder instanceof AdaptadorEstudiante.MyViewHolder) {
                // get the removed item name to display it in snack bar
                String idEst = estudianteList.get(viewHolder.getAdapterPosition()).getIdP();

                // save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
                //remove from the database
                int result = model.deleteEstudiante(idEst);
                if(result > 0){
                    Toast.makeText(this,"Estudiante removido exitosamente!!!" ,Toast.LENGTH_LONG).show();
                    // remove the item from recyclerView
                    mAdapter.removeItem(viewHolder.getAdapterPosition());
                }else {
                    Toast.makeText(this,"No se puedo eliminar el estudiante!!!",Toast.LENGTH_LONG).show();
                }
            }
        } else {
            //If is editing a row object
            Estudiante aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, add_update_estudiante.class);
            intent.putExtra("editable", true);
            intent.putExtra("estudiante", aux);//Se pasa el objeto curso desde la lista de cursos
            mAdapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        }
    }

    @Override
    public void onItemMove(int source, int target) {
        mAdapter.onItemMove(source, target);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cursoList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        } 
    }

    @Override
    public void onContactSelected(Estudiante estudiante) { //TODO get the select item of recycleView
        Intent intent = new Intent(this, ListCursosAsignados.class);
        intent.putExtra("idEstudiante", estudiante);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Selected: " + estudiante.getIdP() + ", " + estudiante.getNombre(), Toast.LENGTH_LONG).show();
    }

    private void checkIntentInformation() {//Aca se realiza el update y el add en la base
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Estudiante aux;
            aux = (Estudiante) getIntent().getSerializableExtra("addEstudiante");
            if (aux == null) {
                aux = (Estudiante) getIntent().getSerializableExtra("editEstudiante");
                if (aux != null) {//Accion de actualizar

                }
            } else {//Accion de agregar
                if(model.insertEstudiante(aux,aux.getCursosAsignados().get(0).getIdC())){
                    estudianteList = model.listEstudiantes();//Se refrescan los estudiantes con el nuevo agregado
                    mAdapter = new AdaptadorEstudiante(estudianteList,this);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(this,"Estudiante agregado exitosamente!!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"Error al agregar el estudiante!!!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void goToAddUpdCurso() {
        Intent intent = new Intent(this, add_update_estudiante.class);
        intent.putExtra("editable", false);
        startActivity(intent);
    }
}

