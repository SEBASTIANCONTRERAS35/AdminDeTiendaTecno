package usuarios;


import Interfaces.AccionesProductos;
import Interfaces.DescuentoStrategy;
import Productos.DispositivoElectronico;
import Excepciones.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Semaphore;




public abstract class Comprador extends Usuario implements AccionesProductos  {

    protected double totalAPagar=0;
    protected  Map<DispositivoElectronico,Integer> Carrito = new HashMap<>();

    private static final ArrayList<Comprador> COMPRADORES = new ArrayList<>();
    private DescuentoStrategy descuentoStrategy;

    protected Comprador(String name, String direccion, long telefono, int edad, String contrasena) {
        super(name, direccion, telefono, edad, contrasena);
       COMPRADORES.add(this);
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
                    }
                    else if (cantidadDeseada<=0){
                        throw  new NumNuevosElementosNoValidoException("Digita un numero valido de elementos");
                    }
                    else {
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
        } catch (SinProductosException | NumNuevosElementosNoValidoException e) {
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
    protected void ticket(String archivo,double totalAPagar) {
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
                    cargarUsuarios("Usuarios.txt",COMPRADORES);
                    nuevoComprador = ClienteVip.crearUsuario(nombre, direccion, telefono, edad, contrasena);
                    guardarUsuarios("Usuarios.txt",COMPRADORES);
                }
                case 2 -> {
                    cargarUsuarios("Usuarios.txt",COMPRADORES);
                    nuevoComprador = Estudiante.crearUsuario(nombre, direccion, telefono, edad, contrasena);
                    guardarUsuarios("Usuarios.txt",COMPRADORES);
                }
                case 3 -> {
                    cargarUsuarios("Usuarios.txt",COMPRADORES);
                    nuevoComprador = Socio.crearUsuario(nombre, direccion, telefono, edad, contrasena);
                    guardarUsuarios("Usuarios.txt",COMPRADORES);
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
    }

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
   protected static Semaphore  semaphore=new Semaphore(1);

     double getTotalAPagar() {
        return totalAPagar;
    }




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




   private  void cambiarContraseña() {
        try {
            limpiarConsola();
            Scanner scanner=new Scanner(System.in);
            System.out.print("Dame el nombre de usuario: ");
            String usuario= scanner.nextLine();
            if (!usuario.toUpperCase().equals(this.getName())){
                throw new NullPointerException("No puedes cambiar una contraseña que no es la tuya");
            }else{

            Usuario cambiarSuContraseña=buscarUsuarioPorNombre(usuario,"Usuarios.txt");
           if (cambiarSuContraseña==null){
               throw new NullPointerException("El nombre de usuario de existe");
           }else{
                   System.out.println("Digita tu nueva contraseña: ");
                   String nuevaContraseña=scanner.nextLine();
                   if (nuevaContraseña==cambiarSuContraseña.getContrasena()){
                       throw new MismaContrasenaException("La contraseña que digitaste es la misma");
                   }else{

                   cambiarSuContraseña.setContrasena(nuevaContraseña);
                   eliminarUsuario(usuario,"Usuarios.txt");
                   cargarUsuarios("Usuarios.txt",COMPRADORES);
               String tipoCuenta=cambiarSuContraseña.getClass().getSimpleName();
               System.out.println("Tipo de cuenta: "+tipoCuenta);
               switch (tipoCuenta){
                   case "Estudiante"->{Estudiante estudiante=new  Estudiante(cambiarSuContraseña.getName(), cambiarSuContraseña.getDireccion(), (int) cambiarSuContraseña.getTelefono(), cambiarSuContraseña.getEdad(), nuevaContraseña);}
                   case "ClienteVip"->{
                       ClienteVip clienteVip =new ClienteVip(cambiarSuContraseña.getName(), cambiarSuContraseña.getDireccion(), (int) cambiarSuContraseña.getTelefono(), cambiarSuContraseña.getEdad(), nuevaContraseña);}
                   case "Socio"->{
                       Socio socio =new Socio(cambiarSuContraseña.getName(), cambiarSuContraseña.getDireccion(), (int) cambiarSuContraseña.getTelefono(), cambiarSuContraseña.getEdad(), nuevaContraseña);}
               }
                   guardarUsuarios("Usuarios.txt",COMPRADORES);}
                   }
            }
        }catch (NullPointerException|MismaContrasenaException e){
            System.out.println(e.getMessage());

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



    @Override
    public String toString() {
        return "Comprador{" +
                "totalAPagar=" + totalAPagar +
                ", Carrito=" + Carrito +
                "} " + super.toString();
    }
}
