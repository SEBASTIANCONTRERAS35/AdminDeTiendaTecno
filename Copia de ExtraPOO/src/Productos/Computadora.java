package Productos;

public abstract class Computadora extends DispositivoElectronico{
    private String graficos;
    private String teclado;
    private int Nnucleos;
    private int ram;

    protected Computadora(int identificador, int calidad, String nombre, String marca, int modelo, double precio, String procesador, String graficos, String teclado, int nnucleos, int ram) {
        super(identificador, calidad, nombre, marca, modelo, precio, procesador);
        this.graficos = graficos;
        this.teclado = teclado;
        Nnucleos = nnucleos;
        this.ram = ram;
    }


    @Override
    public String toString() {
        return super.toString() +
                ", graficos='" + graficos + '\'' +
                ", teclado='" + teclado + '\'' +
                ", Nnucleos=" + Nnucleos +
                ", ram=" + ram;
    }
}
