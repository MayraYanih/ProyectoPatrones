package interfaces;

import java.util.List;

/** Abstracción de persistencia (DIP): los servicios dependen de esta interfaz, no de una lista concreta. */
public interface IRepo<T> {
    void guardar(T entidad);

    /** Busca por la clave natural de la entidad (para Paciente: el DNI). */
    T buscar(String clave);

    List<T> listar();
}
