package com.utp.hexagonal.Aplicacion.casodeuso;

import com.utp.hexagonal.Dominio.modelo.DetallePedido;
import com.utp.hexagonal.Dominio.modelo.Pedido;
import com.utp.hexagonal.Dominio.modelo.Producto;
import com.utp.hexagonal.Dominio.puertos.entrada.PedidoService;
import com.utp.hexagonal.Dominio.puertos.salida.PedidoPersistencePort;
import com.utp.hexagonal.Dominio.puertos.salida.ProductoRepositorio;
import com.utp.hexagonal.Dominio.excepciones.StockInsuficienteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate; // Importación necesaria
import java.time.LocalDateTime; // Importación necesaria
import java.time.LocalTime;    // Importación necesaria
import java.util.List;
import java.util.Optional;

@Service
public class PedidoCasoUso implements PedidoService {

    private final PedidoPersistencePort pedidoPersistencePort;
    private final ProductoRepositorio productoRepositorio;

    public PedidoCasoUso(PedidoPersistencePort pedidoPersistencePort, ProductoRepositorio productoRepositorio) {
        this.pedidoPersistencePort = pedidoPersistencePort;
        this.productoRepositorio = productoRepositorio;
    }

    @Override
    @Transactional // La transacción cubre tanto el guardado del pedido como la actualización del stock
    public Pedido guardarPedido(Pedido pedido) {
        // Primero, validar y reducir stock antes de guardar el pedido para evitar un pedido si el stock es insuficiente
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            for (DetallePedido detalle : pedido.getDetalles()) {

                Optional<Producto> productoOptional = productoRepositorio.obtenerPorId(detalle.getIdProducto());

                if (productoOptional.isPresent()) {
                    Producto producto = productoOptional.get();
                    int nuevoStock = producto.getStock() - detalle.getCantidad();
                    if (nuevoStock < 0) {
                        // Lanzar la excepción personalizada de stock insuficiente
                        throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre() + ". Stock disponible: " + producto.getStock());
                    }
                    // Actualizar el stock del objeto Producto en memoria
                    producto.setStock(nuevoStock);
                    // Actualizar el stock en la base de datos inmediatamente
                    productoRepositorio.actualizar(producto);
                } else {
                    // Si el producto no se encuentra, también es un error de negocio
                    throw new RuntimeException("Producto no encontrado para el detalle con ID: " + detalle.getIdProducto());
                }
            }
        }
        // Después de verificar y actualizar el stock de todos los productos, guardar el pedido
        Pedido pedidoGuardado = pedidoPersistencePort.save(pedido);
        return pedidoGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos() {
        return pedidoPersistencePort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoPersistencePort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoPersistencePort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public long countPedidosToday() {
        // Obtiene la fecha y hora de inicio y fin del día actual
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return pedidoPersistencePort.countPedidosByDateRange(startOfDay, endOfDay);
    }

    @Override
    @Transactional(readOnly = true)
    public double sumVentasToday() {
        // Obtiene la fecha y hora de inicio y fin del día actual
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return pedidoPersistencePort.sumTotalSalesByDateRange(startOfDay, endOfDay);
    }
}
