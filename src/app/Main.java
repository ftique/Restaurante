package app;

import controller.*;
import view.*;
import model.*;


public class Main {
    public static void main(String[] args) {
        // Crear el controlador (maneja pedidos y la cola de prioridad)
        ControladorPedidos controlador = new ControladorPedidos();
        
        controlador.agregarProductoInventario(new ProductoMenu("Hamburguesa", 20, 15));
        controlador.agregarProductoInventario(new ProductoMenu("Pizza", 30, 20));
        controlador.agregarProductoInventario(new ProductoMenu("Ensalada", 15, 10));
        controlador.agregarProductoInventario(new ProductoMenu("Sopa", 10, 12));
        controlador.agregarProductoInventario(new ProductoMenu("Postre", 25, 8));
        
        // Crear las dos ventanas y pasar el mismo controlador a ambas
        VentanaMesero ventanaMesero = new VentanaMesero(controlador);
        VentanaCocina ventanaCocina = new VentanaCocina(controlador);

        // Mostrar ambas ventanas
        ventanaMesero.setVisible(true);
        ventanaCocina.setVisible(true);
    }
}
