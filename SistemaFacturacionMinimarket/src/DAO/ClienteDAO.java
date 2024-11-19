package DAO;

import Conexion.ConexionBDD;
import Modelo.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private ConexionBDD conexion = new ConexionBDD();

    // Registrar un cliente
    public boolean registrarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (dni, nombre, telefono, direccion) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getDni());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getDireccion());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
        }
        return false;
    }

    // Obtener cliente por ID
    public Cliente obtenerClientePorId(int idCliente) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id"),
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("direccion"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cliente por ID: " + e.getMessage());
        }
        return null; // Retorna null si no encuentra el cliente
    }

    public Cliente obtenerClientePorDni(String dni) {
        String sql = "SELECT * FROM clientes WHERE dni = ?";
        try (Connection conn = conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id"),
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("direccion"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cliente por DNI: " + e.getMessage());
        }
        return null;
    }

    // Listar todos los clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("direccion"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }
}
