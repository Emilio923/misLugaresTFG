/*
 * Copyright (c) 2020. Miguel Angel
 */

package net.iescierva.mim.mislugares2019.datos;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import net.iescierva.mim.mislugares2019.Aplicacion;
import net.iescierva.mim.mislugares2019.modelo.GeoException;
import net.iescierva.mim.mislugares2019.modelo.GeoPunto;
import net.iescierva.mim.mislugares2019.modelo.Lugar;
import net.iescierva.mim.mislugares2019.modelo.TipoLugar;

public class LugaresBD extends SQLiteOpenHelper implements RepositorioLugares {

    Context contexto;

    public LugaresBD(Context contexto) { //Como contexto recibimos el objeto Aplicacion
        super(contexto, "lugares", null, 1);
        this.contexto = contexto;
    }

    public static Lugar extraeLugar(Cursor cursor) {
        //extrae y devuelve un lugar de la posición marcada actualmente por el cursor
        Lugar lugar = new Lugar();
        lugar.setNombre(cursor.getString(1));
        lugar.setDireccion(cursor.getString(2));
        try {
            lugar.setPosicion(new GeoPunto(cursor.getDouble(3),
                    cursor.getDouble(4)));
        } catch (GeoException e) {
            e.printStackTrace();
        }
        lugar.setTipo(TipoLugar.values()[cursor.getInt(5)]);
        lugar.setFoto(cursor.getString(6));
        lugar.setTelefono(cursor.getInt(7));
        lugar.setUrl(cursor.getString(8));
        lugar.setComentario(cursor.getString(9));
        lugar.setFecha(cursor.getLong(10));
        lugar.setValoracion(cursor.getFloat(11));
        return lugar;
    }

    //ATENCIÓN: los AUTOINCREMENT usan el mayor ROWID +1, por lo tanto la primera fila
    //tendrá un valor 1 en "_id"
    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE lugares (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "direccion TEXT, " +
                "latitud REAL, " +
                "longitud REAL, " +
                "tipo INTEGER, " +
                "foto TEXT, " +
                "telefono INTEGER, " +
                "url TEXT, " +
                "comentario TEXT, " +
                "fecha BIGINT, " +
                "valoracion REAL)");
        bd.execSQL("INSERT INTO lugares VALUES (null ,'Escuela Politécnica Superior de Gandía'," +
                " 'C/ Paranimf, 1 46730 Gandia (SPAIN)',38.995656,-0.166093," +
                TipoLugar.EDUCACION.ordinal() + ",'',962849300,'http://www.epsg.upv.es'," +
                "'Uno de los mejores lugares para formarse.'," + System.currentTimeMillis() + ", 3.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null ,'Al de siempre'," +
                "'P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)',38.925857,-0.190642," +
                TipoLugar.BAR.ordinal() + ",'',636472405,''," +
                "'No te pierdas el arroz en calabaza.'," + System.currentTimeMillis() + ", 3.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null ,'androidcurso.com'," +
                "'ciberespacio',0.0,0.0," +
                TipoLugar.EDUCACION.ordinal() + ",'',962849300,'http://androidcurso.com'," +
                "'Amplia tus conocimientos sobre Android.'," + System.currentTimeMillis() + ",5.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null ,'Barranco del Infierno'," +
                "'Vía Verde del río Serpis. Villalonga (Valencia)',38.867180,-0.295058," +
                TipoLugar.NATURALEZA.ordinal() + ",'',0," +
                "'http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-verde-del-rio.html'," +
                "'Espectacular ruta para bici o andar'," + System.currentTimeMillis() + ",4.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null ,'La Vital'," +
                "'Avda. La Vital,0 46701 Gandia (Valencia)',38.9705949,-0.1720092," +
                TipoLugar.COMPRAS.ordinal() + ",'',962881070,'http://www.lavital.es'," +
                "'El típico centro comercial'," + System.currentTimeMillis() + ",2.0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
    }

    //Busca en la BD y devuelve el elemento n-ésimo, n>=1
    @Override
    public Lugar elemento(int id) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM lugares WHERE _id = " + id, null);
        try {
            if (cursor.moveToNext())
                return extraeLugar(cursor);
            else
                throw new SQLException("Error al acceder al elemento _id = " + id);
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    @Override
    public void add(Lugar lugar) {
        ///Método no usado en la versión con BDD
    }

    @Override
    public int add_blank() {
        int _id = -1;
        Lugar lugar = new Lugar();
        getWritableDatabase().execSQL("INSERT INTO lugares (nombre ,direccion, latitud, longitud, tipo, foto, telefono, url, " +
                "comentario, fecha, valoracion) VALUES ('', '',  " +
                lugar.getPosicion().getLatitud() + "," +
                lugar.getPosicion().getLongitud() + ", " + lugar.getTipo().ordinal() +
                ", '', 0, '', '', " + lugar.getFecha() + ", 0)");
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT _id FROM lugares WHERE fecha = " + lugar.getFecha(), null);
        if (c.moveToNext()) _id = c.getInt(0);
        c.close();
        return _id;
    }

    @Override
    public void delete(int id) {
        getWritableDatabase().execSQL("DELETE FROM lugares WHERE _id = " + id);
    }

    @Override
    public int size() {
        return 0;
    }

    //actualiza una fila de la tabla lugares dado su ROWID
    @Override
    public void update(int id, Lugar lugar) {
        String sentenciaSQL = "UPDATE lugares SET" +
                " nombre = '" + lugar.getNombre() +
                "', direccion = '" + lugar.getDireccion() +
                "', latitud = " + lugar.getPosicion().getLatitud() +
                " , longitud = " + lugar.getPosicion().getLongitud() +
                " , tipo = " + lugar.getTipo().ordinal() +
                " , foto = '" + lugar.getFoto() +
                "', telefono = " + lugar.getTelefono() +
                " , url = '" + lugar.getUrl() +
                "', comentario = '" + lugar.getComentario() +
                "', fecha = " + lugar.getFecha() +
                " , valoracion = " + lugar.getValoracion() +
                " WHERE _id = " + id;
        getWritableDatabase().execSQL(sentenciaSQL);
    }

    public Cursor extraeCursor() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(contexto);
        String consulta;
        switch (pref.getString("orden", "default")) {
            case "0":   //por orden natural de creación de las filas de la tabla
                consulta = "SELECT * FROM lugares ";
                break;
            case "1":   //por valoración
                consulta = "SELECT * FROM lugares ORDER BY valoracion DESC";
                break;
            case "3":
                consulta = "SELECT * FROM lugares ORDER BY telefono";
                break;
            default:    //2=por distancia
                double lat = ((Aplicacion) contexto.getApplicationContext())
                        .posicionActual.getLatitud();
                double lon = ((Aplicacion) contexto.getApplicationContext())
                        .posicionActual.getLongitud();
                consulta = "SELECT * FROM lugares ORDER BY " +
                        "(" + lat + "-latitud)*(" + lat + "-latitud) + " +
                        "(" + lon + "-longitud )*(" + lon + "-longitud )" + " DESC";
                break;
        }
        consulta += " LIMIT " + pref.getString("maximo", "12");

        //String consulta = "SELECT * FROM lugares ORDER BY valoracion desc";
        SQLiteDatabase bd = getReadableDatabase();
        return bd.rawQuery(consulta, null);
    }
}