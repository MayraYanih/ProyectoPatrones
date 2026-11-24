package servicio;

/** Reglas de validación de datos de entrada (separadas del menú y del flujo de negocio). */
public final class Validador {
    private Validador() { }

    public static boolean nombreValido(String nombre) { return nombre != null && !nombre.trim().isEmpty(); }
    public static boolean dniValido(String dni) { return dni != null && dni.matches("\\d{8}"); }
    public static boolean telefonoValido(String tel) { return tel != null && tel.matches("9\\d{8}"); }
}
