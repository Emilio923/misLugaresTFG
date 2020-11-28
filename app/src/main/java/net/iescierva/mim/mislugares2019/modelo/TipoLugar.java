/*
 * Copyright (c) 2019. Miguel Angel
 */

package net.iescierva.mim.mislugares2019.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import net.iescierva.mim.mislugares2019.R;

public enum TipoLugar implements Parcelable {
    OTROS("Otros", R.drawable.otros),
    RESTAURANTE("Restaurante", R.drawable.restaurante),
    BAR("Bar", R.drawable.bar),
    COPAS("Copas", R.drawable.copas),
    ESPECTACULO("Espectáculo", R.drawable.espectaculos),
    HOTEL("Hotel", R.drawable.hotel),
    COMPRAS("Compras", R.drawable.compras),
    EDUCACION("Educación", R.drawable.educacion),
    DEPORTE("Deporte", R.drawable.deporte),
    NATURALEZA("Naturaleza", R.drawable.naturaleza),
    GASOLINERA("Gasolinera", R.drawable.gasolinera);

    public static final Creator<TipoLugar> CREATOR = new Creator<TipoLugar>() {
        @Override
        public TipoLugar createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public TipoLugar[] newArray(int size) {
            return new TipoLugar[size];
        }
    };
    private final String texto;
    private final int recurso;

    TipoLugar(String texto, int recurso) {
        this.texto = texto;
        this.recurso = recurso;
    }

    public static String[] getNombres() {
        String[] resultado = new String[TipoLugar.values().length];
        for (TipoLugar tipo : TipoLugar.values()) {
            resultado[tipo.ordinal()] = tipo.texto;
        }
        return resultado;
    }

    public String getTexto() {
        return texto;
    }

    public int getRecurso() {
        return recurso;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
