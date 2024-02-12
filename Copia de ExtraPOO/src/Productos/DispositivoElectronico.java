package Productos;

import java.util.HashMap;
import java.util.Map;

public abstract class DispositivoElectronico {
    private int identificador;
    private int calidad;
   private String nombre;
   private String marca;
   private int modelo;
   private double precio;
   private String procesador;
   private static Map<DispositivoElectronico,Integer> inventario=new HashMap<>();

    protected DispositivoElectronico(int identificador, int calidad, String nombre, String marca, int modelo, double precio, String procesador) {
        this.identificador = identificador;
        this.calidad = calidad;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.procesador = procesador;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }


    public static Map<DispositivoElectronico, Integer> getInventario() {
        return inventario;
    }

    public int getIdentificador() {
        return identificador;
    }

    public int getCalidad() {
        return calidad;
    }

    @Override
    public String toString() {
        return  "# Identificador del producto= "+identificador+
                ", calidad=" + calidad +
                ", nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo=" + modelo +
                ", precio=" + precio +
                ", procesador='" + procesador + '\'' ;
    }
}
