// TurnoDao.java
package Modelo.Datos;

import Modelo.Estado;
import Modelo.Medico;
import Modelo.Paciente;
import Modelo.Turno;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TurnoDao {

    public void guardar(Turno turno) {
        Connection con = Conexion.getConexion();
        String sql = "INSERT INTO turno (id_medico, id_paciente, fecha_hora, estado, observaciones) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, turno.getMedico().getId());
            ps.setInt(2, turno.getPaciente().getId());

            LocalTime horaTime = LocalTime.parse(turno.getHora());
            LocalDateTime fechaHoraCompleta = LocalDateTime.of(turno.getFecha(), horaTime);

            ps.setTimestamp(3, Timestamp.valueOf(fechaHoraCompleta));

            ps.setString(4, turno.getEstado().name());
            ps.setString(5, turno.getObservaciones());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    turno.setId(rs.getInt(1));
                    System.out.println("Turno guardado exitosamente: " + turno.getId());
                }
            }
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al insertar el turno: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Turno> listar() {
        List<Turno> turnos = new ArrayList<>();
        Connection con = Conexion.getConexion();
        String sql = "SELECT t.id, t.id_medico, t.id_paciente, t.fecha_hora, t.estado, t.observaciones, " +
                "pm.nombre as m_nombre, pm.apellido as m_apellido, pm.dni as m_dni, pm.mail as m_mail, pm.telefono as m_tel, m.matricula, m.especialidad, " +
                "pp.nombre as p_nombre, pp.apellido as p_apellido, pp.dni as p_dni, pp.mail as p_mail, pp.telefono as p_tel, p.obra_social, p.numero_afiliado " +
                "FROM turno t " +
                "JOIN medico m ON t.id_medico = m.id " +
                "JOIN persona pm ON m.id = pm.id " +
                "JOIN paciente p ON t.id_paciente = p.id " +
                "JOIN persona pp ON p.id = pp.id";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Reconstruimos Medico y Paciente con sus datos
                Medico medico = new Medico();
                medico.setId(rs.getInt("id_medico"));
                medico.setNombre(rs.getString("m_nombre"));
                medico.setApellido(rs.getString("m_apellido"));
                medico.setDni(rs.getString("m_dni"));
                medico.setMail(rs.getString("m_mail"));
                medico.setTelefono(rs.getString("m_tel"));
                medico.setMatricula(rs.getString("matricula"));
                medico.setEspecialidad(rs.getString("especialidad"));

                Paciente paciente = new Paciente();
                paciente.setId(rs.getInt("id_paciente"));
                paciente.setNombre(rs.getString("p_nombre"));
                paciente.setApellido(rs.getString("p_apellido"));
                paciente.setDni(rs.getString("p_dni"));
                paciente.setMail(rs.getString("p_mail"));
                paciente.setTelefono(rs.getString("p_tel"));
                paciente.setObraSocial(rs.getString("obra_social"));
                paciente.setNumeroAfiliado(rs.getString("numero_afiliado"));

                // Reconstruimos Turno
                Timestamp timestamp = rs.getTimestamp("fecha_hora");
                LocalDateTime ldt = timestamp.toLocalDateTime();
                String horaString = String.format("%02d:%02d", ldt.getHour(), ldt.getMinute());

                Turno t = new Turno(
                        rs.getInt("id"),
                        medico,
                        paciente,
                        ldt.toLocalDate(),
                        horaString,
                        Estado.valueOf(rs.getString("estado")),
                        rs.getString("observaciones")
                );
                turnos.add(t);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return turnos;
    }

    public void actualizar(Turno turno) {
        Connection con = Conexion.getConexion();
        String sql = "UPDATE turno SET id_medico=?, id_paciente=?, fecha_hora=?, estado=?, observaciones=? WHERE id=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, turno.getMedico().getId());
            ps.setInt(2, turno.getPaciente().getId());

            LocalTime horaTime = LocalTime.parse(turno.getHora());
            LocalDateTime fechaHoraCompleta = LocalDateTime.of(turno.getFecha(), horaTime);
            ps.setTimestamp(3, Timestamp.valueOf(fechaHoraCompleta));

            ps.setString(4, turno.getEstado().name());
            ps.setString(5, turno.getObservaciones());
            ps.setInt(6, turno.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar el turno: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Nuevo: Método para eliminar turno
    public void eliminar(int id) {
        Connection con = Conexion.getConexion();
        String sql = "DELETE FROM turno WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el turno: " + e.getMessage());
            e.printStackTrace();
        }
    }
}