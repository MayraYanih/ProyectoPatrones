package decorator;

import interfaces.IConsulta;

/** Decorador base: envuelve una IConsulta y delega en ella. */
public abstract class ServicioExtra implements IConsulta {
    protected final IConsulta consultaEnvuelta;

    protected ServicioExtra(IConsulta consulta) {
        this.consultaEnvuelta = consulta;
    }

    @Override
    public String getDescripcion() { return consultaEnvuelta.getDescripcion(); }

    @Override
    public double getCosto() { return consultaEnvuelta.getCosto(); }
}
