package com.utp.hexagonal.Infraestructura.Mapper;

import com.utp.hexagonal.Dominio.modelo.Producto;
import com.utp.hexagonal.Infraestructura.Entity.ProductoEntity;

public class ProductoMapper {

    public static ProductoEntity toEntity(Producto producto) {
        return new ProductoEntity(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }

    public static Producto toDomain(ProductoEntity entity) {
        return new Producto(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getPrecio(),
                entity.getStock()
        );
    }
}
