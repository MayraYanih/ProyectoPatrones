package modelo;

/** Datos del paciente. Solo conoce su identidad, su servicio y (opcionalmente) su última atención. */
public class Paciente {
    private static int contadorId = 1;

    private final String id;
    private final String nombre;
    private final String dni;
    private final String telefono;
    private final Servicio servicio;
    private Atencion atencion; // null mientras no se haya calculado la consulta

    public Paciente(String nombre, String dni, String telefono, Servicio servicio) {
        this.id = String.format("P-%03d", contadorId++);
        this.nombre = nombre.trim().toUpperCase();
        this.dni = dni;
        this.telefono = telefono;
        this.servicio = servicio;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public Servicio getServicio() { return servicio; }
    public Atencion getAtencion() { return atencion; }
    public boolean isAtendido() { return atencion != null; }

    public void registrarAtencion(Atencion atencion) { this.atencion = atencion; }

    @Override
    public String toString() {
        return "ID: " + id + " | Paciente: " + nombre + " | DNI: " + dni
                + " | Tel: " + telefono + " | Servicio: " + servicio.getNombre();
    }
}
