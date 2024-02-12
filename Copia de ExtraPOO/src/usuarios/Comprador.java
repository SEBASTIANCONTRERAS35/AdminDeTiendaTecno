package usuarios;
/**
 * El paquete "usuarios" contiene las clases relacionadas con la gestión de usuarios en el sistema bibliotecario.
 * Proporciona las clases necesarias para crear, autenticar, buscar y administrar usuarios del sistema.
 */

import Interfaces.AccionesProductos;
import Interfaces.DescuentoStrategy;
import Productos.DispositivoElectronico;
import excepciones.ContraseñaEquivocadaException;
import excepciones.NumNuevosElementosNoValidoException;
import excepciones.ProductoNoEncontradoException;
import excepciones.SinProductosException;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Semaphore;


/**
 * Clase donde se define los metodos y atributos de los Usuarios
 * @author emiliocontreras
 * @version 26 Jun 2023
 */

public abstract class Comprador extends Usuario implements AccionesProductos  {

    protected double totalAPagar=0;
    protected  Map<DispositivoElectronico,Integer> Carrito = new HashMap<>();

    private static final ArrayList<Comprador> COMPRADORES = new ArrayList<>();
    private DescuentoStrategy descuentoStrategy;

    protected Comprador(String name, String direccion, long telefono, int edad, String contrasena) {
        super(name, direccion, telefono, edad, contrasena);
       COMPRADORES.add(this);
    }

    /**
     *
     * @param name
     * @param direccion
     * @param telefono
     * @param edad
     * @param contrasena
     * Cada instancia contruida se añadira a la lista de Usuarios
     */



    /**
     * Escribe los usuarios que estan en la lista COMPRADORS en un archivo de texto
     * @param archivo Archivo en el que se escribiran los usuarios
     */

    public static void guardarUsuarios(String archivo) {
        try (PrintWriter writer = new PrintWriter(archivo)) {
            for (Comprador comprador : COMPRADORES) {
                writer.println(comprador.toString());
            }
            writer.flush();
            System.out.println("Usuarios guardados exitosamente en el archivo.");
        } catch (IOException e) {
            System.out.println("Error al guardar los Usuarios en el archivo: " + e.getMessage());
        }
    }

    /**
     * Metodo que lee los usuarios que estan en un archivo de texto para que asi el archivo se actualize cada vez que se haga una modificacion
     * @param archivo archivo de texto  en la que se leeran los libros
     */

    public static void eliminarUsuario(String nombre, String archivo) {
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
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
        }

