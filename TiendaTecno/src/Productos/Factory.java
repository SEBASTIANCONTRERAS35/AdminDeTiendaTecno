package Productos;

public class Factory {
    public static DispositivoElectronico crearCelular(int identificador,int calidad,String nombre, String marca, int modelo, double precio, String procesador, int duracionBateria, int capacidad, int NCamaras, double pulgadas, String debloqueo, int sensores, int tarjetasSim){
        Celular celular =new Celular(identificador,calidad,nombre, marca, modelo, precio, procesador, duracionBateria, capacidad, NCamaras, pulgadas, debloqueo, sensores, tarjetasSim);
        return celular;
    }
    public static DispositivoElectronico crearTablet(int identificador,int calidad,String nombre, String marca, int modelo, double precio, String procesador, int duracionBateria, int capacidad, int NCamaras, double pulgadas, String lapiz, String teclado, boolean programar){
        Tablet tablet=new Tablet(identificador,calidad,nombre, marca, modelo, precio, procesador, duracionBateria, capacidad, NCamaras, pulgadas, lapiz, teclado, programar);
        return tablet;
    }

    public static DispositivoElectronico crearTelevision(int identificador,int calidad,String nombre, String marca, int modelo, double precio, String procesador, String resolucion, String taza, boolean smart, double pulgadas){
        Television television=new Television(identificador,calidad,nombre, marca, modelo, precio, procesador, resolucion, taza, smart, pulgadas);
        return television;
    }

    public static DispositivoElectronico crearPc(int identificador,int calidad,String nombre, String marca, int modelo, double precio, String procesador, String graficos, String teclado, int nnucleos, int ram, int consunoDeEnergia){
        PC pc=new PC(identificador,calidad,nombre, marca, modelo, precio, procesador, graficos, teclado, nnucleos, ram, consunoDeEnergia);
        return pc;
    }
    public static DispositivoElectronico crearSmartPhone(int identificador,int calidad,String nombre, String marca, int modelo, double precio, String procesador, int duracionBateria, int capacidad, int NCamaras, double pulgadas, String debloqueo, int sensores, int tarjetasSim, String modeloIA, String sistemaOperativo){
        SmartPhone smartPhone=new SmartPhone(identificador,calidad,nombre, marca, modelo, precio, procesador, duracionBateria, capacidad, NCamaras, pulgadas, debloqueo, sensores, tarjetasSim, modeloIA, sistemaOperativo);
        return  smartPhone;
    }
    public static DispositivoElectronico crearLaptop(int identificador,int calidad,String nombre, String marca, int modelo, double precio, String procesador, String graficos, String teclado, int nnucleos, int ram, boolean ventiladores, int duracionDeBateria, double pulgadas){
        Laptop laptop=new Laptop(identificador,calidad,nombre, marca, modelo, precio, procesador, graficos, teclado, nnucleos, ram, ventiladores, duracionDeBateria, pulgadas);
        return laptop;
    }

}
