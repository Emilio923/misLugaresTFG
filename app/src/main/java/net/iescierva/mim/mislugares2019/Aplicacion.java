package net.iescierva.mim.mislugares2019;

import android.app.Application;

import net.iescierva.mim.mislugares2019.datos.AdaptadorLugares;
import net.iescierva.mim.mislugares2019.datos.AdaptadorLugaresBD;
import net.iescierva.mim.mislugares2019.datos.LugaresBD;
import net.iescierva.mim.mislugares2019.datos.LugaresLista;
import net.iescierva.mim.mislugares2019.datos.RepositorioLugares;
import net.iescierva.mim.mislugares2019.modelo.GeoPunto;

public class Aplicacion extends Application {

    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;
    public GeoPunto posicionActual = new GeoPunto();

    @Override public void onCreate() {
        super.onCreate();
        lugares = new LugaresBD(this);
        adaptador= new AdaptadorLugaresBD(lugares, lugares.extraeCursor());
    }

    public LugaresBD getLugares() {
        return lugares;
    }

    public AdaptadorLugaresBD getAdaptador() {
        return adaptador;
    }
}
