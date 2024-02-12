package Productos;

public class Television extends DispositivoElectronico {
    private String resolucion;
    private String taza;
    private boolean smart;
    private double pulgadas;

    public Television(int identificador, int calidad, String nombre, String marca, int modelo, double precio, String procesador, String resolucion, String taza, boolean smart, double pulgadas) {
        super(identificador, calidad, nombre, marca, modelo, precio, procesador);
        this.resolucion = resolucion;
        this.taza = taza;
        this.smart = smart;
        this.pulgadas = pulgadas;
    }



    @Override
    public String toString() {
        return "Television: " + super.toString()+
                ", resolucion='" + resolucion + '\'' +
                ", taza='" + taza + '\'' +
                ", smart=" + smart +
                ", pulgadas=" + pulgadas;
    }
}
