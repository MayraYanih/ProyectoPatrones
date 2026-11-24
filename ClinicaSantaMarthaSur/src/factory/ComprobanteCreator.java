package factory;

/**
 * Creator del patrón Factory Method.
 * Cada subclase decide QUÉ comprobante instanciar; quien lo usa solo conoce IComprobantes.
 */
public abstract class ComprobanteCreator {
    /** Factory Method. */
    public abstract IComprobantes crearComprobante();

    public String etiqueta() { return crearComprobante().getTipo(); }
}
