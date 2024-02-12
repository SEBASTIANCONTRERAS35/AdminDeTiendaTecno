package usuarios;
/**
 * El paquete "usuarios" contiene las clases relacionadas con la gestión de usuarios en el sistema bibliotecario.
 * Proporciona las clases necesarias para crear, autenticar, buscar y administrar usuarios del sistema.
 */

import Interfaces.Administrar;
import Productos.DispositivoElectronico;
import Productos.Factory;
import excepciones.ContraseñaEquivocadaException;
import excepciones.NumNuevosElementosNoValidoException;
import excepciones.ProductoNoEncontradoException;
import excepciones.SinProductosException;

import java.io.*;
import java.util.*;

/**
 * Clase Admin que hereda de Usuario
 * @author emiliocontreras
 * @version 26 Jun 2023
 */
public class Admin extends Usuario  implements Administrar{

private final static ArrayList<Admin>ADMINISTRADORES=new ArrayList<>();
    /**
     * Verifica si el nombre de Comprador ya existe
     * @param name
     * @param direccion
     * @param telefono
     * @param edad
     * @param contrasena
     * @return Si no existe otro igual, retornara una nueva instancia
     */

    public static Admin crearUsuario(String name, String direccion, long telefono, int edad, String contrasena) {
        if (ExisteUsuario(name, "Administradores.txt")==false) {
            return new Admin(name,direccion, (int) telefono,edad,contrasena);
        } else {
            System.out.println("El nombre de usuario \""+name+"\" ya existe");
            System.out.println("Digita otro porfavor");
            return null;
        }
    }

    /**
     * Los Bibliotecarios no tienen permitido tener libros prestados
     * @param name
     * @param direccion
     * @param telefono
     * @param edad
     * @param contrasena
     */
    protected Admin(String name, String direccion, int telefono, int edad, String contrasena) {
        super(name, direccion, telefono, edad, contrasena);
        ADMINISTRADORES.add(this);
    }

    /**
     * Metodo en el solo el bibliotecario puede acceder
     * Servira para que dentro de su sesion pueda añadir mas libros al archivo de los libros
     * Se le pedira la informacion de cada libro que desee añadir
     */
public static void iniciarSesion(){
    Scanner scanner=new Scanner(System.in);
    System.out.println("Digita el nombre del admnistrador: ");
    String usuario=scanner.next();
    Admin admin = (Admin) buscarUsuarioPorNombre(usuario, "Administradores.txt");
    try{
        if (admin ==null){
            throw  new NullPointerException("No se encontro ningun administrador con este nombre");
        }else {
            System.out.print("Contraseña: ");
            String contrasena=scanner.next();
            if (!Objects.equals(contrasena, admin.getContrasena())){
                throw new ContraseñaEquivocadaException("Contraseña equivocada");
            }else {
                int opcion=0;
                while(opcion!=7){
                    System.out.println("Bienvenido " + usuario);
                    System.out.println("Que deseas realizar:");
                    System.out.println("1.Mostrar inventario");
                    System.out.println("2.Agregar productos");
                    System.out.println("3.Eliminar productos");
                    System.out.println("4.Establecer descuentos");
                    System.out.println("5.Crear nuevo Admin");
                    System.out.println("6.Eliminar Admin ");
                    System.out.println("7.Cerrar Sesion");
                    opcion= scanner.nextInt();
                    switch (opcion){
                        case 1:{
                            admin.mostrarInventario();
                            break;
                        }
                        case 2:{
                            admin.anadirProductosInventario();
                            break;
                        }
                        case 3:{
                            admin.eliminarProductosInventario();
                            break;
                        }
                        case 4:{
                            System.out.println("Elige uno de los descuentos que puedes establecer");
                        }
                        case 5:{
                            System.out.println("Dame el nombre del nuevo admin: ");
                            String name= scanner.next();
                            System.out.println("Dame la direccion: ");
                            scanner.nextLine();
                            String direccion= scanner.nextLine();
                            System.out.println("Digita su telefono: ");
                            long tel= scanner.nextLong();
                            System.out.println("Digita su edad: ");
                            int edad= scanner.nextInt();
                            System.out.println("Digita su contraseña: ");
                            scanner.nextLine();
                            String contraseña= scanner.nextLine();
                            creacionAdmin(name,direccion,tel,edad,contraseña);
                            break;
                        }
                        case 6:{
                            System.out.println("Digita el nombre del admin a eliminar: ");
                            String name= scanner.next();
                            if (name.equalsIgnoreCase(usuario)){
                                System.out.println("No te puedes eliminar a ti mismo como administrador");
                            }
                           else  eliminarUsuario(name, "Administradores.txt");
                            break;
                        }
                    }
                }


            }

        }
    }catch (ContraseñaEquivocadaException |NullPointerException e){
        System.out.println(e.getMessage());
    }
}


