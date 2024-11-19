package Vista;

import Modelo.Cliente;
import DAO.ClienteDAO;

import javax.swing.*;

public class RegistrarClienteForm extends JFrame {
    public RegistrarClienteForm(String dniPrellenado) {
        setTitle("Registrar Cliente");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblDni = new JLabel("DNI:");
        lblDni.setBounds(20, 20, 100, 20);
        add(lblDni);

        JTextField txtDni = new JTextField();
        txtDni.setBounds(120, 20, 200, 20);
        txtDni.setText(dniPrellenado); // Prellenar DNI si es llamado desde Registrar Venta
        add(txtDni);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 60, 100, 20);
        add(lblNombre);

        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(120, 60, 200, 20);
        add(txtNombre);

        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setBounds(20, 100, 100, 20);
        add(lblDireccion);

        JTextField txtDireccion = new JTextField();
        txtDireccion.setBounds(120, 100, 200, 20);
        add(txtDireccion);

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(20, 140, 100, 20);
        add(lblTelefono);

        JTextField txtTelefono = new JTextField();
        txtTelefono.setBounds(120, 140, 200, 20);
        add(txtTelefono);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 200, 100, 30);
        add(btnGuardar);

        // Acción del botón Guardar
        btnGuardar.addActionListener(e -> {
            String dni = txtDni.getText();
            String nombre = txtNombre.getText();
            String direccion = txtDireccion.getText();
            String telefono = txtTelefono.getText();

            // Validar que los campos no estén vacíos
            if (dni.isEmpty() || nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            // Crear un objeto Cliente
            Cliente cliente = new Cliente();
            cliente.setDni(dni);
            cliente.setNombre(nombre);
            cliente.setDireccion(direccion);
            cliente.setTelefono(telefono);

            // Llamar al DAO para registrar el cliente
            ClienteDAO clienteDAO = new ClienteDAO();
            if (clienteDAO.registrarCliente(cliente)) {
                JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
                dispose(); // Cerrar el formulario
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el cliente.");
            }
        });
    }
}
