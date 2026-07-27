
package Modelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class UsuarioDAO {
 
    /**
     * Método para registrar un nuevo usuario en la BD.
     * Retorna true si se insertó correctamente, false si hubo un error.
     */
    public boolean registrarUsuario(Usuario usr) {
        // La consulta SQL con signos de interrogación para evitar Inyección SQL (Seguridad)
        String sql = "INSERT INTO Usuarios (tipo_usuario, nombres, apellidos, correo, password_hash, "
                   + "matricula_upa, identificacion_local, perfil_anonimo, alias_anonimo, estado_cuenta) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection con = ConexionBD.getConexion();
        
        try {
            // Preparamos la consulta
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usr.getTipoUsuario());
            ps.setString(2, usr.getNombres());
            ps.setString(3, usr.getApellidos());
            ps.setString(4, usr.getCorreo());
            ps.setString(5, usr.getPasswordHash()); // En un proyecto real, esto debe estar encriptado antes de llegar aquí
            ps.setString(6, usr.getMatriculaUpa());
            ps.setString(7, usr.getIdentificacionLocal());
            ps.setBoolean(8, usr.isPerfilAnonimo());
            ps.setString(9, usr.getAliasAnonimo());
            ps.setString(10, "Activo"); // Por defecto, al registrarse, la cuenta está activa

            // Ejecutamos la inserción
            int filasAfectadas = ps.executeUpdate();
            
            // Si filasAfectadas es mayor a 0, significa que se guardó con éxito
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para validar el inicio de sesión.
     * Retorna el objeto Usuario con todos sus datos si el login es correcto, o null si falla.
     */
    public Usuario loginUsuario(String correo, String password) {
        String sql = "SELECT * FROM Usuarios WHERE correo = ? AND password_hash = ? AND estado_cuenta = 'Activo'";
        Usuario usr = null;
        Connection con = ConexionBD.getConexion();
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            // Si rs.next() es true, significa que encontró un usuario con esas credenciales
            if (rs.next()) {
                usr = new Usuario();
                usr.setIdUsuario(rs.getInt("id_usuario"));
                usr.setTipoUsuario(rs.getString("tipo_usuario"));
                usr.setNombres(rs.getString("nombres"));
                usr.setApellidos(rs.getString("apellidos"));
                usr.setCorreo(rs.getString("correo"));
                usr.setPasswordHash(rs.getString("password_hash"));
                usr.setMatriculaUpa(rs.getString("matricula_upa"));
                usr.setIdentificacionLocal(rs.getString("identificacion_local"));
                usr.setPerfilAnonimo(rs.getBoolean("perfil_anonimo"));
                usr.setAliasAnonimo(rs.getString("alias_anonimo"));
                usr.setEstadoCuenta(rs.getString("estado_cuenta"));
                usr.setImagen(rs.getString("imagen"));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error en el login: " + e.getMessage());
        }
        
        return usr; // Retornará null si no encontró coincidencias
    }
    
    /**
     * Método para actualizar la contraseña del usuario.
     * Retorna true si se actualizó correctamente.
     */
    public boolean actualizarContrasena(int idUsuario, String passwordNueva) {
        String sql = "UPDATE usuarios SET password_hash = ? WHERE id_usuario = ?";
        Connection con = ConexionBD.getConexion();
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, passwordNueva);
            ps.setInt(2, idUsuario);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar contraseña: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para actualizar la información del perfil, incluyendo la imagen.
     */
    public boolean actualizarPerfil(Usuario usr) {
        String sql = "UPDATE usuarios SET nombres = ?, apellidos = ?, correo = ?, alias_anonimo = ?, imagen = ? WHERE id_usuario = ?";
        Connection con = ConexionBD.getConexion();
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usr.getNombres());
            ps.setString(2, usr.getApellidos());
            ps.setString(3, usr.getCorreo());
            ps.setString(4, usr.getAliasAnonimo());
            ps.setString(5, usr.getImagen());
            ps.setInt(6, usr.getIdUsuario());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar perfil: " + e.getMessage());
            return false;
        }
    }
      
}
