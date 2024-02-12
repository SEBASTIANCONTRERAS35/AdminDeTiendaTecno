package Productos;

public class Laptop extends Computadora{
    private boolean ventiladores;
    private int duracionDeBateria;
    private double pulgadas;

    public Laptop(int identificador, int calidad, String nombre, String marca, int modelo, double precio, String procesador, String graficos, String teclado, int nnucleos, int ram, boolean ventiladores, int duracionDeBateria, double pulgadas) {
        super(identificador, calidad, nombre, marca, modelo, precio, procesador, graficos, teclado, nnucleos, ram);
        this.ventiladores = ventiladores;
        this.duracionDeBateria = duracionDeBateria;
        this.pulgadas = pulgadas;
    }


    @Override
    public String toString() {
        return "Laptop: " +super.toString()+
                " ventiladores=" + ventiladores +
                ", duracionDeBateria=" + duracionDeBateria +
                ", pulgadas=" + pulgadas;
    }
}
