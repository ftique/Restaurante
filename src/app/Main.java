package app;

import controller.ControladorPedidos;
import model.Persistencia;
import model.ProductoMenu;
import view.*;

import javax.swing.*;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        Map<String, ProductoMenu> menu = Persistencia.cargarMenu();
        if (menu.isEmpty()) {
            menu.put("Pizza",    new ProductoMenu("Pizza",    15, 5));
            menu.put("Ensalada", new ProductoMenu("Ensalada",  5, 4));
            menu.put("Limonada", new ProductoMenu("Limonada",  2, 6));
        }

        ControladorPedidos controlador = new ControladorPedidos(menu);

        SwingUtilities.invokeLater(() -> {
            new VentanaMesero(controlador).setVisible(true);
            new VentanaCocina(controlador).setVisible(true);
        });
    }
}