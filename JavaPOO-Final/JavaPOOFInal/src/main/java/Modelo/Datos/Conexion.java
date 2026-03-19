package Modelo.Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Connection conexion = null;

    private static final String URL = "jdbc:mysql://localhost/finalpoo";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Establecido correctamente");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error al conectar a la base de datos.", e);
        }
        return conexion;
    }

}