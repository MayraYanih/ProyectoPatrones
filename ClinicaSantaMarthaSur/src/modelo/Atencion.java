package modelo;

/** Resultado de calcular/cobrar una consulta. Separa el estado de facturación de los datos del paciente (SRP). */
public class Atencion {
    private final String detalle;
    private final double montoPagado;
    private final String tipoComprobante;

    public Atencion(String detalle, double montoPagado, String tipoComprobante) {
        this.detalle = detalle;
        this.montoPagado = montoPagado;
        this.tipoComprobante = tipoComprobante;
    }

    public String getDetalle() { return detalle; }
    public double getMontoPagado() { return montoPagado; }
    public String getTipoComprobante() { return tipoComprobante; }
}
