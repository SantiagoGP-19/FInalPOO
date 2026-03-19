package Controlador;

import Modelo.*;
import java.time.LocalDate;
import java.util.List;

public class ReporteController {
    private final GestionTurnosMedico gestion = new GestionTurnosMedico();
    private final Reporte reporte = new Reporte();

    public double obtenerOcupacionDiaria(Medico medico, LocalDate fecha) {
        Agenda agenda = gestion.getAgendaPorMedicoYFecha(medico, fecha);
        return reporte.calcularOcupacion(agenda);
    }

    public double obtenerTasaAusentismoGeneral(LocalDate inicio, LocalDate fin) {
        List<Turno> turnos = gestion.getTurnosEntreFechas(inicio, fin);
        return reporte.calcularTasaAusentismo(turnos);
    }
}