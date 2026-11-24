package factory;

/** Producto del Factory Method. Devuelve el texto del comprobante; imprimirlo es tarea de la vista (SRP). */
public interface IComprobantes {
    String getTipo();
    String generar(String cliente, String detalle, double total);
}
