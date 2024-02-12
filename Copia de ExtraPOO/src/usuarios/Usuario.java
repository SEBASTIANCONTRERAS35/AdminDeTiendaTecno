package usuarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public abstract class Usuario {
    private  String name;
    /**
     * Id del usuario
     */
    private  int id;
    /**
     * Direccion del usuario
     */
    private String direccion;
    /**
     * Telefono del usuario
     */
    private long telefono;
    /**
     * Edad del usuario
     */
    private int edad;
    /**
     * Contraseña del usuario
     */
    private String contrasena;
    /**
     * atributo el cual servira para asignar a cada usuario un ID automaticamente
     */
    protected static int contadorID = 0;

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
    public static Usuario buscarUsuarioPorNombre(String nombre, String archivo) {
        nombre = nombre.toUpperCase().replaceAll(" ", ""); // Convertir el nombre a mayúsculas para la comparación
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
            System.out.println("Error al buscar el usuario por nombre: " + e.getMessage());
        }

        return null;
    }

    protected static void limpiarLista(Map lista) {
        lista.clear();
    }
    protected String getName() {
        return name;
    }
    /**
     * Metodo que devuelve el id del usuario
     * @return id del usuario
     */

    protected int getId() {
        return id;
    }
    /**
     * Metodo que devuelve la direccion del usuario
     * @return direccion del usuario
     */
    protected String getDireccion() {
        return direccion;
    }
    /**
     * Metodo que devuelve el telefono del usuario
     * @return telefono del usuario
     */
    protected long getTelefono() {
        return telefono;
    }
    /**
     * Metodo que devuelve la edad del usuario
     * @return edad del usuario
     */

    protected int getEdad() {
        return edad;
    }
    /**
     * Metodo que devuelve la contraseña del usuario
     * @return constraseña del usuario
     */

    protected String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
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
