package strategy;

public class DescEPS implements IDescuento {
    private static final double PORCENTAJE = 0.20;

    @Override
    public double calcular(double monto) { return monto * PORCENTAJE; }

    @Override
    public String getNombre() { return "Seguro EPS (20% desc)"; }
}
