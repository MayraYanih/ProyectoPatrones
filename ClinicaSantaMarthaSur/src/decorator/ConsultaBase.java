package decorator;

import interfaces.IConsulta;
import modelo.Servicio;

/** Componente concreto: la consulta sin extras. */
public class ConsultaBase implements IConsulta {
    private final Servicio servicio;

    public ConsultaBase(Servicio servicio) {
        this.servicio = servicio;
    }

    @Override
    public String getDescripcion() { return "Consulta en " + servicio.getNombre(); }

    @Override
    public double getCosto() { return servicio.getCostoBase(); }
}
