package com.example.labquiz.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.labquiz.R;
import com.example.labquiz.logicaNegocio.Estudiante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AdaptadorEstudiante extends RecyclerView.Adapter<AdaptadorEstudiante.MyViewHolder> implements Filterable {

    private List<Estudiante> estudianteList;
    private List<Estudiante> estudianteListFiltered;
    private AdaptadorEstudianteListener listener;
    private Estudiante deletedItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo1, titulo2, description,profe;
        //two layers
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;

        public MyViewHolder(View view) {
            super(view);
            titulo1 = view.findViewById(R.id.titleFirstLbl);
            titulo2 = view.findViewById(R.id.titleSecLbl);
            description = view.findViewById(R.id.descriptionLbl);
            profe = view.findViewById(R.id.profAsignado);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(estudianteListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public AdaptadorEstudiante(List<Estudiante> estudiantelist, AdaptadorEstudianteListener listener) {
        this.estudianteList = estudiantelist;
        this.listener = listener;
        //init filter
        this.estudianteListFiltered = estudiantelist;
    }

    @Override
    public AdaptadorEstudiante.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdaptadorEstudiante.MyViewHolder holder, int position) {//Lo que se muestra en cada celda de cursos del recycler view
        // rendering view
        final Estudiante estudiante = estudianteListFiltered.get(position);
        holder.titulo1.setText(estudiante.getIdP());
        holder.titulo2.setText(estudiante.getNombre());
        holder.description.setText(estudiante.getApellidos() + " Créditos");
        holder.profe.setText("   Curso asignado: "+estudiante.getCursosAsignados().getIdC() +" "+ estudiante.getCursosAsignados().getDescripcion());
    }

    @Override
    public int getItemCount() { return estudianteListFiltered.size(); }

    public void removeItem(int position) {//Funcion para eliminar el curso de la lista
        deletedItem = estudianteListFiltered.remove(position);
        Iterator<Estudiante> iter = estudianteList.iterator();
        while (iter.hasNext()) {//Se recorre la lista de cursos
            Estudiante aux = iter.next();
            if (deletedItem.equals(aux))//Hasta encontrar el curso que es igual
                iter.remove();//Y cuando lo encuentra lo remueve de la lista
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (estudianteListFiltered.size() == estudianteList.size()) {
            estudianteListFiltered.add(position, deletedItem);
        } else {
            estudianteListFiltered.add(position, deletedItem);
            estudianteList.add(deletedItem);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Estudiante getSwipedItem(int index) {
        if (this.estudianteList.size() == this.estudianteListFiltered.size()) { //not filtered yet
            return estudianteList.get(index);
        } else {
            return estudianteListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (estudianteList.size() == estudianteListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(estudianteList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(estudianteList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(estudianteListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(estudianteListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    estudianteListFiltered = estudianteList;
                } else {
                    List<Estudiante> filteredList = new ArrayList<>();
                    for (Estudiante row : estudianteList) {
                        // filter use two parameters
                        if (row.getIdP().toLowerCase().contains(charString.toLowerCase()) || row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    estudianteListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = estudianteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                estudianteListFiltered = (ArrayList<Estudiante>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdaptadorEstudianteListener {
        void onContactSelected(Estudiante estudiante);
    }

}
