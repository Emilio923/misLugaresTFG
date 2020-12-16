package net.iescierva.mim.mislugares2019.presentacion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import net.iescierva.mim.mislugares2019.Aplicacion;
import net.iescierva.mim.mislugares2019.R;
import net.iescierva.mim.mislugares2019.casos_uso.CasosUsoLugarFecha;
import net.iescierva.mim.mislugares2019.datos.AdaptadorLugaresBD;
import net.iescierva.mim.mislugares2019.datos.LugaresBD;
import net.iescierva.mim.mislugares2019.modelo.Lugar;

import java.text.DateFormat;
import java.util.Date;

import androidx.fragment.app.Fragment;

public class VistaLugarFragment extends Fragment {

    final static int RESULTADO_EDITAR = 1;
    //fotos
    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;
    public int pos;
    public int _id = -1;
    //private RepositorioLugares lugares;
    private LugaresBD lugares;
    private CasosUsoLugarFecha usoLugar;
    private Lugar lugar;
    private ImageView foto;
    private Uri uriUltimaFoto;
    private AdaptadorLugaresBD adaptador;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflador,
                             ViewGroup contenedor,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View vista = inflador.inflate(R.layout.vista_lugar, contenedor, false);
        return vista;
    }

    //recogida de parámetros e inicialización
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null)
            pos = extras.getInt("pos", 0);
        else
            pos = 0;

        v = getView();

        v.findViewById(R.id.url).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                usoLugar.verPgWeb(lugar);
            }
        });
        v.findViewById(R.id.telefono).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                usoLugar.llamarTelefono(lugar);
            }
        });
        v.findViewById(R.id.direccion).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                usoLugar.verMapa(lugar);
            }
        });
        v.findViewById(R.id.vista_lugar).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                usoLugar.verMapa(lugar);
            }
        });
        v.findViewById(R.id.galeria).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                usoLugar.galeria(RESULTADO_GALERIA);
            }
        });
        v.findViewById(R.id.cerrar).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                usoLugar.ponerFoto(pos, "", foto);
            }
        });
        v.findViewById(R.id.camara).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e("camara", "tomar foto 2");
                uriUltimaFoto = usoLugar.tomarFoto(RESULTADO_FOTO);
                Log.e("camara", "resultadooooo: " + usoLugar.tomarFoto(RESULTADO_FOTO));
                Log.e("camara", "resultado2: " + uriUltimaFoto);
            }
        });
        v.findViewById(R.id.hora).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.cambiarHora(pos);
            }
        });
        v.findViewById(R.id.fecha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.cambiarFecha(pos);
            }
        });
        initialize();
        update();
    }

    private void initialize() {
        adaptador = ((Aplicacion) getActivity().getApplication()).getAdaptador();
        lugares = ((Aplicacion) getActivity().getApplication()).getLugares();
        usoLugar = new CasosUsoLugarFecha(getActivity(), this, lugares, adaptador);
        //lugar = lugares.elemento(pos);
        Log.d("dato", "akiiiiii");
        lugar = adaptador.lugarPosicion(pos);
        if(lugar == null){
            Toast.makeText(getContext(), "El ID seleccionado no se relaciona con ningun lugar", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(this, EdicionLugarActivity.class);
            //startActivity(intent);
        }
        Log.d("dato", "hola");
    }

    public void update() {   //anteriormente actualizaVistas()

        if (adaptador.getItemCount() == 0)
            return;

        lugar = adaptador.lugarPosicion(pos);

        foto = v.findViewById(R.id.foto);

        TextView nombre = v.findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());

        TextView tipo = v.findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());

        if (lugar.getDireccion().isEmpty()) {
            v.findViewById(R.id.direccion).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.direccion).setVisibility(View.VISIBLE);
            TextView direccion = v.findViewById(R.id.direccion);
            direccion.setText(lugar.getDireccion());
        }

        if (lugar.getTelefono() == 0) {
            v.findViewById(R.id.telefono).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.telefono).setVisibility(View.VISIBLE);
            TextView telefono = v.findViewById(R.id.telefono);
            telefono.setText(String.valueOf(lugar.getTelefono()));
        }

        if (lugar.getUrl().isEmpty()) {
            v.findViewById(R.id.url).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.url).setVisibility(View.VISIBLE);
            TextView url = v.findViewById(R.id.url);
            url.setText(lugar.getUrl());
        }

        if (lugar.getComentario().isEmpty()) {
            v.findViewById(R.id.comentario).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.url).setVisibility(View.VISIBLE);
            TextView comentario = v.findViewById(R.id.comentario);
            comentario.setText(lugar.getComentario());
        }

        TextView fecha = v.findViewById(R.id.fecha);
        fecha.setText(DateFormat.getDateInstance().format(
                new Date(lugar.getFecha())));

        TextView hora = v.findViewById(R.id.hora);
        hora.setText(DateFormat.getTimeInstance().format(
                new Date(lugar.getFecha())));

        RatingBar valoracion = v.findViewById(R.id.valoracion);
        valoracion.setRating(lugar.getValoracion());
        valoracion.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float valor, boolean fromUser) {
                        lugar.setValoracion(valor);
                        usoLugar.actualizaPosLugar(pos, lugar);
                        pos = adaptador.posicionId(_id);
                    }
                });

        usoLugar.visualizarFoto(lugar, foto);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.vista_lugar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_compartir:
                usoLugar.compartir(lugar);
                return true;
            case R.id.accion_llegar:
                usoLugar.verMapa(lugar);
                return true;
            case R.id.accion_editar:
                usoLugar.editar(pos, RESULTADO_EDITAR);
                return true;
            case R.id.accion_borrar:
                int _id = adaptador.idPosicion(pos);
                usoLugar.borrar(_id);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_EDITAR) {
            //actualizar lugar con los nuevos cambios y actualizar la vista con update()
            //lugar = lugares.elemento(pos);
            lugar = adaptador.lugarPosicion(pos);
            update();

            //Recargar MainActivity después de editar
            Intent refresh = new Intent(getActivity(), MainActivity.class);
            startActivity(refresh);
            //Otra forma: findViewById(R.id.scrollView1).invalidate();

        } else if (requestCode == RESULTADO_GALERIA) {
            Log.e("camara", "se mete en foto");
            if (resultCode == Activity.RESULT_OK) {
                usoLugar.ponerFoto(pos, data.getDataString(), foto);
            } else {
                Toast.makeText(getActivity(), "Foto no cargada", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULTADO_FOTO) {
            Log.e("camara", "se mete en captura");
            Log.e("camara", "resultado: " + resultCode);
            Log.e("camara", "activity: " + Activity.RESULT_OK);
            Log.e("camara", "uri: " + uriUltimaFoto);

            if (resultCode == Activity.RESULT_OK && uriUltimaFoto != null) {
                lugar.setFoto(uriUltimaFoto.toString());
                usoLugar.ponerFoto(pos, lugar.getFoto(), foto);
            } else {
                Toast.makeText(getActivity(), "Error en captura", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULTADO_EDITAR) {
            lugar = lugares.elemento(_id);
            pos = adaptador.posicionId(_id);
            update();
        }
        //getActivity().finish();
    }

    public void verMapa(View view) {
        usoLugar.verMapa(lugar);
    }

    public void llamarTelefono(View view) {
        usoLugar.llamarTelefono(lugar);
    }

    public void verPgWeb(View view) {
        usoLugar.verPgWeb(lugar);
    }

    public void galeria(View view) {
        usoLugar.galeria(RESULTADO_GALERIA);
    }

    public void tomarFoto(View view) {
        Log.e("camara", "tomar foto 1");
        uriUltimaFoto = usoLugar.tomarFoto(RESULTADO_FOTO);
    }

    public void eliminarFoto(View view) {
        usoLugar.ponerFoto(pos, "", foto);
    }


}
