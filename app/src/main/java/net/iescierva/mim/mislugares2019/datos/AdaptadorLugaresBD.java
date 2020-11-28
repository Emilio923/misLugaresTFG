/*
 * Copyright (c) 2020. Miguel Angel
 */

package net.iescierva.mim.mislugares2019.datos;

import android.database.Cursor;

import net.iescierva.mim.mislugares2019.modelo.Lugar;

public class AdaptadorLugaresBD extends AdaptadorLugares {

    protected Cursor cursor;

    public AdaptadorLugaresBD(RepositorioLugares
                                      lugares, Cursor cursor) {
        super(lugares);
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    //devuelve el lugar en la posición. La primera fila es la posición 0
    public Lugar lugarPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return LugaresBD.extraeLugar(cursor);
    }

    //averigua el ROWID dada la posición en el listado (o cursor, que es lo mismo)
    public int idPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        if (cursor.getCount() > 0)
            return cursor.getInt(0);
        else
            return -1;
    }

    //dada la posición en el listado averigua el ROWID
    public int posicionId(int id) {
        int pos = 0;
        while (pos < getItemCount() && idPosicion(pos) != id)
            pos++;
        return pos > getItemCount() ? -1 : pos;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Lugar lugar = lugarPosicion(posicion);
        holder.personaliza(lugar);
        holder.itemView.setTag(new Integer(posicion));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
