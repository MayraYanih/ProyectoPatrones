package vista;

import decorator.ExamenLab;
import decorator.RayosX;
import factory.ComprobanteCreator;
import interfaces.IConsulta;
import modelo.Paciente;
import modelo.Servicio;
import servicio.AtencionService;
import servicio.ReporteCaja;
import servicio.Validador;
import strategy.IDescuento;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/** Única clase que habla con la consola: menús, lectura y validación de entradas. */
public class ConsolaClinica {
    private final Scanner sc;
    private final AtencionService service;

    public ConsolaClinica(Scanner sc, AtencionService service) {
        this.sc = sc;
        this.service = service;
    }

    public void iniciar() {
        int op;
        do {
            System.out.println("\n=== CLÍNICA SANTA MARTHA SUR ===");
            System.out.println("1. Registrar Paciente");
            System.out.println("2. Lista de Pacientes");
            System.out.println("3. Calcular Consulta");
            System.out.println("4. Reporte de Cierre de Caja");
            System.out.println("5. Salir");
            op = leerEntero("Elija una opción: ", 1, 5);

            switch (op) {
                case 1: registrarPaciente(); break;
                case 2: listarPacientes(); break;
                case 3: calcularConsulta(); break;
                case 4: mostrarReporte(); break;
                default: System.out.println("Saliendo del sistema. ¡Hasta luego!");
            }
        } while (op != 5);
    }

    // ---------- Casos de uso ----------
    private void registrarPaciente() {
        String nombre;
        do {
            System.out.print("Ingrese Nombre completo: ");
            nombre = sc.nextLine();
        } while (!Validador.nombreValido(nombre) && mostrar("¡Error! El nombre es obligatorio."));

        String dni;
        while (true) {
            System.out.print("Ingrese DNI: ");
            dni = sc.nextLine();
            if (!Validador.dniValido(dni)) {
                System.out.println("¡Error! El DNI debe tener exactamente 8 dígitos numéricos.");
            } else if (service.existeDni(dni)) {
                System.out.println("¡Error! Ya existe un paciente registrado con este DNI.");
            } else {
                break;
            }
        }

        String telefono;
        do {
            System.out.print("Ingrese Teléfono: ");
            telefono = sc.nextLine();
        } while (!Validador.telefonoValido(telefono)
                && mostrar("¡Error! El teléfono debe tener 9 dígitos e iniciar con 9."));

        System.out.println("Seleccione el Servicio:");
        Servicio[] servicios = Servicio.values();
        for (int i = 0; i < servicios.length; i++) {
            System.out.println((i + 1) + ". " + servicios[i].getNombre() + " (" + soles(servicios[i].getCostoBase()) + ")");
        }
        Servicio servicio = Servicio.desdeOpcion(leerEntero("Opción de servicio: ", 1, servicios.length));

        Paciente p = service.registrarPaciente(nombre, dni, telefono, servicio);
        System.out.println("¡Paciente registrado con éxito! ID asignado: " + p.getId());
    }

    private void listarPacientes() {
        System.out.println("\n--- LISTA DE PACIENTES ---");
        for (Paciente p : service.listarPacientes()) {
            System.out.println(p);
        }
    }

    private void calcularConsulta() {
        System.out.println("\n--- CÁLCULO DE CONSULTA ---");
        System.out.print("Ingrese DNI del paciente: ");
        Paciente paciente = service.buscarPorDni(sc.nextLine().trim());
        if (paciente == null) {
            System.out.println("¡No se encontró ningún paciente registrado con el DNI ingresado!");
            return;
        }
        System.out.println("Paciente: " + paciente.getNombre() + " | Servicio: " + paciente.getServicio().getNombre());

        boolean lab = leerSiNo("¿Agregar Examen de Laboratorio (" + soles(ExamenLab.COSTO) + ")? (si / no): ");
        boolean rx = leerSiNo("¿Agregar Rayos X (" + soles(RayosX.COSTO) + ")? (si / no): ");

        IConsulta consulta = service.armarConsulta(paciente, lab, rx);   // Decorator
        System.out.println("Detalle: " + consulta.getDescripcion());
        System.out.println("Subtotal: " + soles(consulta.getCosto()));

        IDescuento convenio = elegirConvenio();                           // Strategy
        double total = service.calcularTotal(consulta.getCosto(), convenio);
        String detalle = consulta.getDescripcion() + " (" + convenio.getNombre() + ")";
        System.out.println("Convenio: " + convenio.getNombre());
        System.out.println("TOTAL A PAGAR: " + soles(total));

        if (leerSiNo("¿Desea emitir el comprobante ahora? (si / no): ")) {
            int tipo = elegirComprobante();                               // Factory Method
            System.out.println();
            System.out.println(service.emitirComprobante(paciente, detalle, total, tipo));
        } else {
            service.registrarSinComprobante(paciente, detalle, total);
        }
    }