    public void mostrarInventario() {
        Map< DispositivoElectronico,Integer> inventario=DispositivoElectronico.getInventario();
        for (Map.Entry<DispositivoElectronico, Integer> entry : inventario.entrySet()) {
            DispositivoElectronico dispositivo = entry.getKey();
            int cantidad = entry.getValue();

            String descripcion=(dispositivo.toString() + " - Cantidad: " + cantidad);
            System.out.println(formatoProducto(descripcion));
        }
    }
    public void eliminarProductosInventario(){
        Scanner scanner = new Scanner(System.in);
        mostrarInventario();
        System.out.println("Digita el numero identificador del producto a eliminar: ");
        int producto = scanner.nextInt();
        boolean encontrado = false;

        try {
            Map<DispositivoElectronico, Integer> inventario = DispositivoElectronico.getInventario();
            if (inventario.isEmpty()){
                throw new SinProductosException("Aun no hay nada en el inventario");
            }else{

                Iterator<Map.Entry<DispositivoElectronico, Integer>> iterator = inventario.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<DispositivoElectronico, Integer> entry = iterator.next();
                    DispositivoElectronico dispo = entry.getKey();

                    if (dispo.getIdentificador() == producto) {
                        encontrado = true;
                        int elementosEnInventario = entry.getValue();

                        if (elementosEnInventario > 1) {
                            System.out.println("Cuantos elementos de este producto quieres eliminar: ");
                            int elementosAEliminar = scanner.nextInt();

                            if (elementosEnInventario < elementosAEliminar || elementosAEliminar <= 0) {
                                throw new SinProductosException("La cantidad a eliminar no es válida");
                            }

                            if (elementosEnInventario - elementosAEliminar == 0) {
                                iterator.remove();
                            } else {
                                entry.setValue(elementosEnInventario - elementosAEliminar);
                            }
                        }else{
                            iterator.remove();
                        }
                    }
                }

                if (!encontrado) {
                    throw new ProductoNoEncontradoException("El producto que deseas eliminar no existe en el inventario");
                }else{
                    System.out.println("Producto eliminado");
                }
            }
        } catch (SinProductosException | ProductoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }
    public void anadirProductosInventario(){
        Scanner scanner=new Scanner(System.in);
        try{
            System.out.print("Cuantos productos (diferentes) vas a añadir: ");
            int productos=scanner.nextInt();
            if(productos<=0){
                throw new NumNuevosElementosNoValidoException("Numero de nuevos productos no valido");
            }
            else {  for (int i=0;i<productos;i++){
                System.out.println("Producto: "+(i+1));
                System.out.println("Cuantos elementos de este producto vas a añadir : ");
                int cantidadElementos= scanner.nextInt();
                if(cantidadElementos<=0){
                    throw new NumNuevosElementosNoValidoException("Numero de nuevos elementos no valido");
                }
                else{ DispositivoElectronico nuevoDispositivo = null;
                    System.out.println("Numero identificador del producto: ");
                    int identificador= scanner.nextInt();
                    Map<DispositivoElectronico, Integer> inventario = DispositivoElectronico.getInventario();
                    boolean existent = false;
                    for (Map.Entry<DispositivoElectronico, Integer> entry : inventario.entrySet()) {
                        DispositivoElectronico dispositivo = entry.getKey();
                        if (dispositivo.getIdentificador()==identificador) {
                            System.out.println("El producto con el identificador ya existe");
                            System.out.println("Agregado "+cantidadElementos+"  elementos mas de ese producto al inventario");
                            int cantidadExistente = entry.getValue();
                            inventario.remove(entry.getKey());
                            inventario.put(dispositivo, cantidadExistente + cantidadElementos);
                            existent = true;
                            break;
                        }
                    }
                    if(!existent) {
                        System.out.println("Calidad: ");
                        System.out.println("1.Gama alta");
                        System.out.println("2.Gama media");
                        System.out.println("3.Gama baja");
                        int calidad = scanner.nextInt();
                        System.out.println("Nombre: ");
                        scanner.nextLine();
                        String nombre = scanner.next();
                        System.out.println("Marca: ");
                        scanner.nextLine();
                        String marca = scanner.next();
                        System.out.println("Modelo: ");
                        scanner.nextLine();
                        int modelo = scanner.nextInt();
                        System.out.println("Precio: ");
                        double precio = scanner.nextDouble();
                        System.out.println("Procesador: ");
                        scanner.nextLine();
                        String procesador = scanner.next();
                        System.out.println("Tipo de produto: ");
                        System.out.println("1.- Celular");
                        System.out.println("2.- Laptop");
                        System.out.println("3.- Pc");
                        System.out.println("4.- Smartphone");
                        System.out.println("5.- Tablet");
                        System.out.println("6.- Television");
                        int tipo = scanner.nextInt();
                        switch (tipo) {
                            case 1:
                                System.out.println("Duracion de la bateria: (hrs)");
                                int duracionCel = scanner.nextInt();
                                System.out.println("Capacidad: (GBs)");
                                int capacidadCel = scanner.nextInt();
                                System.out.println("Numero de camaras: ");
                                int camarasCel = scanner.nextInt();
                                System.out.println("Tamaño de pantalla: (inch)");
                                double pantallaCel = scanner.nextDouble();
                                System.out.println("Tipo de desbloqueo: ");
                                String desbloqueoCel = scanner.next();
                                System.out.println("Cuantos sensores: ");
                                int sensoresCel = scanner.nextInt();
                                System.out.println("Cuantas tarjetas Sim puedes llevar: ");
                                int tarjetasSim = scanner.nextInt();
                                nuevoDispositivo = Factory.crearCelular(identificador, calidad, nombre, marca, modelo, precio, procesador, duracionCel, capacidadCel, camarasCel, pantallaCel, desbloqueoCel, sensoresCel, tarjetasSim);

                                break;
                            case 2: {
                                System.out.println("Ingrese los Graficos: ");
                                String graficosLap = scanner.next();
                                System.out.println("Ingrese el tipo de teclado de la Lap:");
                                String tecladoLap = scanner.next();
                                System.out.println("Ingrese el número de núcleos de la Lap:");
                                int nucleosLap = scanner.nextInt();
                                System.out.println("Ingrese la cantidad de RAM de la Lap: (Gbs)");
                                int ram = scanner.nextInt();
                                System.out.println("Tiene ventiladores?: Si/No");
                                scanner.next();
                                String respuesta = scanner.next();
                                boolean ventiladores = respuesta.toUpperCase().equals("SI") ? true : false;
                                System.out.println("Duracion de la bateria de la lap: (hrs)");
                                int duracionBateria = scanner.nextInt();
                                System.out.println("Tamaño de la pantalla de la lap: (inch)");
                                double pantallaLap = scanner.nextDouble();
                                nuevoDispositivo = Factory.crearLaptop(identificador, calidad, nombre, marca, modelo, precio, procesador, graficosLap, tecladoLap, nucleosLap, ram, ventiladores, duracionBateria, pantallaLap);

                                break;
                            }
                            case 3: {
                                System.out.println("Ingrese los gráficos de la PC:");
                                String graficos = scanner.next();
                                System.out.println("Ingrese el tipo de teclado de la PC:");
                                String teclado = scanner.next();
                                System.out.println("Ingrese el número de núcleos de la PC:");
                                int nucleosPc = scanner.nextInt();
                                System.out.println("Ingrese la cantidad de RAM de la PC: (Gbs)");
                                int ramPc = scanner.nextInt();
                                System.out.println("Ingrese el consumo de energía de la PC: (watts)");
                                int consumoEnergiaPc = scanner.nextInt();
                                nuevoDispositivo = Factory.crearPc(identificador, calidad, nombre, marca, modelo, precio, procesador, graficos, teclado, nucleosPc, ramPc, consumoEnergiaPc);
                                break;
                            }
                            case 4: {
                                System.out.println("Ingrese la duración de la batería del SmartPhone: (hrs)");
                                int duracionBateriaSP = scanner.nextInt();
                                System.out.println("Ingrese la capacidad de almacenamiento del SmartPhone: (Gbs)");
                                int capacidadSP = scanner.nextInt();
                                System.out.println("Ingrese la cantidad de cámaras del SmartPhone:");
                                int ncamarasSP = scanner.nextInt();
                                System.out.println("Ingrese el tamaño de pantalla del SmartPhone:(inch)");
                                double tamañoPantallaSP = scanner.nextDouble();
                                System.out.println("Ingrese los píxeles de la cámara del SmartPhone:");
                                String desbloqueoSP = scanner.next();
                                System.out.println("Ingrese la cantidad de sensores del SmartPhone:");
                                int sensoresSP = scanner.nextInt();
                                System.out.println("Ingrese la cantidad de tarjetas SIM del SmartPhone:");
                                int tarjetasSimSP = scanner.nextInt();
                                System.out.println("Ingrese el modelo de IA del SmartPhone:");
                                String modeloIASP = scanner.next();
                                System.out.println("Ingrese el sistema operativo del SmartPhone:");
                                String sistemaOperativoSP = scanner.next();
                                nuevoDispositivo = Factory.crearSmartPhone(identificador, calidad, nombre, marca, modelo, precio, procesador, duracionBateriaSP, capacidadSP, ncamarasSP, tamañoPantallaSP, desbloqueoSP, sensoresSP, tarjetasSimSP, modeloIASP, sistemaOperativoSP);
                                break;
                            }
                            case 5: {
                                System.out.println("Ingrese la duración de la batería de la tablet: (hrs)");
                                int duracionDeBateria = scanner.nextInt();
                                System.out.println("Ingrese el número de cámaras de la tablet: ");
                                int ncamaras = scanner.nextInt();
                                System.out.println("Ingrese el tamaño de pantalla del la tablet: (inch)");
                                double tamañoDePantalla = scanner.nextDouble();
                                System.out.println("Ingrese la capacidad de la Tablet: (Gbs)");
                                int capacidad = scanner.nextInt();
                                System.out.println("Ingrese el tipo de lápiz de la Tablet: ");
                                scanner.nextLine();
                                String lapiz = scanner.next();
                                System.out.println("Ingrese el tipo de teclado de la Tablet: ");
                                String teclado = scanner.next();
                                System.out.println("¿La Tablet puede servir para programar? (Si/No): ");
                                scanner.nextLine();
                                String respuesta = scanner.next();
                                boolean programar = respuesta.toUpperCase().equals("SI") ? true : false;
                                nuevoDispositivo = Factory.crearTablet(identificador, calidad, nombre, marca, modelo, precio, procesador, duracionDeBateria, capacidad, ncamaras, tamañoDePantalla, lapiz, teclado, programar);
                                break;
                            }
                            case 6: {
                                System.out.println("Ingrese la resolución de la televisión: ");
                                String resolucion = scanner.next();
                                System.out.println("Ingrese la taza de refresco de la televisión: (Hz)");
                                String taza = scanner.next();
                                System.out.println("¿La televisión es smart? (Si/No): ");
                                scanner.nextLine();
                                String respuesta = scanner.next();
                                boolean smart = respuesta.toUpperCase().equals("SI") ? true : false;
                                System.out.println("Ingrese las pulgadas de la televisión: (inch)");
                                double pulgadas = scanner.nextDouble();
                                nuevoDispositivo = Factory.crearTelevision(identificador, calidad, nombre, marca, modelo, precio, procesador, resolucion, taza, smart, pulgadas);
                                break;
                            }

                        }
                        inventario.put(nuevoDispositivo, cantidadElementos);
                    }
                }

            }}




        }catch (NullPointerException e){
            System.out.println("El usuario digitado no existe");
        }catch (InputMismatchException e){
            System.out.println("El formato no es el esperado");
        }catch (NumNuevosElementosNoValidoException e){
            System.out.println(e.getMessage());
        }
    }






