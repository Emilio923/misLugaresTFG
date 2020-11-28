package net.iescierva.mim.mislugares2019.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Lugar implements Parcelable {
    public static final Parcelable.Creator<Lugar> CREATOR
            = new Parcelable.Creator<Lugar>() {
        public Lugar createFromParcel(Parcel in) {
            return new Lugar(in);
        }

        public Lugar[] newArray(int size) {
            return new Lugar[size];
        }
    };
    private String nombre;
    private TipoLugar tipo;
    private String direccion;
    private GeoPunto posicion;
    private String foto;
    private int telefono;
    private String url;
    private String comentario;
    private long fecha;
    private float valoracion;

    public Lugar(String nombre, String direccion, double latitud,
                 double longitud, TipoLugar tipo, int telefono, String url, String comentario,
                 int valoracion) throws GeoException {
        fecha = System.currentTimeMillis();
        posicion = new GeoPunto(latitud, longitud);
        this.nombre = nombre;
        this.tipo = tipo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.url = url;
        this.comentario = comentario;
        this.valoracion = valoracion;
    }

    public Lugar() {
        fecha = System.currentTimeMillis();
        posicion = new GeoPunto();
        tipo = TipoLugar.OTROS;
    }

    Lugar(Parcel in) {
        nombre = in.readString();
        direccion = in.readString();
        posicion = in.readParcelable(GeoPunto.class.getClassLoader());
        foto = in.readString();
        telefono = in.readInt();
        tipo = in.readParcelable(TipoLugar.class.getClassLoader());
        //tipo = (TipoLugar) in.readSerializable();
        url = in.readString();
        comentario = in.readString();
        fecha = in.readLong();
        valoracion = in.readFloat();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoLugar getTipo() {
        return tipo;
    }

    public void setTipo(TipoLugar tipo) {
        this.tipo = tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public GeoPunto getPosicion() {
        return posicion;
    }

    public void setPosicion(GeoPunto posicion) {
        this.posicion = posicion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    @Override
    public String toString() {
        return "Lugar{" +
                "\nnombre='" + nombre + '\'' +
                "\ntipo lugar=" + tipo +
                ",\ndireccion='" + direccion + '\'' +
                ",\nposicion=" + posicion +
                ",\nfoto='" + foto + '\'' +
                ",\ntelefono=" + telefono +
                ",\nurl='" + url + '\'' +
                ",\ncomentario='" + comentario + '\'' +
                ",\nfecha=" + fecha +
                ",\nvaloracion=" + valoracion +
                "\n}\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(nombre);
        parcel.writeString(direccion);
        parcel.writeParcelable(posicion, 0);
        parcel.writeString(foto);
        parcel.writeInt(telefono);
        parcel.writeParcelable(tipo, 0);
        //parcel.writeSerializable(tipo);
        parcel.writeString(url);
        parcel.writeString(comentario);
        parcel.writeLong(fecha);
        parcel.writeFloat(valoracion);
    }
}