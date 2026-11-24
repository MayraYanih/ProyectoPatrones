package factory;

import java.util.Locale;

public class Factura implements IComprobantes {
    private static final double IGV = 0.18;

    @Override
    public String getTipo() { return "Factura"; }

    @Override
    public String generar(String cliente, String detalle, double total) {
        double subtotal = Math.round(total / (1 + IGV) * 100.0) / 100.0;
        double igv = Math.round((total - subtotal) * 100.0) / 100.0;
        return "--- CLÍNICA SANTA MARTHA SUR (FACTURA) ---\n"
             + "Cliente: " + cliente + "\n"
             + "Concepto: " + detalle + "\n"
             + String.format(Locale.US, "Subtotal: S/ %.2f%n", subtotal)
             + String.format(Locale.US, "IGV (18%%): S/ %.2f%n", igv)
             + String.format(Locale.US, "Total: S/ %.2f%n", total)
             + "------------------------------------------";
    }
}