    private void mostrarReporte() {
        System.out.println("\n=== REPORTE DE CIERRE DE CAJA ===");
        System.out.println("--- DETALLE DE MOVIMIENTOS ---");
        for (Paciente p : service.listarPacientes()) {
            String base = p.getId() + " | " + p.getNombre() + " | DNI: " + p.getDni() + " | ";
            if (p.isAtendido()) {
                System.out.println(base + p.getAtencion().getDetalle()
                        + " | Comprobante: " + p.getAtencion().getTipoComprobante()
                        + " | Ingreso: " + soles(p.getAtencion().getMontoPagado()));
            } else {
                System.out.println(base + "Servicio: " + p.getServicio().getNombre() + " | Estado: Pendiente de atención");
            }
        }
        ReporteCaja r = service.generarReporte();
        System.out.println("---------------------------------");
        System.out.println("Total de clientes registrados: " + r.getRegistrados());
        System.out.println("Clientes atendidos / cobrados: " + r.getAtendidos());
        System.out.println("Clientes pendientes: " + r.getPendientes());
        System.out.println("Comprobantes: " + (r.getComprobantesPorTipo().isEmpty() ? "ninguno" : r.getComprobantesPorTipo()));
        System.out.println("TOTAL DE INGRESOS EN CAJA: " + soles(r.getTotalIngresos()));
        System.out.println("=================================");
    }

    // ---------- Menús dinámicos (se arman con lo que el servicio tiene registrado) ----------
    private IDescuento elegirConvenio() {
        Map<Integer, IDescuento> opciones = service.getConvenios();
        System.out.println("Convenio:");
        opciones.forEach((k, v) -> System.out.println("  " + k + ". " + v.getNombre()));
        return opciones.get(leerOpcion("Seleccione opción de convenio: ", opciones.keySet()));
    }

    private int elegirComprobante() {
        Map<Integer, ComprobanteCreator> opciones = service.getComprobantes();
        System.out.println("Tipo de comprobante:");
        opciones.forEach((k, v) -> System.out.println("  " + k + ". " + v.etiqueta()));
        return leerOpcion("Elija tipo de comprobante: ", opciones.keySet());
    }

    // ---------- Utilidades de lectura ----------
    private int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(linea);
                if (n >= min && n <= max) return n;
            } catch (NumberFormatException ignorada) { }
            System.out.println("¡Ingrese un número entre " + min + " y " + max + "!");
        }
    }

    private int leerOpcion(String mensaje, java.util.Set<Integer> validas) {
        while (true) {
            System.out.print(mensaje);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (validas.contains(n)) return n;
            } catch (NumberFormatException ignorada) { }
            System.out.println("¡Opción no válida! Opciones: " + validas);
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String r = sc.nextLine().trim();
            if (r.equalsIgnoreCase("si") || r.equalsIgnoreCase("sí") || r.equalsIgnoreCase("s")) return true;
            if (r.equalsIgnoreCase("no") || r.equalsIgnoreCase("n")) return false;
            System.out.println("¡Responda si o no!");
        }
    }

    private boolean mostrar(String mensaje) { System.out.println(mensaje); return true; }

    private static String soles(double monto) { return String.format(Locale.US, "S/ %.2f", monto); }
}
