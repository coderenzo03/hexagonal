// src/main/java/com/utp/hexagonal/Infraestructura.Entity.ProductoEntity
package com.utp.hexagonal.Infraestructura.Entity;

import com.utp.hexagonal.Dominio.modelo.Producto;
import jakarta.persistence.*;
import lombok.*;

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "productos") // Especifica el nombre de la tabla en la base de datos
@Data // Genera getters, setters, toString, equals y hashCode de Lombok
@NoArgsConstructor // Genera un constructor sin argumentos de Lombok
@AllArgsConstructor // Genera un constructor con todos los argumentos de Lombok
public class ProductoEntity {

    @Id // Marca este campo como la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID
    // IDENTITY es ideal para PostgreSQL, ya que utiliza la columna de auto-incremento (SERIAL)
    private Long id;

    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    public ProductoEntity(String nombre, String descripcion, double precio, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }
    public static ProductoEntity fromModel(Producto producto) {
        if (producto.getId() != null) {
            // Si el producto ya tiene un ID, lo usamos (típicamente para operaciones de actualización)
            return new ProductoEntity(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getStock()
            );
        } else {
            // Si el producto no tiene un ID (es nuevo), usamos el constructor que deja que la DB lo genere
            return new ProductoEntity(
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getStock()
            );
        }
    }
    public Producto toModel() {
        return new Producto(id, nombre, descripcion, precio, stock);
    }
}
