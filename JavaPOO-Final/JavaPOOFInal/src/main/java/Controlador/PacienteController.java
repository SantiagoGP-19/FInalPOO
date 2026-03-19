package Controlador;

import Modelo.Paciente;
import Modelo.Datos.PacienteDao;
import java.util.List;

public class PacienteController {
    private final PacienteDao dao = new PacienteDao();

    public List<Paciente> listar() {
        return dao.listar();
    }

    public void guardar(Paciente paciente) {
        if (paciente.getId() == 0) {
            dao.guardar(paciente);
        } else {
            dao.actualizar(paciente);
        }
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
}
