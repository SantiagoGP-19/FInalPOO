package Controlador;

import Modelo.*;
import Modelo.Datos.TurnoDao;

import java.time.LocalDate;
import java.util.List;

public class TurnoController {
    private final TurnoDao turnoDao = new TurnoDao();
    private final GestionTurnosMedico gestion = new GestionTurnosMedico();

    public List<Turno> listarTodos() {
        return turnoDao.listar();
    }

    
    public Agenda obtenerAgenda(Medico medico, LocalDate fecha) {
        return gestion.getAgendaPorMedicoYFecha(medico, fecha);
    }

    public void guardar(Turno turno) {
        if (turno.getId() == 0) {
            turnoDao.guardar(turno);
        } else {
            turnoDao.actualizar(turno);
        }
    }

    public void eliminar(int id) {
        turnoDao.eliminar(id);
    }

    public List<Medico> listarMedicos() {
        return gestion.getListaMedicos();
    }

    public List<Paciente> listarPacientes() {
        return new PacienteController().listar();
    }
}