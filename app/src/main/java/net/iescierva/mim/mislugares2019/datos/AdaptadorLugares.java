package net.iescierva.mim.mislugares2019.datos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import net.iescierva.mim.mislugares2019.Aplicacion;
import net.iescierva.mim.mislugares2019.R;
import net.iescierva.mim.mislugares2019.modelo.GeoPunto;
import net.iescierva.mim.mislugares2019.modelo.Lugar;

import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorLugares extends RecyclerView.Adapter<AdaptadorLugares.ViewHolder> {
    protected RepositorioLugares lugares;         // Lista de lugares a mostrar
    protected LayoutInflater inflador;   //Crea Layouts a partir del XML
    protected Context contexto;          //Lo necesitamos para el inflador
    protected View.OnClickListener onClickListener;

    public AdaptadorLugares(RepositorioLugares lugares) {
        this.lugares = lugares;
    }

    public AdaptadorLugares(Context contexto, RepositorioLugares lugares) {
        this.contexto = contexto;
        this.lugares = lugares;
        inflador = (LayoutInflater) contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la Vista desde el xml
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elemento_lista, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    // Cuando se activa el ViewHolder lo personalizamos con los datos del elemento
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Lugar lugar = lugares.elemento(posicion);
        holder.personaliza(lugar);
    }

    // Número de elementos
    @Override
    public int getItemCount() {
        return lugares.size();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, direccion;
        public ImageView foto;
        public RatingBar valoracion;
        public TextView distancia;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            direccion = itemView.findViewById(R.id.direccion);
            foto = itemView.findViewById(R.id.foto);
            valoracion = itemView.findViewById(R.id.valoracion);
            distancia = itemView.findViewById(R.id.distancia);
        }

        // Personalizamos un ViewHolder a partir de un lugar
        public void personaliza(Lugar lugar) {
            GeoPunto pos = ((Aplicacion) itemView.getContext().getApplicationContext())
                    .posicionActual;

            nombre.setText(lugar.getNombre());
            direccion.setText(lugar.getDireccion());
            int id = R.drawable.otros;
            switch (lugar.getTipo()) {
                case RESTAURANTE:
                    id = R.drawable.restaurante;
                    break;
                case BAR:
                    id = R.drawable.bar;
                    break;
                case COPAS:
                    id = R.drawable.copas;
                    break;
                case ESPECTACULO:
                    id = R.drawable.espectaculos;
                    break;
                case HOTEL:
                    id = R.drawable.hotel;
                    break;
                case COMPRAS:
                    id = R.drawable.compras;
                    break;
                case EDUCACION:
                    id = R.drawable.educacion;
                    break;
                case DEPORTE:
                    id = R.drawable.deporte;
                    break;
                case NATURALEZA:
                    id = R.drawable.naturaleza;
                    break;
                case GASOLINERA:
                    id = R.drawable.gasolinera;
                    break;
            }
            foto.setImageResource(id);
            foto.setScaleType(ImageView.ScaleType.FIT_END);
            valoracion.setRating(lugar.getValoracion());
            if (pos.equals(new GeoPunto()) ||
                    lugar.getPosicion().equals(new GeoPunto())) {
                distancia.setText("??? Km");
            } else {
                int d = (int) pos.distancia(lugar.getPosicion());
                if (d < 2000) distancia.setText(d + " m");
                else distancia.setText(d / 1000 + " Km");
            }
        }
    }
}
