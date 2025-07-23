/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import java.io.*;
import java.util.*;

public class Persistencia {

    private static final String ARCHIVO_PEDIDOS = "pedidos.txt";

    public static void guardarPedidos(List<PedidoMesa> pedidos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_PEDIDOS))) {
            for (PedidoMesa pedido : pedidos) {
                String linea = pedido.getMesa() + ";" + pedido.getMesero() + ";";
                linea += String.join(",", 
                        pedido.getProductos().stream()
                        .map(ProductoMenu::getNombre)
                        .toList());
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar pedidos: " + e.getMessage());
        }
    }

    private static final String ARCHIVO_MENU = "menu.txt";

    public static void guardarMenu(Map<String, ProductoMenu> menu) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_MENU))) {
            for (ProductoMenu p : menu.values()) {
                writer.write(p.getNombre() + ";" + p.getTiempoPreparacion() + ";" + p.getInventario());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar menú: " + e.getMessage());
        }
    }
    
    public static List<PedidoMesa> cargarPedidos(Map<String, ProductoMenu> menu) {
        List<PedidoMesa> pedidos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_PEDIDOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length < 3) continue;
                int mesa = Integer.parseInt(partes[0]);
                String mesero = partes[1];
                String[] nombresProductos = partes[2].split(",");

                List<ProductoMenu> productos = new ArrayList<>();
                for (String nombre : nombresProductos) {
                    ProductoMenu producto = menu.get(nombre.trim());
                    if (producto != null) {
                        productos.add(producto);
                    }
                }
                pedidos.add(new PedidoMesa(mesa, mesero, productos));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    public static Map<String, ProductoMenu> cargarMenu() {
        Map<String, ProductoMenu> menu = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_MENU))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    String nombre = partes[0];
                    int tiempo = Integer.parseInt(partes[1]);
                    int inventario = Integer.parseInt(partes[2]);
                    menu.put(nombre, new ProductoMenu(nombre, tiempo, inventario));
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar menú (usando valores por defecto)");
        }
        return menu;
    }
}