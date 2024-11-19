package DAO;

import Conexion.ConexionBDD;
import Modelo.Cliente;
import Modelo.Detalle;
import Modelo.Producto;
import Modelo.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    private ConexionBDD conexion = new ConexionBDD();

    // Registrar venta con cálculos centralizados
    public boolean registrarVenta(Venta venta, List<Detalle> detalles) {
        String sqlVenta = "INSERT INTO ventas (cliente, fecha, total) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalles (id_venta, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexion.getConnection()) {
            conn.setAutoCommit(false);

            int ventaId;
            try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtVenta.setInt(1, venta.getCliente().getId());
                stmtVenta.setString(2, venta.getFecha());
                stmtVenta.setDouble(3, venta.getTotal());
                stmtVenta.executeUpdate();

                ResultSet rs = stmtVenta.getGeneratedKeys();
                if (rs.next()) {
                    ventaId = rs.getInt(1);
                    venta.setId(ventaId);
                } else {
                    conn.rollback();
                    return false;
                }
            }

            for (Detalle detalle : detalles) {
                try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                    stmtDetalle.setInt(1, venta.getId());
                    stmtDetalle.setInt(2, detalle.getProducto().getId());
                    stmtDetalle.setInt(3, detalle.getCantidad());
                    stmtDetalle.setDouble(4, detalle.getSubtotal());
                    stmtDetalle.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar la venta y sus detalles: " + e.getMessage());
            try {
                conexion.getConnection().rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error al realizar rollback: " + rollbackEx.getMessage());
            }
        }

        return false;
    }

    // Obtener una venta por su ID
    public Venta obtenerVentaPorId(int idVenta) {
        String sql = "SELECT * FROM ventas WHERE id = ?";
        try (Connection conn = conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new ClienteDAO().obtenerClientePorId(rs.getInt("cliente"));
                    return new Venta(
                            rs.getInt("id"),
                            cliente,
                            rs.getString("fecha"),
                            rs.getDouble("total")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener venta: " + e.getMessage());
        }
        return null;
    }

    // Obtener detalles de una venta
    public List<Detalle> obtenerDetallesPorVenta(int idVenta) {
        List<Detalle> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalles WHERE id_venta = ?";
        try (Connection conn = conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new ProductoDAO().obtenerProductoPorId(rs.getInt("id_producto"));
                    if (producto == null) {
                        System.out.println("Producto no encontrado para ID: " + rs.getInt("id_producto"));
                        continue;
                    }
                    detalles.add(new Detalle(
                            rs.getInt("id"),
                            producto,
                            rs.getInt("cantidad"),
                            rs.getDouble("subtotal")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalles de venta: " + e.getMessage());
        }
        return detalles;
    }
}
