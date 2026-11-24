package servicio;

import java.util.Map;

/** Datos ya calculados del cierre de caja; la vista solo los muestra. */
public class ReporteCaja {
    private final int registrados;
    private final int atendidos;
    private final Map<String, Integer> comprobantesPorTipo;
    private final double totalIngresos;

    public ReporteCaja(int registrados, int atendidos, Map<String, Integer> comprobantesPorTipo, double totalIngresos) {
        this.registrados = registrados;
        this.atendidos = atendidos;
        this.comprobantesPorTipo = comprobantesPorTipo;
        this.totalIngresos = totalIngresos;
    }

    public int getRegistrados() { return registrados; }
    public int getAtendidos() { return atendidos; }
    public int getPendientes() { return registrados - atendidos; }
    public Map<String, Integer> getComprobantesPorTipo() { return comprobantesPorTipo; }
    public double getTotalIngresos() { return totalIngresos; }
}
