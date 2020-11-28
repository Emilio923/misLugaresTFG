package net.iescierva.mim.mislugares2019.presentacion;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import net.iescierva.mim.mislugares2019.Aplicacion;
import net.iescierva.mim.mislugares2019.R;
import net.iescierva.mim.mislugares2019.casos_uso.CasosUsoLugar;
import net.iescierva.mim.mislugares2019.datos.AdaptadorLugaresBD;
import net.iescierva.mim.mislugares2019.datos.LugaresBD;
import net.iescierva.mim.mislugares2019.modelo.Lugar;
import net.iescierva.mim.mislugares2019.modelo.TipoLugar;

import androidx.appcompat.app.AppCompatActivity;

public class EdicionLugarActivity extends AppCompatActivity {

    //private RepositorioLugares lugares;
    private LugaresBD lugares;
    private CasosUsoLugar usoLugar;
    private int pos;
    private Lugar lugar;
    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;
    private int _id;

    //public AdaptadorLugares adaptador;
    private AdaptadorLugaresBD adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);
        //Asignarle el adaptador
        //adaptador = new AdaptadorLugares(this, lugares);
        adaptador = ((Aplicacion) getApplication()).getAdaptador();
        getExtras();
        createSpinner();

        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos", -1);
        _id = extras.getInt("_id", -1);
        if (_id != -1)
            lugar = lugares.elemento(_id);
        else
            lugar = adaptador.lugarPosicion(pos);
        update();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;

        pos = extras.getInt("pos", 0);
        lugares = ((Aplicacion) getApplication()).getLugares();
        usoLugar = new CasosUsoLugar(this, null, lugares, adaptador);
        //lugar = lugares.elemento(pos);
        lugar = adaptador.lugarPosicion(pos);
    }

    private void createSpinner() {
        tipo = findViewById(R.id.spinner);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, TipoLugar.getNombres());

        adaptador.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);

        tipo.setAdapter(adaptador);
        tipo.setSelection(lugar.getTipo().ordinal());
    }

    private void update() {
        nombre = findViewById(R.id.editTextNombre);
        nombre.setText(lugar.getNombre());

        direccion = findViewById(R.id.editTextDireccion);
        direccion.setText(lugar.getDireccion());

        telefono = findViewById(R.id.editTextTelefono);
        telefono.setText(String.valueOf(lugar.getTelefono()));

        url = findViewById(R.id.editTextDireccionWeb);
        url.setText(lugar.getUrl());

        comentario = findViewById(R.id.editTextComentario);
        comentario.setText(lugar.getComentario());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_id == -1)
            _id = adaptador.idPosicion(pos);
        switch (item.getItemId()) {
            case R.id.accion_cancelar:
                if (_id != -1) lugares.delete(_id);
                finish();
                return true;
            case R.id.accion_guardar:
                lugar.setNombre(nombre.getText().toString());
                lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
                lugar.setDireccion(direccion.getText().toString());
                lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
                lugar.setUrl(url.getText().toString());
                lugar.setComentario(comentario.getText().toString());
                //usoLugar.guardar(pos, lugar);
                if (_id == -1) _id = adaptador.idPosicion(pos);
                usoLugar.guardar(_id, lugar);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}