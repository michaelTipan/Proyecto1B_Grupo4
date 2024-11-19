package Modelo;

public class Venta {
    private int id;
    private Cliente cliente;
    private String fecha;
    private double total;

    // Constructor para crear una nueva venta (sin ID)
    public Venta(Cliente cliente, String fecha, double total) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
    }

    // Constructor completo (con ID)
    public Venta(int id, Cliente cliente, String fecha, double total) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
