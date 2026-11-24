package repositorio;

import interfaces.IRepo;
import modelo.Paciente;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Repositorio en memoria. La clave de búsqueda es el DNI. */
public class PacienteRepo implements IRepo<Paciente> {
    private final List<Paciente> lista = new ArrayList<>();

    @Override
    public void guardar(Paciente p) { lista.add(p); }

    @Override
    public Paciente buscar(String dni) {
        for (Paciente p : lista) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Paciente> listar() { return Collections.unmodifiableList(lista); }
}
