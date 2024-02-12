package usuarios;

import Productos.Descuentos;



public class Estudiante extends Comprador {


    public static Estudiante crearUsuario(String name, String direccion, long telefono, int edad, String contrasena) {
        if (ExisteUsuario(name, "Usuarios.txt")==false) {
            return new Estudiante(name,direccion, (int) telefono,edad,contrasena);
        } else {
            System.out.println("El nombre de usuario \""+name+"\" ya existe");
            System.out.println("Digita otro porfavor");
            return null;
        }
    }


    public Estudiante(String name, String direccion, int telefono, int edad, String contrasena) {
        super(name, direccion, telefono, edad, contrasena);
        setDescuentoStrategy(new Descuentos.DescuentoEstudiante());

    }






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
