package Controlador;

import Modelo.Medico;
import Modelo.Datos.MedicoDao;
import java.util.List;

public class MedicoController {
    private final MedicoDao dao = new MedicoDao();

    public List<Medico> listar() {
        return dao.listar();
    }

    public void guardar(Medico medico) {
        if (medico.getId() == 0) {
            dao.guardar(medico);
        } else {
            dao.actualizar(medico);
        }
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
}
