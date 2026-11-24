package strategy;

public class DescParticular implements IDescuento {
    @Override
    public double calcular(double monto) { return 0.0; }

    @Override
    public String getNombre() { return "Particular (0% desc)"; }
}
