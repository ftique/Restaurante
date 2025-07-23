/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class ControladorPedidos {

    private ArrayList<PedidoMesa> listaPedidos; // Estructura lineal
    private PriorityQueue<PedidoMesa> colaCocina; // Estructura no lineal
    private HashMap<String, ProductoMenu> inventario; // Clave = nombre del producto

    public ControladorPedidos() {
        listaPedidos = new ArrayList<>();
        colaCocina = new PriorityQueue<>(); // requiere que Pedido implemente Comparable
        inventario = new HashMap<>();
    }

    // Añadir un producto al inventario
    public void agregarProductoInventario(ProductoMenu producto) {
        inventario.put(producto.getNombre(), producto);
    }
    
    // Devuelve todo el menú
    public ArrayList<ProductoMenu> obtenerMenu() {
        return new ArrayList<>(inventario.values());
    }

    // Buscar un producto por nombre
    public ProductoMenu buscarProducto(String nombre) {
        return inventario.get(nombre);
    }
    
    // Crear y registrar un pedido
    public boolean registrarPedido(PedidoMesa pedido) {
        // Validar inventario
        for (ProductoMenu p : pedido.getProductos()) {
            ProductoMenu stock = inventario.get(p.getNombre());
            if (stock == null || stock.getInventario() < 1) {
                System.out.println("Producto sin stock: " + p.getNombre());
                return false;
            }
        }

        // Descontar inventario
        for (ProductoMenu p : pedido.getProductos()) {
            ProductoMenu stock = inventario.get(p.getNombre());
            stock.setInventario(stock.getInventario() - 1);
        }

        listaPedidos.add(pedido);
        colaCocina.offer(pedido);
        return true;
    }

    // Consultar cuántos platos de cada tipo están pendientes
    public HashMap<String, Integer> contarPlatosPendientes() {
        HashMap<String, Integer> conteo = new HashMap<>();
        for (PedidoMesa pedido : colaCocina) {
            for (ProductoMenu p : pedido.getProductos()) {
                conteo.put(p.getNombre(), conteo.getOrDefault(p.getNombre(), 0) + 1);
            }
        }
        return conteo;
    }

    // Obtener siguiente pedido para cocina
    public PedidoMesa siguientePedido() {
        return colaCocina.poll(); // Extrae el más prioritario
    }

    public ArrayList<PedidoMesa> getHistorialPedidos() {
        return listaPedidos;
    }
    
    // Devuelve una copia de la cola de pedidos para lectura
    public PriorityQueue<PedidoMesa> obtenerPedidosEnCocina() {
        return new PriorityQueue<>(colaCocina); // devuelve una copia
    }


    
    public HashMap<String, ProductoMenu> getInventario() {
        return inventario;
    }
}

