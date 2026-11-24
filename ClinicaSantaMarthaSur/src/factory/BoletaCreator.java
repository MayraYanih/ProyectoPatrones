package factory;

public class BoletaCreator extends ComprobanteCreator {
    @Override
    public IComprobantes crearComprobante() { return new Boleta(); }
}
