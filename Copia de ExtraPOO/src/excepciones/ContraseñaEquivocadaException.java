package excepciones;

/**
 * El paquete de excepciones del sistema bibliotecario proporciona las clases de excepción
 * personalizadas utilizadas para manejar diferentes situaciones excepcionales en el sistema.
 * Estas excepciones se utilizan para informar y manejar errores y condiciones inesperadas
 * que pueden ocurrir durante la ejecución del sistema bibliotecario.
 *
 * @author emiliocontreras
 * Excepción que se lanza cuando se detecta una contraseña equivocada.
 */
public class ContraseñaEquivocadaException extends Exception {
    /**
     * Crea una nueva instancia de ContraseñaEquivocadaException con un mensaje de error especificado.
     * @param message el mensaje de error que describe la excepción.
     */
    public ContraseñaEquivocadaException(String message) {
        super(message);
    }
}
