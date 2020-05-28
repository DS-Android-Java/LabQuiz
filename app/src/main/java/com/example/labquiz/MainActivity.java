package com.example.labquiz;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.labquiz.adaptador.AdaptadorEstudiante;
import com.example.labquiz.helper.RecyclerItemTouchHelper;
import com.example.labquiz.logicaNegocio.Estudiante;
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

    //Url listar
    String apiUrl = "http://192.168.0.6:8080/Backend_LabQuiz/modelos/estudiantemodelos/list?";
    //String apiUrl = "http://10.0.2.2:8080/Backend_JSON/modelos/curso/list";//Esta para emulador

    //Url operaciones
    String apiUrlAcciones = "http://192.168.0.6:8080/Backend_LabQuiz/Estudiante/Operaciones?";
    //String apiUrlAcciones = "http://10.0.2.2:8080/Backend_JSON/Controlador/curso?";//Esta para emulador

    String apiUrlTemp;

    private RecyclerView mRecyclerView;
    private AdaptadorEstudiante mAdapter;
    private List<Estudiante> estudianteList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton fab;
    ProgressDialog progressDialog;
    //private ModelData model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarC);
        setSupportActionBar(toolbar);
        apiUrlTemp = apiUrl;

        //toolbar fancy stuff
        getSupportActionBar().setTitle(getString(R.string.my_curso));

        mRecyclerView = findViewById(R.id.recycler_estudiantesFld);
        estudianteList = new ArrayList<>();
        estudianteList = new ArrayList<>();
        mAdapter = new AdaptadorEstudiante(estudianteList, this);
        coordinatorLayout = findViewById(R.id.coordinator_layoutC);

        // white background notification bar
        whiteNotificationBar(mRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        //AsyncTask aca se usa el web service para cargar los datos de la base del profesor
        MyAsyncTasksCursoOperaciones myAsyncTasksOC = new MyAsyncTasksCursoOperaciones();
        myAsyncTasksOC.execute();

        // go to update or add career
        fab = findViewById(R.id.addBtnC);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddUpdCurso();
            }
        });

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
                String name = estudianteList.get(viewHolder.getAdapterPosition()).getNombre();
                String idCurso = estudianteList.get(viewHolder.getAdapterPosition()).getIdP();

                apiUrlTemp = apiUrlAcciones + "acc=deleteE"+ "&id_Estudiante=" + idCurso;

                MyAsyncTasksCursoOperaciones myAsyncTasksCursoOperaciones = new MyAsyncTasksCursoOperaciones();
                myAsyncTasksCursoOperaciones.execute();

                // save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
                // remove the item from recyclerView
                mAdapter.removeItem(viewHolder.getAdapterPosition());

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

    public class MyAsyncTasksCursoOperaciones extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            /*progressDialog = new ProgressDialog(MantenimientoProfesorActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";


            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlTemp);

                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                    }

                    // return the data to onPostExecute method
                    Log.w("JSON",current);
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            // dismiss the progress dialog after receiving data from API
            //progressDialog.dismiss();

            //Json
            try{
                Gson gson = new Gson();

                JSONObject jsonObjectMensaje = new JSONObject(s);
                boolean estado = jsonObjectMensaje.getBoolean("error");
                String mensaje = jsonObjectMensaje.getString("mensaje");
                String listC = jsonObjectMensaje.getString("listEst");
                //Se muestra el mensaje de estado de operacion
                Toast.makeText(MainActivity.this,mensaje,Toast.LENGTH_LONG).show();

                estudianteList = (ArrayList<Estudiante>) gson.fromJson(listC,
                        new TypeToken<ArrayList<Estudiante>>() {
                        }.getType());

                mAdapter = new AdaptadorEstudiante(estudianteList, MainActivity.this);
                coordinatorLayout = findViewById(R.id.coordinator_layoutC);

                //white background notification bar
                whiteNotificationBar(mRecyclerView);
                Log.d("dataEstudiantesssss", listC);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
                mRecyclerView.setAdapter(mAdapter);

            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("JSONMENSAJE",s);
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
        Toast.makeText(getApplicationContext(), "Selected: " + estudiante.getIdP() + ", " + estudiante.getNombre(), Toast.LENGTH_LONG).show();
    }

    private void checkIntentInformation() {//Aca se realiza el update y el add en la base
        Bundle extras = getIntent().getExtras();
        Gson gson = new Gson();
        if (extras != null) {
            Estudiante aux;
            aux = (Estudiante) getIntent().getSerializableExtra("addEstudiante");
            if (aux == null) {
                aux = (Estudiante) getIntent().getSerializableExtra("editEstudiante");
                if (aux != null) {//Accion de actualizar
                    //found an item that can be updated
                    String cursoU = "";
                    cursoU = gson.toJson(aux);

                    apiUrlTemp = apiUrlAcciones+"acc=updateE" +"&estudianteU="+cursoU +"&curso_id="+aux.getCursosAsignados().getIdC();
                    MyAsyncTasksCursoOperaciones myAsyncTasksOp = new MyAsyncTasksCursoOperaciones();
                    myAsyncTasksOp.execute();
                }
            } else {//Accion de agregar
                //found a new Curso Object
                String cursoA = "";
                cursoA = gson.toJson(aux);

                apiUrlTemp = apiUrlAcciones+"acc=addE" +"&estudianteA="+cursoA +"&curso_id="+aux.getCursosAsignados().getIdC();
                MyAsyncTasksCursoOperaciones myAsyncTasksOp = new MyAsyncTasksCursoOperaciones();
                myAsyncTasksOp.execute();
            }
        }
    }

    private void goToAddUpdCurso() {
        Intent intent = new Intent(this, add_update_estudiante.class);
        intent.putExtra("editable", false);
        startActivity(intent);
    }
}

