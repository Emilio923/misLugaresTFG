package net.iescierva.mim.mislugares2019.casos_uso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import net.iescierva.mim.mislugares2019.R;
import net.iescierva.mim.mislugares2019.datos.AdaptadorLugaresBD;
import net.iescierva.mim.mislugares2019.datos.LugaresBD;
import net.iescierva.mim.mislugares2019.presentacion.AcercaDeActivity;
import net.iescierva.mim.mislugares2019.presentacion.PreferenciasActivity;

import androidx.fragment.app.FragmentActivity;

public class CasosUsoActividades {

    private FragmentActivity actividad;
    private CasosUsoLugar usoLugar;
    //private RepositorioLugares lugares;
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;

    public CasosUsoActividades(FragmentActivity actividad,
                               LugaresBD lugares,
                               AdaptadorLugaresBD adaptador) {
        this.actividad = actividad;
        this.lugares = lugares;
        this.adaptador = adaptador;
        usoLugar = new CasosUsoLugar(actividad, null, lugares, adaptador);
    }

    public void lanzarAcercaDe(Activity activity) {
        Intent i = new Intent(activity, AcercaDeActivity.class);
        activity.startActivity(i);
    }

    public void lanzarPreferencias(Activity activity) {
        Intent i = new Intent(activity, PreferenciasActivity.class);
        activity.startActivity(i);
    }

    public void lanzarVistaLugarActividad(View view) {
        final EditText entrada = new EditText(actividad);
        entrada.setText(R.string.dialogo_buscar_default_value);
        new AlertDialog.Builder(actividad)
                .setTitle(R.string.titulo_modal_buscar)
                .setMessage(R.string.texto_modal_buscar)
                .setView(entrada)
                .setPositiveButton(R.string.dialogo_borrar_aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int id = Integer.parseInt(entrada.getText().toString());
                        usoLugar.mostrar(id);
                    }
                })
                .setNegativeButton(R.string.dialogo_borrar_cancelar, null)
                .show();
    }
}
