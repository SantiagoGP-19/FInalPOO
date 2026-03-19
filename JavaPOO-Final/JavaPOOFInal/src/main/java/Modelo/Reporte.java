package Modelo;
import java.util.List;


public class Reporte {

    public double calcularOcupacion(Agenda agenda) {
        // Validamos que la agenda no sea nula y tenga turnos
        if (agenda == null || agenda.getTurnos() == null || agenda.getTurnos().isEmpty()) {
            return 0.0;
        }

        List<Turno> turnos = agenda.getTurnos();
        int turnosOcupados = 0;

        // Contamos los turnos que están efectivamente ocupados
        // (ni cancelados ni ausentes = el paciente vino o tiene reserva confirmada)
        for (Turno t : turnos) {
            if (t.getEstado() != Estado.CANCELADO && t.getEstado() != Estado.AUSENTE) {
                turnosOcupados++;
            }
        }

        int totalBloques = turnos.size();

        // Casteamos a double para que la división sea decimal
        return ((double) turnosOcupados / totalBloques) * 100.0;
    }

    public double calcularTasaAusentismo(List<Turno> turnos) {
        if (turnos == null || turnos.isEmpty()) {
            return 0.0;
        }

        int ausentes = 0;
        int totalConsiderados = 0;

        for (Turno t : turnos) {
            // Solo consideramos turnos que ya tuvieron fecha de atención
            if (t.getEstado() == Estado.ATENDIDO ||
                    t.getEstado() == Estado.AUSENTE ||
                    t.getEstado() == Estado.CANCELADO) {

                totalConsiderados++;

                if (t.getEstado() == Estado.AUSENTE) {
                    ausentes++;
                }
            }
        }

        if (totalConsiderados == 0) {
            return 0.0;
        }

        return ((double) ausentes / totalConsiderados) * 100.0;
    }
}