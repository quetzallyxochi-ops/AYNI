package Modelo;

import java.sql.Timestamp;

public class Transaccion {
    // Campos correspondientes a la tabla transacciones de la BD
    private int idTransaccion;
    private int idProducto;
    private int idComprador;
    private int idVendedor;
    private String confirmaComprador;
    private String confirmaVendedor;
    private String estadoGlobal;
    private Timestamp fechaInicio;
    private Timestamp fechaCierre;
    
    // Campos adicionales (JOINs) para facilitar la vista en el JSP
    private String productoTitulo;
    private String productoImagen;
    private String nombreComprador;
    private String nombreVendedor;

    public Transaccion() {
    }

    // --- GETTERS Y SETTERS ---

    public int getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(int idTransaccion) { this.idTransaccion = idTransaccion; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getIdComprador() { return idComprador; }
    public void setIdComprador(int idComprador) { this.idComprador = idComprador; }

    public int getIdVendedor() { return idVendedor; }
    public void setIdVendedor(int idVendedor) { this.idVendedor = idVendedor; }

    public String getConfirmaComprador() { return confirmaComprador; }
    public void setConfirmaComprador(String confirmaComprador) { this.confirmaComprador = confirmaComprador; }

    public String getConfirmaVendedor() { return confirmaVendedor; }
    public void setConfirmaVendedor(String confirmaVendedor) { this.confirmaVendedor = confirmaVendedor; }

    public String getEstadoGlobal() { return estadoGlobal; }
    public void setEstadoGlobal(String estadoGlobal) { this.estadoGlobal = estadoGlobal; }

    public Timestamp getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Timestamp fechaInicio) { this.fechaInicio = fechaInicio; }

    public Timestamp getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(Timestamp fechaCierre) { this.fechaCierre = fechaCierre; }

    // Getters y Setters de los campos adicionales
    public String getProductoTitulo() { return productoTitulo; }
    public void setProductoTitulo(String productoTitulo) { this.productoTitulo = productoTitulo; }

    public String getProductoImagen() { return productoImagen; }
    public void setProductoImagen(String productoImagen) { this.productoImagen = productoImagen; }

    public String getNombreComprador() { return nombreComprador; }
    public void setNombreComprador(String nombreComprador) { this.nombreComprador = nombreComprador; }

    public String getNombreVendedor() { return nombreVendedor; }
    public void setNombreVendedor(String nombreVendedor) { this.nombreVendedor = nombreVendedor; }
}