/*
 * Copyright (c) 2020. Miguel Angel
 */

package net.iescierva.mim.mislugares2019.casos_uso;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import net.iescierva.mim.mislugares2019.Aplicacion;
import net.iescierva.mim.mislugares2019.datos.AdaptadorLugares;
import net.iescierva.mim.mislugares2019.modelo.GeoException;
import net.iescierva.mim.mislugares2019.modelo.GeoPunto;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import static android.content.Context.LOCATION_SERVICE;

public class CasosUsoLocalizacion {
    private static final long DOS_MINUTOS = 5 * 60 * 1000;
    private static final String TAG = "MisLugares2019";
    private Activity actividad;
    private Aplicacion aplicacion;
    private int codigoPermiso;
    private LocationManager manejadorLoc;
    private Location mejorLoc;
    private GeoPunto posicionActual;
    private AdaptadorLugares adaptador;

    public CasosUsoLocalizacion(Activity actividad, int codigoPermiso) {
        this.actividad = actividad;
        this.codigoPermiso = codigoPermiso;
        this.aplicacion = (Aplicacion) actividad.getApplication();
        manejadorLoc = (LocationManager) actividad.getSystemService(LOCATION_SERVICE);
        posicionActual = aplicacion.posicionActual;
        adaptador = aplicacion.getAdaptador();
        ultimaLocalizacion();
    }

    public static String getTAG() {
        return TAG;
    }

    public static void solicitarPermiso(final String permiso, String
            justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)) {
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    public Location getMejorLoc() {
        return mejorLoc;
    }

    public void setMejorLoc(Location mejorLoc) {
        this.mejorLoc = mejorLoc;
    }

    public boolean hayPermisoLocalizacion() {
        int p = ActivityCompat.checkSelfPermission(
                actividad, Manifest.permission.ACCESS_FINE_LOCATION);
        return p == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    void ultimaLocalizacion() {
        if (hayPermisoLocalizacion()) {
            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(
                        LocationManager.GPS_PROVIDER));
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER));
            }
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso localización no puedo mostrar la distancia" +
                            " a los lugares.", codigoPermiso, actividad);
        }
    }

    public void actualizaMejorLocaliz(Location localiz) {
        if (localiz != null && (mejorLoc == null
                || localiz.getAccuracy() < 2 * getMejorLoc().getAccuracy()
                || localiz.getTime() - mejorLoc.getTime() > DOS_MINUTOS)) {
            Log.d(TAG, "Nueva mejor localización");
            mejorLoc = localiz;
            try {
                aplicacion.posicionActual.setLatitud(
                        localiz.getLatitude());
                aplicacion.posicionActual.setLongitud(
                        localiz.getLongitude());
            } catch (GeoException e) {
                e.printStackTrace();
            }
        }
    }

    public void permisoConcedido() {
        ultimaLocalizacion();
        activarProveedores();
        adaptador.notifyDataSetChanged();
    }

    @SuppressLint("MissingPermission")
    public void activarProveedores() {
        if (hayPermisoLocalizacion()) {
            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //parámetros: proveedor(String), MinTime, minDist,
                manejadorLoc.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        20 * 1000, 5, (LocationListener) actividad);
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                manejadorLoc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        10 * 1000, 10, (LocationListener) actividad);
            }
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso localización no puedo mostrar la distancia" +
                            " a los lugares.", codigoPermiso, actividad);
        }
    }

    public void activar() {
        if (hayPermisoLocalizacion()) activarProveedores();
    }

    public void desactivar() {
        if (hayPermisoLocalizacion()) manejadorLoc.removeUpdates((LocationListener) actividad);
    }
}
