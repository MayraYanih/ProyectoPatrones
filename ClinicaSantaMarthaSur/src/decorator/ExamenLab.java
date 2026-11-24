package decorator;

import interfaces.IConsulta;

/** Decorador concreto: suma un examen de laboratorio. */
public class ExamenLab extends ServicioExtra {
    public static final double COSTO = 50.0;

    public ExamenLab(IConsulta consulta) { super(consulta); }

    @Override
    public String getDescripcion() { return super.getDescripcion() + " + Examen de Laboratorio"; }

    @Override
    public double getCosto() { return super.getCosto() + COSTO; }
}
