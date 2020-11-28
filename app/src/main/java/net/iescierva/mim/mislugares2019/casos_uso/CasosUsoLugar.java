package net.iescierva.mim.mislugares2019.casos_uso;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import net.iescierva.mim.mislugares2019.Aplicacion;
import net.iescierva.mim.mislugares2019.R;
import net.iescierva.mim.mislugares2019.datos.AdaptadorLugaresBD;
import net.iescierva.mim.mislugares2019.datos.LugaresBD;
import net.iescierva.mim.mislugares2019.modelo.GeoPunto;
import net.iescierva.mim.mislugares2019.modelo.Lugar;
import net.iescierva.mim.mislugares2019.presentacion.EdicionLugarActivity;
import net.iescierva.mim.mislugares2019.presentacion.VistaLugarActivity;
import net.iescierva.mim.mislugares2019.presentacion.VistaLugarFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class CasosUsoLugar {
    protected FragmentActivity actividad;
    protected AdaptadorLugaresBD adaptador;
    protected Fragment fragment;
    private LugaresBD lugares;

    public CasosUsoLugar(FragmentActivity actividad,
                         Fragment fragment,
                         LugaresBD lugares,
                         AdaptadorLugaresBD adaptador) {
        this.actividad = actividad;
        this.fragment = fragment;
        this.lugares = lugares;
        this.adaptador = adaptador;
    }

    // OPERACIONES BÁSICAS

    public void nuevo() {
        int id = lugares.add_blank();
        GeoPunto posicion = ((Aplicacion) actividad.getApplication()).posicionActual;
        if (!posicion.equals(new GeoPunto())) {
            Lugar lugar = lugares.elemento(id);
            lugar.setPosicion(posicion);
            lugares.update(id, lugar);
        }
        Intent i = new Intent(actividad, EdicionLugarActivity.class);
        i.putExtra("_id", id);
        actividad.startActivity(i);   //$$$
    }

    public void mostrar(int pos) {
        VistaLugarFragment fragmentVista = obtenerFragmentVista();
        if (fragmentVista != null) {
            fragmentVista.pos = pos;
            fragmentVista._id = adaptador.idPosicion(pos);
            fragmentVista.update();
        } else {
            Intent intent = new Intent(actividad, VistaLugarActivity.class);
            intent.putExtra("pos", pos);
            if (fragment != null)
                fragment.startActivityForResult(intent, 0);
            else
                actividad.startActivityForResult(intent, 0);
        }
    }

    public VistaLugarFragment obtenerFragmentVista() {
        FragmentManager manejador = actividad.getSupportFragmentManager();
        return (VistaLugarFragment)
                manejador.findFragmentById(R.id.vista_lugar_fragment);
    }

    public void borrar(final int id) {
        new AlertDialog.Builder(actividad)
                .setTitle(R.string.dialogo_borrar_titulo)
                .setMessage(R.string.dialogo_borrar_texto)

                .setPositiveButton(R.string.dialogo_borrar_aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        lugares.delete(id);
                        adaptador.setCursor(lugares.extraeCursor());
                        adaptador.notifyDataSetChanged();
                        //actividad.finish();
                    }
                })
                .setNegativeButton(R.string.dialogo_borrar_cancelar, null)
                .setCancelable(false)
                .show();
    }

    public Fragment obtenerFragmentSelector() {
        FragmentManager manejador = actividad.getSupportFragmentManager();
        return manejador.findFragmentById(R.id.selector_fragment);
    }

    public void guardar(int id, Lugar nuevoLugar) {
        lugares.update(id, nuevoLugar);
        adaptador.setCursor(lugares.extraeCursor());
        adaptador.notifyDataSetChanged();
    }

    public void editar(int pos, int codigoSolicitud) {
        Intent i = new Intent(actividad, EdicionLugarActivity.class);
        i.putExtra("pos", pos);
        if (fragment != null)
            fragment.startActivityForResult(i, codigoSolicitud);
        else
            actividad.startActivityForResult(i, codigoSolicitud);
    }

    // INTENCIONES
    public void compartir(Lugar lugar) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,
                lugar.getNombre() + " - " + lugar.getUrl());
        actividad.startActivity(i);
    }

    public void llamarTelefono(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + lugar.getTelefono())));
    }

    public void verPgWeb(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(lugar.getUrl())));
    }

    public final void verMapa(Lugar lugar) {
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        //Usar las coordenadas si no son 0, en caso contrario buscar por dirección
        Uri uri = lugar.getPosicion() != new GeoPunto()
                ? Uri.parse("geo:" + lat + ',' + lon)
                : Uri.parse("geo:0,0?q=" + lugar.getDireccion());

        actividad.startActivity(new Intent("android.intent.action.VIEW", uri));
    }

    // FOTOGRAFÍAS
    public void galeria(int resultadoGaleria) {
        String action;
        if (android.os.Build.VERSION.SDK_INT >= 19) { // API 19 - Kitkat
            action = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent intent = new Intent(action,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (fragment != null)
            fragment.startActivityForResult(intent, resultadoGaleria);
        else
            actividad.startActivityForResult(intent, resultadoGaleria);
    }

    public void ponerFoto(int pos, String uri, ImageView imageView) {
        Lugar lugar = adaptador.lugarPosicion(pos);
        lugar.setFoto(uri);
        visualizarFoto(lugar, imageView);
        actualizaPosLugar(pos, lugar);
    }

    public void visualizarFoto(Lugar lugar, ImageView imageView) {
        if (lugar.getFoto() != null && !lugar.getFoto().isEmpty()) {
            //imageView.setImageURI(Uri.parse(lugar.getFoto()));
            imageView.setImageBitmap(reduceBitmap(actividad, lugar.getFoto(), 1024, 1024));
        } else {
            imageView.setImageBitmap(null);
        }
    }

    public Uri tomarFoto(int codigoSolicitud) {
        try {
            Log.e("camara", "se mete");
            Uri uriUltimaFoto;
            File file = File.createTempFile(
                    "img_" + (System.currentTimeMillis() / 1000), ".jpg",
                    actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            if (Build.VERSION.SDK_INT >= 24) {
                uriUltimaFoto = FileProvider.getUriForFile(
                        actividad, "net.iescierva.mim.mislugares2019.fileProvider", file);
            } else {
                uriUltimaFoto = Uri.fromFile(file);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriUltimaFoto);
            if (fragment != null)
                fragment.startActivityForResult(intent, codigoSolicitud);
            else
                actividad.startActivityForResult(intent, codigoSolicitud);
            Log.e("camara", "uri anterior:" + uriUltimaFoto);
            return uriUltimaFoto;
        } catch (IOException ex) {
            Toast.makeText(actividad, "Error al crear fichero de imagen",
                    Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private Bitmap reduceBitmap(Context contexto, String uri,
                                int maxAncho, int maxAlto) {
        try {
            InputStream input = null;
            Uri u = Uri.parse(uri);
            if (u.getScheme().equals("http") || u.getScheme().equals("https")) {
                input = new URL(uri).openStream();
            } else {
                input = contexto.getContentResolver().openInputStream(u);
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(input, null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Fichero/recurso de imagen no encontrado",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Toast.makeText(contexto, "Error accediendo a imagen",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    public void actualizaPosLugar(int pos, Lugar lugar) {
        int id = adaptador.idPosicion(pos);
        guardar(id, lugar);  //
    }
}