    private static void guardarUsuarios(String archivo) {
        try (PrintWriter writer = new PrintWriter(archivo)) {
            for (Usuario usuario : ADMINISTRADORES) {
                writer.println(usuario.toString());
            }
            writer.flush();
            System.out.println("Usuarios guardados exitosamente en el archivo.");
        } catch (IOException e) {
            System.out.println("Error al guardar los Usuarios en el archivo: " + e.getMessage());
        }
    }

    /**
     * Metodo en la que automaticamente al iniciar el programa se crea un bibliotecario este tendra los siguientes atributos:
     * Nombre:Admin
     * Direccion:"Facultad de Ingenieria"
     * Telefono:55987634
     * Edad:40
     * Contraseña:DonRata
     * El usuario bibliotecario se guardara en un archivo de texto llamado "Administradores"
     */

    private static void cargarUsuarios(String archivo) {
        ADMINISTRADORES.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("{")) {
                    String[] parts = line.substring(1, line.length() - 1).split(", ");
                    String nombre = getValue(parts[1]);
                    String direccion = getValue(parts[2]);
                    int ID = Integer.parseInt(getValue(parts[3]));
                    int telefono = Integer.parseInt(getValue(parts[4].replaceAll("[^0-9]", "")));
                    int edad= Integer.parseInt(getValue(String.valueOf(5)));
                    String contrasena = getValue(parts[6]);
                   new Admin(nombre, direccion, telefono, edad, contrasena);
                    contadorID = Math.max(contadorID, ID);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los usuarios desde el archivo: " + e.getMessage());
        }
    }
    private static void creacionAdmin( String name,String direccion,long tel,int edad,String contraseña) {
        try {
            File archivo = new File("Administradores.txt");

            if (archivo.createNewFile()) {
                System.out.println("Archivo creado exitosamente: " + archivo.getName());
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al crear el archivo: " + e.getMessage());
        }
        cargarUsuarios("Administradores.txt");
        crearUsuario(name, direccion, tel, edad, contraseña);
        guardarUsuarios("Administradores.txt");

    }

    private static void eliminarUsuario(String nombre, String archivo) {
        nombre = nombre.toUpperCase().replaceAll(" ", "");
        String nombreArchivoTemporal = "temp.txt";
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivoTemporal))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("{")) {
                    String[] parts = line.substring(1, line.length() - 1).split(", ");
                    String nombreUsuario = getValue(parts[1]);
                    String nombreUsuarioSinEspacios = nombreUsuario.replaceAll(" ", "");
                    if (!nombreUsuarioSinEspacios.toUpperCase().equals(nombre)) {
                        writer.write(line);
                        writer.newLine();
                    } else {
                        encontrado = true;
                    }
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar el Administrador: " + e.getMessage());
        }

        File archivoOriginal = new File(archivo);
        File archivoTemporal = new File(nombreArchivoTemporal);
        if (archivoTemporal.renameTo(archivoOriginal)) {
            if (encontrado) {
                System.out.println("Administrador eliminado correctamente.");
            } else {
                System.out.println("No se encontró el usuario.");
            }
        } else {
            System.out.println("Error al eliminar el usuario. No se pudo reemplazar el archivo original.");
        }
    }

    /**
     *
     * @return informacion del usuario
     */
    @Override
    public String toString() {
        return "{"+
                "Admin"+
                ", nombre=" + this.getName() +
                ", direccion=" + this.getDireccion()  +
                ", ID=" + this.getId() +
                ", telefono=" + this.getTelefono() +
                ", edad=" + this.getEdad() +
                ", contrasena=" + this.getContrasena() +
                '}';
    }
}
