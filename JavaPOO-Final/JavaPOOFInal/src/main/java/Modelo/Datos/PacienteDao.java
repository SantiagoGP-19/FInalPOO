// PacienteDao.java
package Modelo.Datos;

import Modelo.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {

    // LISTAR
    public List<Paciente> listar() {
        List<Paciente> lista = new ArrayList<>();
        // Unimos con la tabla PERSONA para traer nombre, dni, etc.
        String sql = "SELECT p.id, per.nombre, per.apellido, per.dni, per.mail, per.telefono, p.obra_social, p.numero_afiliado " +
                "FROM paciente p JOIN persona per ON p.id = per.id";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                p.setDni(rs.getString("dni"));
                p.setMail(rs.getString("mail"));
                p.setTelefono(rs.getString("telefono"));
                p.setObraSocial(rs.getString("obra_social"));
                p.setNumeroAfiliado(rs.getString("numero_afiliado"));
                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // GUARDAR
    public void guardar(Paciente p) {
        Connection con = Conexion.getConexion();
        try {
            // 1. Insertar en PERSONA primero (Datos básicos)
            String sqlPersona = "INSERT INTO persona (nombre, apellido, dni, mail, telefono) VALUES (?,?,?,?,?)";
            PreparedStatement psPersona = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            psPersona.setString(1, p.getNombre());
            psPersona.setString(2, p.getApellido());
            psPersona.setString(3, p.getDni());
            psPersona.setString(4, p.getMail());
            psPersona.setString(5, p.getTelefono());
            psPersona.executeUpdate();

            // Obtener el ID generado
            ResultSet rs = psPersona.getGeneratedKeys();
            int idGenerado = 0;
            if (rs.next()) idGenerado = rs.getInt(1);

            // 2. Insertar en PACIENTE (Datos médicos) usando el mismo ID
            String sqlPaciente = "INSERT INTO paciente (id, obra_social, numero_afiliado) VALUES (?,?,?)";
            PreparedStatement psPaciente = con.prepareStatement(sqlPaciente);
            psPaciente.setInt(1, idGenerado);
            psPaciente.setString(2, p.getObraSocial());
            psPaciente.setString(3, p.getNumeroAfiliado());
            psPaciente.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Nuevo: ACTUALIZAR PACIENTE (Punto 2)
    public void actualizar(Paciente p) {
        Connection con = Conexion.getConexion();
        try {
            // 1. Actualizar PERSONA
            String sqlPersona = "UPDATE persona SET nombre=?, apellido=?, dni=?, mail=?, telefono=? WHERE id=?";
            PreparedStatement psPersona = con.prepareStatement(sqlPersona);
            psPersona.setString(1, p.getNombre());
            psPersona.setString(2, p.getApellido());
            psPersona.setString(3, p.getDni());
            psPersona.setString(4, p.getMail());
            psPersona.setString(5, p.getTelefono());
            psPersona.setInt(6, p.getId());
            psPersona.executeUpdate();

            // 2. Actualizar PACIENTE
            String sqlPaciente = "UPDATE paciente SET obra_social=?, numero_afiliado=? WHERE id=?";
            PreparedStatement psPaciente = con.prepareStatement(sqlPaciente);
            psPaciente.setString(1, p.getObraSocial());
            psPaciente.setString(2, p.getNumeroAfiliado());
            psPaciente.setInt(3, p.getId());
            psPaciente.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ELIMINAR
    public void eliminar(int id) {
        try (Connection con = Conexion.getConexion()) {
            // Borramos primero de paciente, luego de persona por las FK
            con.prepareStatement("DELETE FROM paciente WHERE id=" + id).executeUpdate();
            con.prepareStatement("DELETE FROM persona WHERE id=" + id).executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}