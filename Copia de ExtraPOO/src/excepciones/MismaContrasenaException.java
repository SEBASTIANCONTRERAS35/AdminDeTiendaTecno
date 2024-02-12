package excepciones;
/**
 * El paquete de excepciones del sistema bibliotecario proporciona las clases de excepción
 * personalizadas utilizadas para manejar diferentes situaciones excepcionales en el sistema.
 * Estas excepciones se utilizan para informar y manejar errores y condiciones inesperadas
 * que pueden ocurrir durante la ejecución del sistema bibliotecario.
 *
 * @author emiliocontreras
 * Excepción que se lanza cuando se intenta establecer una contraseña igual a la contraseña actual.
 */
public class MismaContrasenaException extends Exception {
    /**
     * Crea una nueva instancia de MismaContrasenaException con un mensaje de error especificado.
     * @param message el mensaje de error que describe la excepción.
     */
    public MismaContrasenaException(String message) {
        super(message);
    }
}