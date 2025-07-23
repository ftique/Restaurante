package view;

import controller.ControladorPedidos;
import model.ProductoMenu;
import model.PedidoMesa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class VentanaMesero extends JFrame {
    private JTextField campoMesa;
    private JTextField campoMesero;
    private JList<String> listaProductos;
    private DefaultListModel<String> modeloLista;
    private JButton botonEnviar;

    private ControladorPedidos controlador;

    public VentanaMesero(ControladorPedidos controlador) {
        this.controlador = controlador;
        setTitle("Registro de Pedido - Mesero");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        campoMesa = new JTextField(5);
        campoMesero = new JTextField(15);
        modeloLista = new DefaultListModel<>();

        // Agregamos productos desde el menú
        for (ProductoMenu producto : controlador.obtenerMenu()) {
            modeloLista.addElement(producto.getNombre());
        }

        listaProductos = new JList<>(modeloLista);
        listaProductos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        botonEnviar = new JButton("Enviar Pedido");

        botonEnviar.addActionListener(this::registrarPedido);

        JPanel panel = new JPanel(new GridLayout(6, 1));
        panel.add(new JLabel("Mesa:"));
        panel.add(campoMesa);
        panel.add(new JLabel("Mesero:"));
        panel.add(campoMesero);
        panel.add(new JLabel("Productos:"));
        panel.add(new JScrollPane(listaProductos));
        panel.add(botonEnviar);

        add(panel);
    }

    private void registrarPedido(ActionEvent e) {
        try {
            int mesa = Integer.parseInt(campoMesa.getText());
            String mesero = campoMesero.getText();
            List<String> seleccionados = listaProductos.getSelectedValuesList();

            if (mesero.isBlank() || seleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del mesero y seleccionar al menos un producto.");
                return;
            }

            List<ProductoMenu> productosSeleccionados = new ArrayList<>();
            for (String nombre : seleccionados) {
                ProductoMenu p = controlador.buscarProducto(nombre);
                if (p != null && p.getInventario() > 0) {
                    productosSeleccionados.add(p);
                } else {
                    JOptionPane.showMessageDialog(this, "El producto '" + nombre + "' no tiene inventario suficiente.");
                    return;
                }
            }
            
            PedidoMesa pedido = new PedidoMesa(mesa, mesero, productosSeleccionados);
            controlador.registrarPedido(pedido);
            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.");
            campoMesa.setText("");
            campoMesero.setText("");
            listaProductos.clearSelection();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El número de mesa debe ser un valor numérico.");
        }
    }
}
