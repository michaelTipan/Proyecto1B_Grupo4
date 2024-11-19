package Vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Sistema de Facturación - Grupo 4");
        setSize(500, 500);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Fondo del menú
        JPanel panelFondo = new JPanel();
        panelFondo.setBackground(new Color(240, 248, 255)); // Azul claro
        panelFondo.setLayout(null);
        panelFondo.setBounds(0, 0, 500, 500);
        add(panelFondo);

        // Título del menú
        JLabel lblTitulo = new JLabel("Sistema de Facturación", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 51, 102)); // Azul oscuro
        lblTitulo.setBounds(50, 20, 400, 40);
        panelFondo.add(lblTitulo);

        // Botones estilizados
        JButton btnRegistrarProducto = crearBoton("Registrar Producto", 150);
        JButton btnRegistrarCliente = crearBoton("Registrar Cliente", 200);
        JButton btnRegistrarVenta = crearBoton("Registrar Venta", 250);
        JButton btnSalir = crearBoton("Salir", 300);

        panelFondo.add(btnRegistrarProducto);
        panelFondo.add(btnRegistrarCliente);
        panelFondo.add(btnRegistrarVenta);
        panelFondo.add(btnSalir);

        // Pie de página
        JLabel lblPie = new JLabel("Proyecto realizado por el Grupo 4", SwingConstants.CENTER);
        lblPie.setFont(new Font("Arial", Font.ITALIC, 14));
        lblPie.setForeground(Color.GRAY);
        lblPie.setBounds(50, 420, 400, 30);
        panelFondo.add(lblPie);

        // Acciones de los botones
        btnRegistrarProducto.addActionListener(e -> new RegistrarProductoForm().setVisible(true));
        btnRegistrarCliente.addActionListener(e -> new RegistrarClienteForm("").setVisible(true));
        btnRegistrarVenta.addActionListener(e -> new RegistrarVentaForm().setVisible(true));
        btnSalir.addActionListener(e -> System.exit(0));
    }

    // Método para crear botones con estilo
    private JButton crearBoton(String texto, int yPos) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(0, 51, 102)); // Azul oscuro
        boton.setBounds(150, yPos, 200, 40);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal menu = new MenuPrincipal();
            menu.setVisible(true);
        });
    }
}
