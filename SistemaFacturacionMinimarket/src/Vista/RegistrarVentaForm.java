package Vista;

import Modelo.Cliente;
import Modelo.Detalle;
import Modelo.Producto;
import Modelo.Venta;
import DAO.ClienteDAO;
import DAO.ProductoDAO;
import DAO.VentaDAO;
import Servicios.ServiciosVenta; // Importa la nueva clase ServiciosVenta
import Documentos.FacturaPDF;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrarVentaForm extends JFrame {
    private JTextField txtDniCliente, txtCodigoProducto, txtCantidad, txtSubtotal, txtImpuesto, txtTotal;
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrarCliente, btnAgregarProducto, btnEliminarProducto, btnModificarCantidad,
            btnRegistrarVenta;
    private List<Detalle> detalles = new ArrayList<>();
    private Cliente cliente;

    // Nueva instancia de ServiciosVenta para cálculos
    private final ServiciosVenta serviciosVenta = new ServiciosVenta();

    public RegistrarVentaForm() {
        setTitle("Registrar Venta");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JLabel lblCliente = new JLabel("DNI Cliente:");
        lblCliente.setBounds(20, 20, 100, 20);
        add(lblCliente);

        txtDniCliente = new JTextField();
        txtDniCliente.setBounds(120, 20, 200, 20);
        add(txtDniCliente);

        btnRegistrarCliente = new JButton("Registrar Cliente");
        btnRegistrarCliente.setBounds(330, 20, 150, 20);
        add(btnRegistrarCliente);

        JLabel lblProducto = new JLabel("Código Producto:");
        lblProducto.setBounds(20, 60, 120, 20);
        add(lblProducto);

        txtCodigoProducto = new JTextField();
        txtCodigoProducto.setBounds(140, 60, 200, 20);
        add(txtCodigoProducto);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(20, 100, 100, 20);
        add(lblCantidad);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(120, 100, 200, 20);
        add(txtCantidad);

        btnAgregarProducto = new JButton("Agregar Producto");
        btnAgregarProducto.setBounds(330, 100, 150, 20);
        add(btnAgregarProducto);

        btnEliminarProducto = new JButton("Eliminar Producto");
        btnEliminarProducto.setBounds(490, 100, 150, 20);
        add(btnEliminarProducto);

        btnModificarCantidad = new JButton("Modificar Cantidad");
        btnModificarCantidad.setBounds(650, 100, 150, 20);
        add(btnModificarCantidad);

        // Configurar la tabla para detalles
        JLabel lblDetalles = new JLabel("Detalles de Venta:");
        lblDetalles.setBounds(20, 140, 150, 20);
        add(lblDetalles);

        String[] columnas = { "Cantidad", "Descripción", "Valor Unitario", "Importe" };
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaDetalles = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaDetalles);
        scrollTabla.setBounds(20, 170, 740, 200);
        add(scrollTabla);

        btnRegistrarVenta = new JButton("Registrar Venta");
        btnRegistrarVenta.setBounds(320, 400, 150, 30);
        add(btnRegistrarVenta);

        inicializarCamposTotales();

        btnRegistrarCliente.addActionListener(e -> buscarCliente());
        btnAgregarProducto.addActionListener(e -> agregarProducto());
        btnEliminarProducto.addActionListener(e -> eliminarProducto());
        btnModificarCantidad.addActionListener(e -> modificarCantidad());
        btnRegistrarVenta.addActionListener(e -> registrarVenta());
    }

    private void inicializarCamposTotales() {
        JLabel lblSubtotal = new JLabel("Subtotal:");
        lblSubtotal.setBounds(20, 450, 100, 20);
        add(lblSubtotal);

        txtSubtotal = new JTextField();
        txtSubtotal.setBounds(120, 450, 100, 20);
        txtSubtotal.setEditable(false);
        add(txtSubtotal);

        JLabel lblImpuesto = new JLabel("Impuesto:");
        lblImpuesto.setBounds(230, 450, 100, 20);
        add(lblImpuesto);

        txtImpuesto = new JTextField();
        txtImpuesto.setBounds(320, 450, 100, 20);
        txtImpuesto.setEditable(false);
        add(txtImpuesto);

        JLabel lblTotal = new JLabel("Total:");
        lblTotal.setBounds(430, 450, 100, 20);
        add(lblTotal);

        txtTotal = new JTextField();
        txtTotal.setBounds(500, 450, 100, 20);
        txtTotal.setEditable(false);
        add(txtTotal);
    }

    private void buscarCliente() {
        String dniCliente = txtDniCliente.getText();
        if (dniCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "DNI vacío. Por favor, ingrese un DNI.");
            return;
        }

        ClienteDAO clienteDAO = new ClienteDAO();
        cliente = clienteDAO.obtenerClientePorDni(dniCliente);

        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado. Regístrelo.");
            new RegistrarClienteForm(dniCliente).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Cliente encontrado: " + cliente.getNombre());
        }
    }

    private void agregarProducto() {
        String codigoProducto = txtCodigoProducto.getText();
        String cantidadStr = txtCantidad.getText();

        if (codigoProducto.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar los campos de producto y cantidad.");
            return;
        }

        ProductoDAO productoDAO = new ProductoDAO();
        Producto producto = productoDAO.obtenerProductoPorCodigo(codigoProducto);

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            Detalle detalle = new Detalle(0, producto, cantidad,
                    serviciosVenta.calcularSubtotal(new Detalle(0, producto, cantidad, 0)));
            detalles.add(detalle);

            actualizarTabla();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido.");
        }
    }

    private void eliminarProducto() {
        String codigoProducto = txtCodigoProducto.getText();

        if (codigoProducto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el código del producto a eliminar.");
            return;
        }

        detalles = serviciosVenta.eliminarDetallePorCodigo(detalles, codigoProducto);
        actualizarTabla();
    }

    private void modificarCantidad() {
        String codigoProducto = txtCodigoProducto.getText();
        String cantidadStr = txtCantidad.getText();

        if (codigoProducto.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el código y la nueva cantidad.");
            return;
        }

        try {
            int nuevaCantidad = Integer.parseInt(cantidadStr);
            detalles = serviciosVenta.actualizarCantidad(detalles, codigoProducto, nuevaCantidad);

            actualizarTabla();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido.");
        }
    }

    private void registrarVenta() {
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione o registre un cliente.");
            return;
        }
        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto.");
            return;
        }
    
        VentaDAO ventaDAO = new VentaDAO();
    
        // Calculamos los valores de la venta con la clase ServiciosVenta
        double[] totales = serviciosVenta.obtenerTotales(detalles, 0.12);
        double subtotal = totales[0];
        double impuesto = totales[1];
        double total = totales[2];
    
        Venta venta = new Venta(cliente, "2024-11-19", total);
    
        boolean registroExitoso = ventaDAO.registrarVenta(venta, detalles);
    
        if (registroExitoso) {
            // Generar el PDF con los valores calculados
            String rutaArchivo = "Factura_" + venta.getId() + ".pdf";
            try {
                FacturaPDF.generarFacturaPDF(venta, detalles, subtotal, impuesto, total, rutaArchivo);
                JOptionPane.showMessageDialog(this, "Venta registrada y factura generada en: " + rutaArchivo);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Venta registrada, pero ocurrió un error al generar la factura: " + e.getMessage());
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta.");
        }
    }
    

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);

        for (Detalle detalle : detalles) {
            Object[] fila = {
                    detalle.getCantidad(),
                    detalle.getProducto().getNombre(),
                    String.format("$%.2f", detalle.getProducto().getPrecio()),
                    String.format("$%.2f", detalle.getSubtotal())
            };
            modeloTabla.addRow(fila);
        }

        double[] totales = serviciosVenta.obtenerTotales(detalles, 0.12);
        txtSubtotal.setText(String.format("%.2f", totales[0]));
        txtImpuesto.setText(String.format("%.2f", totales[1]));
        txtTotal.setText(String.format("%.2f", totales[2]));
    }
}
