/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Comparator;
import java.util.List;

public class PedidoMesa implements Comparable<PedidoMesa> {
    private int mesa;
    private String mesero;
    private List<ProductoMenu> productos;

    public PedidoMesa(int mesa, String mesero, List<ProductoMenu> productos) {
        this.mesa = mesa;
        this.mesero = mesero;
        this.productos = productos;
    }

    public int getMesa() { return mesa; }
    public String getMesero() { return mesero; }
    public List<ProductoMenu> getProductos() { return productos; }
    
    @Override
    public int compareTo(PedidoMesa otro) {
        int maxTiempoEste = this.productos.stream()
                .mapToInt(ProductoMenu::getTiempoPreparacion)
                .max()
                .orElse(0);

        int maxTiempoOtro = otro.productos.stream()
                .mapToInt(ProductoMenu::getTiempoPreparacion)
                .max()
                .orElse(0);

        return Integer.compare(maxTiempoEste, maxTiempoOtro); // Mayor tiempo → mayor prioridad
    }
    
    @Override
    public String toString() {
        return "Mesa " + mesa + " | Mesero: " + mesero + " | Productos: " +
                productos.stream().map(ProductoMenu::getNombre).toList();
    }
    public ProductoMenu productoMasDemorado() {
        return productos.stream()
                .max(Comparator.comparingInt(ProductoMenu::getTiempoPreparacion))
                .orElse(null);
    }

}