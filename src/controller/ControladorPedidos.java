/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import model.PedidoMesa;
import model.ProductoMenu;
import model.Persistencia;

import java.util.*;

public class ControladorPedidos {

    private final PriorityQueue<PedidoMesa> colaPrioridad;
    private final List<PedidoMesa> historialPedidos;
    private final Map<String, ProductoMenu> menu;

    public ControladorPedidos(Map<String, ProductoMenu> menu) {
        this.menu = menu;
        this.historialPedidos = new ArrayList<>();

        this.colaPrioridad = new PriorityQueue<>(new Comparator<PedidoMesa>() {
            @Override
            public int compare(PedidoMesa p1, PedidoMesa p2) {
                int max1 = getMaxTiempoPreparacion(p1);
                int max2 = getMaxTiempoPreparacion(p2);
                return Integer.compare(max2, max1); // mayor tiempo = mayor prioridad
            }
        });
        
        List<PedidoMesa> pedidosGuardados = Persistencia.cargarPedidos(menu);
        if (pedidosGuardados != null) {
            historialPedidos.addAll(pedidosGuardados);
            colaPrioridad.addAll(pedidosGuardados);
        }
    }

    public void agregarPedido(PedidoMesa pedido) throws Exception {
        for (ProductoMenu producto : pedido.getProductos()) {
            ProductoMenu original = menu.get(producto.getNombre());
            if (original == null) {
                throw new Exception("El producto '" + producto.getNombre() + "' no existe en el menú.");
            }
            if (original.getInventario() <= 0) {
                throw new Exception("No hay stock de " + producto.getNombre());
            }
        }
        
        descontarStock(pedido);
        colaPrioridad.add(pedido);
        historialPedidos.add(pedido);
        Persistencia.guardarPedidos(historialPedidos);
        Persistencia.guardarMenu(menu);
    }

    private void descontarStock(PedidoMesa pedido) {
        for (ProductoMenu producto : pedido.getProductos()) {
            String nombre = producto.getNombre();
            ProductoMenu original = menu.get(nombre);
            if (original != null) {
                original.reducirInventario();
            }
        }
    }

    public PedidoMesa atenderPedido() {
        PedidoMesa siguiente = colaPrioridad.poll();
        if (siguiente != null) {
            System.out.println("Atendiendo pedido: " + siguiente);
        }
        return siguiente;
    }

    public List<PedidoMesa> getHistorialPedidos() {
        return historialPedidos;
    }

    public List<PedidoMesa> getColaPedidos() {
        return new ArrayList<>(colaPrioridad);
    }
    
    public Map<String, ProductoMenu> getMenu() {
        return menu;
    }
    
    public void mostrarPedidosOrdenados() {
    System.out.println("\nPedidos en cola de prioridad (ordenados por tiempo máximo de preparación):");

    List<PedidoMesa> copiaOrdenada = new ArrayList<>(colaPrioridad);
        copiaOrdenada.sort(Comparator.comparingInt(this::getMaxTiempoPreparacion).reversed());
    
        for (PedidoMesa pedido : copiaOrdenada) {
            System.out.println("Mesa " + pedido.getMesa() +
                " | Mesero: " + pedido.getMesero() +
                " | Productos: " + pedido.getProductos() +
                " | Tiempo máx: " + getMaxTiempoPreparacion(pedido));
        }
    }
    
    public int getMaxTiempoPreparacion(PedidoMesa pedido) {
        int max = 0;
        for (ProductoMenu p : pedido.getProductos()) {
            if (p.getTiempoPreparacion() > max) {
                max = p.getTiempoPreparacion();
            }
        }
        return max;
    }
}