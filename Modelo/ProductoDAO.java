package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    /**
     * Obtiene los productos disponibles. Permite filtrar por categoría y/o buscar por texto.
     */
   public List<Producto> obtenerProductosDisponibles(String busqueda, String categoria, Double min, Double max) {
    List<Producto> lista = new ArrayList<>();
    
    // Consulta base
    String sql = "SELECT p.*, c.nombre AS nombre_categoria "
               + "FROM productos p "
               + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
               + "WHERE p.estado_venta = 'Disponible' AND p.estado_moderacion = 'Aprobado_IA'";
    
    // Filtros dinámicos
    if (categoria != null && !categoria.isEmpty()) {
        sql += " AND c.nombre = ?"; 
    }
    if (busqueda != null && !busqueda.trim().isEmpty()) {
        sql += " AND (p.titulo LIKE ? OR p.descripcion LIKE ?)";
    }
    if (min != null) {
        sql += " AND p.precio >= ?";
    }
    if (max != null) {
        sql += " AND p.precio <= ?";
    }
    
    sql += " ORDER BY p.id_producto DESC";

    Connection con = ConexionBD.getConexion();

    try {
        PreparedStatement ps = con.prepareStatement(sql);
        int paramIndex = 1;
        
        // Asignar variables en el mismo orden que los agregamos al SQL
        if (categoria != null && !categoria.isEmpty()) {
            ps.setString(paramIndex++, categoria);
        }
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            String likeBusqueda = "%" + busqueda.trim() + "%";
            ps.setString(paramIndex++, likeBusqueda);
            ps.setString(paramIndex++, likeBusqueda);
        }
        if (min != null) {
            ps.setDouble(paramIndex++, min);
        }
        if (max != null) {
            ps.setDouble(paramIndex++, max);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("id_producto"));
            p.setIdVendedor(rs.getInt("id_vendedor"));
            p.setIdCategoria(rs.getInt("id_categoria")); 
            p.setTitulo(rs.getString("titulo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPrecio(rs.getDouble("precio"));
            p.setEstadoVenta(rs.getString("estado_venta"));
            p.setImagen(rs.getString("imagen"));
            p.setNombreCategoria(rs.getString("nombre_categoria")); 
            lista.add(p);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener productos: " + e.getMessage());
    }

    return lista;
}
    /**
     * Registra un nuevo producto en la base de datos.
     * Asigna automáticamente estado_moderacion = 'Pendiente' y estado_venta = 'Disponible'.
     */
    public boolean insertarProducto(Producto p) {
        // En tu documento se especifica que "estado de moderación, estado de venta, fecha" son automáticos
        String sql = "INSERT INTO Productos (id_vendedor, id_categoria, titulo, descripcion, precio, imagen, estado_moderacion, estado_venta, fecha_publicacion) "
           + "VALUES (?, ?, ?, ?, ?, ?, 'Pendiente', 'Disponible', CURDATE())";
        Connection con = ConexionBD.getConexion();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, p.getIdVendedor());
            ps.setInt(2, p.getIdCategoria());
            ps.setString(3, p.getTitulo());
            ps.setString(4, p.getDescripcion());
            ps.setDouble(5, p.getPrecio());
            ps.setString(6, p.getImagen());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar producto: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
    
    public boolean agregarAlCarrito(int idUsuario, int idProducto) {
        Connection con = ConexionBD.getConexion();
        boolean exito = false;

        try {
            // Desactivamos el autocommit para hacer una transacción segura
            con.setAutoCommit(false); 

            // 1. Insertar en la tabla carrito
            String sqlCarrito = "INSERT INTO carrito (id_usuario, id_producto, fecha_agregado) VALUES (?, ?, NOW())";
            PreparedStatement psCarrito = con.prepareStatement(sqlCarrito);
            psCarrito.setInt(1, idUsuario);
            psCarrito.setInt(2, idProducto);
            psCarrito.executeUpdate();

            // 2. Actualizar el estado del producto a Reservado
            String sqlProducto = "UPDATE productos SET estado_venta = 'Reservado' WHERE id_producto = ?";
            PreparedStatement psProducto = con.prepareStatement(sqlProducto);
            psProducto.setInt(1, idProducto);
            psProducto.executeUpdate();

            // Si ambas operaciones salen bien, confirmamos los cambios
            con.commit(); 
            exito = true;

        } catch (SQLException e) {
            System.err.println("❌ Error al agregar al carrito: " + e.getMessage());
            try {
                // Si hay error, deshacemos cualquier cambio a medias
                if (con != null) con.rollback(); 
            } catch (SQLException ex) {
                System.err.println("Error en el rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (con != null) con.setAutoCommit(true); // Devolver a su estado normal
            } catch (SQLException e) { }
        }

        return exito;
    }
    
    // Método para obtener un producto específico por su ID
    public Producto obtenerProductoPorId(int idProducto) {
        Producto p = null;
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setIdVendedor(rs.getInt("id_vendedor"));
                    p.setIdCategoria(rs.getInt("id_categoria"));
                    p.setTitulo(rs.getString("titulo"));
                    p.setImagen(rs.getString("imagen"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setEstadoModeracion(rs.getString("estado_moderacion"));
                    p.setEstadoVenta(rs.getString("estado_venta"));
                    p.setFechaPublicacion(rs.getDate("fecha_publicacion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
        }
        return p;
    }

    // Método para obtener los productos en el carrito de un usuario
    public List<Producto> obtenerProductosDelCarrito(int idUsuario) {
        List<Producto> lista = new ArrayList<>();
        // Hacemos un INNER JOIN para traer los datos del producto basándonos en la tabla carrito
        String sql = "SELECT p.* FROM productos p INNER JOIN carrito c ON p.id_producto = c.id_producto WHERE c.id_usuario = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setTitulo(rs.getString("titulo"));
                    p.setImagen(rs.getString("imagen"));
                    p.setPrecio(rs.getDouble("precio"));
                    // Agregamos el producto a la lista
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el carrito: " + e.getMessage());
        }
        return lista;
    }
    
    
    // Método para eliminar un artículo del carrito y volverlo 'Disponible'
    public boolean eliminarDelCarrito(int idUsuario, int idProducto) {
        Connection con = ConexionBD.getConexion();
        boolean exito = false;
        try {
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Borrar el registro del carrito
            String sqlBorrar = "DELETE FROM carrito WHERE id_usuario = ? AND id_producto = ?";
            PreparedStatement psBorrar = con.prepareStatement(sqlBorrar);
            psBorrar.setInt(1, idUsuario);
            psBorrar.setInt(2, idProducto);
            psBorrar.executeUpdate();

            // 2. Regresar el estado del producto a 'Disponible'
            String sqlUpdate = "UPDATE productos SET estado_venta = 'Disponible' WHERE id_producto = ?";
            PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, idProducto);
            psUpdate.executeUpdate();

            con.commit(); // Confirmar cambios
            exito = true;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar del carrito: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { }
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) { }
        }
        return exito;
    }

    // Método para procesar la compra de todos los productos en el carrito del usuario
    public boolean procesarCompraCarrito(int idUsuario) {
        Connection con = ConexionBD.getConexion();
        boolean exito = false;
        try {
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Obtener los productos del carrito (necesitamos id_producto y el id_vendedor para la transacción)
            String sqlSelect = "SELECT c.id_producto, p.id_vendedor FROM carrito c INNER JOIN productos p ON c.id_producto = p.id_producto WHERE c.id_usuario = ?";
            PreparedStatement psSelect = con.prepareStatement(sqlSelect);
            psSelect.setInt(1, idUsuario);
            ResultSet rs = psSelect.executeQuery();

            // Preparar las consultas de inserción y actualización
            String sqlInsertTransaccion = "INSERT INTO transacciones (id_producto, id_comprador, id_vendedor, estado_global, fecha_inicio) VALUES (?, ?, ?, 'En_Proceso', NOW())";
            PreparedStatement psInsert = con.prepareStatement(sqlInsertTransaccion);

            String sqlUpdateProducto = "UPDATE productos SET estado_venta = 'Vendido' WHERE id_producto = ?";
            PreparedStatement psUpdate = con.prepareStatement(sqlUpdateProducto);

            // 2. Recorrer cada producto y ejecutar cambios
            while (rs.next()) {
                int idProducto = rs.getInt("id_producto");
                int idVendedor = rs.getInt("id_vendedor");

                // Generar el registro en la tabla transacciones
                psInsert.setInt(1, idProducto);
                psInsert.setInt(2, idUsuario); // comprador = usuario de sesión actual
                psInsert.setInt(3, idVendedor);
                psInsert.executeUpdate();

                // Cambiar estado a 'Vendido' en la tabla productos
                psUpdate.setInt(1, idProducto);
                psUpdate.executeUpdate();
            }

            // 3. Vaciar completamente el carrito del usuario
            String sqlVaciar = "DELETE FROM carrito WHERE id_usuario = ?";
            PreparedStatement psVaciar = con.prepareStatement(sqlVaciar);
            psVaciar.setInt(1, idUsuario);
            psVaciar.executeUpdate();

            con.commit(); // Confirmar todos los cambios
            exito = true;
        } catch (SQLException e) {
            System.err.println("❌ Error al procesar la compra: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { }
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) { }
        }
        return exito;
    }
    
    // Método para obtener los productos activos de un usuario (para la pantalla Mis Productos)
    public List<Producto> obtenerMisProductosActivos(int idVendedor) {
        List<Producto> lista = new ArrayList<>();
        // Selecciona los productos que no se han vendido
        String sql = "SELECT * FROM productos WHERE id_vendedor = ? AND estado_venta = 'Disponible'";
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idVendedor);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setTitulo(rs.getString("titulo"));
                    p.setImagen(rs.getString("imagen"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setEstadoModeracion(rs.getString("estado_moderacion"));
                    p.setEstadoVenta(rs.getString("estado_venta"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener mis productos activos: " + e.getMessage());
        }
        return lista;
    }
    // Método para eliminar un producto
    public boolean eliminarProducto(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idProducto);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // Método para donar un producto (Cambia el estado a Expirado para accionar el trigger)
    public boolean donarProducto(int idProducto) {
        String sql = "UPDATE productos SET estado_venta = 'Expirado' WHERE id_producto = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idProducto);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al donar producto: " + e.getMessage());
            return false;
        }
    }
    
    // Método para actualizar un producto existente
    public boolean actualizarProducto(Producto p) {
        // Nota: Según la regla de negocio de tu formulario, al actualizar regresa a 'Pendiente'
        String sql = "UPDATE productos SET titulo = ?, descripcion = ?, precio = ?, id_categoria = ?, imagen = ?, estado_moderacion = 'Pendiente' WHERE id_producto = ?";
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getIdCategoria());
            ps.setString(5, p.getImagen());
            ps.setInt(6, p.getIdProducto());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
}