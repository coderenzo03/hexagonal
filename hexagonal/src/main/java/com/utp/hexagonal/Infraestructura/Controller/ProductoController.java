package com.utp.hexagonal.Infraestructura.Controller;

import com.utp.hexagonal.Dominio.modelo.Producto;
import com.utp.hexagonal.Dominio.puertos.entrada.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRABAJADOR')")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRABAJADOR')")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        System.out.println("📦 Creando producto: " + producto.getNombre());
        Producto nuevo = productoService.guardarProducto(producto);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping("/kpi/total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTotalProductos() {
        return ResponseEntity.ok(productoService.countAllProducts());
    }

    @GetMapping("/kpi/stock-bajo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getStockBajo(@RequestParam(defaultValue = "5") int umbral) {
        return ResponseEntity.ok(productoService.countProductsWithStockLessThan(umbral));
    }
}
