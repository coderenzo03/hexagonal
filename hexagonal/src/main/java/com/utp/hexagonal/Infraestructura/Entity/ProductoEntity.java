package com.utp.hexagonal.Infraestructura.Entity;

import com.utp.hexagonal.Dominio.modelo.Producto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    public static ProductoEntity fromModel(Producto producto) {
        ProductoEntity entity = new ProductoEntity();
        entity.setId(producto.getId());
        entity.setNombre(producto.getNombre());
        entity.setDescripcion(producto.getDescripcion());
        entity.setPrecio(producto.getPrecio());
        entity.setStock(producto.getStock());
        return entity;
    }


    public Producto toModel() {
        return new Producto(
                this.id,
                this.nombre,
                this.descripcion,
                this.precio,
                this.stock
        );
    }
}
