package com.utp.hexagonal.Aplicacion.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.utp.hexagonal.Dominio.modelo.DetallePedido;
import com.utp.hexagonal.Dominio.modelo.Pedido;
import java.io.ByteArrayOutputStream;

public class GeneradorPDF {

    public static ByteArrayOutputStream generarTicket(Pedido pedido) {
        Document documento = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(documento, baos);
            documento.open();

            documento.add(new Paragraph(" TIENDITA VIRTUAL"));
            documento.add(new Paragraph("Fecha: " + pedido.getFechaPedido()));
            documento.add(new Paragraph("Cliente: " + pedido.getUsuarioEmail()));
            documento.add(new Paragraph("----------------------------------------------------"));

            for (DetallePedido d : pedido.getDetalles()) {
                documento.add(new Paragraph(
                        d.getCantidad() + " x " + d.getSabor() + " - S/. " + d.getPrecio()
                ));
            }

            double total = pedido.getDetalles().stream()
                    .mapToDouble(d -> d.getCantidad() * d.getPrecio()).sum();

            documento.add(new Paragraph("----------------------------------------------------"));
            documento.add(new Paragraph("Total: S/. " + total));

            documento.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos;
    }
}
