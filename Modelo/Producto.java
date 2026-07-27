package Modelo;

import java.sql.Date;

public class Producto {
    private int idProducto;
    private int idVendedor;
    private String titulo;
    private String descripcion;
    private double precio;
    private int idCategoria;
    private String estadoVenta; // 'Disponible', 'Reservado', 'Vendido'
    private String imagen; // Ruta de la imagen
    private String nombreCategoria;
    private String estadoModeracion;
    private Date fechaPublicacion;

    public Producto() {}

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getIdVendedor() { return idVendedor; }
    public void setIdVendedor(int idVendedor) { this.idVendedor = idVendedor; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getEstadoVenta() { return estadoVenta; }
    public void setEstadoVenta(String estadoVenta) { this.estadoVenta = estadoVenta; }
    
    public String getEstadoModeracion() { return estadoModeracion; }
    public void setEstadoModeracion(String estadoVenta) { this.estadoModeracion = estadoModeracion; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    
    public String getNombreCategoria() {   return nombreCategoria;  }
    public void setNombreCategoria(String nombreCategoria) {   this.nombreCategoria = nombreCategoria;  }
    
    public Date getFechaPublicacion() { return fechaPublicacion;  }

    public void setFechaPublicacion(Date fechaPublicacion) {   this.fechaPublicacion = fechaPublicacion;  }
}