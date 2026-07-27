package Controlador;

import Modelo.Usuario;
import Modelo.UsuarioDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CambiarContrasenaController", urlPatterns = {"/CambiarContrasenaController"})
public class CambiarContrasenaController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");

        if (usuarioActivo == null) {
            response.sendRedirect("login.html");
            return;
        }

        String passActual = request.getParameter("passActual");
        String passNueva = request.getParameter("passNueva");
        String passConfirmar = request.getParameter("passConfirmar");

        // Validaciones requeridas
        if (usuarioActivo.getPasswordHash().equals(passActual) && passNueva.equals(passConfirmar)) {
            UsuarioDAO dao = new UsuarioDAO();
            
            if (dao.actualizarContrasena(usuarioActivo.getIdUsuario(), passNueva)) {
                // Actualizamos el objeto en sesión para mantener consistencia
                usuarioActivo.setPasswordHash(passNueva);
                session.setAttribute("usuario", usuarioActivo);
                
                // Redirige al inicio con mensaje de éxito
                response.sendRedirect("DashboardController?msg=cambio_exitoso");
            } else {
                response.sendRedirect("cambiar_contrasena.jsp?error=db");
            }
        } else {
            response.sendRedirect("cambiar_contrasena.jsp?error=mismatch");
        }
    }
}