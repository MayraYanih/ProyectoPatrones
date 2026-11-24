package factory;

import java.util.Locale;

public class Boleta implements IComprobantes {
    @Override
    public String getTipo() { return "Boleta"; }

    @Override
    public String generar(String cliente, String detalle, double total) {
        return "--- CLÍNICA SANTA MARTHA SUR (BOLETA) ---\n"
             + "Paciente: " + cliente + "\n"
             + "Concepto: " + detalle + "\n"
             + String.format(Locale.US, "Total a Pagar: S/ %.2f%n", total)
             + "----------------------------------------";
    }
}
