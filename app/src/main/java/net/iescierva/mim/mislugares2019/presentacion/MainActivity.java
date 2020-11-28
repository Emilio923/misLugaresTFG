package net.iescierva.mim.mislugares2019.presentacion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.iescierva.mim.mislugares2019.Aplicacion;
import net.iescierva.mim.mislugares2019.R;
import net.iescierva.mim.mislugares2019.casos_uso.CasosUsoActividades;
import net.iescierva.mim.mislugares2019.casos_uso.CasosUsoLocalizacion;
import net.iescierva.mim.mislugares2019.casos_uso.CasosUsoLugar;
import net.iescierva.mim.mislugares2019.datos.LugaresBD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    static final int RESULTADO_PREFERENCIAS = 0;
    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    private static final int SOLICITUD_PERMISO_INTERNET = 2;
    //Debug
    private static boolean MOSTRAR_EVENTO = true;
    //Common info
    //private static RepositorioLugares lugares;
    private static CasosUsoLugar usoLugar;
    private static CasosUsoActividades usoActividades;
    //public AdaptadorLugares adaptador;
    private static LugaresBD lugares;
    //private AdaptadorLugaresBD adaptador;

    MediaPlayer mp;
    //RecyclerView elements
    //private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration separador;
    private CasosUsoLocalizacion usoLocalizacion;
    private Aplicacion aplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aplicacion = (Aplicacion) getApplication();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();  */
                usoLugar.nuevo();
            }
        });
        lugares = ((Aplicacion) getApplication()).getLugares();
        usoLugar = new CasosUsoLugar(this, null, lugares, aplicacion.getAdaptador());
        usoActividades = new CasosUsoActividades(this, lugares, aplicacion.getAdaptador());
        //Asignarle el LayoutManager, en este caso de tipo Linear
        layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);

        //asignarle el separador de elementos
        separador = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        //Reproducir música MIDI
        mp = MediaPlayer.create(this, R.raw.audio);
        mp.start();

        usoLocalizacion = new CasosUsoLocalizacion(this,
                SOLICITUD_PERMISO_LOCALIZACION);

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //lanzar "Preferencias" mediante el menú
            lanzarPreferencias(null);
            return true;
        }
        if (id == R.id.acercaDe) {
            //lanzar "Acerca de" mediante el menú
            lanzarAcercaDe(null);
            return true;
        }
        if (id == R.id.menu_buscar) {
            //lanzar "Vista Lugar" mediante el menú
            lanzarVistaLugar(null);
            return true;
        }
        if (id == R.id.menu_mapa) {
            Intent intent = new Intent(this, MapaActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, PreferenciasActivity.class);
        //startActivity(i);
        startActivityForResult(i, RESULTADO_PREFERENCIAS);
    }

    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    public void lanzarVistaLugar(View view) {
        final EditText entrada = new EditText(this);
        entrada.setText("0");
        new AlertDialog.Builder(this)
                .setTitle("Selección de lugar")
                .setMessage("indica su id:")
                .setView(entrada)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int id = Integer.parseInt(entrada.getText().toString());
                        usoLugar.mostrar(id);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void mostrarPreferencias(View view) {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = "notificaciones: " + pref.getBoolean("notificaciones", true)
                + ", máximo a listar: " + pref.getString("maximo", "?")
                + ", orden: " + pref.getString("orden", "1")
                + ", recibir correos: " + pref.getBoolean("correos", false)
                + ", correo: " + pref.getString("correo", "?")
                + ", tipos: " + pref.getString("tipos", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void salir(View view) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mp.start();
        if (MOSTRAR_EVENTO)
            Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //refrescar el scrollView1 después de resumir la ejecución
        //findViewById(R.id.scrollView1).invalidate();

        //no es necesaario esto, ya se hace en onLocationChanged
        //adaptador.notifyDataSetChanged();

        mp.start();
        usoLocalizacion.activar();
        if (MOSTRAR_EVENTO)
            Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        //mp.pause();
        usoLocalizacion.desactivar();
        if (MOSTRAR_EVENTO)
            Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mp.pause();
        if (MOSTRAR_EVENTO)
            Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mp.start();
        if (MOSTRAR_EVENTO)
            Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mp.stop();
        if (MOSTRAR_EVENTO)
            Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado) {
        super.onSaveInstanceState(estadoGuardado);
        if (mp != null) {
            int pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado) {
        super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null) {
            int pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case SOLICITUD_PERMISO_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }
                return;
            }

            case SOLICITUD_PERMISO_LOCALIZACION :{
                if (requestCode == SOLICITUD_PERMISO_LOCALIZACION
                        && grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    usoLocalizacion.permisoConcedido();
                return;
            }

        }
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d(CasosUsoLocalizacion.getTAG(), "Nueva localización: " + location);
        usoLocalizacion.actualizaMejorLocaliz(location);
        aplicacion.getAdaptador().notifyDataSetChanged();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(CasosUsoLocalizacion.getTAG(), "Cambia estado: " + provider);
        usoLocalizacion.activarProveedores();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(CasosUsoLocalizacion.getTAG(), "Se habilita: " + provider);
        usoLocalizacion.activarProveedores();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(CasosUsoLocalizacion.getTAG(), "Se deshabilita: " + provider);
        usoLocalizacion.activarProveedores();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_PREFERENCIAS) {
            aplicacion.getAdaptador().setCursor(lugares.extraeCursor());
            aplicacion.getAdaptador().notifyDataSetChanged();
            if (usoLugar.obtenerFragmentVista() != null)
                usoLugar.mostrar(0);
        }
    }
}
