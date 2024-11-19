package Conexion;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        ConexionBDD conexion = new ConexionBDD();
        Connection conn = conexion.getConnection();

        if (conn != null) {
            System.out.println("¡Conexión a la base de datos exitosa!");
        } else {
            System.out.println("Error al conectar a la base de datos.");
        }
    }
}
