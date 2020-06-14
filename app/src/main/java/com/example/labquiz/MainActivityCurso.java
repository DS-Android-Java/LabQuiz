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

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.labquiz.R;
import com.example.labquiz.adaptador.AdaptadorCurso;
import com.example.labquiz.helper.RecyclerItemTouchHelper;
import com.example.labquiz.logicaNegocio.Curso;
import com.example.labquiz.model.Modelo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivityCurso extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AdaptadorCurso.AdaptadorCursoListener {

    private RecyclerView mRecyclerView;
    private AdaptadorCurso mAdapter;
    private List<Curso> cursoList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton fab;
    private Modelo model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_curso);
        Toolbar toolbar = findViewById(R.id.toolbarC);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        getSupportActionBar().setTitle(getString(R.string.my_profesor));

        mRecyclerView = findViewById(R.id.recycler_cursosFld);
        cursoList = new ArrayList<>();
        model= Modelo.getIntance(this);
        cursoList= model.listCurso();
        mAdapter = new AdaptadorCurso(cursoList, this);
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
            if (viewHolder instanceof AdaptadorCurso.MyViewHolder) {
                // get the removed item name to display it in snack bar
                String idCurso = cursoList.get(viewHolder.getAdapterPosition()).getIdC();

                // save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
                // remove the item from recyclerView
               int result = model.deleteCurso(idCurso);
                if (result > 0) {
                    Toast.makeText(this, "Curso removido exitosamente!!!", Toast.LENGTH_LONG).show();
                    mAdapter.removeItem(viewHolder.getAdapterPosition());
                }else {
                    Toast.makeText(this, "No se puede eliminar el curso por que hay estudiantes matriculados!!!", Toast.LENGTH_LONG).show();
                }
                }
                } else{
            Curso aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            Intent intent = new Intent(this, add_update_curso.class);
            intent.putExtra("editable", true);
            intent.putExtra("curso", aux);
            mAdapter.notifyDataSetChanged();
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
    public void onContactSelected(Curso curso) { //TODO get the select item of recycleView
        Toast.makeText(getApplicationContext(), "Selected: " + curso.getIdC() + ", " + curso.getDescripcion(), Toast.LENGTH_LONG).show();
    }

    private void checkIntentInformation() {//Aca se realiza el update y el add
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Curso aux;
            aux = (Curso) getIntent().getSerializableExtra("addCurso");
            if (aux == null) {
                aux = (Curso) getIntent().getSerializableExtra("editCurso");
                if (aux != null) {
                if(model.updateCurso(aux)){
                cursoList= model.listCurso();
                mAdapter = new AdaptadorCurso(cursoList, this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Curso actualizado exitosamente!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, TabHostAdmin.class);
                startActivity(intent);
                finish();
                }else {
                    Toast.makeText(this, "Error al actualizar el curso", Toast.LENGTH_LONG).show();
                }
                }
                }else{
                if(model.insertCurso(aux)){
                    cursoList = model.listCurso();
                    mAdapter = new AdaptadorCurso(cursoList, this);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Curso agregado exitosamente!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, TabHostAdmin.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "Error al agregar  el curso!!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void goToAddUpdCurso() {
        Intent intent = new Intent(this, add_update_curso.class);
        intent.putExtra("editable", false);
        startActivity(intent);
    }
}