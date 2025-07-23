/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

public class PedidoMesa {
    private int mesa;
    private String mesero;
    private List<ProductoMenu> productos;

    public PedidoMesa(int mesa, String mesero, List<ProductoMenu> productos) {
        this.mesa = mesa;
        this.mesero = mesero;
        this.productos = productos;
    }
    
    public int getMesa() {
        return mesa;
    }
    
    public String getMesero() {
        return mesero;
    }
    
    public List<ProductoMenu> getProductos() {
        return productos;
    }

    @Override
    public String toString() {
        return "Mesa " + mesa + " | Mesero: " + mesero + " | Productos: " + productos.stream().map(ProductoMenu::getNombre).toList();
    }
}