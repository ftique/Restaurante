package view;

import controller.ControladorPedidos;
import model.PedidoMesa;
import model.ProductoMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VentanaCocina extends JFrame {
    private JTextArea areaPedidos;
    private JButton botonDespachar;
    private ControladorPedidos controlador;

    public VentanaCocina(ControladorPedidos controlador) {
        this.controlador = controlador;
        setTitle("Panel de Cocina");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        iniciarComponentes();
        refrescar();
        new Timer(2000, evt -> refrescar()).start();
    }

    private void iniciarComponentes() {
        areaPedidos = new JTextArea();
        areaPedidos.setEditable(false);
        botonDespachar = new JButton("Despachar Siguiente Pedido");
        botonDespachar.addActionListener(this::despachar);

        add(new JScrollPane(areaPedidos), BorderLayout.CENTER);
        add(botonDespachar, BorderLayout.SOUTH);
    }

    private void refrescar() {
        List<PedidoMesa> cola = controlador.getColaPedidos();
        StringBuilder sb = new StringBuilder();
        for (PedidoMesa p : cola) {
            sb.append("Mesa ").append(p.getMesa())
              .append(" | Mesero: ").append(p.getMesero())
              .append(" | Productos: ");
            for (ProductoMenu pm : p.getProductos()) {
                sb.append(pm.getNombre())
                  .append(" (").append(pm.getTiempoPreparacion()).append("m), ");
            }
            sb.append("\n\n");
        }
        areaPedidos.setText(sb.toString());
    }

    private void despachar(ActionEvent e) {
        PedidoMesa siguiente = controlador.atenderPedido();
        if (siguiente == null) {
            JOptionPane.showMessageDialog(this, "No hay más pedidos.");
        } else {
            JOptionPane.showMessageDialog(this,
                "✅ Pedido Mesa " + siguiente.getMesa() + " despachado.");
            refrescar();
        }
    }
}