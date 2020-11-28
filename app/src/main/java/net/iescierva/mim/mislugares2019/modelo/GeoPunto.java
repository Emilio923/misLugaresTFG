package net.iescierva.mim.mislugares2019.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class GeoPunto implements Parcelable {

    public static final Parcelable.Creator<GeoPunto> CREATOR
            = new Parcelable.Creator<GeoPunto>() {
        public GeoPunto createFromParcel(Parcel in) {
            return new GeoPunto(in);
        }

        public GeoPunto[] newArray(int size) {
            return new GeoPunto[size];
        }
    };
    private double longitud, latitud;

    /**
     * @see GeoPunto
     */
    public GeoPunto() {
        this.latitud = 0;
        this.longitud = 0;
    }

    /**
     * @param latitud  latitud en grados
     * @param longitud longitud en grados
     * @see GeoPunto
     */

    public GeoPunto(double latitud, double longitud) throws GeoException {
        set(latitud, longitud);
    }

    /**
     * @param latitud  latitud en millonésimas de grado
     * @param longitud longitud en millonésimas de grado
     * @see GeoPunto
     */
    public GeoPunto(int latitud, int longitud) throws GeoException {
        //los parámetros son millonésimas de grado
        this(latitud / 1e6, longitud / 1e6);
    }

    private GeoPunto(Parcel in) {
        latitud = in.readDouble();
        longitud = in.readDouble();
    }

    /**
     * @param punto punto geográfico
     * @return distancia (metros)
     * @see GeoPunto
     */
    public double distancia(GeoPunto punto) {
        final double RADIO_TIERRA = 6371000; // en metros
        double dLat = Math.toRadians(latitud - punto.latitud);
        double dLon = Math.toRadians(longitud - punto.longitud);
        double lat1 = Math.toRadians(punto.latitud);
        double lat2 = Math.toRadians(latitud);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) *
                        Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * RADIO_TIERRA;
    }

    /**
     * @return longitud en grados
     * @see GeoPunto
     */
    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) throws GeoException {
        if (longitudOk(longitud))
            this.longitud = longitud;
        else
            throw new GeoException("GeoPunto.setLongitud(double): {" + longitud + "}");
    }

    /**
     * @return latitud en grados
     * @see GeoPunto
     */
    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) throws GeoException {
        if (latitudOk(latitud)) {
            this.latitud = latitud;
        } else
            throw new GeoException("GeoPunto.setlatitud(double): {" + latitud + "}");
    }

    /**
     * @param latitud  latitud en grados
     * @param longitud longitud en grados
     * @see GeoPunto
     */
    public void set(double latitud, double longitud) throws GeoException {
        setLatitud(latitud);
        setLongitud(longitud);
    }

    /**
     * @param latitud latitud en grados
     * @return true si es correcto
     * @see GeoPunto
     */
    private boolean latitudOk(double latitud) {
        return latitud >= -90. && latitud <= 90.;
    }

    /**
     * @param longitud longitud en grados
     * @return true si es correcto
     * @see GeoPunto
     */
    private boolean longitudOk(double longitud) {
        return longitud >= -180. && longitud <= 180.;
    }

    /**
     * @return punto en formato cadena
     * @see GeoPunto
     */
    @Override
    public String toString() {
        return "GeoPunto{" +
                "latitud=" + latitud +
                ",longitud=" + longitud +
                '}';
    }

    /**
     * @param o punto a comparar
     * @return true si es igual
     * @see GeoPunto
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoPunto geoPunto = (GeoPunto) o;
        return Double.compare(geoPunto.longitud, longitud) == 0 &&
                Double.compare(geoPunto.latitud, latitud) == 0;
    }

    /**
     * @return hash de punto
     * @see GeoPunto
     */
    @Override
    public int hashCode() {
        return Objects.hash(latitud, longitud);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitud);
        dest.writeDouble(longitud);
    }
}