package com.utp.hexagonal.Infraestructura.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.utp.hexagonal.Dominio.modelo.DetallePedido;
import com.utp.hexagonal.Dominio.modelo.Pedido;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List; // Importar List
import java.util.Objects; // Importar Objects para chequeo de nulos

public class TicketGenerator {

    public static byte[] generarPDF(Pedido pedido) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document documento = new Document();

        try {
            PdfWriter.getInstance(documento, out);
            documento.open();

            // Configuración de fuentes
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);


            // Título
            documento.add(new Paragraph("ICE FRIO HIELO", titleFont));
            documento.add(new Paragraph("COMPROBANTE DE PEDIDO\n\n", headerFont));

            // Info del usuario
            // Asegúrate de que pedido y sus campos no sean nulos antes de acceder
            documento.add(new Paragraph("Cliente: " + (pedido != null && pedido.getUsuarioEmail() != null ? pedido.getUsuarioEmail() : "N/A"), normalFont));
            String fechaPedidoStr = "N/A";
            if (pedido != null && pedido.getFechaPedido() != null) {
                fechaPedidoStr = pedido.getFechaPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
            documento.add(new Paragraph("Fecha: " + fechaPedidoStr, normalFont));
            documento.add(new Paragraph("\n"));

            // Tabla de productos
            PdfPTable tabla = new PdfPTable(4); // 4 columnas: Sabor, Cantidad, Precio, Subtotal
            tabla.setWidthPercentage(100); // Tabla ocupa el 100% del ancho disponible
            tabla.setSpacingBefore(10f); // Espacio antes de la tabla
            tabla.setSpacingAfter(10f); // Espacio después de la tabla

            // Encabezados de la tabla
            tabla.addCell(new Phrase("Sabor", headerFont));
            tabla.addCell(new Phrase("Cantidad", headerFont));
            tabla.addCell(new Phrase("Precio Unitario", headerFont)); // Ajustado el nombre
            tabla.addCell(new Phrase("Subtotal", headerFont));

            double total = 0;
            // Asegúrate de que la lista de detalles no sea nula o vacía
            List<DetallePedido> detalles = pedido != null ? pedido.getDetalles() : null;

            if (detalles != null && !detalles.isEmpty()) {
                for (DetallePedido detalle : detalles) {
                    if (detalle != null) { // Asegúrate de que cada detalle no sea nulo
                        tabla.addCell(new Phrase(Objects.toString(detalle.getSabor(), "N/A"), normalFont));
                        tabla.addCell(new Phrase(String.valueOf(detalle.getCantidad()), normalFont));
                        tabla.addCell(new Phrase("S/ " + String.format("%.2f", detalle.getPrecio()), normalFont)); // Formato de moneda
                        double subtotal = detalle.getCantidad() * detalle.getPrecio();
                        tabla.addCell(new Phrase("S/ " + String.format("%.2f", subtotal), normalFont)); // Formato de moneda
                        total += subtotal;
                    }
                }
            } else {
                // Añadir una fila si no hay detalles
                PdfPCell noDetailsCell = new PdfPCell(new Phrase("No hay detalles para este pedido.", normalFont));
                noDetailsCell.setColspan(4); // Ocupa todas las columnas
                noDetailsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(noDetailsCell);
            }

            documento.add(tabla);

            // Total
            documento.add(new Paragraph("\nTotal: S/ " + String.format("%.2f", total), totalFont)); // Formato de moneda

            documento.close();
        } catch (Exception e) {
            // Imprime la traza completa de la pila para depuración
            e.printStackTrace();
            // Opcional: Re-lanzar una excepción más específica o loguear con un logger profesional
            // throw new RuntimeException("Error al generar el PDF del ticket", e);
        } finally {
            // Asegúrate de cerrar el documento si ocurrió un error antes de document.close()
            if (documento.isOpen()) {
                documento.close();
            }
        }

        return out.toByteArray();
    }
}
