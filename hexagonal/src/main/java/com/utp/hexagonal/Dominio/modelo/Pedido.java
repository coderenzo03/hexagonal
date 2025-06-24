package com.utp.hexagonal.Dominio.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sabor;
    private int cantidad;

    @Column(name = "usuario_email")
    private String usuarioEmail;
    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @ElementCollection
    @CollectionTable(name = "pedido_detalle_items",
            joinColumns = @JoinColumn(name = "pedido_id"))
    private List<DetallePedido> detalles;

}