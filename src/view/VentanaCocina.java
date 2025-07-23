package view;

import controller.ControladorPedidos;
import model.PedidoMesa;
import model.ProductoMenu;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.PriorityQueue;

public class VentanaCocina extends JFrame {
    private JTextArea areaPedidos;
    private JButton botonDespachar;
    private ControladorPedidos controlador;

    public VentanaCocina(ControladorPedidos controlador) {
        this.controlador = controlador;
        setTitle("Panel de Cocina");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        iniciarComponentes();
        cargarPedidos();
        Timer timer = new Timer(2000, e -> cargarPedidos());
        timer.start();
    }

    private void iniciarComponentes() {
        areaPedidos = new JTextArea();
        areaPedidos.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaPedidos);

        botonDespachar = new JButton("Despachar Siguiente Pedido");
        botonDespachar.addActionListener(this::despacharPedido);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(botonDespachar, BorderLayout.SOUTH);

        add(panel);
    }

    private void cargarPedidos() {
        PriorityQueue<PedidoMesa> cola = controlador.obtenerPedidosEnCocina();
        StringBuilder sb = new StringBuilder();

        for (PedidoMesa pedido : cola) {
            sb.append("Mesa #").append(pedido.getMesa())
              .append(" | Mesero: ").append(pedido.getMesero())
              .append(" | Productos: ");

            List<ProductoMenu> productos = pedido.getProductos();
            for (ProductoMenu p : productos) {
                sb.append(p.getNombre()).append(" (").append(p.getTiempoPreparacion()).append(" min), ");
            }

            ProductoMenu max = pedido.productoMasDemorado();
            if (max != null) {
                sb.append(" | Más demorado: ").append(max.getNombre()).append(" - ").append(max.getTiempoPreparacion()).append(" min");
            }

            sb.append("\n\n");
        }

        areaPedidos.setText(sb.toString());
    }

    private void despacharPedido(ActionEvent e) {
        PedidoMesa siguiente = controlador.siguientePedido();
        if (siguiente == null) {
            JOptionPane.showMessageDialog(this, "No hay pedidos pendientes.");
            return;
        }

        // Disminuir inventario de los productos servidos
        for (ProductoMenu p : siguiente.getProductos()) {
            p.reducirInventario();
        }

        JOptionPane.showMessageDialog(this, "Pedido de la mesa #" + siguiente.getMesa() + " despachado.");
        cargarPedidos();
    }
}
