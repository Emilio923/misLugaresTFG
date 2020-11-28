package net.iescierva.mim.mislugares2019.presentacion;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.iescierva.mim.mislugares2019.Aplicacion;
import net.iescierva.mim.mislugares2019.R;
import net.iescierva.mim.mislugares2019.datos.AdaptadorLugaresBD;
import net.iescierva.mim.mislugares2019.modelo.GeoPunto;
import net.iescierva.mim.mislugares2019.modelo.Lugar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class MapaActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    //private static RepositorioLugares lugares;
    private static AdaptadorLugaresBD adaptador;

    private GoogleMap mapa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        //inicialización usando Aplicacion
        adaptador = ((Aplicacion) getApplication()).getAdaptador();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
        }
        if (adaptador.getItemCount() > 0) {
            GeoPunto p = adaptador.lugarPosicion(0).getPosicion();
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(p.getLatitud(), p.getLongitud()), 12));
        }
        for (int n = 0; n < adaptador.getItemCount(); n++) {
            Lugar lugar = adaptador.lugarPosicion(n);
            GeoPunto p = lugar.getPosicion();
            if (p != null && p.getLatitud() != 0) {
                BitmapDrawable iconoDrawable = (BitmapDrawable)
                        ContextCompat.getDrawable(this, lugar.getTipo().getRecurso());
                Bitmap iGrande = iconoDrawable.getBitmap();
                Bitmap icono = Bitmap.createScaledBitmap(iGrande,
                        iGrande.getWidth() / 5, iGrande.getHeight() / 5, false);
                mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(p.getLatitud(), p.getLongitud()))
                        .title(lugar.getNombre()).snippet(lugar.getDireccion())
                        .icon(BitmapDescriptorFactory.fromBitmap(icono)));
            }
        }
        mapa.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for (int id = 0; id < adaptador.getItemCount(); id++) {
            if (adaptador.lugarPosicion(id).getNombre()
                    .equals(marker.getTitle())) {
                Intent intent = new Intent(this, VistaLugarActivity.class);
                intent.putExtra("pos", id);
                startActivity(intent);
                break;
            }
        }
    }
}