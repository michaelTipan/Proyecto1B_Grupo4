package Servicios;

import Modelo.Detalle;

import java.util.List;

public class ServiciosVenta {

    // Método centralizado para calcular el subtotal
    public double calcularSubtotal(Detalle detalle) {
        return detalle.getCantidad() * detalle.getProducto().getPrecio();
    }

    // Método centralizado para calcular el total
    public double calcularTotal(List<Detalle> detalles) {
        return detalles.stream().mapToDouble(this::calcularSubtotal).sum();
    }

    // Método centralizado para calcular impuestos
    public double calcularImpuesto(double subtotal, double tasaImpuesto) {
        return subtotal * tasaImpuesto;
    }

    // Método centralizado para calcular el total con impuestos
    public double calcularTotalConImpuesto(List<Detalle> detalles, double tasaImpuesto) {
        double subtotal = calcularTotal(detalles);
        double impuesto = calcularImpuesto(subtotal, tasaImpuesto);
        return subtotal + impuesto;
    }

    // Método para obtener subtotales, impuestos y totales directamente
    public double[] obtenerTotales(List<Detalle> detalles, double tasaImpuesto) {
        double subtotal = calcularTotal(detalles);
        double impuesto = calcularImpuesto(subtotal, tasaImpuesto);
        double total = subtotal + impuesto;
        return new double[]{subtotal, impuesto, total};
    }

    // Método para modificar la cantidad de un producto en los detalles
    public List<Detalle> actualizarCantidad(List<Detalle> detalles, String codigoProducto, int nuevaCantidad) {
        for (Detalle detalle : detalles) {
            if (detalle.getProducto().getCodigo().equals(codigoProducto)) {
                detalle.setCantidad(nuevaCantidad); // Actualiza la cantidad
                detalle.setSubtotal(calcularSubtotal(detalle)); // Recalcula el subtotal
            }
        }
        return detalles;
    }

    // Método para eliminar un detalle por código de producto
    public List<Detalle> eliminarDetallePorCodigo(List<Detalle> detalles, String codigoProducto) {
        detalles.removeIf(detalle -> detalle.getProducto().getCodigo().equals(codigoProducto));
        return detalles;
    }
}
