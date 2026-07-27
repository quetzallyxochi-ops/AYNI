
package Controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Atrapamos los datos
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        
        // 2. Consultamos al DAO
        Modelo.UsuarioDAO dao = new Modelo.UsuarioDAO();
        Modelo.Usuario usuarioLogueado = dao.loginUsuario(correo, password);
        
        // 3. Evaluamos la respuesta
        if (usuarioLogueado != null) {
            
            // Creamos la sesión
            HttpSession sesion = request.getSession();
            sesion.setAttribute("usuario", usuarioLogueado);
            
            // 4. Redirección basada en los 3 tipos de usuario
            String tipo = usuarioLogueado.getTipoUsuario();
            
            if (tipo.equals("Institucional")) {
                // Estudiantes y personal de la escuela (Marketplace + Donaciones)
                response.sendRedirect("DashboardController");
                
            } else if (tipo.equals("Local")) {
                // Centros de donación y comunidad (Solo Donaciones)
                response.sendRedirect("inicio_locales.jsp"); 
                
            } else if (tipo.equals("Administrador")) {
                // Administrador del sistema (CRUD)
                response.sendRedirect("dashboard_admin.jsp"); 
            }
            
        } else {
            // Login fallido
            response.sendRedirect("login.html?error=credenciales");
        }
    }
}