package Controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LogoutController", urlPatterns = {"/LogoutController"})
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener la sesión actual. El parámetro "false" asegura que no se cree 
        // una nueva sesión si es que ya no existía ninguna.
        HttpSession session = request.getSession(false);
        
        // 2. Si la sesión existe, la invalidamos (esto destruye el objeto "usuario" guardado)
        if (session != null) {
            session.invalidate();
        }
        
        // 3. Redirigir a la página de login con un mensaje de éxito opcional
        response.sendRedirect("login.html?msg=logout");
    }
}