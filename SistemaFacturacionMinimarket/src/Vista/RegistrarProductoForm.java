package Vista;

import Modelo.Producto;
import DAO.ProductoDAO;

import javax.swing.*;

public class RegistrarProductoForm extends JFrame {
    public RegistrarProductoForm() {
        setTitle("Registrar Producto");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(20, 20, 100, 20);
        add(lblCodigo);

        JTextField txtCodigo = new JTextField();
        txtCodigo.setBounds(120, 20, 200, 20);
        add(txtCodigo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 60, 100, 20);
        add(lblNombre);

        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(120, 60, 200, 20);
        add(txtNombre);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(20, 100, 100, 20);
        add(lblPrecio);

        JTextField txtPrecio = new JTextField();
        txtPrecio.setBounds(120, 100, 200, 20);
        add(txtPrecio);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setBounds(20, 140, 100, 20);
        add(lblStock);

        JTextField txtStock = new JTextField();
        txtStock.setBounds(120, 140, 200, 20);
        add(txtStock);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 200, 100, 30);
        add(btnGuardar);

        // Acción del botón Guardar
        btnGuardar.addActionListener(e -> {
            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            double precio;
            int stock;

            // Validar que los campos no estén vacíos
            if (codigo.isEmpty() || nombre.isEmpty() || txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            try {
                precio = Double.parseDouble(txtPrecio.getText());
                stock = Integer.parseInt(txtStock.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio y stock deben ser números válidos.");
                return;
            }

            // Crear un objeto Producto
            Producto producto = new Producto();
            producto.setCodigo(codigo);
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setStock(stock);

            // Llamar al DAO para registrar el producto
            ProductoDAO productoDAO = new ProductoDAO();
            if (productoDAO.registrarProducto(producto)) {
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
                dispose(); // Cerrar el formulario
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el producto.");
            }
        });
    }
}
