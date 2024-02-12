package usuarios;

import Productos.Descuentos;

/**
 * El paquete "usuarios" contiene las clases relacionadas con la gestión de usuarios en el sistema bibliotecario.
 * Proporciona las clases necesarias para crear, autenticar, buscar y administrar usuarios del sistema.
 */

public class Estudiante extends Comprador {
    /**
     * Verifica si el nombre de Comprador ya existe
     * @param name
     * @param direccion
     * @param telefono
     * @param edad
     * @param contrasena
     * @return Si no existe otro igual, retornara una nueva instancia
     */

    public static Estudiante crearUsuario(String name, String direccion, long telefono, int edad, String contrasena) {
        if (ExisteUsuario(name, "Usuarios.txt")==false) {
            return new Estudiante(name,direccion, (int) telefono,edad,contrasena);
        } else {
            System.out.println("El nombre de usuario \""+name+"\" ya existe");
            System.out.println("Digita otro porfavor");
            return null;
        }
    }

    /**
     *
     * @param name
     * @param direccion
     * @param telefono
     * @param edad
     * @param contrasena
     */
    public Estudiante(String name, String direccion, int telefono, int edad, String contrasena) {
        super(name, direccion, telefono, edad, contrasena);
        setDescuentoStrategy(new Descuentos.DescuentoEstudiante());

    }




    /**
     *
     * @return informacion del usuario
     */


    @Override
    public String toString() {
        return "{"+
                "Estudiante"+
                ", nombre=" + this.getName() +
                ", direccion=" + this.getDireccion()  +
                ", ID=" + this.getId() +
                ", telefono=" + this.getTelefono() +
                ", edad=" + this.getEdad() +
                ", contrasena=" + this.getContrasena() +
                '}';
    }



}
