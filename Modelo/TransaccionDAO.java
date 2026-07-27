package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAO {

    /**
     * Lista 2: Obtiene los productos que el usuario VENDIÓ y están "en proceso" de entrega.
     */
    public List<Transaccion> obtenerVentasEnProceso(int idVendedor) {
        List<Transaccion> lista = new ArrayList<>();
        // Hacemos JOIN con productos (para la info visual) y con usuarios (para saber quién compró)
        String sql = "SELECT t.*, p.titulo, p.imagen, u.nombres, u.apellidos " +
                     "FROM transacciones t " +
                     "INNER JOIN productos p ON t.id_producto = p.id_producto " +
                     "INNER JOIN usuarios u ON t.id_comprador = u.id_usuario " +
                     "WHERE t.id_vendedor = ? AND t.estado_global IN ('En_Proceso', 'Conflicto')";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idVendedor);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaccion t = new Transaccion();
                    t.setIdTransaccion(rs.getInt("id_transaccion"));
                    t.setIdProducto(rs.getInt("id_producto"));
                    t.setIdComprador(rs.getInt("id_comprador"));
                    t.setIdVendedor(rs.getInt("id_vendedor"));
                    t.setEstadoGlobal(rs.getString("estado_global"));
                    
                    // Info extra de los JOINs
                    t.setProductoTitulo(rs.getString("titulo"));
                    t.setProductoImagen(rs.getString("imagen"));
                    t.setNombreComprador(rs.getString("nombres") + " " + rs.getString("apellidos"));
                    
                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener ventas en proceso: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lista 3: Obtiene los productos que el usuario COMPRÓ y están "en proceso" de recoger.
     */
    public List<Transaccion> obtenerComprasEnProceso(int idComprador) {
        List<Transaccion> lista = new ArrayList<>();
        // Hacemos JOIN con productos (para la info visual) y con usuarios (para saber quién vendió)
        String sql = "SELECT t.*, p.titulo, p.imagen, u.nombres, u.apellidos " +
                     "FROM transacciones t " +
                     "INNER JOIN productos p ON t.id_producto = p.id_producto " +
                     "INNER JOIN usuarios u ON t.id_vendedor = u.id_usuario " +
                     "WHERE t.id_comprador = ? AND t.estado_global IN ('En_Proceso', 'Conflicto')";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idComprador);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaccion t = new Transaccion();
                    t.setIdTransaccion(rs.getInt("id_transaccion"));
                    t.setIdProducto(rs.getInt("id_producto"));
                    t.setIdComprador(rs.getInt("id_comprador"));
                    t.setIdVendedor(rs.getInt("id_vendedor"));
                    t.setEstadoGlobal(rs.getString("estado_global"));
                    
                    // Info extra de los JOINs
                    t.setProductoTitulo(rs.getString("titulo"));
                    t.setProductoImagen(rs.getString("imagen"));
                    t.setNombreVendedor(rs.getString("nombres") + " " + rs.getString("apellidos"));
                    
                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener compras en proceso: " + e.getMessage());
        }
        return lista;
    }
    
    // =====================================================================
    // MÉTODOS PARA ACTUALIZAR EL ESTADO DE LA TRANSACCIÓN
    // =====================================================================

    public void confirmarVentaExitosa(int idTransaccion) {
        actualizarConfirmacion(idTransaccion, "confirma_vendedor", "Exitosa");
    }

    public void cancelarVenta(int idTransaccion) {
        actualizarConfirmacion(idTransaccion, "confirma_vendedor", "Cancelada");
    }

    public void confirmarCompraExitosa(int idTransaccion) {
        actualizarConfirmacion(idTransaccion, "confirma_comprador", "Exitosa");
    }

    public void cancelarCompra(int idTransaccion) {
        actualizarConfirmacion(idTransaccion, "confirma_comprador", "Cancelada");
    }

    /**
     * Método privado que registra la confirmación de un lado.
     * ¡El Trigger en la base de datos se encarga del resto!
     */
    private void actualizarConfirmacion(int idTransaccion, String columna, String nuevoEstado) {
        String sql = "UPDATE transacciones SET " + columna + " = ? WHERE id_transaccion = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idTransaccion);
            ps.executeUpdate(); 
            // Al hacer executeUpdate(), MySQL dispara el trigger automáticamente.
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar confirmación: " + e.getMessage());
        }
    }
    
}