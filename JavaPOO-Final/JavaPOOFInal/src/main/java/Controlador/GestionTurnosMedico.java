// GestionTurnosMedico.java
package Controlador;

import Modelo.Agenda;
import Modelo.Datos.MedicoDao;
import Modelo.Datos.TurnoDao;
import Modelo.Medico;
import Modelo.Turno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestionTurnosMedico {

    private final MedicoDao medicoDao = new MedicoDao();
    private final TurnoDao turnoDao = new TurnoDao();

    public GestionTurnosMedico() {}

    public List<Turno> getTurnosEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Turno> todosLosTurnos = turnoDao.listar();
        List<Turno> turnosFiltrados = new ArrayList<>();

        // Simplificado: Usamos loop en lugar de stream
        for (Turno t : todosLosTurnos) {
            if ((t.getFecha().isAfter(fechaInicio) || t.getFecha().equals(fechaInicio)) &&
                    (t.getFecha().isBefore(fechaFin) || t.getFecha().equals(fechaFin))) {
                turnosFiltrados.add(t);
            }
        }
        return turnosFiltrados;
    }

    public Agenda getAgendaPorMedicoYFecha(Medico medico, LocalDate fecha) {
        Agenda agenda = new Agenda(fecha, medico);

        List<Turno> turnosDelDia = new ArrayList<>();
        List<Turno> todosTurnos = turnoDao.listar();

        // Simplificado: Loop en lugar de stream
        for (Turno t : todosTurnos) {
            if (t.getMedico().getId() == medico.getId() && t.getFecha().equals(fecha)) {
                turnosDelDia.add(t);
            }
        }

        agenda.getTurnos().addAll(turnosDelDia);
        return agenda;
    }

    public List<Medico> getListaMedicos() {
        return medicoDao.listar();
    }
}