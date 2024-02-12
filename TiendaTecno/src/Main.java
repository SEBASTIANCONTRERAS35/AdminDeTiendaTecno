import Productos.DispositivoElectronico;
import Productos.Factory;
import usuarios.Admin;
import usuarios.Comprador;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;


public class Main {
    static Scanner scanner=new Scanner(System.in);

    public static void main(String[] args) {
    Admin.creacionAdmin("Admin","Facultad de Ingenieria",55334352,45,"DonRata");
        Map<DispositivoElectronico, Integer> inventario = DispositivoElectronico.getInventario();
        DispositivoElectronico celular = Factory.crearCelular(1, 1, "Smartphone", "Samsung", 2023, 999.99, "Snapdragon", 24, 128, 3, 6.5, "Huella", 5, 2);
        DispositivoElectronico tablet = Factory.crearTablet(2, 2, "Tablet", "Apple", 2023, 899.99, "A14 Bionic", 18, 64, 2, 10.2, "Apple Pencil", "Touch", true);
        DispositivoElectronico television = Factory.crearTelevision(3, 3, "Smart TV", "Sony", 2023, 1499.99, "Bravia X1", "4K", "120Hz", true, 55);
        DispositivoElectronico pc = Factory.crearPc(4, 1, "Gaming PC", "Alienware", 2023, 2499.99, "Intel i9", "Nvidia RTX 3080", "Mechanical", 12, 32, 750);
        DispositivoElectronico smartphone = Factory.crearSmartPhone(5, 2, "Flagship Phone", "OnePlus", 2023, 899.99, "Snapdragon", 20, 256, 4, 6.7, "Face ID", 4, 2, "Neural Engine", "Oxygen OS");
        DispositivoElectronico laptop1 = Factory.crearLaptop(6, 1, "High Performance Laptop", "Dell", 2023, 1599.99, "Intel i7", "Nvidia GTX 3060", "Backlit", 8, 16, true, 10, 15.6);
        DispositivoElectronico laptop2 = Factory.crearLaptop(7, 2, "Business Laptop", "Lenovo", 2023, 1199.99, "Intel i5", "Integrated", "Standard", 4, 8, false, 12, 14);
        DispositivoElectronico pc1 = Factory.crearPc(8, 2, "Home PC", "HP", 2023, 899.99, "AMD Ryzen", "Integrated", "Membrane", 6, 12, 500);
        DispositivoElectronico pc2 = Factory.crearPc(9, 3, "Workstation", "Dell", 2023, 1999.99, "Intel Xeon", "Nvidia Quadro RTX 4000", "Mechanical", 16, 64, 800);
        DispositivoElectronico smartphone2 = Factory.crearSmartPhone(10, 3, "Mid-range Phone", "Xiaomi", 2023, 399.99, "Snapdragon", 12, 128, 3, 6.2, "Fingerprint", 3, 2, "MIUI", "Android");
        DispositivoElectronico celular2 = Factory.crearCelular(11, 2, "Budget Smartphone", "Motorola", 2023, 199.99, "MediaTek", 20, 64, 2, 6.1, "Pattern", 2, 1);
        DispositivoElectronico tablet2 = Factory.crearTablet(12, 1, "Kids Tablet", "Amazon", 2023, 129.99, "Amazon Custom", 12, 32, 1, 7.0, "No", "Touch", false);
        DispositivoElectronico television2 = Factory.crearTelevision(13, 1, "HD TV", "LG", 2023, 499.99, "LG Processor", "1080p", "60Hz", false, 42);
        DispositivoElectronico laptop3 = Factory.crearLaptop(14, 3, "Ultraportable Laptop", "Apple", 2023, 1899.99, "M1 Chip", "Integrated", "Butterfly", 8, 16, false, 12, 13.3);
        DispositivoElectronico pc3 = Factory.crearPc(15, 1, "Family PC", "Acer", 2023, 699.99, "AMD Ryzen", "Integrated", "Standard", 4, 8, 400);
inventario.put(celular, 4);
inventario.put(tablet2, 2);
inventario.put(tablet, 4);
inventario.put(television2, 2);
inventario.put(smartphone2, 5);
inventario.put(laptop3, 7);
inventario.put(pc, 4);
inventario.put(pc3, 2);
inventario.put(celular2, 4);
inventario.put(smartphone, 3);
inventario.put(pc1, 2);
inventario.put(pc2, 1);
inventario.put(laptop1, 4);
inventario.put(laptop2, 2);
inventario.put(television, 1);



        /*
          Nombre de usuario:Admin
          Contraseña: DonRata
         */

       try{

            int opcion=0;
                System.out.println("Bienvenido a DonRata TecnoShop");
                System.out.println("-----------------------------------");
            while (opcion!=4){
                limpiarConsola();
                System.out.println("Elige una de las siguientes opciones: ");
                System.out.println("1.Crear una cuenta nueva");
                System.out.println("2.Iniciar sesion en una cuenta existente");
                System.out.println("3.Iniciar sesion como administrador ");
                System.out.println("4.Salir ");
                opcion=scanner.nextInt();
                limpiarConsola();
                switch (opcion){

                    case 1->{
                        limpiarConsola();
                        Comprador.crearUsuario();

                    }
                   case 2-> {
                        limpiarConsola();
                        Comprador.inicioSesion();
                    }
                    case 3->{
                        limpiarConsola();
                        Admin.iniciarSesion();
                    }

                }
            }
        }catch (InputMismatchException e ){
            System.out.println("El formato no es el esperado ");
        }

        }

        public static void limpiarConsola () {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Error al limpiar la consola: " + e.getMessage());
        }


    }

    }


