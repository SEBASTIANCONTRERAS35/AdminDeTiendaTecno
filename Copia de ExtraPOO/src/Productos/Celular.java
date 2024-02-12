package Productos;

public class Celular extends DispositivoMovil{
    private String debloqueo;
    private int sensores;
    private int tarjetasSim;

    public Celular(int identificador, int calidad, String nombre, String marca, int modelo, double precio, String procesador, int duracionBateria, int capacidad, int NCamaras, double pulgadas, String debloqueo, int sensores, int tarjetasSim) {
        super(identificador, calidad, nombre, marca, modelo, precio, procesador, duracionBateria, capacidad, NCamaras, pulgadas);
        this.debloqueo = debloqueo;
        this.sensores = sensores;
        this.tarjetasSim = tarjetasSim;
    }


    @Override
    public String toString() {
        return "Celular: "+
                super.toString() +
                ", debloqueo='" + debloqueo + '\'' +
                ", sensores=" + sensores +
                ", tarjetasSim=" + tarjetasSim ;
    }
}
