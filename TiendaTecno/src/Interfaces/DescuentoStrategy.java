package Interfaces;

import Productos.DispositivoElectronico;

import java.util.Map;

public interface DescuentoStrategy {
    double aplicarDescuento(double precio, Map<DispositivoElectronico,Integer> Carrito);
}
