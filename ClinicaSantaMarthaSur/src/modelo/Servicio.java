package modelo;

/**
 * Especialidades con su tarifa base.
 * Agregar una especialidad = agregar una constante aquí (OCP); ya no hay if/else de precios.
 */
public enum Servicio {
    MEDICINA_GENERAL("Medicina General", 80.0),
    PEDIATRIA("Pediatría", 100.0),
    CARDIOLOGIA("Cardiología", 150.0);

    private final String nombre;
    private final double costoBase;

    Servicio(String nombre, double costoBase) {
        this.nombre = nombre;
        this.costoBase = costoBase;
    }

    public String getNombre() { return nombre; }
    public double getCostoBase() { return costoBase; }

    /** Convierte la opción del menú (1..n) en un Servicio. */
    public static Servicio desdeOpcion(int opcion) {
        Servicio[] todos = values();
        if (opcion < 1 || opcion > todos.length) {
            throw new IllegalArgumentException("Servicio no válido: " + opcion);
        }
        return todos[opcion - 1];
    }
}
