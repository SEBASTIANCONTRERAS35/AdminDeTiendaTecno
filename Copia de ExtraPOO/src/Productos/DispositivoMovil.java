package Productos;

public abstract class DispositivoMovil extends DispositivoElectronico{
    private int duracionBateria;
    private int capacidad;
    private int NCamaras;
    private double pulgadas;

    protected DispositivoMovil(int identificador, int calidad, String nombre, String marca, int modelo, double precio, String procesador, int duracionBateria, int capacidad, int NCamaras, double pulgadas) {
        super(identificador, calidad, nombre, marca, modelo, precio, procesador);
        this.duracionBateria = duracionBateria;
        this.capacidad = capacidad;
        this.NCamaras = NCamaras;
        this.pulgadas = pulgadas;
    }


    @Override
    public String toString() {
        return super.toString() +
                ", duracionBateria=" + duracionBateria +
                ", capacidad=" + capacidad +
                ", NCamaras=" + NCamaras +
                ", pulgadas=" + pulgadas;
    }
}
