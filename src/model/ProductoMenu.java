/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class ProductoMenu {
    private String nombre;
    private int tiempoPreparacion;
    private int inventario;

    public ProductoMenu(String nombre, int tiempoPreparacion, int inventario) {
        this.nombre = nombre;
        this.tiempoPreparacion = tiempoPreparacion;
        this.inventario = inventario;
    }

    public String getNombre() { return nombre; }
    public int getInventario() { return inventario; }
    public int getTiempoPreparacion() { return tiempoPreparacion; }

    public void reducirInventario() {
        if (inventario > 0) inventario--;
    }

    @Override
    public String toString() {
        return nombre + " (Prep: " + tiempoPreparacion + " min, Stock: " + inventario + ")";
    }
}