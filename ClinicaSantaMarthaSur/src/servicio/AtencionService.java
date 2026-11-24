package servicio;

import decorator.ExamenLab;
import decorator.ConsultaBase;
import decorator.RayosX;
import factory.ComprobanteCreator;
import factory.IComprobantes;
import interfaces.IConsulta;
import interfaces.IRepo;
import modelo.Atencion;
import modelo.Paciente;
import modelo.Servicio;
import strategy.IDescuento;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Lógica de negocio de la clínica. No imprime ni lee de consola.
 * Todas sus dependencias llegan por constructor (DIP):
 *  - IRepo<Paciente>            -> persistencia
 *  - Map<Integer, IDescuento>   -> estrategias de convenio disponibles (Strategy)
 *  - Map<Integer, ComprobanteCreator> -> fábricas de comprobantes (Factory Method)
 */
public class AtencionService {
    public static final String SIN_COMPROBANTE = "Sin comprobante";

    private final IRepo<Paciente> repo;
    private final Map<Integer, IDescuento> convenios;
    private final Map<Integer, ComprobanteCreator> comprobantes;

    public AtencionService(IRepo<Paciente> repo,
                           Map<Integer, IDescuento> convenios,
                           Map<Integer, ComprobanteCreator> comprobantes) {
        this.repo = repo;
        this.convenios = convenios;
        this.comprobantes = comprobantes;
    }

    // ---------- Pacientes ----------
    public boolean existeDni(String dni) { return repo.buscar(dni) != null; }

    public Paciente registrarPaciente(String nombre, String dni, String telefono, Servicio servicio) {
        if (!Validador.nombreValido(nombre)) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (!Validador.dniValido(dni)) throw new IllegalArgumentException("El DNI debe tener 8 dígitos.");
        if (!Validador.telefonoValido(telefono)) throw new IllegalArgumentException("Teléfono inválido (9 dígitos, inicia con 9).");
        if (existeDni(dni)) throw new IllegalArgumentException("Ya existe un paciente con ese DNI.");
        Paciente p = new Paciente(nombre, dni, telefono, servicio);
        repo.guardar(p);
        return p;
    }

    public Paciente buscarPorDni(String dni) { return repo.buscar(dni); }

    public java.util.List<Paciente> listarPacientes() { return repo.listar(); }

    // ---------- Cálculo (Decorator + Strategy) ----------
    public IConsulta armarConsulta(Paciente paciente, boolean conLaboratorio, boolean conRayosX) {
        IConsulta consulta = new ConsultaBase(paciente.getServicio());
        if (conLaboratorio) consulta = new ExamenLab(consulta);
        if (conRayosX) consulta = new RayosX(consulta);
        return consulta;
    }

    public double calcularTotal(double subtotal, IDescuento convenio) {
        return redondear(subtotal - convenio.calcular(subtotal));
    }

    // ---------- Cobro y comprobante (Factory Method) ----------
    /** Registra la atención sin emitir comprobante. */
    public void registrarSinComprobante(Paciente paciente, String detalle, double total) {
        paciente.registrarAtencion(new Atencion(detalle, total, SIN_COMPROBANTE));
    }

    /** Emite el comprobante elegido, registra la atención y devuelve el texto del comprobante. */
    public String emitirComprobante(Paciente paciente, String detalle, double total, int opcionComprobante) {
        ComprobanteCreator creator = comprobantes.get(opcionComprobante);
        if (creator == null) throw new IllegalArgumentException("Tipo de comprobante no válido.");
        IComprobantes comprobante = creator.crearComprobante();
        paciente.registrarAtencion(new Atencion(detalle, total, comprobante.getTipo()));
        return comprobante.generar(paciente.getNombre(), detalle, total);
    }

    // ---------- Reporte ----------
    public ReporteCaja generarReporte() {
        int atendidos = 0;
        double total = 0.0;
        Map<String, Integer> porTipo = new LinkedHashMap<>();
        for (Paciente p : repo.listar()) {
            if (p.isAtendido()) {
                atendidos++;
                total += p.getAtencion().getMontoPagado();
                porTipo.merge(p.getAtencion().getTipoComprobante(), 1, Integer::sum);
            }
        }
        return new ReporteCaja(repo.listar().size(), atendidos, porTipo, redondear(total));
    }

    // ---------- Opciones disponibles (para que la vista arme sus menús) ----------
    public Map<Integer, IDescuento> getConvenios() { return Collections.unmodifiableMap(convenios); }
    public Map<Integer, ComprobanteCreator> getComprobantes() { return Collections.unmodifiableMap(comprobantes); }

    private static double redondear(double v) { return Math.round(v * 100.0) / 100.0; }
}
