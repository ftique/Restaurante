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

    public static List<PedidoMesa> cargarPedidos(Map<String, ProductoMenu> menu) {
        List<PedidoMesa> pedidos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_PEDIDOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                int mesa = Integer.parseInt(partes[0]);
                String mesero = partes[1];
                String[] nombresProductos = partes[2].split(",");

                List<ProductoMenu> productos = new ArrayList<>();
                for (String nombre : nombresProductos) {
                    ProductoMenu p = menu.get(nombre);
                    if (p != null) {
                        productos.add(p);
                    }
                }

                pedidos.add(new PedidoMesa(mesa, mesero, productos));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar pedidos: " + e.getMessage());
        }

        return pedidos;
    }
}
