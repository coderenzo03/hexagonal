package com.utp.hexagonal.Infraestructura.Controller;

import com.utp.hexagonal.Dominio.modelo.DetallePedido; // Necesario para el mapeo
import com.utp.hexagonal.Dominio.modelo.Pedido;
import com.utp.hexagonal.Dominio.puertos.entrada.PedidoService;
import com.utp.hexagonal.Infraestructura.dto.Item; // Tu DTO 'Item' del frontend
import com.utp.hexagonal.Infraestructura.utils.TicketGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime; // Necesario para establecer la fecha del pedido
import java.util.List;
import java.util.stream.Collectors; // Necesario para el mapeo de listas

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService; // Esto inyectará PedidoCasoUso

    // Inyección de dependencia por constructor (preferida en Spring)
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    @PostMapping
    @PreAuthorize("hasRole('TRABAJADOR')") // Solo usuarios con rol 'TRABAJADOR' pueden acceder
    public ResponseEntity<Pedido> crearPedido(@RequestBody List<Item> items, Authentication authentication) {
        // Obtener el email del usuario autenticado desde el objeto Authentication
        String emailDelUsuario = authentication.getName();

        // 1. Crear un objeto Pedido del Dominio y establecer sus propiedades principales
        Pedido pedidoDominio = new Pedido();
        pedidoDominio.setUsuarioEmail(emailDelUsuario); // Asignar el email del usuario autenticado
        pedidoDominio.setFechaPedido(LocalDateTime.now()); // Establecer la fecha actual del pedido

        // 2. Mapear la lista de Items (DTOs) a una lista de DetallePedido (modelos de Dominio)
        List<DetallePedido> detallesDominio = items.stream()
                .map(item -> {
                    // Crear una nueva instancia de DetallePedido para cada Item
                    DetallePedido detalle = new DetallePedido();
                    // Copiar las propiedades del Item (DTO) al DetallePedido (Dominio)
                    detalle.setIdProducto(item.getIdProducto()); // ¡NUEVO: Mapear el ID del producto!
                    detalle.setSabor(item.getSabor());
                    detalle.setCantidad(item.getCantidad());
                    detalle.setPrecio(item.getPrecio());
                    return detalle;
                })
                .collect(Collectors.toList()); // Recolectar en una lista

        // 3. Asignar la lista de DetallePedido mapeados al objeto Pedido del Dominio
        pedidoDominio.setDetalles(detallesDominio);

        // 4. Llamar al servicio de pedido (Caso de Uso) con el objeto Pedido del Dominio
        // El servicio de pedido (PedidoCasoUso) se encargará de la lógica de negocio y la persistencia
        Pedido nuevoPedidoGuardado = pedidoService.guardarPedido(pedidoDominio);

        // 5. Devolver una respuesta HTTP 200 OK con el pedido guardado (modelo de Dominio)
        // Opcionalmente, podrías mapear 'nuevoPedidoGuardado' a un DTO de respuesta específico si el frontend lo requiere diferente.
        return ResponseEntity.ok(nuevoPedidoGuardado);
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long id) {
        // Usa Optional para manejar el caso de que el pedido no sea encontrado
        return pedidoService.obtenerPedidoPorId(id)
                .map(ResponseEntity::ok) // Si se encuentra, devuelve 200 OK con el pedido
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, devuelve 404 Not Found
    }

    @GetMapping("/{id}/ticket")
    public ResponseEntity<byte[]> generarTicketPDF(@PathVariable Long id) {
        // Intenta obtener el pedido por su ID
        return pedidoService.obtenerPedidoPorId(id)
                .map(pedido -> {
                    // Si el pedido se encuentra, genera el PDF
                    byte[] pdfBytes = TicketGenerator.generarPDF(pedido);

                    // Configura los encabezados HTTP para la descarga del PDF
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF); // Tipo de contenido: PDF
                    headers.setContentDispositionFormData("attachment", "pedido_" + id + ".pdf"); // Nombre de archivo para la descarga

                    // Devuelve la respuesta con el PDF como cuerpo y los encabezados
                    return ResponseEntity
                            .ok()
                            .headers(headers)
                            .body(pdfBytes);
                })
                .orElse(ResponseEntity.notFound().build()); // Si el pedido no se encuentra, devuelve 404 Not Found
    }
    @GetMapping("/historial") // Nuevo endpoint para el historial
    @PreAuthorize("hasAnyRole('TRABAJADOR', 'ADMIN')") // Proteger el acceso si es necesario
    public List<Pedido> obtenerHistorialPedidos() {
        return pedidoService.obtenerTodosLosPedidos(); // Llama al nuevo método del caso de uso
    }
    @GetMapping("/kpi/ventas-hoy")
    @PreAuthorize("hasRole('ADMIN')") // Solo el rol ADMIN puede acceder a este KPI
    public ResponseEntity<Double> getVentasHoy() {
        return ResponseEntity.ok(pedidoService.sumVentasToday());
    }
    @GetMapping("/kpi/contar-hoy")
    @PreAuthorize("hasRole('ADMIN')") // Solo el rol ADMIN puede acceder a este KPI
    public ResponseEntity<Long> getContarPedidosHoy() {
        return ResponseEntity.ok(pedidoService.countPedidosToday());
    }
}
