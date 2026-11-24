package factory;

public class FacturaCreator extends ComprobanteCreator {
    @Override
    public IComprobantes crearComprobante() { return new Factura(); }
}
