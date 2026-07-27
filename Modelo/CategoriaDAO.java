package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    /**
     * Obtiene todas las categorías registradas en la base de datos.
     */
    public List<Categoria> obtenerTodas() {
        List<Categoria> lista = new ArrayList<>();
        // Consultamos los campos exactos de tu tabla
        String sql = "SELECT id_categoria, nombre, descripcion FROM categorias ORDER BY nombre ASC";

        Connection con = ConexionBD.getConexion();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener categorías: " + e.getMessage());
        } finally {
            // Cerramos la conexión para evitar saturar la base de datos
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }

        return lista;
    }
}