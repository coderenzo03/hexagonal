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

    // Mapeo de dominio a entidad
    public static ProductoEntity fromModel(Producto producto) {
        return new ProductoEntity(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }

    // Mapeo de entidad a dominio
    public Producto toModel() {
        return new Producto(id, nombre, descripcion, precio, stock);
    }
}
