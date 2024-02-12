package Productos;

public class PC extends Computadora{
    private int consunoDeEnergia;

    public PC(int identificador, int calidad, String nombre, String marca, int modelo, double precio, String procesador, String graficos, String teclado, int nnucleos, int ram, int consunoDeEnergia) {
        super(identificador, calidad, nombre, marca, modelo, precio, procesador, graficos, teclado, nnucleos, ram);
        this.consunoDeEnergia = consunoDeEnergia;
    }



    @Override
    public String toString() {
        return " PC: " +super.toString()+
                ", consunoDeEnergia=" + consunoDeEnergia ;
    }
}
