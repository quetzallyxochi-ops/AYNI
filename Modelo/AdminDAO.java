
package Modelo;
     import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;
public class AdminDAO {
     /**
     * Borrado Lógico: Cambia el estado del usuario a 'Suspendido'.
     * Retorna true si tuvo éxito.
     */
    public boolean bloquearUsuario(int idUsuario) {
        String sql = "UPDATE Usuarios SET estado_cuenta = 'Suspendido' WHERE id_usuario = ?";
        Connection con = ConexionBD.getConexion();
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al bloquear usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Resuelve un conflicto forzando un nuevo estado ('Completada' o 'Cancelada').
     */
    public boolean resolverConflicto(int idTransaccion, String estadoResolucion) {
        String sql = "UPDATE Transacciones SET estado_global = ?, fecha_cierre = NOW() "
                   + "WHERE id_transaccion = ? AND estado_global = 'Conflicto'";
        Connection con = ConexionBD.getConexion();
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, estadoResolucion);
            ps.setInt(2, idTransaccion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al resolver conflicto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Transacción segura: Registra la entrega y actualiza el estado de la donación.
     */
    public boolean registrarEntregaDonacion(int idDonacion, int idOrg, int idAdmin, String observaciones) {
        String sqlInsert = "INSERT INTO Transacciones_Donacion (id_donacion, id_organizacion, id_usuario_admin, observaciones) VALUES (?, ?, ?, ?)";
        String sqlUpdate = "UPDATE Donaciones SET estado_entrega = 'Entregado_a_Fundacion' WHERE id_donacion = ?";
        
        Connection con = ConexionBD.getConexion();
        
        try {
            // Desactivamos el auto-guardado para iniciar la transacción
            con.setAutoCommit(false);
            
            // 1. Insertamos el registro en la bitácora
            PreparedStatement psInsert = con.prepareStatement(sqlInsert);
            psInsert.setInt(1, idDonacion);
            psInsert.setInt(2, idOrg);
            psInsert.setInt(3, idAdmin);
            psInsert.setString(4, observaciones);
            psInsert.executeUpdate();
            
            // 2. Actualizamos el estado de la donación
            PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, idDonacion);
            psUpdate.executeUpdate();
            
            // Si ambas pasan sin errores, confirmamos los cambios (COMMIT)
            con.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("❌ Error en la transacción. Deshaciendo cambios (ROLLBACK): " + e.getMessage());
            try {
                // Si algo falla, deshacemos todo para evitar datos corruptos
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                // Siempre restauramos el autocommit a su estado normal
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Reporte: Obtiene un arreglo de strings con los datos del reporte de transacciones.
     * Nota: En un proyecto real, es mejor crear una clase 'ReporteTransaccionDTO' en lugar de String[].
     */
    public List<String[]> obtenerReporteTransaccionesMarketplace() {
        List<String[]> reporte = new ArrayList<>();
        String sql = "SELECT t.id_transaccion, p.titulo AS Producto, t.precio_final, t.estado_global, "
                   + "CONCAT(v.nombres, ' ', v.apellidos) AS Vendedor, "
                   + "CONCAT(c.nombres, ' ', c.apellidos) AS Comprador "
                   + "FROM Transacciones t "
                   + "INNER JOIN Productos p ON t.id_producto = p.id_producto "
                   + "INNER JOIN Usuarios v ON t.id_vendedor = v.id_usuario "
                   + "INNER JOIN Usuarios c ON t.id_comprador = c.id_usuario "
                   + "WHERE t.estado_global = 'Completada' ORDER BY t.fecha_cierre DESC";
        
        Connection con = ConexionBD.getConexion();
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String[] fila = new String[6];
                fila[0] = String.valueOf(rs.getInt("id_transaccion"));
                fila[1] = rs.getString("Producto");
                fila[2] = String.valueOf(rs.getDouble("precio_final"));
                fila[3] = rs.getString("estado_global");
                fila[4] = rs.getString("Vendedor");
                fila[5] = rs.getString("Comprador");
                reporte.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al generar reporte: " + e.getMessage());
        }
        
        return reporte;
    }
}
