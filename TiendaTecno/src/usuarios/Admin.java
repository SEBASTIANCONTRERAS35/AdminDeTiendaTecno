package usuarios;
import Interfaces.Administrar;
import Productos.DispositivoElectronico;
import Productos.Factory;
import Excepciones.ContraseñaEquivocadaException;
import Excepciones.NumNuevosElementosNoValidoException;
import Excepciones.ProductoNoEncontradoException;
import Excepciones.SinProductosException;
import java.io.*;
import java.util.*;

public class Admin extends Usuario  implements Administrar{

private final static ArrayList<Admin>ADMINISTRADORES=new ArrayList<>();
    private static Admin crearUsuario(String name, String direccion, long telefono, int edad, String contrasena) {
        if (ExisteUsuario(name, "Administradores.txt")==false) {
            return new Admin(name,direccion, (int) telefono,edad,contrasena);
        } else {
            System.out.println("El nombre de usuario \""+name+"\" ya existe");
            System.out.println("Digita otro porfavor");
            return null;
        }
    }


    protected Admin(String name, String direccion, int telefono, int edad, String contrasena) {
        super(name, direccion, telefono, edad, contrasena);
        ADMINISTRADORES.add(this);
    }


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
                while(opcion!=6){
                    System.out.println("Bienvenido " + usuario);
                    System.out.println("Que deseas realizar:");
                    System.out.println("1.Mostrar inventario");
                    System.out.println("2.Agregar productos");
                    System.out.println("3.Eliminar productos");
                    System.out.println("4.Crear nuevo Admin");
                    System.out.println("5.Eliminar Admin ");
                    System.out.println("6.Cerrar Sesion");
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
                        case 5:{
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
                        System.out.println("1. Gama alta");
                        System.out.println("2. Gama media");
                        System.out.println("3. Gama baja");
                        int calidad = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Nombre: ");
                        String nombre = scanner.nextLine();

                        System.out.println("Marca: ");
                        String marca = scanner.nextLine();

                        System.out.println("Modelo: ");
                        int modelo = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Precio: ");
                        double precio = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.println("Procesador: ");
                        String procesador = scanner.nextLine();

                        System.out.println("Tipo de producto: ");
                        System.out.println("1. Celular");
                        System.out.println("2. Laptop");
                        System.out.println("3. PC");
                        System.out.println("4. Smartphone");
                        System.out.println("5. Tablet");
                        System.out.println("6. Televisión");
                        int tipo = scanner.nextInt();
                        scanner.nextLine();
                        switch (tipo) {
                            case 1 -> {
                                System.out.println("Duracion de la bateria (hrs):");
                                int duracionCel = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Capacidad (GBs):");
                                int capacidadCel = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Numero de camaras:");
                                int camarasCel = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Tamaño de pantalla (inch):");
                                double pantallaCel = scanner.nextDouble();
                                scanner.nextLine();
                                System.out.println("Tipo de desbloqueo:");
                                String desbloqueoCel = scanner.nextLine();
                                System.out.println("Cuantos sensores:");
                                int sensoresCel = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Cuantas tarjetas SIM puedes llevar:");
                                int tarjetasSim = scanner.nextInt();
                                scanner.nextLine();
                                nuevoDispositivo = Factory.crearCelular(identificador, calidad, nombre, marca, modelo, precio, procesador, duracionCel, capacidadCel, camarasCel, pantallaCel, desbloqueoCel, sensoresCel, tarjetasSim);
                            }
                            case 2 -> {
                                System.out.println("Ingrese los Gráficos:");
                                String graficosLap = scanner.nextLine();
                                System.out.println("Ingrese el tipo de teclado de la Laptop:");
                                String tecladoLap = scanner.nextLine();
                                System.out.println("Ingrese el número de núcleos de la Laptop:");
                                int nucleosLap = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Ingrese la cantidad de RAM de la Laptop: (GBs)");
                                int ram = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("¿Tiene ventiladores?: Si/No");
                                String respuesta = scanner.nextLine();
                                boolean ventiladores = respuesta.toUpperCase().equals("SI") ? true : false;
                                System.out.println("Duración de la batería de la Laptop: (hrs)");
                                int duracionBateria = scanner.nextInt();
                                System.out.println("Tamaño de la pantalla de la Laptop: (inch)");
                                double pantallaLap = scanner.nextDouble();
                                scanner.nextLine();
                                nuevoDispositivo = Factory.crearLaptop(identificador, calidad, nombre, marca, modelo, precio, procesador, graficosLap, tecladoLap, nucleosLap, ram, ventiladores, duracionBateria, pantallaLap);

                            }
                            case 3 -> {
                                System.out.println("Ingrese los gráficos de la PC:");
                                String graficos = scanner.next();
                                System.out.println("Ingrese el tipo de teclado de la PC:");
                                String teclado = scanner.next();
                                System.out.println("Ingrese el número de núcleos de la PC:");
                                int nucleosPc = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Ingrese la cantidad de RAM de la PC: (GBs)");
                                int ramPc = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Ingrese el consumo de energía de la PC: (watts)");
                                int consumoEnergiaPc = scanner.nextInt();
                                scanner.nextLine();
                                nuevoDispositivo = Factory.crearPc(identificador, calidad, nombre, marca, modelo, precio, procesador, graficos, teclado, nucleosPc, ramPc, consumoEnergiaPc);
                            }
                            case 4 -> {
                                System.out.println("Ingrese la duración de la batería del SmartPhone: (hrs)");
                                int duracionBateriaSP = scanner.nextInt();
                                System.out.println("Ingrese la capacidad de almacenamiento del SmartPhone: (GBs)");
                                int capacidadSP = scanner.nextInt();
                                System.out.println("Ingrese la cantidad de cámaras del SmartPhone:");
                                int ncamarasSP = scanner.nextInt();
                                System.out.println("Ingrese el tamaño de pantalla del SmartPhone: (inch)");
                                double tamañoPantallaSP = scanner.nextDouble();
                                scanner.nextLine();
                                System.out.println("Ingrese tipo de debloqueo: ");
                                String desbloqueoSP = scanner.nextLine();
                                System.out.println("Ingrese la cantidad de sensores del SmartPhone:");
                                int sensoresSP = scanner.nextInt();
                                System.out.println("Ingrese la cantidad de tarjetas SIM del SmartPhone:");
                                int tarjetasSimSP = scanner.nextInt();
                                System.out.println("Ingrese el modelo de IA del SmartPhone:");
                                String modeloIASP = scanner.next();
                                System.out.println("Ingrese el sistema operativo del SmartPhone:");
                                String sistemaOperativoSP = scanner.next();
                                nuevoDispositivo = Factory.crearSmartPhone(identificador, calidad, nombre, marca, modelo, precio, procesador, duracionBateriaSP, capacidadSP, ncamarasSP, tamañoPantallaSP, desbloqueoSP, sensoresSP, tarjetasSimSP, modeloIASP, sistemaOperativoSP);
                            }
                            case 5 -> {
                                System.out.println("Ingrese la duración de la batería de la tablet: (hrs)");
                                int duracionDeBateria = scanner.nextInt();

                                System.out.println("Ingrese el número de cámaras de la tablet: ");
                                int ncamaras = scanner.nextInt();

                                System.out.println("Ingrese el tamaño de pantalla de la tablet: (inch)");
                                double tamañoDePantalla = scanner.nextDouble();

                                System.out.println("Ingrese la capacidad de la Tablet: (GBs)");
                                int capacidad = scanner.nextInt();

                                System.out.println("Ingrese el tipo de lápiz de la Tablet: ");
                                scanner.nextLine(); // Consumir el salto de línea
                                String lapiz = scanner.next();

                                System.out.println("Ingrese el tipo de teclado de la Tablet: ");
                                String teclado = scanner.next();

                                System.out.println("¿La Tablet puede servir para programar? (Si/No): ");
                                scanner.nextLine();
                                String respuesta = scanner.next();
                                boolean programar = respuesta.toUpperCase().equals("SI") ? true : false;
                                nuevoDispositivo = Factory.crearTablet(identificador, calidad, nombre, marca, modelo, precio, procesador, duracionDeBateria, capacidad, ncamaras, tamañoDePantalla, lapiz, teclado, programar);
                            }
                            case 6 -> {
                                System.out.println("Ingrese la resolución de la televisión: ");
                                String resolucion = scanner.next();

                                System.out.println("Ingrese la tasa de refresco de la televisión: (Hz)");
                                String taza = scanner.next();

                                System.out.println("¿La televisión es smart? (Si/No): ");
                                scanner.nextLine(); // Consumir el salto de línea
                                String respuesta = scanner.next();
                                boolean smart = respuesta.toUpperCase().equals("SI") ? true : false;
                                System.out.println("Ingrese las pulgadas de la televisión: (inch)");
                                double pulgadas = scanner.nextDouble();
                                nuevoDispositivo = Factory.crearTelevision(identificador, calidad, nombre, marca, modelo, precio, procesador, resolucion, taza, smart, pulgadas);
                            }
                        }
                        inventario.put(nuevoDispositivo, cantidadElementos);
                    }
                }

            }}




        }catch (InputMismatchException e){
            System.out.println("El formato no es el esperado");
        }catch (NumNuevosElementosNoValidoException e){
            System.out.println(e.getMessage());
        }
    }











    public static void creacionAdmin(String name, String direccion, long tel, int edad, String contraseña) {
        try {
            File archivo = new File("Administradores.txt");

            if (archivo.createNewFile()) {
                System.out.println("Archivo creado exitosamente: " + archivo.getName());
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al crear el archivo: " + e.getMessage());
        }
        cargarUsuarios("Administradores.txt",ADMINISTRADORES);
        crearUsuario(name, direccion, tel, edad, contraseña);
        guardarUsuarios("Administradores.txt",ADMINISTRADORES);

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
