package view;

import controller.ControladorPedidos;
import model.PedidoMesa;
import model.ProductoMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        campoMesa   = new JTextField(5);
        campoMesero = new JTextField(15);
        modeloLista = new DefaultListModel<>();

        Map<String, ProductoMenu> menu = controlador.getMenu();
        for (String nombre : menu.keySet()) {
            modeloLista.addElement(nombre);
        }

        listaProductos = new JList<>(modeloLista);
        listaProductos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        botonEnviar = new JButton("Enviar Pedido");
        botonEnviar.addActionListener(this::registrarPedido);

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Mesa:"));    panel.add(campoMesa);
        panel.add(new JLabel("Mesero:"));  panel.add(campoMesero);
        panel.add(new JLabel("Productos:"));
        panel.add(new JScrollPane(listaProductos));
        panel.add(botonEnviar);

        add(panel, BorderLayout.CENTER);
    }

    private void registrarPedido(ActionEvent e) {
        try {
            int mesa = Integer.parseInt(campoMesa.getText().trim());
            String mesero = campoMesero.getText().trim();
            List<String> seleccion = listaProductos.getSelectedValuesList();

            if (mesero.isEmpty() || seleccion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresá mesero y al menos un producto.");
                return;
            }

            List<ProductoMenu> productos = new ArrayList<>();
            for (String nom : seleccion) {
                ProductoMenu p = controlador.getMenu().get(nom);
                if (p == null || p.getInventario() <= 0) {
                    JOptionPane.showMessageDialog(this, "Sin stock de: " + nom);
                    return;
                }
                productos.add(p);
            }

            PedidoMesa pedido = new PedidoMesa(mesa, mesero, productos);
            controlador.agregarPedido(pedido);
            JOptionPane.showMessageDialog(this, "✅ Pedido registrado.");
            campoMesa.setText("");
            campoMesero.setText("");
            listaProductos.clearSelection();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El número de mesa debe ser entero.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}