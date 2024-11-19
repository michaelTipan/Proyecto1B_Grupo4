package Documentos;

import Modelo.Detalle;
import Modelo.Venta;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.util.List;

public class FacturaPDF {

    public static void generarFacturaPDF(Venta venta, List<Detalle> detalles, double subtotal, double impuesto, double total, String rutaArchivo) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
            document.open();

            // Colores y fuentes
            BaseColor azulOscuro = new BaseColor(0, 51, 102);   // Azul oscuro
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, azulOscuro);
            Font subTituloFontBlanco = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            Font whiteFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);

            // Encabezado con logo, número de factura y fecha
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{2, 1});

            PdfPCell logoCell = new PdfPCell(new Phrase("MINIMARKET\nG4", tituloFont));
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(logoCell);

            PdfPCell facturaInfoCell = new PdfPCell();
            facturaInfoCell.setBorder(Rectangle.NO_BORDER);
            facturaInfoCell.addElement(new Paragraph("FACTURA", tituloFont));
            facturaInfoCell.addElement(new Paragraph("N° " + String.format("%05d", venta.getId()), normalFont));

            // Fecha con título en negrilla y contenido normal
            Paragraph fechaParagraph = new Paragraph();
            fechaParagraph.add(new Chunk("Fecha: ", boldFont));
            fechaParagraph.add(new Chunk(venta.getFecha(), normalFont));
            facturaInfoCell.addElement(fechaParagraph);

            facturaInfoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(facturaInfoCell);

            document.add(headerTable);
            document.add(new Paragraph(" ")); // Espacio

            // Título de "Información de Contacto"
            PdfPTable infoTitleTable = new PdfPTable(1);
            infoTitleTable.setWidthPercentage(100);

            PdfPCell tituloCell = new PdfPCell(new Phrase("INFORMACIÓN DE CONTACTO", subTituloFontBlanco));
            tituloCell.setBackgroundColor(azulOscuro);
            tituloCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tituloCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tituloCell.setPadding(10);
            tituloCell.setBorder(Rectangle.BOX);
            infoTitleTable.addCell(tituloCell);

            document.add(infoTitleTable);

            // Tabla de información de contacto (2x2)
            PdfPTable infoTable = new PdfPTable(2); // Dos columnas
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10f);

            // Fila 1: CI/RUC / Nombre y Apellido
            infoTable.addCell(crearCeldaConTexto("CI/RUC:", venta.getCliente().getDni(), boldFont, normalFont));
            infoTable.addCell(crearCeldaConTexto("Nombre y Apellido:", venta.getCliente().getNombre(), boldFont, normalFont));

            // Fila 2: Teléfono / Dirección
            infoTable.addCell(crearCeldaConTexto("Teléfono:", venta.getCliente().getTelefono(), boldFont, normalFont));
            infoTable.addCell(crearCeldaConTexto("Dirección:", venta.getCliente().getDireccion(), boldFont, normalFont));

            document.add(infoTable);

            // Tabla de detalles de la venta
            PdfPTable detallesTable = new PdfPTable(4);
            detallesTable.setWidthPercentage(100);
            detallesTable.setWidths(new float[]{1, 3, 2, 2});

            // Encabezados con texto blanco y tamaño reducido
            detallesTable.addCell(crearCeldaConFondo("CANTIDAD", whiteFont, azulOscuro));
            detallesTable.addCell(crearCeldaConFondo("DESCRIPCIÓN", whiteFont, azulOscuro));
            detallesTable.addCell(crearCeldaConFondo("VALOR UNITARIO", whiteFont, azulOscuro));
            detallesTable.addCell(crearCeldaConFondo("IMPORTE", whiteFont, azulOscuro));

            // Detalles
            for (Detalle detalle : detalles) {
                detallesTable.addCell(new PdfPCell(new Phrase(String.valueOf(detalle.getCantidad()), normalFont)));
                detallesTable.addCell(new PdfPCell(new Phrase(detalle.getProducto().getNombre(), normalFont)));
                detallesTable.addCell(new PdfPCell(new Phrase(String.format("$%.2f", detalle.getProducto().getPrecio()), normalFont)));
                detallesTable.addCell(new PdfPCell(new Phrase(String.format("$%.2f", detalle.getSubtotal()), normalFont)));
            }

            document.add(detallesTable);

            // Resumen de totales
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(50);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            totalTable.addCell(crearCeldaConFondo("SUBTOTAL:", whiteFont, azulOscuro));
            totalTable.addCell(new PdfPCell(new Phrase(String.format("$%.2f", subtotal), normalFont)));

            totalTable.addCell(crearCeldaConFondo("IMPUESTO (12%):", whiteFont, azulOscuro));
            totalTable.addCell(new PdfPCell(new Phrase(String.format("$%.2f", impuesto), normalFont)));

            totalTable.addCell(crearCeldaConFondo("TOTAL:", whiteFont, azulOscuro));
            totalTable.addCell(new PdfPCell(new Phrase(String.format("$%.2f", total), boldFont)));

            document.add(totalTable);

            // Mensaje final
            Paragraph mensajeFinal = new Paragraph("Gracias por su compra.", boldFont);
            mensajeFinal.setAlignment(Element.ALIGN_CENTER);
            document.add(mensajeFinal);

            document.close();
            System.out.println("Factura generada correctamente en: " + rutaArchivo);
        } catch (Exception e) {
            System.err.println("Error al generar la factura en PDF: " + e.getMessage());
        }
    }

    private static PdfPCell crearCeldaConFondo(String texto, Font font, BaseColor fondo) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(fondo);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    private static PdfPCell crearCeldaConTexto(String titulo, String valor, Font tituloFont, Font valorFont) {
        PdfPCell cell = new PdfPCell();
        Paragraph contenido = new Paragraph();
        contenido.add(new Chunk(titulo, tituloFont));
        contenido.add(new Chunk(" " + valor, valorFont));
        cell.addElement(contenido);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }
}
