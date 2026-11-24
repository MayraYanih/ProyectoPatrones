package main;

import factory.BoletaCreator;
import factory.ComprobanteCreator;
import factory.FacturaCreator;
import interfaces.IRepo;
import modelo.Paciente;
import modelo.Servicio;
import repositorio.PacienteRepo;
import servicio.AtencionService;
import strategy.DescEPS;
import strategy.DescParticular;
import strategy.IDescuento;
import vista.ConsolaClinica;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Raíz de composición: el único lugar donde se hace "new" de las clases concretas
 * y se inyectan las dependencias (DIP). Agregar un convenio o un comprobante = una línea aquí (OCP).
 */
public class Main {
    public static void main(String[] args) {
        IRepo<Paciente> repo = new PacienteRepo();

        Map<Integer, IDescuento> convenios = new LinkedHashMap<>();
        convenios.put(1, new DescParticular());
        convenios.put(2, new DescEPS());

        Map<Integer, ComprobanteCreator> comprobantes = new LinkedHashMap<>();
        comprobantes.put(1, new BoletaCreator());
        comprobantes.put(2, new FacturaCreator());

        AtencionService service = new AtencionService(repo, convenios, comprobantes);

        // Datos iniciales de prueba
        service.registrarPaciente("MAYRA HUAMANI", "72345678", "987654321", Servicio.MEDICINA_GENERAL);
        service.registrarPaciente("CARLOS MENDOZA", "45612378", "912345678", Servicio.PEDIATRIA);

        try (Scanner sc = new Scanner(System.in)) {
            new ConsolaClinica(sc, service).iniciar();
        }
    }
}
