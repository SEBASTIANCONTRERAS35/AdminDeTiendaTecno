package Productos;

public class Tablet extends DispositivoMovil{
    private String lapiz;
    private String teclado;
    private boolean programar;

    public Tablet(int identificador, int calidad, String nombre, String marca, int modelo, double precio, String procesador, int duracionBateria, int capacidad, int NCamaras, double pulgadas, String lapiz, String teclado, boolean programar) {
        super(identificador, calidad, nombre, marca, modelo, precio, procesador, duracionBateria, capacidad, NCamaras, pulgadas);
        this.lapiz = lapiz;
        this.teclado = teclado;
        this.programar = programar;
    }



    @Override
    public String toString() {
        return "Tablet: " + super.toString()+
                ", lapiz='" + lapiz + '\'' +
                ", teclado='" + teclado + '\'' +
                ", programar=" + programar;

    }
}
