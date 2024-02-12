package Productos;

public class SmartPhone extends Celular{
    private String modeloIA;
    private String sistemaOperativo;

    public SmartPhone(int identificador, int calidad, String nombre, String marca, int modelo, double precio, String procesador, int duracionBateria, int capacidad, int NCamaras, double pulgadas, String debloqueo, int sensores, int tarjetasSim, String modeloIA, String sistemaOperativo) {
        super(identificador, calidad, nombre, marca, modelo, precio, procesador, duracionBateria, capacidad, NCamaras, pulgadas, debloqueo, sensores, tarjetasSim);
        this.modeloIA = modeloIA;
        this.sistemaOperativo = sistemaOperativo;
    }



    @Override
    public String toString() {
        return "SmartPhone: " +super.toString()+
                ", modeloIA='" + modeloIA + '\'' +
                ", sistemaOperativo='" + sistemaOperativo + '\'' ;
    }
}
