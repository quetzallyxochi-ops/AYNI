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

@WebServlet(name = "ConfirmarPassController", urlPatterns = {"/ConfirmarPassController"})
public class ConfirmarPassController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Validar sesión
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");
        
        if (usuarioActivo == null) {
            response.sendRedirect("login.html?error=no_sesion");
            return;
        }

        // 2. Obtener la contraseña ingresada en el formulario
        String passwordIngresada = request.getParameter("password");
        
        // 3. Validar la contraseña contra la base de datos
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        // Usamos el correo del usuario en sesión y la contraseña que acaba de ingresar, reciclando tu método
        Usuario validacion = usuarioDAO.loginUsuario(usuarioActivo.getCorreo(), passwordIngresada);
        
        // 4. Redirigir según el resultado
        if (validacion != null) {
            // Contraseña correcta: Redirigimos al controlador de Mis Productos
            response.sendRedirect("MisProductosController");
        } else {
            // Contraseña incorrecta: Devolvemos a la pantalla de confirmación con un mensaje de error
            response.sendRedirect("confirmar_pass_productos.jsp?error=pass_incorrecta");
        }
    }
}