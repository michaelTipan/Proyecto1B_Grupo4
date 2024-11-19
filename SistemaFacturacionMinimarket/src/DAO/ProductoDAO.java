package DAO;

import Conexion.ConexionBDD;
import Modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private ConexionBDD conexion = new ConexionBDD();

    public boolean registrarProducto(Producto producto) {
        String sql = "INSERT INTO productos (codigo, nombre, stock, precio) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getCodigo());
            stmt.setString(2, producto.getNombre());
            stmt.setInt(3, producto.getStock());
            stmt.setDouble(4, producto.getPrecio());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar producto: " + e.getMessage());
        }
        return false;
    }

    public List<Producto> listarProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection conn = conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("stock"),
                        rs.getDouble("precio"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return productos;
    }

    public Producto obtenerProductoPorId(int idProducto) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (Connection conn = conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("stock"),
                            rs.getDouble("precio"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener producto por ID: " + e.getMessage());
        }
        return null; // Retorna null si no se encuentra el producto
    }

    public Producto obtenerProductoPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try (Connection conn = conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("stock"),
                            rs.getDouble("precio")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener producto por código: " + e.getMessage());
        }
        return null;
    }

}
