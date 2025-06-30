// src/main/java/com/utp/hexagonal/Infraestructura.Controller.ProductoController
package com.utp.hexagonal.Infraestructura.Controller;

import com.utp.hexagonal.Dominio.modelo.Producto;
import com.utp.hexagonal.Dominio.puertos.entrada.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger; // Importar la clase Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    // Crear una instancia de Logger para esta clase
    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRABAJADOR')")
    public ResponseEntity<List<Producto>> listarProductos() {
        logger.info("Solicitud para listar todos los productos."); // Usar logger
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRABAJADOR')")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        logger.info("Solicitud para obtener producto con ID: {}", id); // Usar logger con placeholder
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            logger.warn("Producto con ID {} no encontrado.", id); // Log de advertencia
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        logger.info("Solicitud para actualizar producto con ID: {}", id);
        Producto actualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        logger.info("Solicitud para eliminar producto con ID: {}", id);
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        logger.info("📦 Creando producto: {}", producto.getNombre()); // Usar logger
        Producto nuevo = productoService.guardarProducto(producto);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping("/kpi/total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTotalProductos() {
        logger.info("Solicitud para obtener el total de productos.");
        return ResponseEntity.ok(productoService.countAllProducts());
    }

    @GetMapping("/kpi/stock-bajo")
    @PreAuthorize("hasAnyRole('ADMIN')") // Asumiendo que trabajadores también podrían ver esto
    public ResponseEntity<Long> getStockBajo(@RequestParam(defaultValue = "5") int umbral) {
        logger.info("Solicitud para obtener productos con stock bajo (umbral: {}).", umbral);
        return ResponseEntity.ok(productoService.countProductsWithStockLessThan(umbral));
    }
}
    