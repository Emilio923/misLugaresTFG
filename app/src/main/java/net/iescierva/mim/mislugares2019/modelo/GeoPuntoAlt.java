package net.iescierva.mim.mislugares2019.modelo;

public class GeoPuntoAlt extends GeoPunto {
    public final static double ALTURA_MAXIMA = 9000;   // >Everest
    public final static double ALTURA_MINIMA = -500;   // <Mar Muerto
    private double altura;

    public GeoPuntoAlt(double latitud, double longitud, double altura) throws GeoException {
        super(latitud, longitud);
        this.altura = altura;
    }

    public double distancia(GeoPuntoAlt punto) {
        double d = super.distancia(punto);
        return Math.sqrt(Math.pow(d, 2) + Math.pow(punto.altura - this.altura, 2));
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) throws GeoException {
        if (altura >= ALTURA_MINIMA && altura <= ALTURA_MAXIMA)
            this.altura = altura;
        else
            throw new GeoException("GeoPuntoAlt.setAltura(double): {" + altura + "}");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoPuntoAlt)) return false;
        if (!super.equals(o)) return false;
        GeoPuntoAlt that = (GeoPuntoAlt) o;
        return Double.compare(that.altura, altura) == 0;
    }

    /*
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), altura);
    }
    */

    @Override
    public String toString() {
        return "GeoPuntoAlt{" + super.toString() +
                ",altura=" + altura +
                '}';
    }
}
