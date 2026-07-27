
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    
    // Credenciales de la base de datos (ajusten esto según su servidor local)
    private static final String URL = "jdbc:mysql://localhost:3306/EcoRed_UPA?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    // Instancia única de la conexión
    private static Connection conexion = null;
    
    // Constructor privado para evitar que instancien la clase con "new"
    private ConexionBD() {}
    
    // Método para obtener la conexión
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Registrar el driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Establecer la conexión
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión exitosa a la BD EcoRed_UPA");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró el driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error en la conexión a la Base de Datos.");
            e.printStackTrace();
        }
        return conexion;
    }
    
    // Método para cerrar la conexión cuando termine el proceso
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔌 Conexión cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
}