        File archivoOriginal = new File(archivo);
        File archivoTemporal = new File(nombreArchivoTemporal);
        if (archivoTemporal.renameTo(archivoOriginal)) {
            if (encontrado) {
                System.out.println("Comprador eliminado correctamente.");
            } else {
                System.out.println("No se encontró el usuario.");
            }
        } else {
            System.out.println("Error al eliminar el usuario. No se pudo reemplazar el archivo original.");
        }
    }
    public static void cargarUsuarios(String archivo) {
        COMPRADORES.clear();
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
                    String tipoCuenta=parts[0];
                    switch (tipoCuenta){
                        case "Estudiante"->{
                          Estudiante estudiante=new  Estudiante(nombre, direccion, telefono, edad, contrasena);}
                        case "ClienteVip"->{
                            ClienteVip clienteVip =new ClienteVip(nombre,direccion,telefono,edad,contrasena);}
                        case "Socio"->{
                         Socio socio =new Socio(nombre,direccion,telefono,edad,contrasena);}
                    }
                    contadorID = Math.max(contadorID, ID);

                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los usuarios desde el archivo: " + e.getMessage());
        }
    }





    @Override
    public void añadirAlCarrito() {
        limpiarConsola();
        mostrarProductos();
        Scanner scanner = new Scanner(System.in);
        int cantidadDeseada = 0, cantidadExistentes = 0;
        DispositivoElectronico dispositivoAlCarro = null;

        try {
            System.out.println("Digita el numero de identificador de producto");
            int producto = scanner.nextInt();
            Map<DispositivoElectronico, Integer> inventario = DispositivoElectronico.getInventario();
            boolean encontrado = false;

            for (Map.Entry<DispositivoElectronico, Integer> entry : inventario.entrySet()) {
                dispositivoAlCarro = entry.getKey();
                if (dispositivoAlCarro.getIdentificador() == producto && comprobarCalidad(dispositivoAlCarro)) {
                    encontrado = true;
                    cantidadExistentes = entry.getValue();
                    System.out.println("Cuantos vas a querer? ¡TENEMOS " + cantidadExistentes + "!");
                    cantidadDeseada = scanner.nextInt();

                    if (cantidadDeseada > cantidadExistentes) {
                        throw new SinProductosException("No hay en inventario la cantidad que necesitas");
                    } else {
                        boolean yaEnCarrito = false;

                        for (Map.Entry<DispositivoElectronico, Integer> enCarrito : this.Carrito.entrySet()) {
                            DispositivoElectronico dispositivoEnCarrito = enCarrito.getKey();
                            if (dispositivoEnCarrito.getIdentificador() == dispositivoAlCarro.getIdentificador()) {
                                yaEnCarrito = true;
                                int cantidadEnCarrito = enCarrito.getValue();
                                this.Carrito.put(dispositivoEnCarrito, cantidadDeseada + cantidadEnCarrito);
                                break;
                            }
                        }

                        if (!yaEnCarrito) {
                            this.Carrito.put(dispositivoAlCarro, cantidadDeseada);
                        }

                        this.totalAPagar += dispositivoAlCarro.getPrecio() * cantidadDeseada;
                    }
                    break;
                }
            }

            if (cantidadExistentes - cantidadDeseada == 0) {
                DispositivoElectronico.getInventario().remove(dispositivoAlCarro);
            } else {
                DispositivoElectronico.getInventario().put(dispositivoAlCarro, (cantidadExistentes - cantidadDeseada));
            }

            if (!encontrado) {
                throw new SinProductosException("Producto no encontrado");
            }
        } catch (SinProductosException e) {
            System.out.println(e.getMessage());
        }
    }


    private boolean comprobarCalidad(DispositivoElectronico dispositivo) {
        int calidad = dispositivo.getCalidad();
        String claseActual = this.getClass().getSimpleName();

        if (claseActual.equals("Estudiante")) {
            return calidad == 1;
        } else if (claseActual.equals("Socio")) {
            return calidad < 3;
        } else if (claseActual.equals("ClienteVip")) {
            return true;
        }

        return false;
    }


    @Override
    public void mostrarCarrito() {
        System.out.println("TU CARRITO");
        for (Map.Entry<DispositivoElectronico, Integer> entry : this.Carrito.entrySet()){
            DispositivoElectronico dispositivoElectronico= entry.getKey();
            int cantidad= entry.getValue();
            String descripcion=(dispositivoElectronico + " - Cantidad: " + cantidad);
            System.out.println(formatoProducto(descripcion));
        }
        System.out.println("TOTAL A PAGAR: "+this.totalAPagar);
    }
    public void ticket(String archivo,double totalAPagar) {
          try (PrintWriter writer = new PrintWriter(archivo)) {
              for (Map.Entry<DispositivoElectronico, Integer> entry : this.Carrito.entrySet()){
                  writer.println(entry.getKey().toString()+" - Cantidad: "+entry.getValue());
              }
              writer.println("El total fue de "+totalAPagar);
              writer.println("GRACIAS POR TU COMPRA");
              System.out.println("Ticket creado exitosamente en el archivo.");
          } catch (IOException e) {
              System.out.println("Error " + e.getMessage());
          }
      }
      public void Comprar() {
        try {
            Scanner scanner = new Scanner(System.in);
            limpiarConsola();
            if (this.Carrito.isEmpty()) {
                throw new SinProductosException("Aun no tienes productos en tu carrito");
            } else {

                mostrarCarrito();
                System.out.println("Esta seguro que quieres finalizar tu compra? Si/No");
                String respuesta = scanner.nextLine().toUpperCase();
                if (respuesta.equals("SI")) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = currentDate.format(formatter);
                    this.totalAPagar = this.descuentoStrategy.aplicarDescuento(this.totalAPagar,this.Carrito);
                    ticket(this.getName() + " - " + formattedDate + ".txt",totalAPagar);
                    limpiarLista(this.Carrito);
                    this.totalAPagar = 0;
                }
            }
        }catch (SinProductosException e){
            System.out.println(e.getMessage());
        }
      }





    public  void mostrarProductos() {
        System.out.println("Bienvenido");
        System.out.println("Dale un vistazo a los productos que tenemos para ti");
        Map < DispositivoElectronico,Integer>inventario=DispositivoElectronico.getInventario();
        for (Map.Entry<DispositivoElectronico, Integer> entry : inventario.entrySet()) {
            DispositivoElectronico dispositivo = entry.getKey();
            int cantidad = entry.getValue();
           if (comprobarCalidad(dispositivo)){
               String descripcion=(dispositivo + " - Cantidad: " + cantidad);
               System.out.println(formatoProducto(descripcion));
           }
        }
    }

    /**
     * Obtiene el valor de una parte de cadena que sigue al signo de igual (=).
     * @param part La parte de cadena de la que se quiere obtener el valor.
     * @return El valor que sigue al signo de igual (=) en la parte de cadena especificada.
     */

    /**
     * Metodo que devuelve el nombre del usuario
     * @return nombre del usuario
     */

    /**
     * Metodo en la que se crearan los usuarios, se les preguntara sus atributos y dependiendo de que tipo de cuenta
     * son se hara una instancia
     */
    public static void crearUsuario() {
      Scanner  scanner = new Scanner(System.in);
        try {

            System.out.print("Nombre de Comprador: ");
            String nombre = scanner.nextLine();
            System.out.print("Direccion: ");
            String direccion = scanner.nextLine();
            System.out.print("Telefono (6 digitos) : ");
            int telefono = scanner.nextInt();
            System.out.print("Edad: ");
            int edad = scanner.nextInt();
            System.out.print("Contraseña: ");
            scanner.nextLine();
            String contrasena = scanner.nextLine();
            limpiarConsola();
            System.out.println("Selecciona el tipo de cuenta que eres: ");
            System.out.println("1.Cliente Vip");
            System.out.println("2.Estudiante");
            System.out.println("3.Socio");
            System.out.print("Opcion: ");
            int cuenta = scanner.nextInt();
            limpiarConsola();
            Comprador nuevoComprador = null;
            switch (cuenta) {
                case 1 -> {
                    cargarUsuarios("Usuarios.txt");
                    nuevoComprador = ClienteVip.crearUsuario(nombre, direccion, telefono, edad, contrasena);
                    guardarUsuarios("Usuarios.txt");
                }
                case 2 -> {
                    cargarUsuarios("Usuarios.txt");
                    nuevoComprador = Estudiante.crearUsuario(nombre, direccion, telefono, edad, contrasena);
                    guardarUsuarios("Usuarios.txt");
                }
                case 3 -> {
                    cargarUsuarios("Usuarios.txt");
                    nuevoComprador = Socio.crearUsuario(nombre, direccion, telefono, edad, contrasena);
                    guardarUsuarios("Usuarios.txt");
                }
            }
            if (nuevoComprador != null) {
                System.out.println("¡¡CUENTA CREADA CON EXITO!!");
                System.out.println("Bienvenido " + nombre);
            }

            System.out.println("Porfavor vuelve a iniciar sesion en la pagina principal");
            System.out.print("Presiona enter para continuar ");
            scanner.nextLine();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("El formato no es el esperado");
            System.out.print("Presiona enter para continuar ");
            scanner.nextLine();
            scanner.nextLine();
        }

    }

    /**
     *Inicia sesion de un usuario
     * Este método coordina el inicio de sesión de un usuario utilizando un semáforo para
     * controlar el acceso concurrente.
     * Permite que solo un usuario inicie sesion a la vez
     */
   public static void inicioSesion() {
        try {
            semaphore.acquire();
            UsuarioThread usuarioThread = new UsuarioThread();
            usuarioThread.start();
            usuarioThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }/*

    /**
     * Opciones una vez que el hilo del usuario pueda iniciar sesion
     */
    static class UsuarioThread extends Thread {
        Scanner scanner = new Scanner(System.in);

        @Override
        public void run() {
            try {
                Comprador usuario = Comprador.iniciarSesion();
                if (usuario != null) {
                    int opcion2 = 0;
                    while (opcion2 != 7) {
                        limpiarConsola();
                        System.out.println("LLevas un total de $"+usuario.getTotalAPagar());
                        System.out.println("Que quieres hacer? ");
                        System.out.println("1.Mostrar Inventario");
                        System.out.println("2.Mostrar Carrito");
                        System.out.println("3.Añadir productos al Carrito ");
                        System.out.println("4.Eliminar productos del carrito");
                        System.out.println("5.Comprar");
                        System.out.println("6.Cambiar tu contraseña");
                        System.out.println("7.Cerrar Sesion");
                        opcion2 = scanner.nextInt();
                        scanner.nextLine();
                        switch (opcion2) {
                            case 1:
                                usuario.mostrarProductos();
                                System.out.println("Presiona enter para continuar");
                                scanner.nextLine();
                                break;
                            case 2:
                                usuario.mostrarCarrito();
                                System.out.println("Presiona enter para continuar");
                                scanner.nextLine();
                                break;
                            case 3:
                                usuario.añadirAlCarrito();
                                System.out.println("Presiona enter para continuar");
                                scanner.nextLine();
                                scanner.nextLine();
                                break;
                            case 4:
                                usuario.eliminarProductoDelCarrito();
                                System.out.println("Presiona enter para continuar");
                                scanner.nextLine();
                                break;
                            case 5:
                                usuario.Comprar();
                                System.out.println("Presiona enter para continuar");
                                scanner.nextLine();
                                break;
                            case 6:
                                usuario.cambiarContraseña();
                                System.out.println("Presiona enter para continuar");
                                scanner.nextLine();
                                break;
                        }
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("El formato no es el esperado");
            }
        }
    }

     double getTotalAPagar() {
        return totalAPagar;
    }

    /**
     * Semaforo para controlar la concurrencia
     * Una sesion a la vez
     */
   protected static Semaphore  semaphore=new Semaphore(1);

    /**
     * Metodo para validar credenciales a los usuarios para que inicien sesion
     * @return Sesion de usuario validada
     */
    public static Comprador iniciarSesion(){
        limpiarConsola();
            Scanner scanner=new Scanner(System.in);
        System.out.print("Comprador: ");
        String usuario=scanner.nextLine();
        Usuario sesion1=buscarUsuarioPorNombre(usuario, "Usuarios.txt");
        try{
            if (sesion1==null){
                throw  new NullPointerException("El usuario digitado no existe");
            }else {
                System.out.println("Comprador encontrado");
                System.out.println("Contraseña: ");
                String contrasena=scanner.nextLine();
                if (!Objects.equals(contrasena, sesion1.getContrasena())){
                    throw new ContraseñaEquivocadaException("Contraseña equivocada");

                }else {
                    System.out.println("Bienvenido "+usuario);
                    System.out.print("Presiona enter para continuar ");
                    scanner.nextLine();
                    return (Comprador) sesion1;
                }

            }
        }catch (NullPointerException | ContraseñaEquivocadaException e){
            System.out.println(e.getMessage());
            System.out.print("Presiona enter para continuar ");
            scanner.nextLine();
            return null;
        }


    }

    /**
     * Metodo que se usara para hacer devoluciones de libros a los usuarios
     * Buscara el libro que el usuario quiera hacer devolucion por medio de su nombre
     * Se eliminara el libro de su archivo de texto y se añadira al de los libros
     */

    /**
     * Metodo que se usara para hacer busqueda de libros por los usuarios
     * Buscara el libro que el usuario quiera por medio que el usuario decida
     */



    /**
     * Metodo que servira para que en caso de que los usuarios deseen, cambien su contraseña
     * Se verifica si la nueva contraseña no es la misma que la antes tenia
     */


   public  void cambiarContraseña() {
        try {
            limpiarConsola();
            Scanner scanner=new Scanner(System.in);
            System.out.print("Dame el nombre de usuario: ");
            String usuario= scanner.nextLine();
            Usuario cambiarSuContraseña=buscarUsuarioPorNombre(usuario,"Usuarios.txt");
           if (cambiarSuContraseña==null){
               throw new NullPointerException();
           }else{
                   System.out.println("Digita tu nueva contraseña: ");

                   String nuevaContraseña=scanner.nextLine();
                   cambiarSuContraseña.setContrasena(nuevaContraseña);
                   eliminarUsuario(usuario,"Usuarios.txt");
                   cargarUsuarios("Usuarios.txt");
               String tipoCuenta=cambiarSuContraseña.getClass().getSimpleName();
               System.out.println("Tipo de cuenta: "+tipoCuenta);
               switch (tipoCuenta){
                   case "Estudiante"->{Estudiante estudiante=new  Estudiante(cambiarSuContraseña.getName(), cambiarSuContraseña.getDireccion(), (int) cambiarSuContraseña.getTelefono(), cambiarSuContraseña.getEdad(), nuevaContraseña);}
                   case "ClienteVip"->{
                       ClienteVip clienteVip =new ClienteVip(cambiarSuContraseña.getName(), cambiarSuContraseña.getDireccion(), (int) cambiarSuContraseña.getTelefono(), cambiarSuContraseña.getEdad(), nuevaContraseña);}
                   case "Socio"->{
                       Socio socio =new Socio(cambiarSuContraseña.getName(), cambiarSuContraseña.getDireccion(), (int) cambiarSuContraseña.getTelefono(), cambiarSuContraseña.getEdad(), nuevaContraseña);}
               }
                   guardarUsuarios("Usuarios.txt");}
        }catch (NullPointerException e){
            System.out.println("El nombre de usuario  no existe");

        }
    }

    /**
     * Asignara una contraseña al usuario
     * @param contrasena Contraseña nueva
     */

    /**
     * Limpia la consola del sistema operativo actual.
     * La implementación varía según el sistema operativo.
     */
    public static void limpiarConsola() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Error al limpiar la consola: " + e.getMessage());
        }
    }



    public void eliminarProductoDelCarrito() {
        try {
            Scanner scanner = new Scanner(System.in);
            if (this.Carrito.isEmpty()){
                throw new SinProductosException("Aun no tienes productos en tu carrito");
            }else{
            mostrarCarrito();
            System.out.println("Digita el número identificador del producto que quieres eliminar de tu carrito");
            int producto = scanner.nextInt();
            boolean encontrado = false;

            Iterator<Map.Entry<DispositivoElectronico, Integer>> iterator = this.Carrito.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<DispositivoElectronico, Integer> entry = iterator.next();
                DispositivoElectronico dispositivoElectronico = entry.getKey();

                if (dispositivoElectronico.getIdentificador() == producto) {
                    encontrado = true;
                    int cantidad = entry.getValue();

                    System.out.println("Cuantos elementos vas a eliminar? ");
                    int elementos = scanner.nextInt();

                    if (elementos > cantidad) {
                        throw new SinProductosException("No tienes esa cantidad de elementos en tu carrito");
                    } else if (elementos<=0){
                        throw new NumNuevosElementosNoValidoException("Numero no valido a eliminar");
                    }else if (elementos == cantidad) {
                        iterator.remove();
                    } else {
                        entry.setValue(cantidad - elementos);
                    }
                        this.totalAPagar-=dispositivoElectronico.getPrecio()*elementos;
                    // Actualiza el inventario
                    Map<DispositivoElectronico, Integer> inventario = DispositivoElectronico.getInventario();
                    inventario.merge(dispositivoElectronico, elementos, Integer::sum);
                    System.out.println("Producto eliminado del carrito exitosamente");
                    break;
                }
            }

            if (!encontrado) {
                throw new ProductoNoEncontradoException("El producto con este número de identificador no está en tu carrito");
            }
            }
        } catch (ProductoNoEncontradoException | NumNuevosElementosNoValidoException|SinProductosException e) {
            System.out.println(e.getMessage());
        }
    }

    protected DescuentoStrategy getDescuentoStrategy() {
        return descuentoStrategy;
    }

    protected void setDescuentoStrategy(DescuentoStrategy descuentoStrategy) {
        this.descuentoStrategy = descuentoStrategy;
    }

    /**
     *
     * @return Informacion del comprador
     */

    @Override
    public String toString() {
        return "Comprador{" +
                "totalAPagar=" + totalAPagar +
                ", Carrito=" + Carrito +
                "} " + super.toString();
    }
}
