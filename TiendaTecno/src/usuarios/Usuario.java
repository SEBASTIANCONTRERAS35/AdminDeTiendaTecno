package usuarios;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public abstract class Usuario {
    private  String name;
    private  int id;
    private String direccion;
    private long telefono;
    private int edad;
    private String contrasena;
    private static int contadorID = 0;

    public Usuario(String name, String direccion, long telefono, int edad, String contrasena) {
        contadorID++;
        this.id = contadorID;
        this.name = name;
        this.direccion = direccion;
        this.telefono = telefono;
        this.edad =edad;
        this.contrasena = contrasena;
    }
    protected static String getValue(String part) {
        return part.substring(part.indexOf('=') + 1).trim();
    }

    protected static Boolean ExisteUsuario(String nombre, String archivo) {

        nombre = nombre.toUpperCase().replaceAll(" ", "");
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("{")) {
                    String[] parts = line.substring(1, line.length() - 1).split(", ");
                    String nombreUsuario = getValue(parts[1]);
                    String nombreUsuarioSinEspacios = nombreUsuario.replaceAll(" ", "");
                    if (nombreUsuarioSinEspacios.toUpperCase().equals(nombre)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar el usuario por nombre: " + e.getMessage());
        }

        return false;
    }
    protected static Usuario buscarUsuarioPorNombre(String nombre, String archivo) {
        nombre = nombre.toUpperCase().replaceAll(" ", "");
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("{")) {
                    String[] parts = line.substring(1, line.length() - 1).split(", ");
                    String nombreUsuario = getValue(parts[1]);
                    String nombreUsuarioSinEspacios = nombreUsuario.replaceAll(" ", "");
                    if (nombreUsuarioSinEspacios.toUpperCase().equals(nombre)) {
                        String direccion = getValue(parts[2]);
                        int ID = Integer.parseInt(getValue(parts[3]));
                        int telefono = Integer.parseInt(getValue(parts[4].replaceAll("[^0-9]", "")));
                        int edad = Integer.parseInt(getValue(String.valueOf(5))) ;// Convertir a LocalDate
                        String contrasena = getValue(parts[6]);
                        String tipoCuenta=parts[0];
                        switch (tipoCuenta){
                            case "Estudiante"->{
                                ;Estudiante estudiante=new  Estudiante(nombre, direccion, telefono, edad, contrasena);
                                return estudiante; }
                            case "ClienteVip"->{
                                ClienteVip clienteVip =new ClienteVip(nombre,direccion,telefono,edad,contrasena);
                                return clienteVip;}
                            case "Socio"->{
                                Socio socio =new Socio(nombre,direccion,telefono,edad,contrasena);
                                return socio;}
                            case "Admin"->{
                                Admin admin =new Admin(nombre, direccion, telefono, edad, contrasena);
                                return admin;
                            }
                        }
                        contadorID = Math.max(contadorID, ID);


                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar el usuario por nombre: " + e.getMessage()+". No se preocupe si es la primera vez que corre el programa ");
        }

        return null;
    }
    protected static void guardarUsuarios(String archivo, ArrayList<? extends Usuario>list) {
        try (PrintWriter writer = new PrintWriter(archivo)) {
            for (Usuario usuario : list) {
                writer.println(usuario.toString());
            }
            writer.flush();
            System.out.println("Usuarios guardados exitosamente en el archivo.");
        } catch (IOException e) {
            System.out.println("Error al guardar los Usuarios en el archivo: " + e.getMessage());
        }
    }
    protected static void cargarUsuarios(String archivo, ArrayList <?extends Usuario>list) {
        list.clear();
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
                        case "Admin"->{
                            Admin admin =new Admin(nombre,direccion,telefono,edad,contrasena);
                        }
                    }
                    setContadorID( Math.max(getContadorID(), ID));

                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los usuarios desde el archivo: " + e.getMessage()+ "No se preocupe si es la primera vez que corre el programa");
        }
    }
    protected static void eliminarUsuario(String nombre, String archivo) {
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
            System.out.println("Error al eliminar el Usuario: " + e.getMessage());
        }

        File archivoOriginal = new File(archivo);
        File archivoTemporal = new File(nombreArchivoTemporal);
        if (archivoTemporal.renameTo(archivoOriginal)) {
            if (encontrado) {
                System.out.println("Usuario eliminado correctamente.");
            } else {
                System.out.println("No se encontró el usuario.");
            }
        } else {
            System.out.println("Error al eliminar el usuario. No se pudo reemplazar el archivo original.");
        }
    }

    protected static void limpiarLista(Map lista) {
        lista.clear();
    }
    protected static void limpiarConsola() {
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
    protected String getName() {
        return name;
    }

    protected int getId() {
        return id;
    }

    protected String getDireccion() {
        return direccion;
    }

    protected long getTelefono() {
        return telefono;
    }


    protected int getEdad() {
        return edad;
    }


    protected String getContrasena() {
        return contrasena;
    }

    protected void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    protected static String formatoProducto(String product) {
        String[] details = product.split(", ");
        StringBuilder formattedDetails = new StringBuilder();
        formattedDetails.append("+-------------------------+\n");
        for (String detail : details) {
            formattedDetails.append("| ").append(detail).append("\n");
        }
        formattedDetails.append("+-------------------------+\n");

        return formattedDetails.toString();
    }

    protected static int getContadorID() {
        return contadorID;
    }

    public static void setContadorID(int contadorID) {
        Usuario.contadorID = contadorID;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                ", edad=" + edad +
                '}';
    }
}
