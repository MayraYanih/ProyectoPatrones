package decorator;

import interfaces.IConsulta;

/** Segundo decorador: demuestra que se pueden sumar servicios sin tocar el código existente (OCP). */
public class RayosX extends ServicioExtra {
    public static final double COSTO = 70.0;

    public RayosX(IConsulta consulta) { super(consulta); }

    @Override
    public String getDescripcion() { return super.getDescripcion() + " + Rayos X"; }

    @Override
    public double getCosto() { return super.getCosto() + COSTO; }
}
