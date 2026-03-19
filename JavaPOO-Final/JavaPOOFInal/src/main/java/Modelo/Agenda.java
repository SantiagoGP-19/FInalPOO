package Modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private LocalDate fecha = LocalDate.now();
    private List<Turno> turnos;
    private Medico medico;

    public Agenda(LocalDate fecha, Medico medico) {
        this.turnos = new ArrayList<>();
        this.fecha = fecha;
        this.medico = medico;
    }

    public List<Turno> getTurnos() {
        return turnos; }

    public LocalDate getFecha() {
        return fecha; }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha; }

    public Medico getMedico() {
        return medico; }

    public void setMedico(Medico medico){
        this.medico = medico; }

    public boolean estaOcupado(LocalDateTime fechaHoraBuscada, int excludeId) {
        LocalDate fechaB = fechaHoraBuscada.toLocalDate();
        LocalTime timeB = fechaHoraBuscada.toLocalTime();
        String horaB = String.format("%02d:%02d", timeB.getHour(), timeB.getMinute());

        for (Turno t : turnos) {
            if (t.getId() != excludeId &&  // Ignorar el turno actual
                    t.getEstado() != Estado.CANCELADO &&
                    t.getFecha().equals(fechaB) &&
                    t.getHora().equals(horaB)) {
                return true; // Ocupado por otro turno
            }
        }
        return false;
    }
}