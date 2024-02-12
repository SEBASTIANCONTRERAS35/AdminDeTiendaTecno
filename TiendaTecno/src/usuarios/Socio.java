package usuarios;

import Productos.Descuentos;
import Excepciones.SinCashBackException;
import Excepciones.SinProductosException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Socio extends Comprador {

    private double cashback=0.0;

    public static Socio crearUsuario(String name, String direccion, long telefono, int edad, String contrasena) {
        if (ExisteUsuario(name, "Usuarios.txt")==false) {
            return new Socio(name,direccion, (int) telefono,edad,contrasena);
        } else {
            System.out.println("El nombre de usuario \""+name+"\" ya existe");
            System.out.println("Digita otro porfavor");
            return null;
        }
    }


    public Socio(String name, String direccion, int telefono, int edad, String contrasena) {
        super(name, direccion, telefono, edad, contrasena);
        setDescuentoStrategy(new Descuentos.DescuentoSocio());
    }



    @Override
    public String toString() {
        return "{"+
                 "Socio"+
                ", nombre=" + this.getName() +
                ", direccion=" + this.getDireccion()  +
                ", ID=" + this.getId() +
                ", telefono=" + this.getTelefono() +
                ", edad=" + this.getEdad() +
                ", contrasena=" + this.getContrasena() +
                '}';
    }


    @Override
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
                    double total=totalAPagar;
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = currentDate.format(formatter);
                    this.totalAPagar = getDescuentoStrategy().aplicarDescuento(this.totalAPagar,this.Carrito);
                    if (this.cashback>0){
                        System.out.println("Quieres pagar con tu cashback? Si/No");
                        String cashBackUsar= scanner.nextLine();
                        if (cashBackUsar.toUpperCase().equals("SI")){
                            System.out.println("Actualmente tienes de cashBack: "+this.cashback);
                            System.out.println("Cuanto quieres usar de tu cashBack?: ");
                            double cash= scanner.nextDouble();
                            if (this.cashback<cash){
                                throw new SinCashBackException("No tienes tanto cashBack");
                            }
                            else if (cash<=0){
                                throw new NullPointerException("Digita un numero valido");
                            }else if(cash<totalAPagar){
                                totalAPagar-=cash;
                                System.out.println("Tu cashBack no cubre la deuda total, el totalAPagar es: "+totalAPagar);
                            }else{
                                System.out.println("Tu cashBack cubrio la deuda");
                            }
                        }
                    }
                    this.cashback+=total*0.1;
                    ticket(this.getName() + " - " + formattedDate + ".txt",totalAPagar);
                    limpiarLista(this.Carrito);
                    this.totalAPagar = 0;
                }
            }
        }catch (SinProductosException|NullPointerException|SinCashBackException e){
            System.out.println(e.getMessage());
        }
    }
}
