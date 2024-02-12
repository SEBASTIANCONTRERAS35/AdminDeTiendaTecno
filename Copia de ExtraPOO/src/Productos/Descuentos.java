package Productos;

import Interfaces.DescuentoStrategy;

import java.util.Map;

public class Descuentos {
    public static class DescuentoSocio implements DescuentoStrategy {
        @Override
        public double aplicarDescuento(double totalAPagar, Map<DispositivoElectronico,Integer> Carrito) {
            double regalo=0;
            for (Map.Entry<DispositivoElectronico, Integer> entry : Carrito.entrySet()){
                DispositivoElectronico dispositivoElectronico= entry.getKey();
                int cantidad= entry.getValue();
                if (cantidad>=3){
                    System.out.println("LLevas muchos "+dispositivoElectronico.getNombre()+", se te hara un descuento del 30%");
                    double totalProductos=dispositivoElectronico.getPrecio()*cantidad;
                    regalo+=totalProductos*0.3;
                }
            }

                return totalAPagar-=regalo;
        }
    }

    public static class DescuentoVIP implements DescuentoStrategy {

        @Override
        public double aplicarDescuento(double totalAPagar, Map<DispositivoElectronico,Integer> Carrito) {
            System.out.println("Se te hara un descuento del 10%");
            System.out.println("Tu total a pagar fue de "+totalAPagar);
            double descuento=totalAPagar*0.1;
            totalAPagar-=descuento;
            System.out.println("Con el descuento son "+totalAPagar);
            return totalAPagar;
        }
    }

    public static class DescuentoEstudiante implements DescuentoStrategy {
        @Override
        public double aplicarDescuento(double totalAPagar, Map<DispositivoElectronico,Integer> Carrito) {
            if (totalAPagar>=5000){
                System.out.println("Como tu compra es mas de 5000 se te hara un descuento del 40%");
                System.out.println("Tu total a pagar fue de "+totalAPagar);
                double descuento=totalAPagar*0.4;
                totalAPagar-=descuento;
                System.out.println("Con el descuento son "+totalAPagar);
                return totalAPagar;
            }
            else return totalAPagar;

        }
    }

}


