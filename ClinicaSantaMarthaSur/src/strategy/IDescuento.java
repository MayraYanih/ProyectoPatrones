package strategy;

/** Estrategia: cómo se calcula el descuento según el convenio. */
public interface IDescuento {
    double calcular(double monto);
    String getNombre();
}
