// MedicoDao.java
package Modelo.Datos;

import Modelo.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao {

    public List<Medico> listar() {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT m.id, per.nombre, per.apellido, per.dni, per.mail, per.telefono, m.matricula, m.especialidad " +
                "FROM medico m JOIN persona per ON m.id = per.id";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medico m = new Medico();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                m.setApellido(rs.getString("apellido"));
                m.setDni(rs.getString("dni"));
                m.setMail(rs.getString("mail"));
                m.setTelefono(rs.getString("telefono"));
                m.setMatricula(rs.getString("matricula"));
                m.setEspecialidad(rs.getString("especialidad"));
                lista.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // GUARDAR MÉDICO
    public void guardar(Medico m) {
        Connection con = Conexion.getConexion();
        try {
            // 1. Insertar en PERSONA
            String sqlPersona = "INSERT INTO persona (nombre, apellido, dni, mail, telefono) VALUES (?,?,?,?,?)";
            PreparedStatement psPersona = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            psPersona.setString(1, m.getNombre());
            psPersona.setString(2, m.getApellido());
            psPersona.setString(3, m.getDni());
            psPersona.setString(4, m.getMail());
            psPersona.setString(5, m.getTelefono());
            psPersona.executeUpdate();

            ResultSet rs = psPersona.getGeneratedKeys();
            int idGenerado = 0;
            if (rs.next()) idGenerado = rs.getInt(1);

            // 2. Insertar en MEDICO
            String sqlMedico = "INSERT INTO medico (id, matricula, especialidad) VALUES (?,?,?)";
            PreparedStatement psMedico = con.prepareStatement(sqlMedico);
            psMedico.setInt(1, idGenerado);
            psMedico.setString(2, m.getMatricula());
            psMedico.setString(3, m.getEspecialidad());
            psMedico.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Nuevo: ACTUALIZAR MÉDICO (Punto 2)
    public void actualizar(Medico m) {
        Connection con = Conexion.getConexion();
        try {
            // 1. Actualizar PERSONA
            String sqlPersona = "UPDATE persona SET nombre=?, apellido=?, dni=?, mail=?, telefono=? WHERE id=?";
            PreparedStatement psPersona = con.prepareStatement(sqlPersona);
            psPersona.setString(1, m.getNombre());
            psPersona.setString(2, m.getApellido());
            psPersona.setString(3, m.getDni());
            psPersona.setString(4, m.getMail());
            psPersona.setString(5, m.getTelefono());
            psPersona.setInt(6, m.getId());
            psPersona.executeUpdate();

            // 2. Actualizar MEDICO
            String sqlMedico = "UPDATE medico SET matricula=?, especialidad=? WHERE id=?";
            PreparedStatement psMedico = con.prepareStatement(sqlMedico);
            psMedico.setString(1, m.getMatricula());
            psMedico.setString(2, m.getEspecialidad());
            psMedico.setInt(3, m.getId());
            psMedico.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminar(int id) {
        try (Connection con = Conexion.getConexion()) {
            con.prepareStatement("DELETE FROM medico WHERE id=" + id).executeUpdate();
            con.prepareStatement("DELETE FROM persona WHERE id=" + id).executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}